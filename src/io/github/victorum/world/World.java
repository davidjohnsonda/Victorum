package io.github.victorum.world;

import io.github.victorum.inventory.block.BlockRegistry;
import io.github.victorum.inventory.block.BlockType;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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

    public Chunk getChunkIfExists(ChunkCoordinates coordinates){
        return chunkData.get(coordinates);
    }

    public BlockType getBlockTypeAt(int globalX, int globalY, int globalZ){
        if(globalY < 0 || globalY >= Chunk.CHUNK_HEIGHT){
            return BlockRegistry.BLOCK_TYPE_AIR;
        }else{
            BlockCoordinates blockCoordinates = new BlockCoordinates(globalX, globalZ);
            Chunk chunk = getChunk(blockCoordinates.getChunkX(), blockCoordinates.getChunkZ());
            return chunk.getBlockTypeAt(blockCoordinates.getLocalX(), globalY, blockCoordinates.getLocalZ());
        }
    }

    public void setBlockTypeAt(int globalX, int globalY, int globalZ, BlockType type){
        BlockCoordinates blockCoordinates = new BlockCoordinates(globalX, globalZ);
        Chunk chunk = getChunk(blockCoordinates.getChunkX(), blockCoordinates.getChunkZ());
        chunk.setBlockTypeAt(blockCoordinates.getLocalX(), globalY, blockCoordinates.getLocalZ(), type);
        chunk.setStatus(ChunkStatus.HOLDING_DATA);
    }

    public BlockType getBlockTypeAt(float globalX, float globalY, float globalZ){
        return getBlockTypeAt((int)Math.floor(globalX), (int)Math.floor(globalY), (int)Math.floor(globalZ));
    }

    public ConcurrentHashMap<ChunkCoordinates, Chunk> getChunkData(){
        return chunkData;
    }

    public Chunk getRandomLoadedChunk(Random random) {
        Set<Map.Entry<ChunkCoordinates, Chunk>> entrySet = chunkData.entrySet();
        int id = random.nextInt(entrySet.size());
        Iterator<Map.Entry<ChunkCoordinates, Chunk>> it = entrySet.iterator();
        for(int i = 0; i< id - 1; ++i){ it.next(); }
        return it.next().getValue();
    }

}
