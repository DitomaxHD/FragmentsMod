package org.ditomax.fragmentsmod.datagen;


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import org.ditomax.fragmentsmod.item.ModItems;
import org.ditomax.fragmentsmod.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.FRAGMENTS_ITEMS)
                .add(ModItems.SCHATTENSCHLEIER)
                .add(ModItems.BOGEN_FRAGMENT)
                .add(ModItems.MACE_FRAGMENT)
                .add(ModItems.FRAGMENT_SCHWERT)
                .add(ModItems.BRUSTPLATTEN_FRAGMENT)
                .add(ModItems.STAERKE_FRAGMENT)
                .add(ModItems.ELYTRA_FRAGMENT)
                .add(ModItems.PANDORA_FRAGMENT)
                .add(ModItems.FRAGMENT_HALSKETTE)
                .add(ModItems.SPITZHACKEN_FRAGMENT)
                .add(ModItems.PFLANZEN_FRAGMENT)
                .add(ModItems.HORDENTREIBER_FRAGMENT)
                .add(ModItems.SPEER_FRAGMENT);
    }
}
