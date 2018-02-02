package io.github.victorum.world.generator;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockType;
import io.github.victorum.world.Chunk;
import io.github.victorum.world.generator.decoration.ChunkDecorationGenerator;
import io.github.victorum.world.generator.height.NoiseFunction;
import io.github.victorum.world.generator.height.PerlinNoise;

import java.util.Random;

public class WorldGenerator{
    public static final int SEA_LEVEL = 100;
    public static final int SAND_LEVEL = SEA_LEVEL+3;
    private final NoiseFunction dirtFunction;
    private final PerlinNoise perlinNoise;

    public WorldGenerator(){
        Random random = new Random(0L);
        dirtFunction = new NoiseFunction(random);
        perlinNoise = new PerlinNoise(random);
    }

    public void generateChunk(Chunk chunk){
        int cx, cy, cz;

        int offsetX = chunk.getChunkCoordinates().getChunkX()*Chunk.CHUNK_SIZE;
        int offsetZ = chunk.getChunkCoordinates().getChunkZ()*Chunk.CHUNK_SIZE;

        for(cx=0;cx<Chunk.CHUNK_SIZE;++cx){
            for(cz=0;cz<Chunk.CHUNK_SIZE;++cz){
                int maximumHeight = heightAt(cx+offsetX, cz+offsetZ);
                int coveringStartHeight = maximumHeight - (int)Math.round(dirtFunction.scaledNoise(cx, cz)*5);
                if(coveringStartHeight < 0) coveringStartHeight = 0;

                BlockType covering = maximumHeight < SAND_LEVEL ? BlockRegistry.BLOCK_TYPE_SAND : BlockRegistry.BLOCK_TYPE_DIRT;
                BlockType topCovering = maximumHeight < SAND_LEVEL ? BlockRegistry.BLOCK_TYPE_SAND : BlockRegistry.BLOCK_TYPE_GRASS;

                for(cy=0;cy<coveringStartHeight;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, BlockRegistry.BLOCK_TYPE_STONE);
                }
                for(cy=coveringStartHeight;cy<maximumHeight;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, covering);
                }
                chunk.setBlockTypeAt(cx, maximumHeight, cz, topCovering);
                for(cy=maximumHeight+1;cy<Chunk.CHUNK_HEIGHT;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, cy < SEA_LEVEL ? BlockRegistry.BLOCK_TYPE_WATER : BlockRegistry.BLOCK_TYPE_AIR);
                }
            }
        }

        long seed = 6563;
        new ChunkDecorationGenerator(chunk.getChunkCoordinates(), this, seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(-1, -1), this,  seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(0, -1), this,  seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(1, -1), this,  seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(-1, 0), this,  seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(1, 0), this,  seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(-1, 1), this, seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(0, 1), this, seed).generateForChunk(chunk, this);
        new ChunkDecorationGenerator(chunk.getChunkCoordinates().add(1, 1), this, seed).generateForChunk(chunk, this);
    }

    public int heightAt(int x, int z){
        double fractionalHeight = perlinNoise.noiseAt(x, z);

        fractionalHeight = Math.pow(fractionalHeight, 2);

        return (int)Math.round(fractionalHeight*Chunk.CHUNK_HEIGHT);
    }

}
