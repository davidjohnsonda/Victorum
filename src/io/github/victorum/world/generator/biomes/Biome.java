package io.github.victorum.world.generator.biomes;

import io.github.victorum.world.generator.height.OctaveAmplitudeController;

public class Biome{
    private final OctaveAmplitudeController.AmplitudePair amplitudePair;

    public Biome(OctaveAmplitudeController.AmplitudePair amplitudePair){
        this.amplitudePair = amplitudePair;
    }

    public OctaveAmplitudeController.AmplitudePair getAmplitudePair(){
        return amplitudePair;
    }

}
