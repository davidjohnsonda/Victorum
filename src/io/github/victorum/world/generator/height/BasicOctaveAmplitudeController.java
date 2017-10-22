package io.github.victorum.world.generator.height;

public class BasicOctaveAmplitudeController implements OctaveAmplitudeController{
    private static final AmplitudePair AMPLITUDE_PAIR = new AmplitudePair(0, 1);

    @Override
    public AmplitudePair getAmplitude(int x, int z){
        return AMPLITUDE_PAIR;
    }

}
