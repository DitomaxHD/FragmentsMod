package org.ditomax.fragmentsmod.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class ModToolMaterials{

    public static final ToolMaterial FRAGMENTS_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, Integer.MAX_VALUE, 1F, 1F, 22, ItemTags.NETHERITE_TOOL_MATERIALS);

    public static final ToolMaterial PICKAXE_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2000,
            1.010f,
            8.0f,
            22,
            ItemTags.NETHERITE_TOOL_MATERIALS
    );
}
