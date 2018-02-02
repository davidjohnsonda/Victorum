package io.github.victorum.util;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.util.ArrayList;

public class MeshGenerator{
    private final ArrayList<Vector3f> vertices = new ArrayList<>();
    private final ArrayList<Vector2f> texCoords = new ArrayList<>();
    private final ArrayList<Integer> indices = new ArrayList<>();

    public Mesh toMesh(){
        int[] indicesArray = new int[indices.size()];
        for(int x=0;x<indicesArray.length;++x){
            indicesArray[x] = indices.get(x);
        }

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords.toArray(new Vector2f[texCoords.size()])));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indicesArray));
        mesh.updateBound();

        return mesh;
    }

    public int addVertex(Vector3f vertex, float texCoordX, float texCoordZ){
        return addVertex(vertex, new Vector2f(texCoordX, texCoordZ));
    }

    public int addVertex(Vector3f vertex, Vector2f texCoord){
        int vertexId = vertices.size();
        vertices.add(vertex);
        texCoords.add(texCoord);
        return vertexId;
    }

    public ArrayList<Vector3f> getVertices(){
        return vertices;
    }

    public ArrayList<Vector2f> getTexCoords(){
        return texCoords;
    }

    public ArrayList<Integer> getIndices(){
        return indices;
    }

}
