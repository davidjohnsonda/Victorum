package io.github.victorum.world.generator.height;

import io.github.victorum.util.MathUtil;

import java.util.Random;

public class Octave{
    private final NoiseFunction noiseFunction;
    private final int frequency;
    private final double amplitude;

    public Octave(Random random, int frequency, double amplitude){
        this.noiseFunction = new NoiseFunction(random);
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    public double noiseAt(int x, int z){
        int minX = min(x);
        int minZ = min(z);
        int maxX = minX+1;
        int maxZ = minZ+1;

        double fracX = (x-minX*frequency)/(double)frequency;
        double fracZ = (z-minZ*frequency)/(double)frequency;

        double n1 = noiseFunction.scaledNoise(minX, minZ);
        double n2 = noiseFunction.scaledNoise(maxX, minZ);
        double n3 = noiseFunction.scaledNoise(maxX, maxZ);
        double n4 = noiseFunction.scaledNoise(minX, maxZ);

        double upperInterpolatedValue = MathUtil.cosineInterpolation(n1, n2, fracX);
        double lowerInterpolatedValue = MathUtil.cosineInterpolation(n4, n3, fracX);

        double noise =  MathUtil.cosineInterpolation(upperInterpolatedValue, lowerInterpolatedValue, fracZ);
        return noise*amplitude;
    }

    private int min(int c){
        if(c < 0){
            return -(min(-c)+1);
        }else{
            return c/frequency;
        }
    }

}
