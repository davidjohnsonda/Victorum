package victorum.engine.common;

import java.util.HashMap;

public class VictorumPlanet {
    public VictorumBlock[] blockArray = new VictorumBlock[CommonParams.planetSize * CommonParams.planetSize * CommonParams.planetSize];
    public HashMap<String, Object> properties = new HashMap<>();
}
