package org.ditomax.fragmentsmod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.ditomax.fragmentsmod.util.Discord;
import org.ditomax.fragmentsmod.util.FragmentProtectionState;
import org.ditomax.fragmentsmod.util.ModTags;
import org.ditomax.fragmentsmod.util.config.ModConfig;

import java.util.UUID;

public class SavetimeHandler {

    private static FragmentProtectionState state;


    public static void grantProtection(PlayerEntity player, ItemStack pickedUpFragment) {
        UUID playerUUID = player.getUuid();
        Item fragmentItem = pickedUpFragment.getItem();

        if (state.lastPickupMap.containsKey(fragmentItem)) {
            UUID lastPlayer = state.lastPickupMap.get(fragmentItem);

            if (playerUUID.equals(lastPlayer)) {
                player.sendMessage(Text.literal(
                        "§cDu kannst die Macht der Fragmente nicht missbrauchen."
                ), true);
                return;
            }
        }

        Discord.sendDiscordMessageWithImage(ModConfig.getActiveConfig().discordWebhookUrl, pickedUpFragment, player);
        state.savetimeMap.put(playerUUID, ModConfig.getActiveConfig().schutzzeit);

        state.lastPickupMap.put(fragmentItem, playerUUID);

        registerAllFragmentsInInventory(player);

        player.sendMessage(Text.literal("§aDu bist nun " + ModConfig.getActiveConfig().schutzzeit/20 + " Sekunden vor Angriffen geschützt!"), true);
        state.markDirty();
    }

    private static void registerAllFragmentsInInventory(PlayerEntity player) {
        UUID playerUUID = player.getUuid();

        // Haupt-Inventar durchsuchen
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty() && stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                state.lastPickupMap.put(stack.getItem(), playerUUID);
            }
        }

        // Offhand durchsuchen
        for (ItemStack stack : player.getInventory().offHand) {
            if (!stack.isEmpty() && stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                state.lastPickupMap.put(stack.getItem(), playerUUID);
            }
        }

        // Armor durchsuchen
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty() && stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
                state.lastPickupMap.put(stack.getItem(), playerUUID);
            }
        }

        state.markDirty();
    }

    public static void initialize(MinecraftServer server) {
        state = FragmentProtectionState.getServerState(server);

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && entity instanceof PlayerEntity target) {

                if (state.savetimeMap.containsKey(player.getUuid())) {
                    player.sendMessage(Text.literal("§cDu hast keinen Schutz mehr!"), false);
                    target.sendMessage(Text.literal("§6Du kannst ihn jetzt auch angreifen!"), false);

                    state.savetimeMap.remove(player.getUuid());
                    state.markDirty();
                }

                if (state.savetimeMap.containsKey(target.getUuid())) {
                    player.sendMessage(Text.literal("§cDieser Spieler ist momentan vor Angriffen geschützt."), true);

                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(mcServer -> {
            state.savetimeMap.forEach((uuid, cooldown) -> {
                if (cooldown > 0) {
                    state.savetimeMap.put(uuid, cooldown - 1);
                }
            });

            state.savetimeMap.entrySet().removeIf(entry -> entry.getValue() <= 0);
            state.markDirty();
        });
    }
}