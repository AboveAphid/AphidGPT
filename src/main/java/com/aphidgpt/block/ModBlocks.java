package com.aphidgpt.block;

import com.aphidgpt.AphidGPT;
import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block STEEL_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).requiresTool().strength(7.0F).sounds(BlockSoundGroup.METAL));

    public static final Block POWER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).requiresTool().strength(9.0F).sounds(BlockSoundGroup.COPPER_BULB));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(AphidGPT.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(AphidGPT.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        AphidGPT.LOGGER.info("Registering custom blocks...");


        registerBlock("steel_block", STEEL_BLOCK);
        registerBlock("power_block", POWER_BLOCK);

    }
}