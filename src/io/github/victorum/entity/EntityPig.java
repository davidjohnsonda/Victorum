package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityPig extends EntityAnimal{
    private static final Random pigCutenessRandomizer = new Random();

    private static Spatial create(AssetManager assetManager){
        Spatial pig = assetManager.loadModel("pig.obj");
        pig.setLocalScale(0.25f + pigCutenessRandomizer.nextFloat()*.5f);
        return pig;
    }

    public EntityPig(World world, AssetManager assetManager){
<<<<<<< HEAD
        super(world, create(assetManager));
=======
        super(world, createOstrich(assetManager), true);
>>>>>>> 008b1c2a883b0288d9ef777b99ecbdef86157f95
    }

}
