package io.github.victorum.world;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChunkMeshGenerator{
    private final Chunk chunk;
    private final ArrayList<Vector3f> vertices = new ArrayList<>();
    private final ArrayList<Integer> indices = new ArrayList<>();

    private int[] indicesArray;
    private Vector3f[] verticesArray;

    private volatile Mesh mesh;

    public ChunkMeshGenerator(Chunk chunk){
        this.chunk = chunk;
    }

    public void generateMesh(){
        int cx, cy, cz;
        for(cy=0;cy<Chunk.CHUNK_HEIGHT;++cy){
            for(cx=0;cx<Chunk.CHUNK_SIZE;++cx){
                for(cz=0;cz<Chunk.CHUNK_SIZE;++cz){
                    generateBlock(cx, cy, cz);
                }
            }
        }

        verticesArray = vertices.toArray(new Vector3f[vertices.size()]);
        indicesArray = new int[indices.size()];
        for(int x=0;x<indicesArray.length;++x){
            indicesArray[x] = indices.get(x);
        }

        mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verticesArray));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indicesArray));
        mesh.updateBound();
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

            if(!isBlockSolid(cx - 1, cy, cz)){
                addFace(v1, v2, v3, v4);
            }

            if(!isBlockSolid(cx + 1, cy, cz)){
                addFace(v8, v7, v6, v5);
            }

            if(!isBlockSolid(cx, cy - 1, cz)){
                addFace(v1, v2, v6, v5);
            }

            if(!isBlockSolid(cx, cy + 1, cz)){
                addFace(v4, v3, v7, v8);
            }

            if(!isBlockSolid(cx, cy, cz - 1)){
                addFace(v5, v1, v4, v8);
            }

            if(!isBlockSolid(cx, cy, cz + 1)){
                addFace(v2, v6, v7, v3);
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

    private void addFace(Vector3f a, Vector3f b, Vector3f c, Vector3f d){
        int aid = addVertex(a);
        int bid = addVertex(b);
        int cid = addVertex(c);
        int did = addVertex(d);

        indices.add(aid);
        indices.add(bid);
        indices.add(cid);

        indices.add(cid);
        indices.add(did);
        indices.add(aid);
    }

    private int addVertex(Vector3f v){
        int id = vertices.size();
        vertices.add(v);
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
