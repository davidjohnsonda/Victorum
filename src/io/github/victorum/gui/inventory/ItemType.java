package io.github.victorum.gui.inventory;

import io.github.victorum.block.TextureCoordinates;

public class ItemType {
    private final int itemId;
    private final TextureCoordinates textureCoordinates;

    public ItemType(int itemId, TextureCoordinates textureCoordinates)
    {
        this.itemId = itemId;
        this.textureCoordinates = textureCoordinates;
    }

    public final int getItemId(){
        return itemId;
    }

    public TextureCoordinates getTexture(){
        return textureCoordinates;
    }

}
