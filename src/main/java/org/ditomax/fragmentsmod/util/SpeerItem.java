package org.ditomax.fragmentsmod.util;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ditomax.fragmentsmod.util.config.ModConfig;

import java.util.List;

public class SpeerItem extends Item {

    public static final Identifier BASE_ATTACK_DAMAGE_MODIFIER_ID = Identifier.ofVanilla("base_attack_damage");
    public static final Identifier BASE_ATTACK_SPEED_MODIFIER_ID = Identifier.ofVanilla("base_attack_speed");

    public SpeerItem(ToolMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
        super(settings
                .component(DataComponentTypes.TOOL, createToolComponent())
                .component(DataComponentTypes.ATTRIBUTE_MODIFIERS, createAttributeModifiers(material, baseAttackDamage, attackSpeed)));
    }

    public static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(), 1.0F, 2);
    }

    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                baseAttackDamage + material.attackDamageBonus(),
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(
                                BASE_ATTACK_SPEED_MODIFIER_ID,
                                attackSpeed,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack, 15);
        }

        stack.remove(ModComponents.PREV_POS);
        return true;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack, 15);
        }

        stack.remove(ModComponents.PREV_POS);
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        user.getStackInHand(hand).set(ModComponents.PREV_POS, user.getPos());
        return ActionResult.CONSUME;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 20 * 5;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) return;
        if (!(user instanceof PlayerEntity player)) return;

        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            player.stopUsingItem();
            return;
        }

        Vec3d startPos = stack.get(ModComponents.PREV_POS);
        if (startPos == null) {
            stack.set(ModComponents.PREV_POS, user.getPos());
            return;
        }

        double distanceMoved = user.getPos().distanceTo(startPos);

        if (distanceMoved < 0.05D) return;

        double exponent = 2.0;
        double speedNorm = Math.min(1.0, (distanceMoved - 0.1) / (5.0 - 0.1));

        float damage = (float) (ModConfig.getActiveConfig().minSpeerDamage +
                (ModConfig.getActiveConfig().maxSpeerDamage - ModConfig.getActiveConfig().minSpeerDamage) * Math.pow(speedNorm, exponent));
        float knockback = (float) (ModConfig.getActiveConfig().minSpeerKnockback +
                (ModConfig.getActiveConfig().maxSpeerKnockback - ModConfig.getActiveConfig().minSpeerKnockback) * Math.pow(speedNorm, exponent));

        Vec3d lookVec = user.getRotationVec(1.0F);
        Vec3d targetPos = user.getEyePos().add(lookVec);
        Box box = new Box(targetPos, targetPos).expand(1.0D);

        boolean hitEntity = false;

        for (Entity entity : world.getOtherEntities(user, box)) {
            if (user.hasVehicle() && user.getVehicle().equals(entity)) continue;

            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                    entity.damage(serverWorld, serverWorld.getDamageSources().playerAttack(player), damage);

                    double xRatio = MathHelper.sin(user.getYaw() * 0.017453292F);
                    double zRatio = -MathHelper.cos(user.getYaw() * 0.017453292F);
                    livingEntity.takeKnockback(knockback, xRatio, zRatio);

                    player.getItemCooldownManager().set(stack, 60);
                    hitEntity = true;
                    break;
                }
            }
        }

        if (hitEntity) {
            player.stopUsingItem();
        }

        stack.set(ModComponents.PREV_POS, user.getPos());
    }
}