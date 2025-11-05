package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class FragmentDropMixin {


    @Inject(method = "interact", at = @At("RETURN"))
    private void onInteract(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (entity instanceof ItemFrameEntity && cir.getReturnValue().isAccepted()) {
            PlayerEntity playerEntity = (PlayerEntity) (Object) this;

            if (playerEntity.getWorld().isClient()) return;

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerEntity;

            if (serverPlayer.getServer() != null) {
                serverPlayer.getServer().execute(() -> GlowHandler.checkForFragments(serverPlayer));
            }
        }
    }
}
