package victorum.engine.client;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

import victorum.api.ClientEngine;
import victorum.engine.common.mod.ModManager;

import java.awt.Dimension;
import java.awt.Toolkit;

public class VictorumClient extends SimpleApplication implements ClientEngine{
    private static final int DISPLAY_PADDING = 200;
    private final ModManager modManager = new ModManager(this);

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        AppSettings settings = new AppSettings(true);
        settings.setWidth((int)Math.round(screenSize.getWidth()-DISPLAY_PADDING));
        settings.setHeight((int)Math.round((screenSize.getWidth()-DISPLAY_PADDING)*(9/16.0)));
        settings.setVSync(false); //for debugging, so we can see actual fps
        settings.setResizable(true);
        settings.setTitle("Victorum");

        VictorumClient client = new VictorumClient();
        client.setSettings(settings);
        client.setShowSettings(false);
        client.start();
    }

    @Override
    public void simpleInitApp(){
        stateManager.attach(modManager);
    }

}
