package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FragmentHalsketteItem extends Item {

    public FragmentHalsketteItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
