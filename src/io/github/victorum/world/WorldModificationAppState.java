package io.github.victorum.world;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.util.VAppState;


public class WorldModificationAppState extends VAppState implements ActionListener {
    private static final float INTERVAL_DISTANCE = 0.01f;
    private static final int INTERVAL_COUNT = (int)Math.ceil(15/INTERVAL_DISTANCE);
    private int blockX, blockY, blockZ, placeBlockX, placeBlockY, placeBlockZ;

    @Override
    protected void initialize(Application application) {
        /*blockCursor = new Geometry("Cursor", new Box(0.6f, 0.6f, 0.6f));
        Material material = new Material(getVictorum().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.White);
        blockCursor.setMaterial(material);
        getVictorum().getRootNode().attachChild(blockCursor);*/

        InputManager inputManager = getVictorum().getInputManager();
        inputManager.addMapping("blockplace", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("blockbreak", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "blockplace", "blockbreak");
    }

    private void updateBlockData(){
        Vector3f location = getVictorum().getCamera().getLocation();
        Vector3f direction = getVictorum().getCamera().getDirection().normalize();
        Vector3f currentLocation = new Vector3f();

        for(int i=0;i<INTERVAL_COUNT;++i){
            currentLocation.set(direction);
            currentLocation.multLocal(i*INTERVAL_DISTANCE);
            currentLocation.addLocal(location);

            placeBlockX = blockX;
            placeBlockY = blockY;
            placeBlockZ = blockZ;

            blockX = (int)Math.floor(currentLocation.x);
            blockY = (int)Math.floor(currentLocation.y);
            blockZ = (int)Math.floor(currentLocation.z);

            if(getVictorum().getWorldAppState().getWorld().getBlockTypeAt(blockX, blockY, blockZ).isSolid()){
                break;
            }
        }
    }

    @Override
    public void onAction(String label, boolean value, float f) {
        updateBlockData();
        switch(label){
            case "blockbreak":
                getVictorum().getWorldAppState().getWorld().setBlockTypeAt(blockX, blockY, blockZ, BlockRegistry.BLOCK_TYPE_AIR);
                break;
            case "blockplace":
                getVictorum().getWorldAppState().getWorld().setBlockTypeAt(placeBlockX, placeBlockY, placeBlockZ, BlockRegistry.BLOCK_TYPE_STONE);
                break;
        }
    }

    @Override protected void cleanup(Application application) {}
    @Override protected void onEnable() {  }
    @Override protected void onDisable() {  }

}
