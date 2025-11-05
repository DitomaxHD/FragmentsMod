package org.ditomax.fragmentsmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItemEntities {
    public static final EntityType<UndestructableItemEntity> UNDESTRUCTABLE_ITEM =
            EntityType.Builder.create(UndestructableItemEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .makeFireImmune()
                    .maxTrackingRange(6)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("fragmentsmod", "undestructable_item")));


    public static void register() {
        Registry.register(Registries.ENTITY_TYPE,
                Identifier.of("fragmentsmod", "undestructable_item"),
                UNDESTRUCTABLE_ITEM);
    }
}
