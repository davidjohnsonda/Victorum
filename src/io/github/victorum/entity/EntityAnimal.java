package io.github.victorum.entity;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import io.github.victorum.world.World;

import java.util.Random;

public class EntityAnimal extends Entity{
    private static Random random = new Random(System.currentTimeMillis());
    private float countdown = -1, jumpCountdown = 0;

    public EntityAnimal(World world, Spatial spatial){
        super(world, spatial, new Vector3f(0, 0, 0));
    }

    @Override
    public void update(float tpf){
        if(countdown < 0f){
            Vector3f newDirection = new Vector3f(nextFloat(), 0, nextFloat());
            newDirection.normalizeLocal();
            setForwardDirection(newDirection);

            Vector3f leftDirection = newDirection.cross(Vector3f.UNIT_Y);
            setLeftDirection(leftDirection);

            setForward(true);

            jumpCountdown = random.nextFloat() < .25f ? 0.1f : 0;

            countdown = random.nextFloat()*5;
        }else{
            countdown -= tpf;
        }

        if(jumpCountdown >= 0){
            jump();
            jumpCountdown -= tpf;
        }
    }

    private float nextFloat(){
        return random.nextFloat() * (random.nextBoolean() ? 1f : -1f);
    }

}
