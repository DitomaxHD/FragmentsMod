package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.explosion.Explosion;
import org.ditomax.fragmentsmod.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class FragmentsExplosionMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventProtectedItemEntityDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        ItemStack stack = itemEntity.getStack();
        if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
            if (source.isIn(DamageTypeTags.IS_EXPLOSION) ) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "isImmuneToExplosion",
            at = @At("HEAD"),
            cancellable = true
    )
    private void makeProtectedItemsImmuneToExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        ItemStack stack = itemEntity.getStack();
        if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
            cir.setReturnValue(true);
        }
    }
}