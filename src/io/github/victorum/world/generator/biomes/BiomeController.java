package io.github.victorum.world.generator.biomes;

import io.github.victorum.world.generator.height.BasicOctaveAmplitudeController;
import io.github.victorum.world.generator.height.Octave;
import io.github.victorum.world.generator.height.OctaveAmplitudeController;

import java.util.Random;

public class BiomeController implements OctaveAmplitudeController{
    private final Biome biomePlains = new Biome(new OctaveAmplitudeController.AmplitudePair(.5, .52));
    private final Biome biomeMountains = new Biome(new OctaveAmplitudeController.AmplitudePair(.2, .95));
    private final Biome biomeLowlands = new Biome(new OctaveAmplitudeController.AmplitudePair(.2, .4));
    private final Octave humidityOctave;
    private final Octave temperatureOctave;
    private final Octave heightVariationOctave;

    public BiomeController(Random random){
        humidityOctave = new Octave(random, new BasicOctaveAmplitudeController(), 256, 1);
        temperatureOctave = new Octave(random, new BasicOctaveAmplitudeController(), 256, 1);
        heightVariationOctave = new Octave(random, new BasicOctaveAmplitudeController(), 256, 1);
    }

    public Biome getBiomeAt(int x, int z){
        double heightVariation = heightVariationOctave.noiseAt(x, z);
        double humidity = humidityOctave.noiseAt(x, z);

        if(heightVariation > .75){
            return biomeMountains;
        }else{
            if(humidity < .5){
                return biomeLowlands;
            }else{
                return biomePlains;
            }
        }
    }

    @Override
    public AmplitudePair getAmplitude(int x, int z){
        return getBiomeAt(x, z).getAmplitudePair();
    }

}
