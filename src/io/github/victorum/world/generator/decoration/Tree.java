package io.github.victorum.world.generator.decoration;

import io.github.victorum.inventory.block.BlockRegistry;
import io.github.victorum.util.MathUtil;
import io.github.victorum.world.BlockCoordinates;
import io.github.victorum.world.Chunk;
import io.github.victorum.world.ChunkCoordinates;
import io.github.victorum.world.generator.WorldGenerator;

import java.util.Random;

public class Tree{
    private int baseX, baseZ, height, radius;

    protected static Tree tryTreeCreation(ChunkCoordinates base, Random random, WorldGenerator generator){
        Tree tree = new Tree();
        tree.baseX = base.getChunkX()*Chunk.CHUNK_SIZE+random.nextInt(Chunk.CHUNK_SIZE);
        tree.baseZ = base.getChunkZ()*Chunk.CHUNK_SIZE+random.nextInt(Chunk.CHUNK_SIZE);

        if(generator.heightAt(tree.baseX, tree.baseZ) < WorldGenerator.SEA_LEVEL){
            return null;
        }

        tree.radius = random.nextInt(2)+3;
        tree.height = random.nextInt(3)+tree.radius+1;
        return tree;
    }

    protected void generateForChunk(Chunk chunk, WorldGenerator worldGenerator){
        ChunkCoordinates cc = chunk.getChunkCoordinates();
        BlockCoordinates coordinates = new BlockCoordinates(baseX, baseZ);
        int baseHeight = worldGenerator.heightAt(baseX, baseZ);

        if(coordinates.getChunkX() == cc.getChunkX() && coordinates.getChunkZ() == cc.getChunkZ()){
            for(int i=baseHeight;i<baseHeight+height;++i){
                chunk.setBlockTypeAt(coordinates.getLocalX(), i, coordinates.getLocalZ(), BlockRegistry.BLOCK_TYPE_LOG);
            }
        }

        int topY = baseHeight+height;
        int startX = baseX-radius;
        int endX = baseX+radius;
        int startY = topY-radius;
        int endY = topY+radius;
        int startZ = baseZ-radius;
        int endZ = baseZ+radius;

        for(int x=startX;x<=endX;++x){
            for(int y=startY;y<=endY;++y){
                for(int z=startZ;z<=endZ;++z){
                    if(MathUtil.distanceSquared(x, y, z, baseX, topY, baseZ) < radius*radius){
                        BlockCoordinates blockCoordinates = new BlockCoordinates(x, z);
                        if(blockCoordinates.getChunkX() == cc.getChunkX() && blockCoordinates.getChunkZ() == cc.getChunkZ()){
                            chunk.setBlockTypeAt(blockCoordinates.getLocalX(), y, blockCoordinates.getLocalZ(), BlockRegistry.BLOCK_TYPE_LEAVES);
                        }
                    }
                }
            }
        }
    }

}
