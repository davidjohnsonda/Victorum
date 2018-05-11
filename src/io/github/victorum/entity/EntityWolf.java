package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityWolf extends EntityAnimal{
    private static final Random wolfCutenessRandomizer = new Random();

    private static Spatial create(AssetManager assetManager){
        Spatial wolf = assetManager.loadModel("wolf.obj");
        wolf.setLocalScale(0.25f + wolfCutenessRandomizer.nextFloat()*.5f);
        return wolf;
    }

    public EntityWolf(World world, AssetManager assetManager){
<<<<<<< HEAD
        super(world, create(assetManager));
=======
        super(world, createOstrich(assetManager), true);
>>>>>>> 008b1c2a883b0288d9ef777b99ecbdef86157f95
    }

}
