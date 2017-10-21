package io.github.victorum.world;

import java.util.concurrent.ConcurrentHashMap;

public class World{
    public static final int WORLD_SIZE_IN_CHUNKS = 16;
    private final ConcurrentHashMap<ChunkCoordinates, Chunk> chunkData = new ConcurrentHashMap<>();

    public Chunk getChunk(int chunkX, int chunkZ){
        return getChunk(new ChunkCoordinates(chunkX, chunkZ));
    }

    public Chunk getChunk(ChunkCoordinates coordinates){
        return chunkData.computeIfAbsent(coordinates, Chunk::new);
    }

}
