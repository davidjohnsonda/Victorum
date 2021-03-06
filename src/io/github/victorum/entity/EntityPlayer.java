package io.github.victorum.entity;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

import io.github.victorum.Victorum;

public class EntityPlayer extends Entity implements ActionListener{
    private final Victorum victorum;

    public EntityPlayer(Victorum victorum){
        super(victorum.getWorldAppState().getWorld(), new Node(),
                new Vector3f(.25f, 0, 0),
                new Vector3f(-.25f, 0, 0),
                new Vector3f(0, 0, .25f),
                new Vector3f(0, 0, -.25f),
                new Vector3f(0, 0, 0),
                new Vector3f(.25f, 1.7f, 0),
                new Vector3f(-.25f, 1.7f, 0),
                new Vector3f(0, 1.7f, .25f),
                new Vector3f(0, 1.7f, -.25f),
                new Vector3f(0, 1.7f, 0)
        );
        this.victorum = victorum;

        victorum.getInputManager().addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
        victorum.getInputManager().addMapping("backward", new KeyTrigger(KeyInput.KEY_S));
        victorum.getInputManager().addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        victorum.getInputManager().addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        victorum.getInputManager().addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        victorum.getInputManager().addListener(this, "forward", "backward", "left", "right", "jump");
    }

    @Override
    public void update(float tpf){
        Camera camera = victorum.getCamera();
        camera.setLocation(getSpatial().getLocalTranslation().add(0, 1.7f, 0));
        setForwardDirection(camera.getDirection());
        setLeftDirection(camera.getLeft());
    }

    @Override
    public void onAction(String label, boolean value, float tpf){
        switch(label){
            case "forward":
                setForward(value);
                break;
            case "backward":
                setBackwards(value);
                break;
            case "left":
                setLeft(value);
                break;
            case "right":
                setRight(value);
                break;
            case "jump":
                jump();
                break;
        }
    }

    @Override
    public void onCollision() {}

}
