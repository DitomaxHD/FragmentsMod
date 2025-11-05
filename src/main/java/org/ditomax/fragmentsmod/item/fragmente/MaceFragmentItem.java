package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;

public class MaceFragmentItem extends MaceItem {

    public MaceFragmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

}
