package io.github.victorum.entity;

import com.jme3.app.Application;

import io.github.victorum.util.VAppState;

import java.util.ArrayList;

public class EntityAppState extends VAppState{
    private ArrayList<Entity> entityArrayList = new ArrayList<>();

    @Override
    protected void initialize(Application application) {
        addEntity(new PlayerEntity(getVictorum()));
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    public void update(float tpf){
        super.update(tpf);
        for(Entity entity : entityArrayList){
            entity.update(tpf);
            entity.updatePhysics(tpf);
        }
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void addEntity(Entity entity){
        getVictorum().getRootNode().attachChild(entity.getSpatial());
        entityArrayList.add(entity);
    }

}
