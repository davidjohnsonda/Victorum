package io.github.victorum.world.generator.decoration;

import io.github.victorum.world.Chunk;
import io.github.victorum.world.ChunkCoordinates;
import io.github.victorum.world.generator.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.CRC32;

public class ChunkDecorationGenerator{
    private final ArrayList<Tree> treeArrayList = new ArrayList<>();

    public ChunkDecorationGenerator(ChunkCoordinates coordinates, WorldGenerator worldGenerator, long seed){
        CRC32 crc32 = new CRC32();
        crc32.update(coordinates.getChunkX());
        crc32.update(coordinates.getChunkZ());

        Random random = new Random(crc32.getValue());
        int treeCount = random.nextInt(4);

        for(int i=0;i<treeCount;++i){
            Tree tree = Tree.tryTreeCreation(coordinates, random, worldGenerator);
            if(tree != null){
                treeArrayList.add(tree);
            }
        }
    }

    public synchronized void generateForChunk(Chunk chunk, WorldGenerator worldGenerator){
        for(Tree tree : treeArrayList){
            tree.generateForChunk(chunk, worldGenerator);
        }
    }

}
