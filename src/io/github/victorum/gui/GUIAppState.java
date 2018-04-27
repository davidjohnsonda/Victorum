package io.github.victorum.gui;

import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;

import io.github.victorum.inventory.ItemStack;
import io.github.victorum.inventory.TextureCoordinates;
import io.github.victorum.inventory.InventoryAppState;
import io.github.victorum.util.MeshGenerator;
import io.github.victorum.util.VAppState;

public class GUIAppState extends VAppState implements ActionListener {
    private static final int INVENTORY_BLOCK_SIZE = 48;
    private Node inventoryNode;

    @Override
    protected void initialize(Application application) {
        getVictorum().getInputManager().addMapping("0", new KeyTrigger(KeyInput.KEY_0));
        getVictorum().getInputManager().addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        getVictorum().getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        getVictorum().getInputManager().addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        getVictorum().getInputManager().addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        getVictorum().getInputManager().addMapping("5", new KeyTrigger(KeyInput.KEY_5));
        getVictorum().getInputManager().addMapping("6", new KeyTrigger(KeyInput.KEY_6));
        getVictorum().getInputManager().addMapping("7", new KeyTrigger(KeyInput.KEY_7));
        getVictorum().getInputManager().addMapping("8", new KeyTrigger(KeyInput.KEY_8));
        getVictorum().getInputManager().addMapping("9", new KeyTrigger(KeyInput.KEY_9));
        getVictorum().getInputManager().addListener(this, "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        inventoryNode = new Node("Inventory Node");
        getVictorum().getGuiNode().attachChild(inventoryNode);

        refreshInventory();
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

    private void refreshInventory() {
        inventoryNode.detachAllChildren();
        for(int i = 0; i < InventoryAppState.HOT_BAR_SIZE; i++) {
            ItemStack itemStack = getVictorum().getInventoryAppState().getItemAt(i);
            if(itemStack != null){
                displayItem(itemStack, i);
            }
        }
        displaySelectionBox();
    }

    private void displaySelectionBox()
    {
        Picture pic = new Picture("SelectionBox");
        pic.setImage(getVictorum().getAssetManager(), "/selectionbox.png", true);
        pic.setWidth(INVENTORY_BLOCK_SIZE);
        pic.setHeight(INVENTORY_BLOCK_SIZE);
        pic.setPosition(getInventoryOffset() + INVENTORY_BLOCK_SIZE * getVictorum().getInventoryAppState().getSelectedIndex(), 0);
        inventoryNode.attachChild(pic);
    }

    private void displayItem(ItemStack item, int index) {
        MeshGenerator mesh = new MeshGenerator();
        Material material = new Material(getVictorum().getAssetManager(), "/Common/MatDefs/Misc/Unshaded.j3md");
        Texture textureAtlas = getVictorum().getAssetManager().loadTexture(new TextureKey(item.getInventoryRenderable().getInventoryAtlas(), false));
        textureAtlas.setMagFilter(Texture.MagFilter.Nearest);
        textureAtlas.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        textureAtlas.setAnisotropicFilter(2);
        material.setTexture("ColorMap", textureAtlas);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        Vector3f a = new Vector3f(getInventoryOffset() + INVENTORY_BLOCK_SIZE * index, 0, 0);
        Vector3f b = new Vector3f(getInventoryOffset() + INVENTORY_BLOCK_SIZE * (index + 1), 0, 0);
        Vector3f c = new Vector3f(getInventoryOffset() + INVENTORY_BLOCK_SIZE * (index + 1), INVENTORY_BLOCK_SIZE, 0);
        Vector3f d = new Vector3f(getInventoryOffset() + INVENTORY_BLOCK_SIZE * index, INVENTORY_BLOCK_SIZE, 0);
        TextureCoordinates textureCoordinates = item.getInventoryRenderable().getInventoryAtlasCoordinates();
        int aid = mesh.addVertex(a, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasEndY());
        int bid = mesh.addVertex(b, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasEndY());
        int cid = mesh.addVertex(c, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasStartY());
        int did = mesh.addVertex(d, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasStartY());
        mesh.getIndices().add(aid);
        mesh.getIndices().add(bid);
        mesh.getIndices().add(cid);
        mesh.getIndices().add(cid);
        mesh.getIndices().add(did);
        mesh.getIndices().add(aid);
        Geometry geometry = new Geometry("InventoryItem", mesh.toMesh());
        geometry.setMaterial(material);
        inventoryNode.attachChild(geometry);
        if(item.getCount() != 1)
        {
            BitmapText hudText = new BitmapText(getVictorum().getGuiFont(), false);
            hudText.setSize(getVictorum().getGuiFont().getCharSet().getRenderedSize());
            hudText.setColor(ColorRGBA.White);
            hudText.setText(Integer.toString(item.getCount()));
            hudText.setLocalTranslation(getInventoryOffset() + INVENTORY_BLOCK_SIZE * index, hudText.getLineHeight(), 0);
            inventoryNode.attachChild(hudText);
        }
    }

    private int getInventoryOffset()
    {
        int width = getVictorum().getSettings().getWidth();
        int hotBarWidth = InventoryAppState.HOT_BAR_SIZE * INVENTORY_BLOCK_SIZE;
        int availableWidth = width - hotBarWidth;
        return availableWidth / 2;
    }

    @Override
    public void onAction(String s, boolean b, float v) {
        if (b) {
            int initialIndex = Integer.parseInt(s) - 1;
            if (initialIndex == -1) {
                initialIndex = 9;
            }
            int index = Math.min(InventoryAppState.HOT_BAR_SIZE - 1, Math.max(0, initialIndex));
            getVictorum().getInventoryAppState().setSelectedIndex(index);
            refreshInventory();
        }
    }
}
