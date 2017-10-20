package io.github.victorum.world;

public final class ChunkCoordinates{
    private final int chunkX, chunkZ;

    public ChunkCoordinates(int chunkX, int chunkZ){
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkX(){
        return chunkX;
    }

    public int getChunkZ(){
        return chunkZ;
    }

    @Override
    public int hashCode(){
        return chunkX | (chunkZ << 16);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof ChunkCoordinates){
            ChunkCoordinates cc2 = ((ChunkCoordinates) obj);
            return cc2.chunkX == chunkX && cc2.chunkZ == chunkZ;
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return "(" + chunkX + ", " + chunkZ + ")";
    }

}
