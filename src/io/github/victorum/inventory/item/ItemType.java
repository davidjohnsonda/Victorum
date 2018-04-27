package io.github.victorum.inventory.item;

import io.github.victorum.inventory.InventoryRenderable;
import io.github.victorum.inventory.TextureCoordinates;

public class ItemType implements InventoryRenderable{
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

    @Override
    public TextureCoordinates getInventoryAtlasCoordinates(){
        return textureCoordinates;
    }

    @Override
    public String getInventoryAtlas() {
        return "guiatlas.png";
    }

}
