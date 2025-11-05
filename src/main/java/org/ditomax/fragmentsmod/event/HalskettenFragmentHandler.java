package org.ditomax.fragmentsmod.event;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.ditomax.fragmentsmod.item.ModItems;
import org.ditomax.fragmentsmod.util.config.ModConfig;

public class HalskettenFragmentHandler {

    public static void register() {
        net.fabricmc.fabric.api.event.player.AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && entity instanceof LivingEntity target) {

                if (hasHalskettenFragmentInInventory(player)) {
                    damageArmor(target, ModConfig.getActiveConfig().halsketteArmorDamageBoost);
                }
            }
            return ActionResult.PASS;
        });
    }

    private static boolean hasHalskettenFragmentInInventory(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(ModItems.FRAGMENT_HALSKETTE)) {
                return true;
            }
        }
        return false;
    }

    private static void damageArmor(LivingEntity entity, float amount) {
        EquipmentSlot[] armorSlots = {
                EquipmentSlot.FEET,
                EquipmentSlot.LEGS,
                EquipmentSlot.CHEST,
                EquipmentSlot.HEAD
        };

        for (EquipmentSlot slot : armorSlots) {
            ItemStack armorPiece = entity.getEquippedStack(slot);
            if (!armorPiece.isEmpty() && armorPiece.isDamageable()) {
                armorPiece.damage((int) amount, entity, slot);
            }
        }
    }
}
