package org.ditomax.fragmentsmod.effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.ditomax.fragmentsmod.Fragmentsmod;


public class ModEffects {

    public static final RegistryEntry<StatusEffect> GLOW = registerStatusEffect("glow",
            new FragmentGlowEffect(StatusEffectCategory.NEUTRAL, 0x000000));

    public static final RegistryEntry<StatusEffect> STRENGTH = registerStatusEffect("strength",
            new FragmentStrengthEffect(StatusEffectCategory.NEUTRAL, 0x000000)
                    .addAttributeModifier(EntityAttributes.ATTACK_DAMAGE,
                            Identifier.of(Fragmentsmod.MOD_ID, "strength"),
                            3.0,
                            EntityAttributeModifier.Operation.ADD_VALUE));

    public static final RegistryEntry<StatusEffect> RESISTANCE = registerStatusEffect("resistance",
            new FragmentResistanceEffect(StatusEffectCategory.NEUTRAL, 0x000000)
                    .addAttributeModifier(EntityAttributes.ARMOR,
                        Identifier.of(Fragmentsmod.MOD_ID, "resistance.armor"),
                        5.0,
                        EntityAttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(EntityAttributes.ARMOR_TOUGHNESS,
                            Identifier.of(Fragmentsmod.MOD_ID, "resistance.armor_toughness"),
                            5.0,
                            EntityAttributeModifier.Operation.ADD_VALUE));

    public static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Fragmentsmod.MOD_ID, name), statusEffect);
    }

    public static void initialize() {}
}
