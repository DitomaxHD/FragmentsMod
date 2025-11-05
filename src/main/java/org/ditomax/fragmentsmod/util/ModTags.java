package org.ditomax.fragmentsmod.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.ditomax.fragmentsmod.Fragmentsmod;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> FRAGMENTS_ITEMS = createTag("fragments_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Fragmentsmod.MOD_ID, name));
        }
    }
}
