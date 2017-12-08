package io.github.victorum.world;

import com.jme3.app.Application;
import io.github.victorum.Victorum;

import java.util.concurrent.ConcurrentHashMap;

public class World{
    Victorum data;
    public World(Application app)
    {
        data = (Victorum)app;
    }
    public static final int WORLD_SIZE_IN_CHUNKS = 16;
    private final ConcurrentHashMap<ChunkCoordinates, Chunk> chunkData = new ConcurrentHashMap<>();

    public Chunk getChunk(int chunkX, int chunkZ){
        return getChunk(new ChunkCoordinates(chunkX, chunkZ));
    }

    private Chunk createChunk(ChunkCoordinates coordinates) {
        return new Chunk(coordinates, this);
    }
    public Chunk getChunk(ChunkCoordinates coordinates)
    {
        if(chunkData.get(coordinates) == null)
        {
            Chunk ch = new Chunk(coordinates, this);
            data.getWorldAppState().worldGenerator.generateChunk(ch);
            chunkData.put(coordinates, ch);
            data.getWorldAppState().refreshChunkMesh(ch);
            data.getWorldAppState().updateMeshInScenegraph(data.getWorldAppState().pendingMeshes.pollLast());
        }
        return chunkData.get(coordinates);
    }

    public ConcurrentHashMap<ChunkCoordinates, Chunk> getChunkData(){
        return chunkData;
    }

}
