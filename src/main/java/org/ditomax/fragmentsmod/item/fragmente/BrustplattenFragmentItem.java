package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;

public class BrustplattenFragmentItem extends ArmorItem {

    public BrustplattenFragmentItem(ArmorMaterial material, EquipmentType type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
