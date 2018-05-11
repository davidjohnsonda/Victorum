package io.github.victorum.world;

import io.github.victorum.inventory.block.BlockRegistry;
import io.github.victorum.inventory.block.BlockType;

import java.io.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Chunk{
    public static int CHUNK_SIZE = 16;
    public static int CHUNK_HEIGHT = 256;
    private final ChunkCoordinates chunkCoordinates;
    private final World world;
    private final AtomicIntegerArray blockTypeData = new AtomicIntegerArray(CHUNK_SIZE*CHUNK_SIZE*CHUNK_HEIGHT);
    private volatile ChunkStatus status = ChunkStatus.POST_INIT;
    private volatile boolean isModified = false;

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
        isModified = true;
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

    public boolean contains(int globalX, int globalZ){
        BlockCoordinates coordinates = new BlockCoordinates(globalX, globalZ);
        return coordinates.getChunkX() == coordinates.getChunkX() && coordinates.getChunkZ() == coordinates.getChunkZ();
    }

    public void save(File chunkFile){
        try(
            FileOutputStream fileOutputStream = new FileOutputStream(chunkFile);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(gzipOutputStream);
        ){
            for(int y=0;y<CHUNK_HEIGHT;++y){
                for(int x=0;x<CHUNK_SIZE;++x){
                    for(int z=0;z<CHUNK_SIZE;++z){
                        dataOutputStream.writeInt(getBlockTypeAt(x, y, z).getBlockId());
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void load(File chunkFile){
        try(
                FileInputStream fileInputStream = new FileInputStream(chunkFile);
                GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
                DataInputStream dataInputStream = new DataInputStream(gzipInputStream);
        ){
            for(int y=0;y<CHUNK_HEIGHT;++y){
                for(int x=0;x<CHUNK_SIZE;++x){
                    for(int z=0;z<CHUNK_SIZE;++z){
                        int id = dataInputStream.readInt();
                        BlockType blockType = BlockRegistry.getBlockType(id);
                        setBlockTypeAt(x, y, z, blockType);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean isModified(){
        return isModified;
    }

    public void setModified(boolean modified){
        isModified = modified;
    }

}
