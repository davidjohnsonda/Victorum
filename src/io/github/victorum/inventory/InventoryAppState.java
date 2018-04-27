package io.github.victorum.inventory;

import com.jme3.app.Application;
import io.github.victorum.inventory.item.ItemRegistry;
import io.github.victorum.util.VAppState;

import java.util.ArrayList;

public class InventoryAppState extends VAppState {
    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    public static final int INVENTORY_SIZE = 36;
    public static final int HOT_BAR_SIZE = 9;
    private int selectedIndex = 0;

    @Override
    protected void initialize(Application application) {
        inventory[0] = new ItemStack(ItemRegistry.ITEM_TYPE_AXE, 2);
        inventory[1] = new ItemStack(ItemRegistry.ITEM_TYPE_PICKAXE, 2);
        inventory[2] = new ItemStack(ItemRegistry.ITEM_TYPE_SHOVEL, 2);
        inventory[3] = new ItemStack(ItemRegistry.ITEM_TYPE_AXE, 1);
        inventory[4] = new ItemStack(ItemRegistry.ITEM_TYPE_PICKAXE, 1);
        inventory[5] = new ItemStack(ItemRegistry.ITEM_TYPE_SHOVEL, 1);
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    public void update(float tpf) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public ItemStack getItemAt(int index){
        return inventory[index];
    }

    public void setItemAt(int index, ItemStack itemStack){
        inventory[index] = itemStack;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }

}
