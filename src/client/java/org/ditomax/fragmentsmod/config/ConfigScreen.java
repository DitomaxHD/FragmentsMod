package org.ditomax.fragmentsmod.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.ditomax.fragmentsmod.util.config.ConfigManager;
import org.ditomax.fragmentsmod.util.config.ModConfig;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        final ModConfig localConfig = ModConfig.getLocalInstance();
        final ModConfig activeConfig = ModConfig.getActiveConfig();
        final boolean isServer = ModConfig.isServerConfigLoaded();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Fragments Mod Config"));

        if (isServer) {
            builder.setTitle(Text.literal("Fragments Mod Config (Server-gesteuert)"));
            builder.setSavingRunnable(() -> {
            });
        } else {
            builder.setSavingRunnable(ConfigManager::saveConfig);
        }

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory speer = builder.getOrCreateCategory(Text.literal("Speer"));
        speer.addEntry(entryBuilder.startFloatField(Text.literal("Minimal speer damage"), activeConfig.minSpeerDamage)
                .setDefaultValue(localConfig.minSpeerDamage)
                .setSaveConsumer(newValue -> localConfig.minSpeerDamage = newValue)
                .build());

        speer.addEntry(entryBuilder.startFloatField(Text.literal("Maximal speer damage"), activeConfig.maxSpeerDamage)
                .setDefaultValue(localConfig.maxSpeerDamage)
                .setSaveConsumer(newValue -> localConfig.maxSpeerDamage = newValue)
                .build());

        speer.addEntry(entryBuilder.startFloatField(Text.literal("Minimal speer knockback"), activeConfig.minSpeerKnockback)
                .setDefaultValue(localConfig.minSpeerKnockback)
                .setSaveConsumer(newValue -> localConfig.minSpeerKnockback = newValue)
                .build());


        speer.addEntry(entryBuilder.startFloatField(Text.literal("Maximal speer knockback"), activeConfig.maxSpeerKnockback)
                .setDefaultValue(localConfig.maxSpeerKnockback)
                .setSaveConsumer(newValue -> localConfig.maxSpeerKnockback = newValue)
                .build());

        ConfigCategory elytra = builder.getOrCreateCategory(Text.literal("Elytra"));
        elytra.addEntry(entryBuilder.startFloatField(Text.literal("Boost strenght"), activeConfig.elytraBoostStrength)
                .setDefaultValue(localConfig.elytraBoostStrength)
                .setSaveConsumer(newValue -> localConfig.elytraBoostStrength = newValue)
                .build());

        elytra.addEntry(entryBuilder.startFloatField(Text.literal("Cooldown (ticks)"), activeConfig.elytraCooldown)
                .setDefaultValue(localConfig.elytraCooldown)
                .setSaveConsumer(newValue -> localConfig.elytraCooldown = newValue)
                .build());

        elytra.addEntry(entryBuilder.startFloatField(Text.literal("Reload cooldown (ticks)"), activeConfig.longElytraCooldown)
                .setDefaultValue(localConfig.longElytraCooldown)
                .setSaveConsumer(newValue -> localConfig.longElytraCooldown = newValue)
                .build());

        ConfigCategory halskette = builder.getOrCreateCategory(Text.literal("Halskette"));
        halskette.addEntry(entryBuilder.startFloatField(Text.literal("Armor damage boost"), activeConfig.halsketteArmorDamageBoost)
                .setDefaultValue(localConfig.halsketteArmorDamageBoost)
                .setSaveConsumer(newValue -> localConfig.halsketteArmorDamageBoost = newValue)
                .build());

        ConfigCategory hordentreiber = builder.getOrCreateCategory(Text.literal("Hordentreiber"));
        hordentreiber.addEntry(entryBuilder.startIntField(Text.literal("Maximum zombies amount"), activeConfig.hordentreiberMaxZombies)
                .setDefaultValue(localConfig.hordentreiberMaxZombies)
                .setSaveConsumer(newValue -> localConfig.hordentreiberMaxZombies = newValue)
                .build());

        hordentreiber.addEntry(entryBuilder.startIntField(Text.literal("Zombie spawn percentage"), activeConfig.hordentreiberSpawnProcentage)
                .setDefaultValue(localConfig.hordentreiberSpawnProcentage)
                .setSaveConsumer(newValue -> localConfig.hordentreiberSpawnProcentage = newValue)
                .build());

        ConfigCategory allgemein = builder.getOrCreateCategory(Text.literal("Allgemein"));
        allgemein.addEntry(entryBuilder.startIntField(Text.literal("Savetime"), activeConfig.schutzzeit)
                .setDefaultValue(localConfig.schutzzeit)
                .setSaveConsumer(newValue -> localConfig.schutzzeit = newValue)
                .build());

        allgemein.addEntry(entryBuilder.startStrField(Text.literal("Webhook"), activeConfig.discordWebhookUrl)
                .setDefaultValue(localConfig.discordWebhookUrl)
                .setSaveConsumer(newValue -> localConfig.discordWebhookUrl = newValue)
                .build());

        allgemein.addEntry(entryBuilder.startIntField(Text.literal("Maximal fragment amount"), activeConfig.maxFragmentCount)
                .setDefaultValue(localConfig.maxFragmentCount)
                .setSaveConsumer(newValue -> localConfig.maxFragmentCount = newValue)
                .build());


        return builder.build();
    }
}