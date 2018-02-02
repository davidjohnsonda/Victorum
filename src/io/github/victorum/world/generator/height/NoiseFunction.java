package io.github.victorum.world.generator.height;

import io.github.victorum.util.MathUtil;

import java.util.Random;

public class NoiseFunction{
    private final int a, b, c, o;
    private final double d;

    public NoiseFunction(Random random){
        a = MathUtil.randomPrimeInRange(random, 10000, 99999);
        b = MathUtil.randomPrimeInRange(random, 100000, 999999);
        c = MathUtil.randomPrimeInRange(random, 1000000000, Integer.MAX_VALUE);
        d = MathUtil.randomPrimeInRange(random, 1000000000, Integer.MAX_VALUE);
        o = MathUtil.randomPrimeInRange(random, 50, 200);
    }

    public double noise(int x){
        x = (x<<13) ^ x;
        return ( 1.0 - ( (x * (x * x * a + b) + c) & 0x7fffffff) / d);
    }

    public double noise(int x, int z){
        return noise(x + z*o);
    }

    public double scaledNoise(int x, int z){
        double rawNoise = noise(x, z);
        return (rawNoise+1)*.5;
    }

}
