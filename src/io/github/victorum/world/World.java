package io.github.victorum.world;

import java.util.concurrent.ConcurrentHashMap;

public class World{
    private final ConcurrentHashMap<ChunkCoordinates, Chunk> chunkData = new ConcurrentHashMap<>();

    public Chunk getChunk(int chunkX, int chunkZ){
        return getChunk(new ChunkCoordinates(chunkX, chunkZ));
    }

    public Chunk getChunk(ChunkCoordinates coordinates){
        return chunkData.computeIfAbsent(coordinates, this::createChunk);
    }

    private Chunk createChunk(ChunkCoordinates coordinates){
        return new Chunk(coordinates, this);
    }

    public ConcurrentHashMap<ChunkCoordinates, Chunk> getChunkData(){
        return chunkData;
    }

}
