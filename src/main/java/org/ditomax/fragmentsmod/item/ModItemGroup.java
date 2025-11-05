package org.ditomax.fragmentsmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ditomax.fragmentsmod.Fragmentsmod;

public class ModItemGroup {

    public static final ItemGroup FRAGMENTS_ITEMS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(org.ditomax.fragmentsmod.Fragmentsmod.MOD_ID, "fragments_item"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.SCHATTENSCHLEIER))
                            .displayName(Text.translatable("itemgroup.fragmentsmod.fragments_items"))
                            .entries(((displayContext, entries) -> {
                                        entries.add(ModItems.SCHATTENSCHLEIER);
                                        entries.add(ModItems.BOGEN_FRAGMENT);
                                        entries.add(ModItems.MACE_FRAGMENT);
                                        entries.add(ModItems.FRAGMENT_SCHWERT);
                                        entries.add(ModItems.BRUSTPLATTEN_FRAGMENT);
                                        entries.add(ModItems.STAERKE_FRAGMENT);
                                        entries.add(ModItems.ELYTRA_FRAGMENT);
                                        entries.add(ModItems.PANDORA_FRAGMENT);
                                        entries.add(ModItems.FRAGMENT_HALSKETTE);
                                        entries.add(ModItems.SPITZHACKEN_FRAGMENT);
                                        entries.add(ModItems.PFLANZEN_FRAGMENT);
                                        entries.add(ModItems.HORDENTREIBER_FRAGMENT);
                                        entries.add(ModItems.SPEER_FRAGMENT);
                            })).build());

    public static void initialize() {
        Fragmentsmod.LOGGER.info("Registering Item Groups for " + Fragmentsmod.MOD_ID);
    }
}
