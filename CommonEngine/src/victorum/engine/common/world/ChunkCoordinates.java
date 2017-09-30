package victorum.engine.common.world;

public final class ChunkCoordinates{
    final int chunkX, chunkZ;

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
    public int hashCode(){ //for use as hashmap key
        return chunkX*chunkZ;
    }

    @Override
    public boolean equals(Object obj){ //for use as hashmap key
        if(obj instanceof ChunkCoordinates){
            ChunkCoordinates other = ((ChunkCoordinates) obj);
            return other.chunkX == chunkX && other.chunkZ == chunkZ;
        }else{
            return false;
        }
    }

}
