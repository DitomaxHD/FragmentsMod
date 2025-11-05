package org.ditomax.fragmentsmod.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.ditomax.fragmentsmod.Fragmentsmod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FragmentProtectionState extends PersistentState {

    public Map<UUID, Integer> savetimeMap;
    public Map<Item, UUID> lastPickupMap;

    public FragmentProtectionState(Map<UUID, Integer> savetimeMap, Map<Item, UUID> lastPickupMap) {
        this.savetimeMap = new HashMap<>(savetimeMap);
        this.lastPickupMap = new HashMap<>(lastPickupMap);
        markDirty();
    }

    public FragmentProtectionState() {
        this(new HashMap<>(), new HashMap<>());
    }

    public static final Codec<Map<UUID, Integer>> SAVETIME_CODEC = Codec.unboundedMap(
            Codec.STRING.xmap(UUID::fromString, UUID::toString),
            Codec.INT
    );

    public static final Codec<Map<Item, UUID>> LAST_PICKUP_CODEC = Codec.unboundedMap(
            Identifier.CODEC.xmap(Registries.ITEM::get, Registries.ITEM::getId),
            Codec.STRING.xmap(UUID::fromString, UUID::toString)
    );

    public static final Codec<FragmentProtectionState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SAVETIME_CODEC.optionalFieldOf("savetime_data", new HashMap<>()).forGetter(state -> state.savetimeMap),
            LAST_PICKUP_CODEC.optionalFieldOf("last_pickup_data", new HashMap<>()).forGetter(state -> state.lastPickupMap)
    ).apply(instance, FragmentProtectionState::new));

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), this)
                .resultOrPartial(error ->
                        Fragmentsmod.LOGGER.error("Fehler beim Speichern: {}", error)
                )
                .ifPresent(tag -> nbt.put("data", tag));
        return nbt;
    }

    public static FragmentProtectionState load(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound dataTag = nbt.getCompound("data");

        return CODEC.decode(registries.getOps(NbtOps.INSTANCE), dataTag)
                .resultOrPartial(error -> Fragmentsmod.LOGGER.error("Fehler beim Laden von FragmentProtectionState: {}", error))
                .map(Pair::getFirst)
                .orElseGet(FragmentProtectionState::new);
    }

    public static FragmentProtectionState getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        PersistentStateManager stateManager = world.getPersistentStateManager();

        PersistentState.Type<FragmentProtectionState> type = new PersistentState.Type<>(
                FragmentProtectionState::new,
                FragmentProtectionState::load,
                null
        );

        return stateManager.getOrCreate(type, "fragment_protection_state");
    }
}