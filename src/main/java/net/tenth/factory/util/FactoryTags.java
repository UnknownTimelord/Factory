package net.tenth.factory.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.tenth.factory.Factory;

public class FactoryTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_COPPER_TOOL = tag("needs_copper_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Factory.MOD_ID, name));
        }
        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }
}
