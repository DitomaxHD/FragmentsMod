package org.ditomax.fragmentsmod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.ditomax.fragmentsmod.item.ModItems;
import org.ditomax.fragmentsmod.util.config.ModConfig;
import org.ditomax.fragmentsmod.util.payload.ElytraBoostPayload;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ElytraFragmentHandler {
    public static final Map<UUID, Integer> BOOST_COOLDOWNS = new ConcurrentHashMap<>();
    public static final Map<UUID, Integer> BOOST_COUNT = new ConcurrentHashMap<>();

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ElytraBoostPayload.ID, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayerEntity player = context.player();

                    if (player.isGliding() && ElytraFragmentHandler.hasCustomElytra(player)) {
                        ElytraFragmentHandler.boostPlayer(player);
                    }
                })
        );

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ElytraFragmentHandler.BOOST_COOLDOWNS.forEach((uuid, cooldown) -> {
                if (cooldown > 0) {
                    int newCooldown = cooldown - 1;
                    ElytraFragmentHandler.BOOST_COOLDOWNS.put(uuid, newCooldown);
                    if (newCooldown % 20 == 0) {
                        ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                        if (player != null) {
                            int secondsRemaining = newCooldown / 20;
                            String bar = ElytraFragmentHandler.getBar(cooldown, secondsRemaining);
                            player.sendMessage(
                                    Text.literal("§eBoost: " + bar + " §f" + secondsRemaining + "s"),
                                    true
                            );
                        }
                    }
                }
            });

            ElytraFragmentHandler.BOOST_COOLDOWNS.entrySet().removeIf(entry -> entry.getValue() <= 0);
        });
    }

    private static @NotNull String getBar(Integer cooldown, int secondsRemaining) {
        int maxSeconds = cooldown >= (int) ModConfig.getActiveConfig().longElytraCooldown ?
                (int) ModConfig.getActiveConfig().longElytraCooldown / 20 :
                (int) ModConfig.getActiveConfig().elytraCooldown / 20;

        int barLength = 10;
        int filledBars = Math.max(0, (int) ((double) (maxSeconds - secondsRemaining) / maxSeconds * barLength));
        int emptyBars = Math.max(0, barLength - filledBars);

        return "§a" + "█".repeat(filledBars) + "§7" + "█".repeat(emptyBars);
    }

    public static boolean hasCustomElytra(PlayerEntity player) {
        ItemStack chestItem = player.getEquippedStack(EquipmentSlot.CHEST);
        return !chestItem.isEmpty() && chestItem.getItem() == ModItems.ELYTRA_FRAGMENT;
    }

    public static void boostPlayer(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();

        int remainingCooldown = BOOST_COOLDOWNS.getOrDefault(playerId, 0);
        if (remainingCooldown > 0) {
            return;
        }

        int currentBoosts = BOOST_COUNT.getOrDefault(playerId, 0);
        currentBoosts++;
        BOOST_COUNT.put(playerId, currentBoosts);

        // Setze Cooldown
        if (currentBoosts >= 5) {
            BOOST_COOLDOWNS.put(playerId, (int) ModConfig.getActiveConfig().longElytraCooldown);
            BOOST_COUNT.remove(playerId);
        } else {
            BOOST_COOLDOWNS.put(playerId, (int) ModConfig.getActiveConfig().elytraCooldown);
        }

        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d velocity = player.getVelocity();

        player.setVelocity(
                velocity.add(
                        lookVec.x * ModConfig.getActiveConfig().elytraBoostStrength,
                        lookVec.y * ModConfig.getActiveConfig().elytraBoostStrength,
                        lookVec.z * ModConfig.getActiveConfig().elytraBoostStrength
                )
        );

        player.velocityModified = true;

        player.getWorld().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ITEM_FIRECHARGE_USE,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
        );
    }
}
