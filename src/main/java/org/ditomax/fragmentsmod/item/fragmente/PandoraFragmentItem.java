package org.ditomax.fragmentsmod.item.fragmente;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PandoraFragmentItem extends Item {

    public PandoraFragmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (world.isClient) return;
        if (world.getTime() % 10 != 0) return;

        List<RegistryEntry<StatusEffect>> toRemove = new ArrayList<>();

        for (StatusEffectInstance effect : List.copyOf(player.getStatusEffects())) {
            StatusEffect type = effect.getEffectType().value();

            if (type.getCategory() == StatusEffectCategory.HARMFUL)  {
                toRemove.add(effect.getEffectType());
            }
        }

        for (RegistryEntry<StatusEffect> effectType : toRemove) {
            player.removeStatusEffect(effectType);
        }
    }
}
