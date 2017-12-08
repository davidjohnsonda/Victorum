package io.github.victorum.player;

import com.jme3.app.Application;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import io.github.victorum.Victorum;
import io.github.victorum.util.VAppState;
import io.github.victorum.world.Chunk;
import io.github.victorum.world.ChunkCoordinates;
import io.github.victorum.world.WorldAppState;

public class Player extends VAppState implements ActionListener, AnalogListener {
    public Spatial model;
    public Vector3f position;
    public Vector3f velocity;
    public Vector3f acceleration;
    public Vector3f rotation;
    public Vector3f rVelocity;
    public Vector3f rAcceleration;
    public static float gravity = -9.81f;
    public static double rotationalCheckMultiplier = 180 / Math.PI;
    public boolean shouldJump = false;
    public float xAxisMovement = 0;
    public float zAxisMovement = 0;
    public float jumpVelocity = 6.26419f;
    public float movementRate = 2;
    public float xAxisRotation = 0;
    public float yAxisRotation = 0;
    public float rotationRate = (float)Math.PI / 6;
    public boolean click = false;
    public Player()
    {

    }

    @Override
    protected void initialize(Application application) {
        model = new Geometry("Box", new Box(1, 1, 1));
        position = new Vector3f(32, 1024, 32);
        getApplication().getCamera().setLocation(position.add(new Vector3f(1, 0, 0)));
        velocity = new Vector3f(0, 0, 0);
        acceleration = new Vector3f(0, gravity, 0);
        rotation = new Vector3f(0, 0, 0);
        rVelocity = new Vector3f(0, 0, 0);
        rAcceleration = new Vector3f(0, 0, 0);
        Material mat = new Material(application.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.DarkGray);
        model.setMaterial(mat);
        ((Victorum) application).getRootNode().attachChild(model);
        model.setModelBound(new BoundingBox());
    }

    @Override
    public void onAnalog(String name, float keyPressed, float tpf)
    {
        if(name == "Forward")
        {
            zAxisMovement += keyPressed;
        }
        if(name == "Backward")
        {
            zAxisMovement -= keyPressed;
        }
        if(name == "Left")
        {
            xAxisMovement += keyPressed;
        }
        if(name == "Right")
        {
            xAxisMovement -= keyPressed;
        }
        if(name == "MouseLeft" && click)
        {
            yAxisRotation -= keyPressed;
        }
        if(name == "MouseRight" && click)
        {
            yAxisRotation += keyPressed;
        }
        if(name == "MouseUp" && click)
        {
            xAxisRotation -= keyPressed;
        }
        if(name == "MouseDown" && click)
        {
            xAxisRotation += keyPressed;
        }
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf)
    {
        if(name.equals("Jump"))
        {
            shouldJump = keyPressed;
        }
        if(name.equals("Click"))
        {
            click = keyPressed;
        }
    }

