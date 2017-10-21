package io.github.victorum.world.generator;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class PerlinNoise{
    private final CopyOnWriteArrayList<Octave> octaves = new CopyOnWriteArrayList<>();

    public PerlinNoise(long seed){
        Random random = new Random(seed);
        octaves.add(new Octave(random, 256, 128)); //the sum of all amplitudes must be strictly less than Chunk.CHUNK_HEIGHT
        octaves.add(new Octave(random, 128, 32));
        octaves.add(new Octave(random, 32, 16));
        octaves.add(new Octave(random, 16, 8));
    }

    public double noise(int x, int z){
        double total = 0;
        for(Octave octave : octaves){
            total += octave.noiseAt(x, z);
        }
        return total;
    }

}
