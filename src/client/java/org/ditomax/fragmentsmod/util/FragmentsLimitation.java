
package org.ditomax.fragmentsmod.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.ditomax.fragmentsmod.event.GlowHandler;
import org.ditomax.fragmentsmod.util.config.ModConfig;


@Environment(EnvType.CLIENT)
public class FragmentsLimitation {

    public static void registerClient() {
        HudRenderCallback.EVENT.register((DrawContext drawContext, RenderTickCounter tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;

            if (client.player == null || client.world == null) {
                return;
            }

            if (client.options.hudHidden || player.isSpectator()) {
                return;
            }

            if (GlowHandler.getFragmentsCount(player) >= ModConfig.getActiveConfig().maxFragmentCount) {
                drawFragmentsCount(client, drawContext, 0xAA0000, player);

            } else if (GlowHandler.getFragmentsCount(player) == ModConfig.getActiveConfig().maxFragmentCount-1) {
                drawFragmentsCount(client, drawContext, 0xFFAA00, player);

            } else if (GlowHandler.getFragmentsCount(player) == 0) {
                drawFragmentsCount(client, drawContext, 0xFFFFFF, player);

            } else {
                drawFragmentsCount(client, drawContext, 0x00AA00, player);
            }
        });
    }

    public static void drawFragmentsCount(MinecraftClient client, DrawContext drawContext, int color, PlayerEntity player) {
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        String text = "[" + GlowHandler.getFragmentsCount(player) + "/" + ModConfig.getActiveConfig().maxFragmentCount + "]";

        int hotbarWidth = 182;
        int hotbarX = (screenWidth - hotbarWidth) / 2;
        int hotbarRightX = hotbarX + hotbarWidth;

        int x = hotbarRightX + 5;
        int y = screenHeight - 15;
        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                x,
                y,
                color,
                true
        );
    }
}