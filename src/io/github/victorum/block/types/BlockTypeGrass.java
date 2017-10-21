package io.github.victorum.block.types;

import io.github.victorum.block.BlockSide;
import io.github.victorum.block.BlockType;
import io.github.victorum.block.TextureCoordinates;

public class BlockTypeGrass extends BlockType{
    private static final TextureCoordinates TOP_COORDINATES = new TextureCoordinates(0, 0);
    private static final TextureCoordinates BOTTOM_COORDINATES = new TextureCoordinates(2, 0);
    private static final TextureCoordinates SIDE_COORDINATES = new TextureCoordinates(1, 0);

    public BlockTypeGrass(int blockId){
        super(blockId, null);
    }

    @Override
    public TextureCoordinates getTexture(BlockSide side){
        switch(side){
            case POSITIVE_Y:
                return TOP_COORDINATES;
            case NEGATIVE_Y:
                return BOTTOM_COORDINATES;
            default:
                return SIDE_COORDINATES;
        }
    }

}
