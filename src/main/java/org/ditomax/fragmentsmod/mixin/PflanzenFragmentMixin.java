package org.ditomax.fragmentsmod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.ditomax.fragmentsmod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(Block.class)
public class PflanzenFragmentMixin {

    @Unique
    private static final Random random = new Random();

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
    private static void doubleDrops(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.block.entity.BlockEntity blockEntity, net.minecraft.entity.Entity entity, ItemStack tool, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (entity instanceof ServerPlayerEntity player) {
            boolean hasPflanzenFragment = false;

            for (int i = 0; i < player.getInventory().size(); i++) {
                if (player.getInventory().getStack(i).getItem() == ModItems.PFLANZEN_FRAGMENT) {
                    hasPflanzenFragment = true;
                    break;
                }
            }

            if (hasPflanzenFragment && state.getBlock() instanceof CropBlock) {
                List<ItemStack> drops = cir.getReturnValue();

                for (ItemStack drop : drops) {
                    int max = random.nextInt(1,4);

                    for (int i = 0; i != max; i++) {
                        ItemStack copy = drop.copy();
                        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5, copy);
                        world.spawnEntity(itemEntity);
                    }
                }
            }
        }
    }
}
