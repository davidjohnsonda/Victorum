package io.github.victorum.world;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockSide;
import io.github.victorum.block.BlockType;
import io.github.victorum.block.TextureCoordinates;
import io.github.victorum.util.MeshGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChunkMeshGenerator{
    private final Chunk chunk;
    private final MeshGenerator mainMeshGenerator = new MeshGenerator();
    private volatile Mesh mesh;

    public ChunkMeshGenerator(Chunk chunk){
        this.chunk = chunk;
    }

    public void generateMesh(){
        try{
            System.out.println("Starting chunk generation: " + chunk.getChunkCoordinates());
            long startAt = System.currentTimeMillis();

            int cx, cy, cz;
            for(cy=0;cy<Chunk.CHUNK_HEIGHT;++cy){
                for(cx=0;cx<Chunk.CHUNK_SIZE;++cx){
                    for(cz=0;cz<Chunk.CHUNK_SIZE;++cz){
                        generateBlock(cx, cy, cz);
                    }
                }
            }

            mesh = mainMeshGenerator.toMesh();

            System.out.println("Finished chunk generation: " + chunk.getChunkCoordinates() + " in " + (System.currentTimeMillis() - startAt) + "ms");
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    private void generateBlock(int cx, int cy, int cz){
        BlockType type = chunk.getBlockTypeAt(cx, cy, cz);
        if(type.getBlockId() != BlockRegistry.BLOCK_TYPE_AIR.getBlockId()){ //check if is air block
            Vector3f v1 = new Vector3f(cx, cy, cz);
            Vector3f v2 = new Vector3f(cx + 1, cy, cz);
            Vector3f v3 = new Vector3f(cx + 1, cy + 1, cz);
            Vector3f v4 = new Vector3f(cx, cy + 1, cz);
            Vector3f v5 = new Vector3f(cx, cy, cz + 1);
            Vector3f v6 = new Vector3f(cx + 1, cy, cz + 1);
            Vector3f v7 = new Vector3f(cx + 1, cy + 1, cz + 1);
            Vector3f v8 = new Vector3f(cx, cy + 1, cz + 1);

            if(shouldRenderFace(type, cx, cy, cz - 1)){
                addFace(v1, v2, v3, v4, type.getTexture(BlockSide.NEGATIVE_X));
            }

            if(shouldRenderFace(type, cx, cy, cz + 1)){
                addFace(v6, v5, v8, v7, type.getTexture(BlockSide.POSITIVE_X));
            }

            if(shouldRenderFace(type, cx, cy - 1, cz)){
                addFace(v1, v2, v6, v5, type.getTexture(BlockSide.NEGATIVE_Y));
            }

            if(shouldRenderFace(type, cx, cy + 1, cz)){
                addFace(v4, v3, v7, v8, type.getTexture(BlockSide.POSITIVE_Y));
            }

            if(shouldRenderFace(type, cx - 1, cy, cz)){
                addFace(v5, v1, v4, v8, type.getTexture(BlockSide.NEGATIVE_Z));
            }

            if(shouldRenderFace(type, cx + 1, cy, cz)){
                addFace(v2, v6, v7, v3, type.getTexture(BlockSide.POSITIVE_Z));
            }
        }
    }

    private boolean shouldRenderFace(BlockType currentType, int x, int y, int z){
        BlockType otherType;
        if(x < 0 || x >= Chunk.CHUNK_SIZE || z < 0 || z >= Chunk.CHUNK_SIZE || y < 0 || y >= Chunk.CHUNK_HEIGHT){
            BlockCoordinates blockCoordinates = new BlockCoordinates(chunk.getChunkCoordinates().getChunkX(), chunk.getChunkCoordinates().getChunkZ(), x, z);
            otherType =  chunk.getWorld().getBlockTypeAt(
                blockCoordinates.getGlobalX(),
                y,
                blockCoordinates.getGlobalZ()
            );
        }else{
            otherType = chunk.getBlockTypeAt(x, y, z);
        }


        return (
                otherType.getBlockId() != currentType.getBlockId() &&
                    (
                        otherType.getBlockId() == BlockRegistry.BLOCK_TYPE_AIR.getBlockId() || 
                        otherType.getBlockId() == BlockRegistry.BLOCK_TYPE_WATER.getBlockId()
                    )
                );
    }

    private void addFace(Vector3f a, Vector3f b, Vector3f c, Vector3f d, TextureCoordinates textureCoordinates){
        int aid = mainMeshGenerator.addVertex(a, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasEndY());
        int bid = mainMeshGenerator.addVertex(b, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasEndY());
        int cid = mainMeshGenerator.addVertex(c, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasStartY());
        int did = mainMeshGenerator.addVertex(d, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasStartY());

        mainMeshGenerator.getIndices().add(aid);
        mainMeshGenerator.getIndices().add(bid);
        mainMeshGenerator.getIndices().add(cid);

        mainMeshGenerator.getIndices().add(cid);
        mainMeshGenerator.getIndices().add(did);
        mainMeshGenerator.getIndices().add(aid);
    }

    public Mesh getMesh(){
        return mesh;
    }

    public Chunk getChunk(){
        return chunk;
    }

}
