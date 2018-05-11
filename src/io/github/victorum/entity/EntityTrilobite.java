package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityTrilobite extends EntityAnimal{
    private static final Random triloCutenessRandomizer = new Random();

    private static Spatial createOstrich(AssetManager assetManager){
        Spatial cuteOstrich = assetManager.loadModel("trilo.obj");
        cuteOstrich.setLocalScale(0.25f + triloCutenessRandomizer.nextFloat()*.5f);
        return cuteOstrich;
    }

    public EntityTrilobite(World world, AssetManager assetManager){
        super(world, createOstrich(assetManager), false);
    }

}
