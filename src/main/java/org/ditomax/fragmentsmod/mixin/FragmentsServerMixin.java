package org.ditomax.fragmentsmod.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class FragmentsServerMixin {

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntity Serverplayer = (ServerPlayerEntity) (Object) this;

        GlowHandler.checkForFragments(Serverplayer);
    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void onSpawn(CallbackInfo ci) {
        ServerPlayerEntity Serverplayer = (ServerPlayerEntity) (Object) this;

        GlowHandler.checkForFragments(Serverplayer);
    }
}