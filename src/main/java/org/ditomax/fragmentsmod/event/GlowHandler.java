package org.ditomax.fragmentsmod.event;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.ditomax.fragmentsmod.item.ModItems;
import org.ditomax.fragmentsmod.util.ModTags;
import org.ditomax.fragmentsmod.util.config.ModConfig;

import java.util.List;

public class GlowHandler {

    public static void checkForFragments(ServerPlayerEntity player) {
        boolean hasAnyFragment = checkItemPresence(player, null);

        boolean hasStrengthFragment = checkItemPresence(player, ModItems.STAERKE_FRAGMENT);
        boolean hasPandoraFragment = checkItemPresence(player, ModItems.PANDORA_FRAGMENT);

        if (hasAnyFragment) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.GLOWING, -1, 0, false, false, false));

            if (hasStrengthFragment) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.STRENGTH, -1, 0, false, false, false));
            } else {
                player.removeStatusEffect(StatusEffects.STRENGTH);
            }

            if (hasPandoraFragment) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SATURATION, -1, 0, false, false, false));
            } else {
                player.removeStatusEffect(StatusEffects.SATURATION);
            }

        } else {
            player.removeStatusEffect(StatusEffects.GLOWING);
            player.removeStatusEffect(StatusEffects.STRENGTH);
            player.removeStatusEffect(StatusEffects.SATURATION);
        }
    }

    private static boolean checkItemPresence(ServerPlayerEntity player, Item targetItem) {
        for (ItemStack item : player.getInventory().main) {
            if (targetItem == null ? item.isIn(ModTags.Items.FRAGMENTS_ITEMS) : item.isOf(targetItem)) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().offHand) {
            if (targetItem == null ? stack.isIn(ModTags.Items.FRAGMENTS_ITEMS) : stack.isOf(targetItem)) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (targetItem == null ? stack.isIn(ModTags.Items.FRAGMENTS_ITEMS) : stack.isOf(targetItem)) {
                return true;
            }
        }
        
        ItemStack cursor = player.currentScreenHandler != null ? player.currentScreenHandler.getCursorStack() : ItemStack.EMPTY;
        return targetItem == null ? cursor.isIn(ModTags.Items.FRAGMENTS_ITEMS) : cursor.isOf(targetItem);
    }

    public static int getFragmentsCount(PlayerEntity player) {
        int count = getFragmentsCountWithoutCursor(player);

        if (player.currentScreenHandler != null) {
            ItemStack cursor = player.currentScreenHandler.getCursorStack();
            if (cursor.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                count += cursor.getCount();
            }
        }

        return count;
    }

    public static int getFragmentsCountWithoutCursor(PlayerEntity player) {
        int count = 0;

        for (ItemStack stack : player.getInventory().main) {
            if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                count += stack.getCount();
            }
        }

        for (ItemStack stack : player.getInventory().offHand) {
            if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                count += stack.getCount();
            }
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                count += stack.getCount();
            }
        }

        return count;
    }


    public static void dropExcessFragments(ServerPlayerEntity player) {
        if (player.getAbilities().creativeMode) {
            return;
        }

        int maxFragments = ModConfig.getActiveConfig().maxFragmentCount;

        ItemStack cursor = player.currentScreenHandler != null ? player.currentScreenHandler.getCursorStack() : ItemStack.EMPTY;
        int cursorFragmentCount = cursor.isIn(ModTags.Items.FRAGMENTS_ITEMS) ? cursor.getCount() : 0;

        int inventoryFragments = getFragmentsCountWithoutCursor(player);

        int currentTotal = inventoryFragments + cursorFragmentCount;

        if (currentTotal <= maxFragments) {
            return;
        }

        int excess = currentTotal - maxFragments;
        int initialExcess = excess;

        if (cursorFragmentCount > 0 && excess > 0) {
            int dropAmount = Math.min(excess, cursorFragmentCount);

            ItemStack toDrop = cursor.copyWithCount(dropAmount);
            player.dropItem(toDrop, true);

            cursor.decrement(dropAmount);

            if (player.currentScreenHandler != null) {
                player.currentScreenHandler.setCursorStack(cursor);
            }

            excess -= dropAmount;
        }

        if (excess > 0) {

            List<List<ItemStack>> allInventories = List.of(
                    player.getInventory().main,
                    player.getInventory().offHand,
                    player.getInventory().armor
            );

            for (List<ItemStack> inventory : allInventories) {
                for (int i = inventory.size() - 1; i >= 0 && excess > 0; i--) {
                    ItemStack stack = inventory.get(i);

                    if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                        int dropAmount = Math.min(excess, stack.getCount());

                        ItemStack toDrop = stack.copyWithCount(dropAmount);
                        player.dropItem(toDrop, true);

                        stack.decrement(dropAmount);

                        excess -= dropAmount;
                    }
                }
            }
        }

        if (initialExcess > 0) {
            player.sendMessage(
                    Text.of(String.format("§cDein Limit von %d Fragmenten wurde überschritten. Überschüssige Fragmente wurden fallengelassen.", maxFragments)),
                    true
            );
        }

        player.getServer().execute(() -> checkForFragments(player));
    }
}