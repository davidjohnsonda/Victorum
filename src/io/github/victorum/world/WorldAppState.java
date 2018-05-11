package io.github.victorum.world;

import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

import io.github.victorum.entity.EntityOstrich;
import io.github.victorum.util.ThreadingUtil;
import io.github.victorum.util.VAppState;
import io.github.victorum.world.generator.WorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WorldAppState extends VAppState{
    private static final long CHUNK_TICK_INTERVAL_MS = 25;
    private static final int VIEW_DISTANCE = 16;
    private final World world = new World();
    private final WorldGenerator worldGenerator = new WorldGenerator();
    private final HashMap<ChunkCoordinates, Node> chunkMeshes = new HashMap<>();
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
        chunkMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
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

        int viewRegionStartX = camPosChunkX-VIEW_DISTANCE;
        int viewRegionEndX = camPosChunkX+VIEW_DISTANCE;
        int viewRegionStartZ = camPosChunkZ-VIEW_DISTANCE;
        int viewRegionEndZ = camPosChunkZ+VIEW_DISTANCE;

        int scheduledTasks = 0;

        doChunkTick(camPosChunkX, camPosChunkZ);
        outer:for(int r=1;r<VIEW_DISTANCE;++r){
            int startX = camPosChunkX-r;
            int endX = camPosChunkX+r;
            int startZ = camPosChunkZ-r;
            int endZ = camPosChunkZ+r;

            for(int ccx=startX;ccx<=endX;++ccx){
                scheduledTasks += doChunkTick(ccx, startZ);
                scheduledTasks += doChunkTick(ccx, endZ);
            }

            for(int ccz=startZ+1;ccz<endZ;++ccz){
                scheduledTasks += doChunkTick(startX, ccz);
                scheduledTasks += doChunkTick(endX, ccz);
            }

            if(scheduledTasks > 4) break outer;
        }

        for(Map.Entry<ChunkCoordinates, Chunk> entry : world.getChunkData().entrySet()){
            if(
                entry.getKey().getChunkX() < viewRegionStartX ||
                entry.getKey().getChunkX() >= viewRegionEndX ||
                entry.getKey().getChunkZ() < viewRegionStartZ ||
                entry.getKey().getChunkZ() >= viewRegionEndZ
            ){
                entry.getValue().setStatus(ChunkStatus.UNLOADED); //first to start other things being able to drop this chunk's processing
                Node node = chunkMeshes.get(entry.getKey());
                if(node != null) getVictorum().getRootNode().detachChild(node);
                chunkMeshes.remove(entry.getKey());
                world.getChunkData().remove(entry.getKey());
            }
        }
    }

    private int doChunkTick(int ccx, int ccz){
        Chunk chunk = world.getChunk(ccx, ccz);
        switch(chunk.getStatus()){
            case POST_INIT:
                requestChunkData(chunk);
                return 1;
            case HOLDING_DATA:
                return requestMeshRefresh(chunk);
        }
        return 0;
    }

    private void requestChunkData(Chunk chunk){
        chunk.setStatus(ChunkStatus.AWAITING_DATA);
        ThreadingUtil.EXECUTOR_SERVICE.submit(() -> loadChunkData(chunk));
    }

    private int requestMeshRefresh(Chunk chunk){
        int chunkX = chunk.getChunkCoordinates().getChunkX();
        int chunkZ = chunk.getChunkCoordinates().getChunkZ();

        if(
            isChunkReadyForMesh(chunkX+1, chunkZ) &&
            isChunkReadyForMesh(chunkX-1, chunkZ) &&
            isChunkReadyForMesh(chunkX, chunkZ+1) &&
            isChunkReadyForMesh(chunkX, chunkZ-1)
        ){
            chunk.setStatus(ChunkStatus.AWAITING_MESH_REFRESH);
            ThreadingUtil.EXECUTOR_SERVICE.submit(() -> refreshChunkMesh(chunk));
            return 1;
        }

        return 0;
    }

    private boolean isChunkReadyForMesh(int chunkX, int chunkZ){
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        return chunk != null && chunk.isReadyForMesh();
    }

    private void loadChunkData(Chunk chunk){
        if(chunk.getStatus() == ChunkStatus.UNLOADED) return;
        worldGenerator.generateChunk(chunk);
        chunk.setStatus(ChunkStatus.HOLDING_DATA);
    }

    private void refreshChunkMesh(Chunk chunk){
        if(chunk.getStatus() == ChunkStatus.UNLOADED) return;
        ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunk);
        chunkMeshGenerator.generateMesh();
        if(chunk.getStatus() == ChunkStatus.UNLOADED) return;
        pendingMeshes.add(chunkMeshGenerator);
        chunk.setStatus(ChunkStatus.IDLE);
    }

    private void updateMeshInScenegraph(ChunkMeshGenerator generator){
        if(generator.getChunk().getStatus() == ChunkStatus.UNLOADED) return;

        Chunk chunk = generator.getChunk();
        Node node = chunkMeshes.get(chunk.getChunkCoordinates());

        if(node == null){
            node = new Node();

            Geometry mainGeometry = new Geometry("Main", generator.getMesh());
            mainGeometry.setMaterial(chunkMaterial);
            node.attachChild(mainGeometry);

            Geometry waterGeometry = new Geometry("Water", generator.getWaterMesh());
            waterGeometry.setMaterial(chunkMaterial);
            waterGeometry.setQueueBucket(RenderQueue.Bucket.Translucent);
            waterGeometry.setLocalTranslation(0, -0.1f, 0);
            node.attachChild(waterGeometry);

            node.setLocalTranslation(
                    chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE,
                    0,
                    chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE
            );

            getVictorum().getRootNode().attachChild(node);

            chunkMeshes.put(chunk.getChunkCoordinates(), node);

            /*
            getVictorum().getEntityAppState().addEntity(
                    new EntityOstrich(world, getVictorum().getAssetManager()),
                    new Vector3f(
                            chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE/2,
                            128,
                            chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE/2
                    )
            );
             */
        }else{
            Geometry mainGeometry = (Geometry)node.getChild("Main");
            Geometry waterGeometry = (Geometry)node.getChild("Water");
            mainGeometry.setMesh(generator.getMesh());
            waterGeometry.setMesh(generator.getWaterMesh());
        }
    }

    public World getWorld(){
        return world;
    }

}
