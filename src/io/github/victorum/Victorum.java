package io.github.victorum;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Vector3f;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import io.github.victorum.player.Player;
import io.github.victorum.world.WorldAppState;

public class Victorum extends SimpleApplication{
    private final WorldAppState worldAppState = new WorldAppState();
    private final Player playerAppState = new Player();
    public static void main(String[] args){
        Victorum victorum = new Victorum();
        victorum.start();
    }

    @Override
    public void simpleInitApp(){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        stateManager.attach(worldAppState);
        flyCam.setMoveSpeed(16f);
        cam.setLocation(new Vector3f(0, 145, 0));
        stateManager.attach(playerAppState);
        flyCam.setDragToRotate(false);
        flyCam.setMoveSpeed(0f);
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_MEMORY);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_HIDE_STATS);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_CAMERA_POS);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Forward",  new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("MouseLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("MouseRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("MouseUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("MouseDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(playerAppState, "Left", "Right", "Forward", "Backward", "Jump", "MouseLeft", "MouseRight", "MouseUp", "MouseDown", "Click");
    }

    @Override
    public void simpleUpdate(float tpf){

    }

    @Override
    public void stop(){
        super.stop();
        System.exit(0); //necessary because of background threads in io.github.victorum.util.ThreadingUtil
    }

    public WorldAppState getWorldAppState(){
        return worldAppState;
    }

}
