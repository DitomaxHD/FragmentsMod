package org.ditomax.fragmentsmod.util.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ElytraBoostPayload() implements CustomPayload {
    public static final Id<ElytraBoostPayload> ID = new Id<>(
            Identifier.of("fragmentsmod", "elytra_boost")
    );

    public static final PacketCodec<PacketByteBuf, ElytraBoostPayload> CODEC =
            PacketCodec.unit(new ElytraBoostPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}