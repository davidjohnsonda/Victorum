package io.github.victorum.world;

public final class BlockCoordinates{
    private final int chunkX, chunkZ;
    private final int localX, localZ;
    private final int globalX, globalZ;

    public BlockCoordinates(int globalX, int globalZ){
        this.globalX = globalX;
        this.globalZ = globalZ;
        this.chunkX = Math.floorDiv(globalX, Chunk.CHUNK_SIZE);
        this.chunkZ = Math.floorDiv(globalZ, Chunk.CHUNK_SIZE);
        this.localX = globalX - chunkX*Chunk.CHUNK_SIZE;
        this.localZ = globalZ - chunkZ*Chunk.CHUNK_SIZE;
    }

    public BlockCoordinates(int chunkX, int chunkZ, int localX, int localZ){
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.localX = localX;
        this.localZ = localZ;
        this.globalX = chunkX*Chunk.CHUNK_SIZE+localX;
        this.globalZ = chunkZ*Chunk.CHUNK_SIZE+localZ;
    }

    /*private int localToGlobalCoordinate(int blockCoordinate, int chunkCoordinate){

    }*/

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalZ() {
        return localZ;
    }

    public int getGlobalX() {
        return globalX;
    }

    public int getGlobalZ() {
        return globalZ;
    }

}
