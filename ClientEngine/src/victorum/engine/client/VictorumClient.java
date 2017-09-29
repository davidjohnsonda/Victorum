package victorum.engine.client;

import com.jme3.app.SimpleApplication;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.*;
import com.simsilica.lemur.*;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.style.BaseStyles;
import victorum.engine.common.*;
import java.awt.*;
import java.io.*;
import java.util.*;


public class VictorumClient extends SimpleApplication {
    private ArrayList<VictorumBlock> blocks = new ArrayList<>();
    private ArrayList<VictorumEntity> entities = new ArrayList<>();
    private ArrayList<VictorumWindow> GUI = new ArrayList<>();
    
    private HashMap<String, Object> otherData = new HashMap<>();
    private HashMap<String, Object> playerParams = new HashMap<>();
    
    private static final int DISPLAY_PADDING = 200;
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        AppSettings settings = new AppSettings(true);
        settings.setWidth((int)Math.round(screenSize.getWidth()-DISPLAY_PADDING));
        settings.setHeight((int)Math.round((screenSize.getWidth()-DISPLAY_PADDING)*(9/16.0)));
        settings.setVSync(false); //for debugging, so we can see actual fps
        settings.setResizable(false);
        settings.setTitle("Victorum");

        VictorumClient client = new VictorumClient();
        client.setSettings(settings);
        client.setShowSettings(false);
        client.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        playerParams.replace("UIEvents", new ArrayList<UIEventData>());
    }
    
    private void displayBlock(VictorumBlock vb) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Blocks/" + (String)vb.properties.get("Texture") + ".jpg"));
        assetManager.loadModel("Models/Blocks/" + (String)vb.properties.get("Model") + ".obj").setMaterial(mat);
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot);
        pivot.attachChild(model);
        pivot.rotate((float)vb.properties.get("RotX"), (float)vb.properties.get("RotY"), (float)vb.properties.get("RotZ"));
        pivot.setLocalTranslation((float)vb.properties.get("PosX"), (float)vb.properties.get("PosY"), (float)vb.properties.get("PosZ"));
    }

    private void displayWindow(VictorumWindow vw) {
        Container window = new Container();
        guiNode.attachChild(window);
        window.setLocalTranslation((float)vw.properties.get("PosX"), (float)vw.properties.get("PosY"), (float)vw.properties.get("PosZ"));
        window.setLocalScale((float)vw.properties.get("SizeX"), (float)vw.properties.get("SizeY"), (float)vw.properties.get("SizeZ"));
        
        for(Panel control : (ArrayList<Panel>)vw.properties.get("Controls")) {
            window.addChild(control).addMouseListener(new DefaultMouseListener() {
                @Override
                public void mouseButtonEvent(MouseButtonEvent evt, Spatial target, Spatial capture) {
                    if(evt.isReleased()) {

                    }
                }
            });
        }
    }
}
