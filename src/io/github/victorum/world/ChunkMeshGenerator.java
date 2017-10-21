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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChunkMeshGenerator{
    private final Chunk chunk;
    private final ArrayList<Vector3f> vertices = new ArrayList<>();
    private final ArrayList<Vector2f> texCoords = new ArrayList<>();
    private final ArrayList<Integer> indices = new ArrayList<>();

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

            Vector3f[] verticesArray = vertices.toArray(new Vector3f[vertices.size()]);
            Vector2f[] texCoordsArray = texCoords.toArray(new Vector2f[texCoords.size()]);
            int[] indicesArray = new int[indices.size()];
            for(int x=0;x<indicesArray.length;++x){
                indicesArray[x] = indices.get(x);
            }

            mesh = new Mesh();
            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verticesArray));
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoordsArray));
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indicesArray));
            mesh.updateBound();

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

            if(!isBlockSolid(cx, cy, cz - 1)){
                addFace(v1, v2, v3, v4, type.getTexture(BlockSide.NEGATIVE_X));
            }

            if(!isBlockSolid(cx, cy, cz + 1)){
                addFace(v8, v7, v6, v5, type.getTexture(BlockSide.POSITIVE_X));
            }

            if(!isBlockSolid(cx, cy - 1, cz)){
                addFace(v1, v2, v6, v5, type.getTexture(BlockSide.NEGATIVE_Y));
            }

            if(!isBlockSolid(cx, cy + 1, cz)){
                addFace(v4, v3, v7, v8, type.getTexture(BlockSide.POSITIVE_Y));
            }

            if(!isBlockSolid(cx - 1, cy, cz)){
                addFace(v5, v1, v4, v8, type.getTexture(BlockSide.NEGATIVE_Z));
            }

            if(!isBlockSolid(cx + 1, cy, cz)){
                addFace(v2, v6, v7, v3, type.getTexture(BlockSide.POSITIVE_Z));
            }
        }
    }

    private boolean isBlockSolid(int x, int y, int z){
        if(x < 0 || x >= Chunk.CHUNK_SIZE || z < 0 || z >= Chunk.CHUNK_SIZE || y < 0 || y >= Chunk.CHUNK_HEIGHT){
            return false;
        }else{
            return chunk.getBlockTypeAt(x, y, z).getBlockId() != BlockRegistry.BLOCK_TYPE_AIR.getBlockId();
        }
    }

    private void addFace(Vector3f a, Vector3f b, Vector3f c, Vector3f d, TextureCoordinates textureCoordinates){
        int aid = addVertex(a, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasEndY());
        int bid = addVertex(b, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasEndY());
        int cid = addVertex(c, textureCoordinates.getAtlasEndX(), textureCoordinates.getAtlasStartY());
        int did = addVertex(d, textureCoordinates.getAtlasStartX(), textureCoordinates.getAtlasStartY());

        indices.add(aid);
        indices.add(bid);
        indices.add(cid);

        indices.add(cid);
        indices.add(did);
        indices.add(aid);
    }

    private int addVertex(Vector3f v, float texCoordX, float texCoordY){
        int id = vertices.size();
        vertices.add(v);
        texCoords.add(new Vector2f(texCoordX, texCoordY));
        return id;
    }

    public Mesh getMesh(){
        return mesh;
    }

    public Chunk getChunk(){
        return chunk;
    }

    private void writeObj(){ //this method is used to dump chunk data into an .obj to allow it to be examined in Blender
        try(
                FileOutputStream fileOutputStream = new FileOutputStream(new File(chunk.getChunkCoordinates() + ".obj"));
        ){
            for(Vector3f vertex : vertices){
                fileOutputStream.write(("v " + vertex.x + " " + vertex.y + " " + vertex.z + "\n").getBytes());
            }

            for(int ii=0;ii<indices.size();ii+=3){
                fileOutputStream.write(("f " + (indices.get(ii)+1) + " " + (indices.get(ii+1)+1) + " " + (indices.get(ii+2)+1) + "\n").getBytes());
            }

            fileOutputStream.flush();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
