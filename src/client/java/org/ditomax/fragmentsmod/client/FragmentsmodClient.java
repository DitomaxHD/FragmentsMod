package org.ditomax.fragmentsmod.client;

import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ditomax.fragmentsmod.entity.ModItemEntities;
import org.ditomax.fragmentsmod.entity.UndestructableItemEntity;
import org.ditomax.fragmentsmod.util.FragmentsLimitation;
import org.ditomax.fragmentsmod.util.ModKeybindings;
import org.ditomax.fragmentsmod.util.config.ModConfig;
import org.ditomax.fragmentsmod.util.payload.ConfigSyncPayload;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FragmentsmodClient implements ClientModInitializer {

    private static final Gson GSON = new Gson();
    public static KeyBinding boostKey;

    @Override
    public void onInitializeClient() {
        //Server config
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ModConfig receivedConfig = GSON.fromJson(payload.configJson(), ModConfig.class);
                ModConfig.setServerConfig(receivedConfig);
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                client.execute(ModConfig::resetServerConfig));

        ModKeybindings.registerClient();
        FragmentsLimitation.registerClient();

        boostKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fragmentsmod.elytra_fragment_boost",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.fragments.controls"
        ));

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.world != null) {
                for (Entity entity : client.world.getEntities()) {
                    try {
                        EntityRenderer<?, ?> renderer = client.getEntityRenderDispatcher().getRenderer(entity);
                        if (renderer == null) {
                            entity.discard();
                        }
                    } catch (Exception e) {
                        entity.discard();
                    }
                }
            }
        });
    }
}
