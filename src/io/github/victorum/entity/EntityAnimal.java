package io.github.victorum.entity;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import io.github.victorum.world.World;

import java.util.Random;

public class EntityAnimal extends Entity{
    private static Random random = new Random(System.currentTimeMillis());
    private float countdown = -1, jumpCountdown = 0, rotation = 0;
    private boolean swim;

    public EntityAnimal(World world, Spatial spatial, boolean swim){
        super(world, spatial, new Vector3f(0, 0, 0));
        this.swim = swim;
    }

    @Override
    public void update(float tpf){
        if(countdown < 0f){
            float angle = random.nextFloat()*FastMath.TWO_PI;
            Vector3f newDirection = new Vector3f(FastMath.cos(angle), 0, -FastMath.sin(angle));
            setForwardDirection(newDirection);

            getSpatial().getLocalRotation().fromAngles(0, angle-FastMath.PI/2.0f, 0);

            Vector3f leftDirection = newDirection.cross(Vector3f.UNIT_Y);
            setLeftDirection(leftDirection);

            setForward(true);

            jumpCountdown = random.nextFloat() < .25f ? 0.1f : 0;

            countdown = random.nextFloat()*5;
        }else{
            countdown -= tpf;
        }

        if(isUnderwater() && isSwim()) jump();
    }

    @Override
    public void onCollision() {
        jump();
    }

    private float nextFloat(){
        return random.nextFloat() * (random.nextBoolean() ? 1f : -1f);
    }

    public boolean isSwim(){
        return swim;
    }

}
