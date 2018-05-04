package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityPig extends EntityAnimal{
    private static final Random pigCutenessRandomizer = new Random();

    private static Spatial createOstrich(AssetManager assetManager){
        Spatial cuteOstrich = assetManager.loadModel("pig.obj");
        cuteOstrich.setLocalScale(0.25f + pigCutenessRandomizer.nextFloat()*.5f);
        return cuteOstrich;
    }

    public EntityPig(World world, AssetManager assetManager){
        super(world, createOstrich(assetManager));
    }

}
