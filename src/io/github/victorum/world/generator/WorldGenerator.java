package io.github.victorum.world;

import io.github.victorum.block.BlockRegistry;

import java.util.Random;

public class WorldGenerator{

    protected void generateChunk(Chunk chunk){
        Random random = new Random(chunk.getChunkCoordinates().hashCode());
        int cx, cy, cz;

        for(cx=0;cx<Chunk.CHUNK_SIZE;++cx){
            for(cz=0;cz<Chunk.CHUNK_SIZE;++cz){
                int height = random.nextInt(Chunk.CHUNK_HEIGHT);
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
