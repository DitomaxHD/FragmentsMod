package org.ditomax.fragmentsmod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.ditomax.fragmentsmod.util.ModTags;

public class FragmentsDropHandler {

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity) {
                processItemEntity(itemEntity);
            }
        });
    }

    private static void processItemEntity(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getStack();
        if (stack.isIn(ModTags.Items.FRAGMENTS_ITEMS)) {
            makeItemIndestructible(itemEntity);
        }
    }

    private static void makeItemIndestructible(ItemEntity itemEntity) {
        itemEntity.setNeverDespawn();
        itemEntity.setInvulnerable(true);
    }
}