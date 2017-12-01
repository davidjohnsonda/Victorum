package io.github.victorum.world;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockType;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Chunk{
    public static int CHUNK_SIZE = 16;
    public static int CHUNK_HEIGHT = 256;
    private final ChunkCoordinates chunkCoordinates;
    private final World world;
    private final AtomicIntegerArray blockTypeData = new AtomicIntegerArray(CHUNK_SIZE*CHUNK_SIZE*CHUNK_HEIGHT);
    private volatile ChunkStatus status = ChunkStatus.POST_INIT;

    protected Chunk(ChunkCoordinates chunkCoordinates, World world){
        this.chunkCoordinates = chunkCoordinates;
        this.world = world;
    }

    public ChunkCoordinates getChunkCoordinates(){
        return chunkCoordinates;
    }

    public BlockType getBlockTypeAt(int x, int y, int z){
        return BlockRegistry.getBlockType(blockTypeData.get(transformCoordinates(x, y, z)));
    }

    public void setBlockTypeAt(int x, int y, int z, BlockType type){
        blockTypeData.set(transformCoordinates(x, y, z), type.getBlockId());
    }

    private int transformCoordinates(int x, int y, int z){
        return y*CHUNK_SIZE*CHUNK_SIZE+x*CHUNK_SIZE+z;
    }

    public ChunkStatus getStatus(){
        return status;
    }

    public void setStatus(ChunkStatus status){
        this.status = status;
    }

    public boolean isReadyForMesh(){
        return status != ChunkStatus.POST_INIT && status != ChunkStatus.AWAITING_DATA;
    }

    public World getWorld(){
        return world;
    }

}
