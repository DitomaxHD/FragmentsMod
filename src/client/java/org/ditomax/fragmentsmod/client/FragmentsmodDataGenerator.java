package org.ditomax.fragmentsmod.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.ditomax.fragmentsmod.datagen.ModItemTagProvider;
import org.ditomax.fragmentsmod.datagen.ModModelProvider;

public class FragmentsmodDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModItemTagProvider::new);
    }
}
