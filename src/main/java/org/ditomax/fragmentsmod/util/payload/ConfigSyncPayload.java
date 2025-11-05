package org.ditomax.fragmentsmod.util.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncPayload(String configJson) implements CustomPayload {

    public static final Id<ConfigSyncPayload> ID = new Id<>(Identifier.of("fragmentsmod", "config_sync"));

    public static final PacketCodec<PacketByteBuf, ConfigSyncPayload> CODEC = PacketCodec.of(
            ConfigSyncPayload::write, ConfigSyncPayload::new
    );

    private ConfigSyncPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.configJson);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}