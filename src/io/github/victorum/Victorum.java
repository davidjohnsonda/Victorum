package io.github.victorum;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import com.jme3.system.AppSettings;
import io.github.victorum.entity.EntityAppState;
import io.github.victorum.world.WorldAppState;
import io.github.victorum.world.WorldModificationAppState;

public class Victorum extends SimpleApplication{
    private final WorldAppState worldAppState = new WorldAppState();
    private final EntityAppState entityAppState = new EntityAppState();
    private final WorldModificationAppState worldModificationAppState = new WorldModificationAppState();

    public static void main(String[] args){
        Victorum victorum = new Victorum();
        victorum.start();
    }

    @Override
    public void simpleInitApp() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        stateManager.attach(worldAppState);
        stateManager.attach(entityAppState);
        stateManager.attach(worldModificationAppState);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(16f);
        cam.setLocation(new Vector3f(0, 145, 0));
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        cam.setFrustumPerspective(90, settings.getWidth() / ((float) settings.getHeight()), 0.001f, 10000);
        setDisplayStatView(false);
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

    public EntityAppState getEntityAppState(){
        return entityAppState;
    }

    public AppSettings getSettings(){
        return settings;
    }

}
