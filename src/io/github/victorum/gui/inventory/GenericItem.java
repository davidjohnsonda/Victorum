package io.github.victorum.gui.inventory;

import io.github.victorum.block.BlockType;

public class GenericItem {
    private BlockType blockType;
    private ItemType itemType;
    private GenericItemType blockOrItem;
    private int quantity;
    public GenericItem(BlockType type, int count)
    {
        blockType = type;
        itemType = null;
        blockOrItem = GenericItemType.BLOCK;
        quantity = count;
        if(quantity == 0)
        {
            setType(ItemRegistry.ITEM_TYPE_EMPTY);
            quantity = 1;
        }
    }
    public GenericItem(ItemType type, int count)
    {
        blockType = null;
        itemType = type;
        blockOrItem = GenericItemType.ITEM;
        quantity = count;
        if(quantity == 0)
        {
            setType(ItemRegistry.ITEM_TYPE_EMPTY);
            quantity = 1;
        }
    }
    public BlockType getBlockType()
    {
        return blockType;
    }
    public ItemType getItemType()
    {
        return itemType;
    }
    public GenericItemType getBlockOrItem()
    {
        return blockOrItem;
    }
    public Object getType()
    {
        if(blockOrItem == GenericItemType.BLOCK)
        {
            return blockType;
        }
        else
        {
            return itemType;
        }
    }
    public void setType(BlockType type)
    {
        blockType = type;
        itemType = null;
        blockOrItem = GenericItemType.BLOCK;
    }
    public void setType(ItemType type)
    {
        blockType = null;
        itemType = type;
        blockOrItem = GenericItemType.ITEM;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int count)
    {
        quantity = count;
        if(quantity == 0)
        {
            setType(ItemRegistry.ITEM_TYPE_EMPTY);
            quantity = 1;
        }
    }
}
