package victorum.mod;

import victorum.api.ClientEngine;
import victorum.api.Engine;
import victorum.api.Mod;
import victorum.api.ServerEngine;

public class VictorumMod extends Mod{

    @Override
    public void onInit(Engine engine){
        if(engine instanceof ServerEngine){
            System.out.println("VictorumMod is running on the server.");
        }else if(engine instanceof ClientEngine){
            System.out.println("VictorumMod is running on the client.");
        }else{
            System.out.println("VictorumMod is running in some unknown location.");
        }
    }

    @Override
    public void onDeinit(){
        System.out.println("VictorumMod is shutting down D:.");
    }

}
