package io.github.victorum.world;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import io.github.victorum.entity.*;
import io.github.victorum.util.VAppState;
import java.util.Random;

public class EntitySpawnAppState extends VAppState {
    private float counter = 0;
    private Random random = new Random();

    @Override
    public void update(float secondsPerLastFrame) {
        counter += secondsPerLastFrame;

        if(counter >= .350) {
            Entity newAnimal = null;

            int determinant = random.nextInt(5);

            switch (determinant) {
                case 0:
                    newAnimal = new EntityOstrich(getVictorum().getWorldAppState().getWorld(), getVictorum().getAssetManager());
                    break;
                case 1:
                    newAnimal = new EntityWolf(getVictorum().getWorldAppState().getWorld(), getVictorum().getAssetManager());
                    break;
                case 2:
                    newAnimal = new EntityPig(getVictorum().getWorldAppState().getWorld(), getVictorum().getAssetManager());
                    break;
                case 3:
                    newAnimal = new EntityCapybara(getVictorum().getWorldAppState().getWorld(), getVictorum().getAssetManager());
                    break;
                case 4:
                    newAnimal = new EntityTrilobite(getVictorum().getWorldAppState().getWorld(), getVictorum().getAssetManager());
                    break;
            }

            getVictorum().getEntityAppState().addEntity(newAnimal, randomizedLocation(newAnimal));

            counter = 0;
        }
    }

    private Vector3f randomizedLocation(Entity animal) {
        // If it's water, loop and pick a new location

        Chunk loadedChunk = getVictorum().getWorldAppState().getWorld().getRandomLoadedChunk(random);

        Vector3f vector = new Vector3f(loadedChunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE/2,
                128,
                loadedChunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE/2);

        return vector;
    }

    @Override
    protected void onDisable() { }

    @Override
    protected void initialize(Application application) { }

    @Override
    protected void cleanup(Application application) { }

    @Override
    protected void onEnable() { }
}
