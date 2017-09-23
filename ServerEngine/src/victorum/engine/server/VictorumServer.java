package victorum.engine.server;

import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class VictorumServer extends SimpleApplication{

    public static void main(String[] args){
        new VictorumServer().start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp(){

    }

}
