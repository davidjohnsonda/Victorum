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
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import io.github.victorum.Victorum;
import io.github.victorum.block.BlockSide;
import io.github.victorum.block.TextureCoordinates;
import io.github.victorum.gui.inventory.GenericItem;
import io.github.victorum.gui.inventory.GenericItemType;
import io.github.victorum.gui.inventory.InventoryAppState;
import io.github.victorum.util.MeshGenerator;
import io.github.victorum.util.VAppState;

import java.util.ArrayList;
import java.util.List;

public class GUIAppState extends VAppState implements ActionListener {
    private Victorum application;
    private static final int inventoryBlockSize = 48;
    @Override
    protected void initialize(Application application) {
        this.application = (Victorum)application;
        this.application.getInputManager().addMapping("0", new KeyTrigger(KeyInput.KEY_0));
        this.application.getInputManager().addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        this.application.getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        this.application.getInputManager().addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        this.application.getInputManager().addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        this.application.getInputManager().addMapping("5", new KeyTrigger(KeyInput.KEY_5));
        this.application.getInputManager().addMapping("6", new KeyTrigger(KeyInput.KEY_6));
        this.application.getInputManager().addMapping("7", new KeyTrigger(KeyInput.KEY_7));
        this.application.getInputManager().addMapping("8", new KeyTrigger(KeyInput.KEY_8));
        this.application.getInputManager().addMapping("9", new KeyTrigger(KeyInput.KEY_9));
        this.application.getInputManager().addListener(this, "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    public void update(float tpf) {
        application.getGuiNode().detachAllChildren();
        displayInventory();
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    private void displayInventory()
    {
        ArrayList<GenericItem> inventoryItems = application.getInventoryAppState().getInventory();
        for(int i = 0; i < InventoryAppState.getHotBarSize(); i++)
        {
            displayItem(inventoryItems.get(i), i);
        }
        displaySelectionBox();
    }

    private void displaySelectionBox()
    {
        Picture pic = new Picture("SelectionBox");
        pic.setImage(application.getAssetManager(), "/selectionbox.png", true);
        pic.setWidth(inventoryBlockSize);
        pic.setHeight(inventoryBlockSize);
        pic.setPosition(getInventoryOffset() + inventoryBlockSize * application.getInventoryAppState().getSelectedIndex(), 0);
        application.getGuiNode().attachChild(pic);
    }

    private void displayItem(GenericItem item, int index)
    {
        String path = (item.getBlockOrItem() == GenericItemType.BLOCK) ? "/atlas.png" : "/guiatlas.png";
        MeshGenerator mesh = new MeshGenerator();
        Material material = new Material(getVictorum().getAssetManager(), "/Common/MatDefs/Misc/Unshaded.j3md");
        Texture textureAtlas = getVictorum().getAssetManager().loadTexture(new TextureKey(path, false));
        textureAtlas.setMagFilter(Texture.MagFilter.Nearest);
        textureAtlas.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        textureAtlas.setAnisotropicFilter(2);
        material.setTexture("ColorMap", textureAtlas);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        Vector3f a = new Vector3f(getInventoryOffset() + inventoryBlockSize * index, 0, 0);
        Vector3f b = new Vector3f(getInventoryOffset() + inventoryBlockSize * (index + 1), 0, 0);
        Vector3f c = new Vector3f(getInventoryOffset() + inventoryBlockSize * (index + 1), inventoryBlockSize, 0);
        Vector3f d = new Vector3f(getInventoryOffset() + inventoryBlockSize * index, inventoryBlockSize, 0);
        TextureCoordinates textureCoordinates = (item.getBlockOrItem() == GenericItemType.BLOCK) ? item.getBlockType().getTexture(BlockSide.NEGATIVE_Z) : item.getItemType().getTexture();
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
        application.getGuiNode().attachChild(geometry);
        if(item.getQuantity() != 1)
        {
            BitmapText hudText = new BitmapText(application.getGuiFont(), false);
            hudText.setSize(application.getGuiFont().getCharSet().getRenderedSize());
            hudText.setColor(ColorRGBA.White);
            hudText.setText(Integer.toString(item.getQuantity()));
            hudText.setLocalTranslation(getInventoryOffset() + inventoryBlockSize * index, hudText.getLineHeight(), 0);
            application.getGuiNode().attachChild(hudText);
        }
    }

    private int getInventoryOffset()
    {
        int width = application.getSettings().getWidth();
        int hotBarWidth = InventoryAppState.getHotBarSize() * inventoryBlockSize;
        int availableWidth = width - hotBarWidth;
        return availableWidth / 2;
    }

    @Override
    public void onAction(String s, boolean b, float v) {
        if(b)
        {
            boolean isInteger = false;
            try
            {
                int outputTest = Integer.parseInt(s);
                isInteger = true;
            }
            catch (Exception e)
            {

            }
            if(isInteger)
            {
                int initialIndex = Integer.parseInt(s) - 1;
                if(initialIndex == -1)
                {
                    initialIndex = 9;
                }
                int index = Math.min(InventoryAppState.getHotBarSize() - 1, Math.max(0, initialIndex));
                application.getInventoryAppState().setSelectedIndex(index);
            }
        }
    }
}
