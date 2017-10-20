package io.github.victorum.block;

public class BlockType {
    private final int blockId;
    private final TextureCoordinates textureCoordinates;

    public BlockType(int blockId, TextureCoordinates textureCoordinates){
        this.blockId = blockId;
        this.textureCoordinates = textureCoordinates;
    }

    public final int getBlockId(){
        return blockId;
    }

    public TextureCoordinates getTexture(BlockSide side){
        return textureCoordinates;
    }

}
