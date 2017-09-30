package victorum.engine.server;

import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;
import victorum.api.ServerEngine;
import victorum.engine.common.mod.ModManager;

public class VictorumServer extends SimpleApplication implements ServerEngine{
    private final ModManager modManager = new ModManager(this);

    public static void main(String[] args){
        new VictorumServer().start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp(){
        stateManager.attach(modManager);
    }

}
