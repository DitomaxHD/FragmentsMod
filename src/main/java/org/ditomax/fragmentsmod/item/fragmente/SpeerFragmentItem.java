package org.ditomax.fragmentsmod.item.fragmente;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.ditomax.fragmentsmod.util.SpeerItem;

public class SpeerFragmentItem extends SpeerItem {

    public SpeerFragmentItem(ToolMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
        super(material, baseAttackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }
}
