package io.github.victorum;

import com.jme3.app.SimpleApplication;

import com.jme3.math.ColorRGBA;
import io.github.victorum.world.WorldAppState;

public class Victorum extends SimpleApplication{
    private final WorldAppState worldAppState = new WorldAppState();

    public static void main(String[] args){
        Victorum victorum = new Victorum();
        victorum.start();
    }

    @Override
    public void simpleInitApp(){
        stateManager.attach(worldAppState);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(128f);
        viewPort.setBackgroundColor(ColorRGBA.Blue);
    }

    @Override
    public void stop(){
        super.stop();
        System.exit(0); //neccessary because of background threads in io.github.victorum.util.ThreadingUtil
    }

    public WorldAppState getWorldAppState(){
        return worldAppState;
    }

}