package io.github.victorum.entity;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;

import io.github.victorum.util.VAppState;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityAppState extends VAppState{
    private PlayerEntity playerEntity;
    private ArrayList<Entity> entityArrayList = new ArrayList<>();

    @Override
    protected void initialize(Application application) {
        playerEntity = new PlayerEntity(getVictorum());
        addEntity(playerEntity, new Vector3f(0, 256, 0));
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    public void update(float tpf){
        super.update(tpf);
        Iterator<Entity> entityIterator = entityArrayList.iterator();
        while(entityIterator.hasNext()){
            Entity entity = entityIterator.next();
            entity.update(tpf);
            entity.updatePhysics(tpf);
            if(entity.isRemoved()){
                getVictorum().getRootNode().detachChild(entity.getSpatial());
                entityIterator.remove();
                System.out.println("Actually removing entity");
            }
        }
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void addEntity(Entity entity, Vector3f location){
        entity.getSpatial().setLocalTranslation(location);
        getVictorum().getRootNode().attachChild(entity.getSpatial());
        entityArrayList.add(entity);
    }

    public PlayerEntity getPlayerEntity(){
        return playerEntity;
    }

}
