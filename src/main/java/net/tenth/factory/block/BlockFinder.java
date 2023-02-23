package net.tenth.factory.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockFinder {
    public static Block getNorth(BlockEntity pEntity) {
        return pEntity.getLevel().getBlockState(pEntity.getBlockPos().north()).getBlock();
    }

    public static Block getEast(BlockEntity pEntity) {
        return pEntity.getLevel().getBlockState(pEntity.getBlockPos().east()).getBlock();
    }

    public static Block getSouth(BlockEntity pEntity) {
        return pEntity.getLevel().getBlockState(pEntity.getBlockPos().south()).getBlock();
    }

    public static Block getWest(BlockEntity pEntity) {
        return pEntity.getLevel().getBlockState(pEntity.getBlockPos().west()).getBlock();
    }

    public static Direction getFacing(BlockEntity pEntity) {
        System.out.println("The pEntity in getFacing is: " + pEntity);
        try { pEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING); }
        catch (IllegalArgumentException e) {
            return null;
        }
        return pEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
    }

    public static Block getBlockFromCoords(BlockEntity pEntity, int x, int y, int z) {
        return pEntity.getLevel().getBlockState((new BlockPos(x, y, z))).getBlock();
    }

    public static Boolean getMultiblock(BlockEntity pEntity, Block leftBlock, Block rightBlock, Block frameBlock) {
        if(pEntity == null) {
            return false;
        }
        BlockPos mainBlockPos = pEntity.getBlockPos();
        int x = mainBlockPos.getX();
        int y = mainBlockPos.getY();
        int z = mainBlockPos.getZ();
        switch(getFacing(pEntity)) {
            case NORTH -> {
                if(getBlockFromCoords(pEntity, x, y + 1, z) == frameBlock
                && getBlockFromCoords(pEntity, x, y + 2, z) == frameBlock
                && getBlockFromCoords(pEntity, x + 1, y, z) == leftBlock
                && getBlockFromCoords(pEntity, x + 1, y + 1, z) == frameBlock
                && getBlockFromCoords(pEntity, x + 1, y + 2, z) == frameBlock
                && getBlockFromCoords(pEntity, x - 1, y, z) == rightBlock
                && getBlockFromCoords(pEntity, x - 1, y + 1, z) == frameBlock
                && getBlockFromCoords(pEntity, x - 1, y + 2, z) == frameBlock) {
                    System.out.println("Found Multiblock of: ");
                    System.out.println("Frame Frame Frame");
                    System.out.println("Frame Frame Frame");
                    System.out.println("Tank Chest Tank");
                }
            }
            case EAST -> {

                }
            case SOUTH -> {

                }
            case WEST -> {

                }
            }
        return false;
        }
}
