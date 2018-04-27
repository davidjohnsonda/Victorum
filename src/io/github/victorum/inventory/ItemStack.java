package io.github.victorum.inventory;

public class ItemStack {
    private InventoryRenderable inventoryRenderable;
    private int count;

    public ItemStack(InventoryRenderable inventoryRenderable, int count) {
        this.inventoryRenderable = inventoryRenderable;
        this.count = count;
    }

    public InventoryRenderable getInventoryRenderable() {
        return inventoryRenderable;
    }

    public void setInventoryRenderable(InventoryRenderable inventoryRenderable) {
        this.inventoryRenderable = inventoryRenderable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
