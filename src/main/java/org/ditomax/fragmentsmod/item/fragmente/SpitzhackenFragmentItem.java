package org.ditomax.fragmentsmod.item.fragmente;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SpitzhackenFragmentItem extends PickaxeItem {

    public SpitzhackenFragmentItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof ServerPlayerEntity player) {
            float pitch = player.getPitch();
            Direction facing = player.getHorizontalFacing();

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        BlockPos targetPos;

                        if (pitch < -45) {
                            targetPos = pos.add(x, 0, z);
                        } else if (pitch > 45) {
                            targetPos = pos.add(x, 0, z);
                        } else {
                            targetPos = switch (facing) {
                                case NORTH, SOUTH -> pos.add(x, y, 0);
                                case WEST, EAST -> pos.add(0, y, z);
                                default -> pos.add(x, y, z);
                            };
                        }

                        BlockState targetState = world.getBlockState(targetPos);

                        // Prüfe ob der Block abgebaut werden kann
                        if (!targetState.isAir() && targetState.getHardness(world, targetPos) >= 0) {
                            world.breakBlock(targetPos, true, player);
                        }
                    }
                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }
}
