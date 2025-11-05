package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.ditomax.fragmentsmod.util.ModTags;
import org.ditomax.fragmentsmod.util.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class FragmentBlockPickupMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        ItemStack stack = this.getStack();

        if (!stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
            return;
        }

        if (player.getWorld().isClient) {
            return;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (serverPlayer.getAbilities().creativeMode) {
                return;
            }

            if (GlowHandler.getFragmentsCount(serverPlayer) >= ModConfig.getActiveConfig().maxFragmentCount) {
                serverPlayer.sendMessage(Text.of("§cDu bist nicht mächtig genug um mehr Fragmente zu tragen!"), true);

                ci.cancel();
            }
        }
    }
}