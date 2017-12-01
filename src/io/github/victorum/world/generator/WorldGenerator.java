package io.github.victorum.world.generator;

import io.github.victorum.block.BlockRegistry;
import io.github.victorum.block.BlockType;
import io.github.victorum.world.Chunk;
import io.github.victorum.world.generator.biomes.BiomeController;
import io.github.victorum.world.generator.height.NoiseFunction;
import io.github.victorum.world.generator.height.Octave;

import java.util.Random;

public class WorldGenerator{
    private final BiomeController biomeController;
    private final NoiseFunction dirtFunction;
    private Octave terrainOctaves[];

    public WorldGenerator(){
        Random random = new Random(0L);
        biomeController = new BiomeController(random);
        dirtFunction = new NoiseFunction(random);

        terrainOctaves = new Octave[8];
        for(int i=0;i<terrainOctaves.length;++i){
            terrainOctaves[i] = new Octave(random, biomeController, (int)Math.pow(2, i), Math.pow(2, i));
        }
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

                BlockType covering = maximumHeight < 110 ? BlockRegistry.BLOCK_TYPE_SAND : BlockRegistry.BLOCK_TYPE_DIRT;
                BlockType topCovering = maximumHeight < 110 ? BlockRegistry.BLOCK_TYPE_SAND : BlockRegistry.BLOCK_TYPE_GRASS;

                for(cy=0;cy<coveringStartHeight;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, BlockRegistry.BLOCK_TYPE_STONE);
                }
                for(cy=coveringStartHeight;cy<maximumHeight;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, covering);
                }
                chunk.setBlockTypeAt(cx, maximumHeight, cz, topCovering);
                for(cy=maximumHeight+1;cy<Chunk.CHUNK_HEIGHT;++cy){
                    chunk.setBlockTypeAt(cx, cy, cz, cy < 100 ? BlockRegistry.BLOCK_TYPE_WATER : BlockRegistry.BLOCK_TYPE_AIR);
                }
            }
        }
    }

    private int heightAt(int x, int z){
        double total = 0;
        for(Octave terrainOctave : terrainOctaves){
            total += terrainOctave.noiseAt(x, z);
        }
        return (int)Math.round(total);
    }

}
