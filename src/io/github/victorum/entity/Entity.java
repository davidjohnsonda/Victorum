package io.github.victorum.entity;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import io.github.victorum.world.World;

public abstract class Entity{
    private World world;
    private Spatial spatial;
    private Vector3f forwardDirection = new Vector3f();
    private Vector3f leftDirection = new Vector3f();
    private boolean forward, backwards, left, right, isOnGround;
    private Vector3f velocity, airAcceleration;

    public Entity(World world, Spatial spatial){
        this.world = world;
        this.spatial = spatial;
        velocity = new Vector3f();
        airAcceleration = new Vector3f();
        forward = backwards = left = right = isOnGround = false;
    }

    protected void updatePhysics(float tpf){
        Vector3f netDirection = new Vector3f(0, 0, 0);

        if(forward){
            netDirection.addLocal(forwardDirection);
        }

        if(backwards){
            netDirection.addLocal(forwardDirection.negate());
        }

        if(left){
            netDirection.addLocal(leftDirection);
        }

        if(right){
            netDirection.addLocal(leftDirection.negate());
        }

        netDirection.setY(0);
        netDirection.normalizeLocal();

        if(isOnGround){
            velocity.set(netDirection.mult(10f));
            if(velocity.length() != 0){
                isOnGround = applyVelocitySeparately(netDirection, tpf).isHitY();
            }
        }else{
            airAcceleration.set(netDirection);
            airAcceleration.addLocal(0, -9.8f, 0);
            velocity.addLocal(airAcceleration.multLocal(tpf));
            isOnGround = applyVelocitySeparately(velocity, tpf).isHitY();
        }
    }

    private HitData applyVelocitySeparately(Vector3f velocity, float tpf){
        HitData hitData = new HitData(false, false, false);
        float x = velocity.getX();
        float y = velocity.getY();
        float z = velocity.getZ();

        velocity.set(0, 0, 0);

        velocity.setX(x);
        if(applyVelocity(velocity, tpf)) hitData.hitX = true;
        velocity.setX(0);

        velocity.setY(y);
        if(applyVelocity(velocity, tpf)) hitData.hitY = true;
        velocity.setY(0);

        velocity.setZ(z);
        if(applyVelocity(velocity, tpf)) hitData.hitZ = true;
        velocity.setZ(0);

        velocity.set(x, y, z);

        return hitData;
    }

    private boolean applyVelocity(Vector3f velocity, float tpf){
        velocity = velocity.mult(tpf);
        int stepCount = (int)Math.ceil(velocity.length())*5;
        Vector3f step = velocity.divide(stepCount);
        Vector3f location = spatial.getLocalTranslation();
        boolean isCollided = false;

        for(int i=0;i<stepCount;++i){
            location.addLocal(step);
            if(world.getBlockTypeAt(location.getX(), location.getY(), location.getZ()).isSolid()){
                location.subtractLocal(step);
                isCollided = true;
                break;
            }
        }

        spatial.setLocalTranslation(location);

        System.out.println(velocity + " " + tpf + " " + isCollided);

        return isCollided;
    }

    public void jump(){
        isOnGround = false;
        velocity.addLocal(0, 3f, 0);
    }

    public abstract void update(float tpf);

    public Vector3f getForwardDirection() {
        return forwardDirection;
    }

    public void setForwardDirection(Vector3f forwardDirection) {
        this.forwardDirection = forwardDirection;
    }

    public Vector3f getLeftDirection() {
        return leftDirection;
    }

    public void setLeftDirection(Vector3f leftDirection) {
        this.leftDirection = leftDirection;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isBackwards() {
        return backwards;
    }

    public void setBackwards(boolean backwards) {
        this.backwards = backwards;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public final Spatial getSpatial(){
        return spatial;
    }

    private static final class HitData{
        protected boolean hitX;
        protected boolean hitY;
        protected boolean hitZ;

        public HitData(boolean hitX, boolean hitY, boolean hitZ){
            this.hitX = hitX;
            this.hitY = hitY;
            this.hitZ = hitZ;
        }

        public boolean isHitX() {
            return hitX;
        }

        public boolean isHitY() {
            return hitY;
        }

        public boolean isHitZ() {
            return hitZ;
        }
    }

}
