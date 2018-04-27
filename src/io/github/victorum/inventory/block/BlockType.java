package io.github.victorum.inventory.block;

import io.github.victorum.inventory.InventoryRenderable;
import io.github.victorum.inventory.TextureCoordinates;

public class BlockType implements InventoryRenderable {
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

    @Override
    public String getInventoryAtlas() {
        return "atlas.png";
    }

    @Override
    public TextureCoordinates getInventoryAtlasCoordinates() {
        return getTexture(BlockSide.POSITIVE_Z);
    }
}
