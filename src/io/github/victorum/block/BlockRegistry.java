package io.github.victorum.block;

import io.github.victorum.block.types.BlockTypeGrass;

public final class BlockRegistry{
    public static BlockType BLOCK_TYPE_AIR = new BlockType(0, null, false);
    public static BlockType BLOCK_TYPE_GRASS = new BlockTypeGrass(1);
    public static BlockType BLOCK_TYPE_DIRT = new BlockType(2, new TextureCoordinates(2, 0));
    public static BlockType BLOCK_TYPE_STONE = new BlockType(3, new TextureCoordinates(0, 1));
    public static BlockType BLOCK_TYPE_SAND = new BlockType(4, new TextureCoordinates(1, 3));
    public static BlockType BLOCK_TYPE_WATER = new BlockType(5, new TextureCoordinates(0, 3), false);
    public static BlockType BLOCK_TYPE_LOG = new BlockType(6, new TextureCoordinates(0, 2));
    public static BlockType BLOCK_TYPE_LEAVES = new BlockType(7, new TextureCoordinates(2, 2));
    private static BlockType[] blockTypes = new BlockType[8];

    private BlockRegistry(){}

    static {
        registerBlock(BLOCK_TYPE_AIR);
        registerBlock(BLOCK_TYPE_GRASS);
        registerBlock(BLOCK_TYPE_DIRT);
        registerBlock(BLOCK_TYPE_STONE);
        registerBlock(BLOCK_TYPE_SAND);
        registerBlock(BLOCK_TYPE_WATER);
        registerBlock(BLOCK_TYPE_LOG);
        registerBlock(BLOCK_TYPE_LEAVES);
    }

    private static void registerBlock(BlockType blockType){
        blockTypes[blockType.getBlockId()] = blockType;
    }

    public static BlockType getBlockType(int id){
        return blockTypes[id];
    }

}
