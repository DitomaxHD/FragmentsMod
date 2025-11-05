package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ElytraFragmentItem extends Item {

    public ElytraFragmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}