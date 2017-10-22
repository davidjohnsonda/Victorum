package io.github.victorum.world.generator.height;

public interface OctaveAmplitudeController{

    class AmplitudePair{
        final double min, max;
        public AmplitudePair(double min, double max){
            this.min = min;
            this.max = max;
        }
    }

    AmplitudePair getAmplitude(int x, int z);

}
