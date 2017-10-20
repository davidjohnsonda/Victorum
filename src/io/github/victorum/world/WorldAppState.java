package io.github.victorum.world;

import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

import io.github.victorum.util.VAppState;

import java.util.HashMap;

public class WorldAppState extends VAppState{
    private static final long CHUNK_TICK_INTERVAL_MS = 256;
    private final World world = new World();
    private final WorldGenerator worldGenerator = new WorldGenerator();
    private final HashMap<ChunkCoordinates, Geometry> chunkMeshes = new HashMap<>();
    private Material chunkMaterial;
    private long nextChunkTick = 0;

    @Override
    protected void initialize(Application application){
        chunkMaterial = new Material(getVictorum().getAssetManager(), "/Common/MatDefs/Misc/Unshaded.j3md");
        chunkMaterial.setColor("Color", ColorRGBA.Green);
        chunkMaterial.setBoolean("VertexColor", false);
        chunkMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    }

    @Override
    protected void cleanup(Application application){

    }

    @Override
    public void update(float tpf){
        if(System.currentTimeMillis() > nextChunkTick){
            doChunkTick();
            nextChunkTick = System.currentTimeMillis() + CHUNK_TICK_INTERVAL_MS;
        }
    }

    @Override protected void onEnable(){}
    @Override protected void onDisable(){}

    protected synchronized void doChunkTick(){
        int ccx, ccz;
        for(ccx=0;ccx<World.WORLD_SIZE_IN_CHUNKS;++ccx){
            for(ccz=0;ccz<World.WORLD_SIZE_IN_CHUNKS;++ccz){
                Chunk chunk = world.getChunk(ccx, ccz);
                switch(chunk.getStatus()){
                    case POST_INIT:
                        requestChunkData(chunk);
                        break;
                    case HOLDING_DATA:
                        requestMeshRefresh(chunk);
                        break;
                }
            }
        }
    }

    private void requestChunkData(Chunk chunk){
        System.out.println("Requesting Chunk Data: " + chunk.getChunkCoordinates());
        chunk.setStatus(ChunkStatus.AWAITING_DATA);
        //ThreadingUtil.EXECUTOR_SERVICE.submit(() -> loadChunkData(chunk));
        loadChunkData(chunk);
    }

    private void requestMeshRefresh(Chunk chunk){
        System.out.println("Requesting Mesh Refresh: " + chunk.getChunkCoordinates());
        chunk.setStatus(ChunkStatus.AWAITING_MESH_REFRESH);
        //ThreadingUtil.EXECUTOR_SERVICE.submit(() -> refreshChunkMesh(chunk));
        refreshChunkMesh(chunk);
    }

    private void loadChunkData(Chunk chunk){
        System.out.println("Generating Chunk: " + chunk.getChunkCoordinates());
        worldGenerator.generateChunk(chunk);
        chunk.setStatus(ChunkStatus.HOLDING_DATA);
    }

    private void refreshChunkMesh(Chunk chunk){
        System.out.println("Generating mesh: " + chunk.getChunkCoordinates());
        ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunk);
        chunkMeshGenerator.generateMesh();
        getVictorum().enqueue(() -> updateMeshInScenegraph(chunk, chunkMeshGenerator));
        chunk.setStatus(ChunkStatus.IDLE);
    }

    private synchronized void updateMeshInScenegraph(Chunk chunk, ChunkMeshGenerator generator){
        Geometry geometry = chunkMeshes.get(chunk.getChunkCoordinates());

        if(geometry == null){
            Mesh mesh = new Mesh();
            generator.applyToMeshObject(mesh);
            mesh.updateBound();

            geometry = new Geometry("Chunk", mesh);
            geometry.setMaterial(chunkMaterial);
            geometry.setLocalTranslation(
                chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE,
                0,
                chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE
            );
            getVictorum().getRootNode().attachChild(geometry);
            geometry.updateModelBound();

            chunkMeshes.put(chunk.getChunkCoordinates(), geometry);



            System.out.println("Showing mesh: " + chunk.getChunkCoordinates());
        }else{
            generator.applyToMeshObject(geometry.getMesh());
            geometry.updateModelBound();
        }
    }

}
