package org.ditomax.fragmentsmod.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.ditomax.fragmentsmod.client.FragmentsmodClient;
import org.ditomax.fragmentsmod.util.payload.ElytraBoostPayload;

public class ModKeybindings {

    private static boolean boostKeyWasDown = false;

    public static void registerClient() {
        ClientTickEvents.END_CLIENT_TICK.register( client -> {
            if (client.player == null) return;

            boolean boostKeyIsDown = FragmentsmodClient.boostKey.isPressed();

            if (boostKeyIsDown && !boostKeyWasDown) {
                if (client.player.isGliding()) {
                    ClientPlayNetworking.send(new ElytraBoostPayload());
                }
            }

            boostKeyWasDown = boostKeyIsDown;
        });
    }
}
