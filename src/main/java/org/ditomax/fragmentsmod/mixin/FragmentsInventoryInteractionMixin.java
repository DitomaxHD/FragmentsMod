package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class FragmentsInventoryInteractionMixin {

    @Inject(method = "onSlotClick", at = @At("TAIL"))
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (player != null && !player.getWorld().isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            GlowHandler.checkForFragments(serverPlayer);
        }
    }

    @Inject(method = "onClosed", at = @At("HEAD"))
    private void onClosed(PlayerEntity player, CallbackInfo ci) {
        if (player != null && !player.getWorld().isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            GlowHandler.dropExcessFragments(serverPlayer);
            GlowHandler.checkForFragments(serverPlayer);
        }
    }
}