    @Override
    public void update(float tpf)
    {
        velocity = velocity.add(acceleration.divide(1 / tpf));
        Vector3f additionalVelocity = new Vector3f(xAxisMovement * (float)Math.cos(rotation.y) * (float)Math.cos(rotation.z), xAxisMovement * (float)Math.cos(rotation.x) * (float)Math.sin(rotation.z), -xAxisMovement * (float)Math.cos(rotation.x) * (float)Math.sin(rotation.y)).add(new Vector3f(zAxisMovement * (float)Math.sin(rotation.y) * (float)Math.cos(rotation.z), zAxisMovement * (float)Math.cos(rotation.x) * (float)Math.sin(rotation.z), zAxisMovement * (float)Math.cos(rotation.x) * (float)Math.cos(rotation.y))).mult(movementRate);
        xAxisMovement = 0;
        zAxisMovement = 0;
        Vector3f difference = additionalVelocity;
        Vector3f total = velocity.mult(tpf);
        CollisionResults results = new CollisionResults();
        int checks = (int)Math.ceil(total.length());
        Vector3f perCheck = total.divide(checks);
        for(int i = 0; i < checks; i++)
        {
            Vector3f previousPosition = new Vector3f(position);
            position = position.add(perCheck);
            model.setLocalTranslation(position);
            model.updateModelBound();
            try
            {
                int result = ((Victorum)getApplication()).getWorldAppState().chunkMeshes.get(((Victorum)getApplication()).getWorldAppState().world.getChunk((int)position.x / Chunk.CHUNK_SIZE, (int)position.z / Chunk.CHUNK_SIZE).getChunkCoordinates()).collideWith(model.getWorldBound(), results);
                if(result != 0)
                {
                    position = previousPosition;
                    velocity = new Vector3f(0, 0, 0);
                    if(shouldJump)
                    {
                        velocity = new Vector3f(0, jumpVelocity, 0);
                    }
                    model.setLocalTranslation(position);
                    break;
                }
            }
            catch (Exception e)
            {

            }
        }
        total = difference;
        checks = (int)Math.ceil(total.length());
        perCheck = total.divide(checks);
        for(int i = 0; i < checks; i++)
        {
            Vector3f previousPosition = new Vector3f(position);
            position = position.add(perCheck);
            model.setLocalTranslation(position);
            model.updateModelBound();
            try
            {
                int result = ((Victorum)getApplication()).getWorldAppState().chunkMeshes.get(((Victorum)getApplication()).getWorldAppState().world.getChunk((int)position.x / Chunk.CHUNK_SIZE, (int)position.z / Chunk.CHUNK_SIZE).getChunkCoordinates()).collideWith(model.getWorldBound(), results);
                if(result != 0)
                {
                    position = previousPosition;
                    velocity = new Vector3f(0, 0, 0);
                    model.setLocalTranslation(position);
                    break;
                }
            }
            catch (Exception e)
            {

            }
        }
        rVelocity = rVelocity.add(rAcceleration.divide(1 / tpf));
        float[] eulerAngles1 = getApplication().getCamera().getRotation().toAngles(null);
        Vector3f rDifference = new Vector3f(0, yAxisRotation, 0).mult(rotationRate);
        total = rVelocity.mult(tpf);
        checks = (int)Math.ceil(total.length() * rotationalCheckMultiplier);
        perCheck = total.divide(checks);
        for(int i = 0; i < checks; i++)
        {
            Vector3f previousRotation = new Vector3f(rotation);
            rotation = rotation.add(perCheck);
            model.rotate(rotation.x, rotation.y, rotation.z);
            model.updateModelBound();
            try
            {
                int result = ((Victorum)getApplication()).getWorldAppState().chunkMeshes.get(((Victorum)getApplication()).getWorldAppState().world.getChunk((int)position.x / Chunk.CHUNK_SIZE, (int)position.z / Chunk.CHUNK_SIZE).getChunkCoordinates()).collideWith(model.getWorldBound(), results);
                if(result != 0)
                {
                    rotation = previousRotation;
                    rVelocity = new Vector3f(0, 0, 0);
                    model.rotate(rotation.x, rotation.y, rotation.z);
                    break;
                }
            }
            catch (Exception e)
            {

            }
        }
        total = rDifference;
        checks = (int)Math.ceil(total.length() * rotationalCheckMultiplier);
        perCheck = total.divide(checks);
        for(int i = 0; i < checks; i++)
        {
            Vector3f previousRotation = new Vector3f(rotation);
            rotation = rotation.add(perCheck);
            model.rotate(rotation.x, rotation.y, rotation.z);
            model.updateModelBound();
            try
            {
                int result = ((Victorum)getApplication()).getWorldAppState().chunkMeshes.get(((Victorum)getApplication()).getWorldAppState().world.getChunk((int)position.x / Chunk.CHUNK_SIZE, (int)position.z / Chunk.CHUNK_SIZE).getChunkCoordinates()).collideWith(model.getWorldBound(), results);
                if(result != 0)
                {
                    rotation = previousRotation;
                    rVelocity = new Vector3f(0, 0, 0);
                    model.rotate(rotation.x, rotation.y, rotation.z);
                    break;
                }
            }
            catch (Exception e)
            {

            }
        }
        getApplication().getCamera().setLocation(position);
        getApplication().getCamera().setRotation((new Quaternion()).fromAngles(rotation.x + xAxisRotation * rotationRate, rotation.y, rotation.z));
        yAxisRotation = 0;
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
