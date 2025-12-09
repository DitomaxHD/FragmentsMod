package org.ditomax.fragmentsmod.item.fragmente;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ditomax.fragmentsmod.util.config.ModConfig;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class HordentreiberFragmentItem extends SwordItem {

    private final Random random = new Random();

    public static final Set<ZombieEntity> ZOMBIES = new HashSet<>();

    public HordentreiberFragmentItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    private static GoalSelector getGoalSelector(MobEntity mob) {
        try {
            Field goalSelectorField = MobEntity.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            return (GoalSelector) goalSelectorField.get(mob);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access goalSelector", e);
        }
    }

    private static GoalSelector getTargetSelector(MobEntity mob) {
        try {
            Field targetSelectorField = MobEntity.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
            return (GoalSelector) targetSelectorField.get(mob);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access targetSelector", e);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.getWorld().isClient) {
            return true;
        }

        if (ZOMBIES.contains(target)) return true;

        for (ZombieEntity zom : ZOMBIES) {
            zom.remove(Entity.RemovalReason.DISCARDED);
        }

        if (random.nextInt(0, 100) < ModConfig.getActiveConfig().hordentreiberSpawnProcentage) {
            int zombieCount = random.nextInt(1, ModConfig.getActiveConfig().hordentreiberMaxZombies);

            for (int i = 0; i < zombieCount; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = 2 + random.nextDouble() * 2;

                double x = target.getX() + Math.cos(angle) * distance;
                double y = target.getY();
                double z = target.getZ() + Math.sin(angle) * distance;

                BlockPos spawnPos = findSafeSpawnPosition(target.getWorld(), new BlockPos((int)x, (int)y, (int)z));

                ZombieEntity zombie = new ZombieEntity(net.minecraft.entity.EntityType.ZOMBIE, target.getWorld());
                zombie.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);

                zombie.setCanPickUpLoot(false);
                zombie.setPersistent();
                zombie.setDespawnCounter(1200);

                Predicate<Goal> removeAllPredicate = (goal) -> true;
                getGoalSelector(zombie).clear(removeAllPredicate);
                getTargetSelector(zombie).clear(removeAllPredicate);

                zombie.setTarget(target);
                zombie.setStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 1, false, false, false), zombie);
                zombie.setStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 1, false, false, false), zombie);
                zombie.setStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, -1, 1, false, false, false), zombie);

                getTargetSelector(zombie).add(1, new ActiveTargetGoal<>(zombie, LivingEntity.class, 10, true, false,
                        (entity, serverWorld) -> entity == target && !ZOMBIES.contains(entity)
                ));
                getGoalSelector(zombie).add(2, new MeleeAttackGoal(zombie, 1.0, false));
                getGoalSelector(zombie).add(3, new WanderNearTargetGoal(zombie, 1.0, 32.0F));

                zombie.setTarget(target);

                zombie.setCustomNameVisible(false);

                ZOMBIES.add(zombie);

                target.getWorld().spawnEntity(zombie);

                zombie.setAttacker(null);
            }
        }

        return true;
    }

    private BlockPos findSafeSpawnPosition(World world, BlockPos startPos) {
        if (isPositionSafe(world, startPos)) {
            return startPos;
        }

        for (int xOffset = -3; xOffset <= 3; xOffset++) {
            for (int zOffset = -3; zOffset <= 3; zOffset++) {
                for (int yOffset = -3; yOffset <= 3; yOffset++) {
                    BlockPos checkPos = startPos.add(xOffset, yOffset, zOffset);

                    if (isPositionSafe(world, checkPos)) {
                        return checkPos;
                    }
                }
            }
        }

        for (int y = 0; y < 10; y++) {
            BlockPos checkPos = startPos.up(y);
            if (isPositionSafe(world, checkPos)) {
                return checkPos;
            }
        }

        return startPos.up(3);
    }

    private boolean isPositionSafe(World world, BlockPos pos) {
        BlockState stateAtPos = world.getBlockState(pos);
        BlockState stateAbove = world.getBlockState(pos.up());
        BlockState stateBelow = world.getBlockState(pos.down());

        boolean positionClear = stateAtPos.isAir() || stateAtPos.isReplaceable();
        boolean aboveClear = stateAbove.isAir() || stateAbove.isReplaceable();

        boolean solidBelow = !stateBelow.isAir() && stateBelow.isSolidBlock(world, pos.down());

        return positionClear && aboveClear && solidBelow;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }
}
