package org.ditomax.fragmentsmod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.ditomax.fragmentsmod.event.SavetimeHandler;
import org.ditomax.fragmentsmod.util.ModTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class FragmentPickupMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(
            method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
            at = @At("RETURN")
    )
    private void afterInsertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        if (player.getWorld().isClient) {
            return;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

        if (player.getServer() != null) {
            player.getServer().execute(() -> GlowHandler.checkForFragments(serverPlayer));
        }
    }

    @Inject(
            method = "insertStack(Lnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD")
    )
    private void afterInsertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (player.getWorld().isClient) {
            return;
        }

        if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {

            SavetimeHandler.grantProtection(player, stack); //unteranderem auch der discord webhook
        }
    }

    @Inject(
            method = "removeStack(II)Lnet/minecraft/item/ItemStack;",
            at = @At("RETURN")
    )
    private void onRemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (player.getWorld().isClient()) return;

        ItemStack removed = cir.getReturnValue();
        if (removed != null && removed.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
            player.getServer().execute(() -> {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                GlowHandler.checkForFragments(serverPlayer);
            });
        }
    }
}