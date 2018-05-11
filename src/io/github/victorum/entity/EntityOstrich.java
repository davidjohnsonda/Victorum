package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import io.github.victorum.world.World;

import java.util.Random;

public class EntityOstrich extends EntityAnimal{
    private static final Random ostrichCutenessRandomizer = new Random();

    private static Spatial create(AssetManager assetManager){
        Spatial cuteOstrich = assetManager.loadModel("ostrich.obj");
        cuteOstrich.setLocalScale(0.25f + ostrichCutenessRandomizer.nextFloat()*.5f);
        return cuteOstrich;
    }

    public EntityOstrich(World world, AssetManager assetManager){
        super(world, create(assetManager));
    }

}
