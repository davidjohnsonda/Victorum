package io.github.victorum.entity;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import io.github.victorum.world.World;

public class EntityCube extends EntityAnimal{

    private static Spatial makeCube(AssetManager assetManager){
        Node node = new Node();
        Geometry geometry = new Geometry("Animal", new Box(.5f, .5f, .5f));
        geometry.setLocalTranslation(0, .5f, 0);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Green);
        geometry.setMaterial(material);
        node.attachChild(geometry);
        return node;
    }

    public EntityCube(World world, AssetManager assetManager){
        super(world, makeCube(assetManager));
    }
}
