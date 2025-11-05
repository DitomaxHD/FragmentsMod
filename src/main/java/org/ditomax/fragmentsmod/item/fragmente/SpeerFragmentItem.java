package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import org.ditomax.fragmentsmod.util.SpeerItem;

public class SpeerFragmentItem extends SpeerItem {

    public SpeerFragmentItem(ToolMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
        super(material, baseAttackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
