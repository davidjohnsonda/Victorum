package io.github.victorum.entity.player;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import io.github.victorum.util.VAppState;

public class PlayerAppState extends VAppState implements ActionListener{
    private static final float GRAVITY = -9.81f;
    private static final float JUMP_VELOCITY = -3*GRAVITY;
    private Vector3f position;
    private Vector3f worldVelocity;
    private Vector3f playerVelocity;
    private Vector3f worldAcceleration;
    private Vector3f playerAcceleration;
    private boolean isJumping = false;
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    private float jumpTimeRemaining = 0.0f;
    private boolean isOnGround = false;

    @Override
    protected void initialize(Application application) {
        position = new Vector3f(0, 1024, 0);
        worldVelocity = new Vector3f(0, 0, 0);
        playerVelocity = new Vector3f(0, 0, 0);
        worldAcceleration = new Vector3f(0, GRAVITY, 0);

        getVictorum().getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        getVictorum().getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        getVictorum().getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        getVictorum().getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        getVictorum().getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        getVictorum().getInputManager().addListener(this, "Forward", "Backward", "Left", "Right", "Jump");
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf)
    {
        switch(name){
            case "Forward":
                forward = keyPressed;
                break;
            case "Backward":
                backward = keyPressed;
                break;
            case "Left":
                left = keyPressed;
                break;
            case "Right":
                right = keyPressed;
                break;
            case "Jump":
                jumpTimeRemaining = 0.16f;
                break;
        }
    }

    @Override
    public void update(float tpf)
    {
        if(jumpTimeRemaining > 0){
            worldVelocity.addLocal(0, JUMP_VELOCITY*tpf, 0);
            jumpTimeRemaining -= tpf;
        }else{
            worldVelocity.addLocal(worldAcceleration.mult(tpf));
        }
        isOnGround = applyVelocity(worldVelocity, tpf);

        if(isOnGround){
            worldVelocity.set(0, 0, 0);
            playerVelocity.set(0, 0, 0);
        }

        if(forward) playerVelocity.addLocal(getVictorum().getCamera().getDirection());
        if(backward) playerVelocity.subtractLocal(getVictorum().getCamera().getDirection());
        if(left) playerVelocity.addLocal(getVictorum().getCamera().getLeft());
        if(right) playerVelocity.subtractLocal(getVictorum().getCamera().getLeft());
        playerVelocity.normalizeLocal();
        playerVelocity.multLocal(10f);

        if(applyVelocitySeparately(playerVelocity, tpf)) isJumping = false;

        getApplication().getCamera().setLocation(position.add(0, 1.7f, 0));
    }

    private boolean applyVelocitySeparately(Vector3f velocity, float tpf){
        float x = velocity.getX();
        float y = velocity.getY();
        float z = velocity.getZ();
        boolean hit = false;

        velocity.set(0, 0, 0);

        velocity.setX(x);
        if(applyVelocity(velocity, tpf)) hit = true;
        velocity.setX(0);

        velocity.setY(y);
        if(applyVelocity(velocity, tpf)) hit = true;
        velocity.setY(0);

        velocity.setZ(z);
        if(applyVelocity(velocity, tpf)) hit = true;
        velocity.setZ(0);

        velocity.set(x, y, z);

        return hit;
    }

    private boolean applyVelocity(Vector3f velocity, float tpf){
        Vector3f moveDistance = velocity.mult(tpf);
        int stepCount = (int)Math.ceil(moveDistance.length())*5;
        Vector3f moveAmount = moveDistance.divide(stepCount);

        for(int stepIndex=0;stepIndex<stepCount;++stepIndex){
            position.addLocal(moveAmount);
            if(getVictorum().getWorldAppState().getWorld().getBlockTypeAt(position.getX(), position.getY(), position.getZ()).isSolid()){
                position.subtractLocal(moveAmount);
                return true;
            }
        }

        return false;
    }

    @Override protected void cleanup(Application application){}
    @Override protected void onEnable(){}
    @Override protected void onDisable(){}
}
