package io.github.victorum.world.generator.height;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class PerlinNoise{
    private static final int OCTAVE_COUNT = 8;
    private static final double MAXIMUM_AMPLITUDE;
    private final AtomicReferenceArray<Octave> octaveArrayList = new AtomicReferenceArray<>(OCTAVE_COUNT);

    static{
        double maxamp = 0;
        for(int i=0;i<OCTAVE_COUNT;++i){
            maxamp += 1.0/Math.pow(2, OCTAVE_COUNT-i);
        }
        MAXIMUM_AMPLITUDE = maxamp;
    }

    public PerlinNoise(Random random){
        for(int i=0;i<OCTAVE_COUNT;++i){
            octaveArrayList.set(i, new Octave(random, (int)Math.pow(2, i+1), 1.0/Math.pow(2, OCTAVE_COUNT-i)));
        }
    }

    public double noiseAt(int x, int z){
        double total = 0;
        for(int i=0;i<OCTAVE_COUNT;++i){
            total += octaveArrayList.get(i).noiseAt(x, z);
        }
        return total/MAXIMUM_AMPLITUDE;
    }

}
