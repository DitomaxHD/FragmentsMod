package org.ditomax.fragmentsmod.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import org.ditomax.fragmentsmod.item.ModItems;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SCHATTENSCHLEIER, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MACE_FRAGMENT, Models.HANDHELD_MACE);
        itemModelGenerator.register(ModItems.FRAGMENT_SCHWERT, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STAERKE_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.ELYTRA_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.PANDORA_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.FRAGMENT_HALSKETTE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPITZHACKEN_FRAGMENT, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PFLANZEN_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.HORDENTREIBER_FRAGMENT, Models.HANDHELD);
        itemModelGenerator.register(ModItems.SPEER_FRAGMENT, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BRUSTPLATTEN_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.BOGEN_FRAGMENT, Models.GENERATED);
    }
}