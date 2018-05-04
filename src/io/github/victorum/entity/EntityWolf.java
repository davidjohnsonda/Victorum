package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityWolf extends EntityAnimal{
    private static final Random wolfCutenessRandomizer = new Random();

    private static Spatial createOstrich(AssetManager assetManager){
        Spatial cuteOstrich = assetManager.loadModel("wolf.obj");
        cuteOstrich.setLocalScale(0.25f + wolfCutenessRandomizer.nextFloat()*.5f);
        return cuteOstrich;
    }

    public EntityWolf(World world, AssetManager assetManager){
        super(world, createOstrich(assetManager));
    }

}
