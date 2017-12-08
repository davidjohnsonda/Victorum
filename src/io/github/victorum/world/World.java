package io.github.victorum.world;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockType;

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

    public BlockType getBlockTypeAt(int globalX, int globalY, int globalZ){
        if(globalY < 0 || globalY >= Chunk.CHUNK_HEIGHT){
            return BlockRegistry.BLOCK_TYPE_AIR;
        }else {
            Chunk chunk = getChunk(translateGlobalToChunkCoordinate(globalX), translateGlobalToChunkCoordinate(globalZ));

            if(chunk.getStatus() == ChunkStatus.POST_INIT || chunk.getStatus() == ChunkStatus.UNLOADED || chunk.getStatus() == ChunkStatus.AWAITING_DATA){
                return BlockRegistry.BLOCK_TYPE_STONE;
            }

            return chunk.getBlockTypeAt(
                    globalX - chunk.getChunkCoordinates().getChunkX() * Chunk.CHUNK_SIZE,
                    globalY,
                    globalZ - chunk.getChunkCoordinates().getChunkZ() * Chunk.CHUNK_SIZE
            );
        }
    }

    private int translateGlobalToChunkCoordinate(int c){
        if(c < 0){
            return c/Chunk.CHUNK_SIZE-1;
        }else{
            return c/Chunk.CHUNK_SIZE;
        }
    }

    public BlockType getBlockTypeAt(float globalX, float globalY, float globalZ){
        return getBlockTypeAt((int) globalX, (int) globalY, (int) globalZ);
    }

    public ConcurrentHashMap<ChunkCoordinates, Chunk> getChunkData(){
        return chunkData;
    }

}
