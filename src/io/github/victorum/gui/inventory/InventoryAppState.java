package io.github.victorum.gui.inventory;

import com.jme3.app.Application;
import io.github.victorum.Victorum;
import io.github.victorum.block.BlockRegistry;
import io.github.victorum.util.VAppState;

import java.util.ArrayList;

public class InventoryAppState extends VAppState {
    private ArrayList<GenericItem> inventory;
    private static final int inventorySize = 36;
    private static final int hotBarSize = 9;
    private Victorum application;
    private int selectedIndex = 0;
    @Override
    protected void initialize(Application application) {
        inventory = new ArrayList<GenericItem>();
        for(int i = 0; i < inventorySize; i++)
        {
            inventory.add(new GenericItem(ItemRegistry.ITEM_TYPE_EMPTY, 1));
        }
        inventory.set(0, new GenericItem(ItemRegistry.ITEM_TYPE_AXE, 2));
        inventory.set(1, new GenericItem(ItemRegistry.ITEM_TYPE_PICKAXE, 2));
        inventory.set(2, new GenericItem(ItemRegistry.ITEM_TYPE_SHOVEL, 2));
        inventory.set(3, new GenericItem(ItemRegistry.ITEM_TYPE_AXE, 1));
        inventory.set(4, new GenericItem(ItemRegistry.ITEM_TYPE_PICKAXE, 1));
        inventory.set(5, new GenericItem(ItemRegistry.ITEM_TYPE_SHOVEL, 1));
        this.application = (Victorum)application;
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

    public ArrayList<GenericItem> getInventory()
    {
        return inventory;
    }

    public void setInventory(ArrayList<GenericItem> input)
    {
        inventory = input;
    }

    public static int getInventorySize()
    {
        return inventorySize;
    }

    public static int getHotBarSize()
    {
        return hotBarSize;
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
