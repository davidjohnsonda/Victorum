package io.github.victorum.world.generator;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.world.Chunk;

public class WorldGenerator{
    private final PerlinNoise perlinNoise;

    public WorldGenerator(){
        perlinNoise = new PerlinNoise(0L);
    }

    public void generateChunk(Chunk chunk){
        int cx, cy, cz;

        int offsetX = chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE;
        int offsetZ = chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE;

        for(cx=0;cx<Chunk.CHUNK_SIZE;++cx){
            for(cz=0;cz<Chunk.CHUNK_SIZE;++cz){
                int height = (int)Math.round(perlinNoise.noise(offsetX + cx, offsetZ + cz));
                for(cy=0;cy<height;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, BlockRegistry.BLOCK_TYPE_GRASS);
                }
                for(cy=height;cy<Chunk.CHUNK_HEIGHT;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, BlockRegistry.BLOCK_TYPE_AIR);
                }
            }
        }
    }

}
