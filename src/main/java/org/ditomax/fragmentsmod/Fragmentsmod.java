package org.ditomax.fragmentsmod;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.ditomax.fragmentsmod.entity.ModItemEntities;
import org.ditomax.fragmentsmod.event.ElytraFragmentHandler;
import org.ditomax.fragmentsmod.event.FragmentsDropHandler;
import org.ditomax.fragmentsmod.event.HalskettenFragmentHandler;
import org.ditomax.fragmentsmod.event.SavetimeHandler;
import org.ditomax.fragmentsmod.item.ModItemGroup;
import org.ditomax.fragmentsmod.item.ModItems;
import org.ditomax.fragmentsmod.util.ModComponents;
import org.ditomax.fragmentsmod.util.config.ConfigManager;
import org.ditomax.fragmentsmod.util.config.ModConfig;
import org.ditomax.fragmentsmod.util.payload.ConfigSyncPayload;
import org.ditomax.fragmentsmod.util.payload.ElytraBoostPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fragmentsmod implements ModInitializer {

    public static final String MOD_ID = "fragmentsmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Gson GSON = new Gson();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ElytraBoostPayload.ID, ElytraBoostPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (server.isDedicated()) {
                String configJson = GSON.toJson(ModConfig.getLocalInstance());
                sender.sendPacket(new ConfigSyncPayload(configJson));
            }
        });

        ElytraFragmentHandler.register();
        ConfigManager.loadConfig();
        ModItemGroup.initialize();
        ModItems.initialize();
        ModItemEntities.register();
        FragmentsDropHandler.register();
        ModComponents.initialize();
        HalskettenFragmentHandler.register();

        ServerLifecycleEvents.SERVER_STARTED.register(SavetimeHandler::initialize);
    }
}
