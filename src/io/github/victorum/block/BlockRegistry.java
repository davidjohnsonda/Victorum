package io.github.victorum.block;

import io.github.victorum.block.types.BlockTypeGrass;

public final class BlockRegistry{
    public static BlockType BLOCK_TYPE_AIR = new BlockType(0, null);
    public static BlockType BLOCK_TYPE_GRASS = new BlockTypeGrass(1);
    private static BlockType[] blockTypes = new BlockType[2];

    private BlockRegistry(){}

    static {
        registerBlock(BLOCK_TYPE_AIR);
        registerBlock(BLOCK_TYPE_GRASS);
    }

    private static void registerBlock(BlockType blockType){
        blockTypes[blockType.getBlockId()] = blockType;
    }

    public static BlockType getBlockType(int id){
        return blockTypes[id];
    }

}
