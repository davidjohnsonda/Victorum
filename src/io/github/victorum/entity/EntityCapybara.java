package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import io.github.victorum.world.World;

import java.util.Random;

public class EntityCapybara extends EntityAnimal{
    private static final Random capybaraCutenessRandomizer = new Random();

    private static Spatial create(AssetManager assetManager){
        Spatial capybara = assetManager.loadModel("capybara.obj");
        capybara.setLocalScale(0.25f + capybaraCutenessRandomizer.nextFloat()*.5f);
        return capybara;
    }

    public EntityCapybara(World world, AssetManager assetManager){
<<<<<<< HEAD
        super(world, create(assetManager));
=======
        super(world, createOstrich(assetManager), true);
>>>>>>> 008b1c2a883b0288d9ef777b99ecbdef86157f95
    }

}
