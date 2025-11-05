package org.ditomax.fragmentsmod.util.config;

public class ModConfig {

    private static ModConfig localInstance;
    private static ModConfig serverInstance;
    private static boolean isServerConfigLoaded = false;

    public float minSpeerDamage = 2F;
    public float minSpeerKnockback = 1F;
    public float maxSpeerDamage = 15F;
    public float maxSpeerKnockback = 3F;
    public float elytraBoostStrength = 1.5F;
    public float elytraCooldown = 200F;
    public float longElytraCooldown = 1200F;
    public float halsketteArmorDamageBoost = 3.0F;
    public int hordentreiberMaxZombies = 5;
    public int hordentreiberSpawnProcentage = 30;
    public int schutzzeit = 1200;
    public String discordWebhookUrl = "";
    public int maxFragmentCount = 3;


    public ModConfig() {}

    public static ModConfig getLocalInstance() {
        if (localInstance == null) {
            localInstance = new ModConfig();
        }
        return localInstance;
    }

    public static void setLocalInstance(ModConfig config) {
        localInstance = config;
    }

    public static ModConfig getActiveConfig() {
        if (isServerConfigLoaded && serverInstance != null) {
            return serverInstance;
        }
        return getLocalInstance();
    }

    public static void setServerConfig(ModConfig config) {
        serverInstance = config;
        isServerConfigLoaded = true;
    }

    public static void resetServerConfig() {
        serverInstance = null;
        isServerConfigLoaded = false;
    }

    public static boolean isServerConfigLoaded() {
        return isServerConfigLoaded;
    }
}