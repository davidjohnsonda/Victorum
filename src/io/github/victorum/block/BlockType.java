package io.github.victorum.block;

public class BlockType {
    private final int blockId;
    private final TextureCoordinates textureCoordinates;
    private final boolean isSolid;

    public BlockType(int blockId, TextureCoordinates textureCoordinates){
        this(blockId, textureCoordinates, true);
    }

    public BlockType(int blockId, TextureCoordinates textureCoordinates, boolean isSolid){
        this.blockId = blockId;
        this.textureCoordinates = textureCoordinates;
        this.isSolid = isSolid;
    }

    public final boolean isSolid(){
        return isSolid;
    }

    public final int getBlockId(){
        return blockId;
    }

    public TextureCoordinates getTexture(BlockSide side){
        return textureCoordinates;
    }

}
