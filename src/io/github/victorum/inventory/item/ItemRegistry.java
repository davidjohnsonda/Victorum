package io.github.victorum.inventory.item;

import io.github.victorum.inventory.TextureCoordinates;

public final class ItemRegistry {
    public static ItemType ITEM_TYPE_EMPTY = new ItemType(0, new TextureCoordinates(0, 1));
    public static ItemType ITEM_TYPE_SHOVEL = new ItemType(1, new TextureCoordinates(0, 0));
    public static ItemType ITEM_TYPE_AXE = new ItemType(2, new TextureCoordinates(1, 0));
    public static ItemType ITEM_TYPE_PICKAXE = new ItemType(3, new TextureCoordinates(2, 0));
    private static ItemType[] itemTypes = new ItemType[4];

    private ItemRegistry(){}

    static {
        registerItem(ITEM_TYPE_EMPTY);
        registerItem(ITEM_TYPE_SHOVEL);
        registerItem(ITEM_TYPE_AXE);
        registerItem(ITEM_TYPE_PICKAXE);
    }

    private static void registerItem(ItemType itemType){
        itemTypes[itemType.getItemId()] = itemType;
    }

    public static ItemType getItemType(int id){
        return itemTypes[id];
    }

}
