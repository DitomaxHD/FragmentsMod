package org.ditomax.fragmentsmod.item;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.ditomax.fragmentsmod.Fragmentsmod;
import org.ditomax.fragmentsmod.util.ModTags;

import java.util.EnumMap;

public class ModArmorMaterials {
    public static final RegistryKey<EquipmentAsset> FRAGMENT_ASSET_KEY =
            RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Fragmentsmod.MOD_ID, "brustplatten_fragment"));

    public static final ArmorMaterial FRAGMENT_ARMOR_MATERIAL = new ArmorMaterial(500, Util.make(new EnumMap<>(EquipmentType.class), map -> {
        map.put(EquipmentType.BOOTS, 3);
        map.put(EquipmentType.LEGGINGS, 6);
        map.put(EquipmentType.CHESTPLATE, 8);
        map.put(EquipmentType.HELMET, 3);
        map.put(EquipmentType.BODY, 11);
    }), 20, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F , 0.1F, ModTags.Items.FRAGMENTS_ITEMS, FRAGMENT_ASSET_KEY);
}
