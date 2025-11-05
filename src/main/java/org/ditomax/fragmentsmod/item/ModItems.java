package org.ditomax.fragmentsmod.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;
import org.ditomax.fragmentsmod.Fragmentsmod;
import org.ditomax.fragmentsmod.item.fragmente.*;

import java.util.Optional;
import java.util.function.Function;

public class ModItems {

    public static final Item SCHATTENSCHLEIER = registerItem("schattenschleier", settings ->
            new SchattenschleierItem(ModToolMaterials.FRAGMENTS_TOOL_MATERIAL, 7F, 2F, settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item FRAGMENT_SCHWERT = registerItem("fragment_schwert", settings ->
            new FragmentSchwertItem(ModToolMaterials.FRAGMENTS_TOOL_MATERIAL, 7.0F, 3.0F, settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item SPITZHACKEN_FRAGMENT = registerItem("spitzhacken_fragment", settings ->
            new SpitzhackenFragmentItem(ModToolMaterials.PICKAXE_TOOL_MATERIAL, 4, 4.0F, settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item HORDENTREIBER_FRAGMENT = registerItem("hordentreiber_fragment", settings ->
            new HordentreiberFragmentItem(ModToolMaterials.FRAGMENTS_TOOL_MATERIAL, 6.5F, 4F, settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));


    public static final Item BRUSTPLATTEN_FRAGMENT = registerItem("brustplatten_fragment", settings ->
            new BrustplattenFragmentItem(ModArmorMaterials.FRAGMENT_ARMOR_MATERIAL, EquipmentType.CHESTPLATE, settings
                    .rarity(Rarity.EPIC)
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .maxCount(1)));


    public static final Item BOGEN_FRAGMENT = registerItem("bogen_fragment", settings ->
            new BogenFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item MACE_FRAGMENT = registerItem("mace_fragment", settings ->
            new MaceFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .attributeModifiers(MaceItem.createAttributeModifiers())
                    .maxCount(1)));

    public static final Item STAERKE_FRAGMENT = registerItem("staerke_fragment", settings ->
            new StaerkeFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item ELYTRA_FRAGMENT = registerItem("elytra_fragment", settings ->
            new ElytraFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .component(DataComponentTypes.GLIDER, Unit.INSTANCE)
                    .component(DataComponentTypes.EQUIPPABLE,
                            new EquippableComponent(
                                    EquipmentSlot.CHEST,
                                    SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA,
                                    Optional.of(RegistryKey.of(RegistryKey.ofRegistry(Identifier.of("equipment_asset")), Identifier.of("fragmentsmod", "elytra_fragment"))),
                                    Optional.empty(),
                                    Optional.empty(),
                                    true,
                                    true,
                                    true
                            ))
                    .rarity(Rarity.RARE)
                    .maxCount(1)));

    public static final Item PANDORA_FRAGMENT = registerItem("pandora_fragment", settings ->
            new PandoraFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item FRAGMENT_HALSKETTE = registerItem("fragment_halskette", settings ->
            new FragmentHalsketteItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item PFLANZEN_FRAGMENT = registerItem("pflanzen_fragment", settings ->
            new PflanzenFragmentItem(settings
                    .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                    .rarity(Rarity.EPIC)
                    .maxCount(1)));

    public static final Item SPEER_FRAGMENT = registerItem("speer_fragment", settings -> new SpeerFragmentItem(
            ModToolMaterials.FRAGMENTS_TOOL_MATERIAL, 4, 3F, settings
                        .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true))
                        .rarity(Rarity.EPIC)
                        .maxCount(1)));

    public static void initialize() {
        Fragmentsmod.LOGGER.info("Registering Mod Items for " + Fragmentsmod.MOD_ID);
    }

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Fragmentsmod.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Fragmentsmod.MOD_ID, name)))));
    }
}
