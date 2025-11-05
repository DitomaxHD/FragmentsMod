package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import org.ditomax.fragmentsmod.item.fragmente.HordentreiberFragmentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ZombieConfigMixin {

    @Inject(
            method = "dropLoot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventNoDrop(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ZombieEntity zombie) {
            if (HordentreiberFragmentItem.ZOMBIES.contains(zombie)) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "dropExperience",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventNoDropXp(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ZombieEntity zombie) {
            if (HordentreiberFragmentItem.ZOMBIES.contains(zombie)) {
                ci.cancel();
            }
        }
    }
}