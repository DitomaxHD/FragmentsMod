package org.ditomax.fragmentsmod.util;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ModComponents {

    public static final ComponentType<Vec3d> PREV_POS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("spears", "prev_pos"),
            ComponentType.<Vec3d>builder()
                    .codec(Vec3d.CODEC)
                    .build()
    );

    public static void initialize() {
    }
}


