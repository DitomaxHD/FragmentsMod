package org.ditomax.fragmentsmod.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.ditomax.fragmentsmod.client.FragmentsmodClient;
import org.ditomax.fragmentsmod.util.payload.ElytraBoostPayload;

public class ModKeybindings {

    public static void registerClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (FragmentsmodClient.boostKey.wasPressed() && client.player.isGliding()) {
                ClientPlayNetworking.send(new ElytraBoostPayload());
            }
        });
    }
}
