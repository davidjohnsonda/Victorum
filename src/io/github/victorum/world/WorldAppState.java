package io.github.victorum.world;

import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

import io.github.victorum.util.ThreadingUtil;
import io.github.victorum.util.VAppState;
import io.github.victorum.world.generator.WorldGenerator;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WorldAppState extends VAppState{
    private static final long CHUNK_TICK_INTERVAL_MS = 25;
    private final World world = new World();
    private final WorldGenerator worldGenerator = new WorldGenerator();
    private final HashMap<ChunkCoordinates, Geometry> chunkMeshes = new HashMap<>();
    private final ConcurrentLinkedDeque<ChunkMeshGenerator> pendingMeshes = new ConcurrentLinkedDeque<>();
    private Material chunkMaterial;
    private long nextChunkTick = 0;

    @Override
    protected void initialize(Application application){
        chunkMaterial = new Material(getVictorum().getAssetManager(), "/Common/MatDefs/Misc/Unshaded.j3md");
        Texture textureAtlas = getVictorum().getAssetManager().loadTexture(new TextureKey("/atlas.png", false));
        textureAtlas.setMagFilter(Texture.MagFilter.Nearest);
        textureAtlas.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        textureAtlas.setAnisotropicFilter(2);
        chunkMaterial.setTexture("ColorMap", textureAtlas);
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

        ChunkMeshGenerator pendingChunkMesh = pendingMeshes.poll();
        if(pendingChunkMesh != null){
            updateMeshInScenegraph(pendingChunkMesh);
        }
    }

    @Override protected void onEnable(){}
    @Override protected void onDisable(){}

    protected void doChunkTick(){
        int camPosX = (int)getVictorum().getCamera().getLocation().getX();
        int camPosZ = (int)getVictorum().getCamera().getLocation().getZ();

        int camPosChunkX = camPosX/Chunk.CHUNK_SIZE;
        int camPosChunkZ = camPosZ/Chunk.CHUNK_SIZE;

        int viewRegionStartX = camPosChunkX-8;
        int viewRegionEndX = camPosChunkX+8;
        int viewRegionStartZ = camPosChunkZ-8;
        int viewRegionEndZ = camPosChunkZ+8;

        int ccx, ccz, scheduledTasks = 0, maximumTasks = 3;
        outer:for(ccx=viewRegionStartX;ccx<viewRegionEndX;++ccx){
            for(ccz=viewRegionStartZ;ccz<viewRegionEndZ;++ccz){
                Chunk chunk = world.getChunk(ccx, ccz);
                switch(chunk.getStatus()){
                    case POST_INIT:
                        requestChunkData(chunk);
                        ++scheduledTasks;
                        break;
                    case HOLDING_DATA:
                        requestMeshRefresh(chunk);
                        ++scheduledTasks;
                        break;
                }
                if(scheduledTasks > maximumTasks) break outer;
            }
        }
    }

    private void requestChunkData(Chunk chunk){
        chunk.setStatus(ChunkStatus.AWAITING_DATA);
        ThreadingUtil.EXECUTOR_SERVICE.submit(() -> loadChunkData(chunk));
    }

    private void requestMeshRefresh(Chunk chunk){
        chunk.setStatus(ChunkStatus.AWAITING_MESH_REFRESH);
        ThreadingUtil.EXECUTOR_SERVICE.submit(() -> refreshChunkMesh(chunk));
    }

    private void loadChunkData(Chunk chunk){
        worldGenerator.generateChunk(chunk);
        chunk.setStatus(ChunkStatus.HOLDING_DATA);
    }

    private void refreshChunkMesh(Chunk chunk){
        ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunk);
        chunkMeshGenerator.generateMesh();
        pendingMeshes.add(chunkMeshGenerator);
        chunk.setStatus(ChunkStatus.IDLE);
    }

    private void updateMeshInScenegraph(ChunkMeshGenerator generator){
        Chunk chunk = generator.getChunk();
        Geometry geometry = chunkMeshes.get(chunk.getChunkCoordinates());

        if(geometry == null){
            geometry = new Geometry("Chunk", generator.getMesh());
            geometry.setMaterial(chunkMaterial);
            geometry.setLocalTranslation(
                chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE,
                0,
                chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE
            );
            getVictorum().getRootNode().attachChild(geometry);

            chunkMeshes.put(chunk.getChunkCoordinates(), geometry);
        }else{
            geometry.setMesh(generator.getMesh());
        }
    }

}
