package io.github.victorum.util;

import java.util.Random;

public final class MathUtil{

    private MathUtil() {}

    public static boolean isPrime(int n){
        int sqrtN = (int)Math.floor(Math.sqrt(n));
        for(int i=2;i<sqrtN;++i){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    public static int randomPrimeInRange(Random random, int min, int max){
        int n = random.nextInt(max-min)+min;
        while(!isPrime(n)){
            if(++n >= max){
                n = min;
            }
        }
        return n;
    }

    public static double cosineInterpolation(double a, double b, double x){
        double ft = x*Math.PI;
        double f = (1 - Math.cos(ft))*.5;
        return a*(1-f)+b*f;
    }

}
