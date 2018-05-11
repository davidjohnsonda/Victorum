package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityTrilobite extends EntityAnimal{
    private static final Random triloCutenessRandomizer = new Random();

    private static Spatial create(AssetManager assetManager){
        Spatial trilobite = assetManager.loadModel("trilo.obj");
        trilobite.setLocalScale(0.25f + triloCutenessRandomizer.nextFloat()*.5f);
        return trilobite;
    }

    public EntityTrilobite(World world, AssetManager assetManager){
        super(world, create(assetManager));
    }

}
