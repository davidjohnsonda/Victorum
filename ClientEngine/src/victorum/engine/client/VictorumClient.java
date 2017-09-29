package victorum.engine.client;

import com.jme3.app.SimpleApplication;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.style.BaseStyles;
import victorum.engine.common.*;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VictorumClient extends SimpleApplication{
    private ArrayList<VictorumBlock> blocks = new ArrayList<>();
    private ArrayList<VictorumEntity> entities = new ArrayList<>();
    private ArrayList<VictorumWindow> GUI = new ArrayList<>();
    private HashMap<String, Object> otherData = new HashMap<>();
    private HashMap<String, Object> playerParams = new HashMap<>();
    private static final int DISPLAY_PADDING = 200;
    public static void main(String[] args){
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
    @Override
    public void simpleUpdate(float tpf)
    {

    }
    private void displayBlock(VictorumBlock vb)
    {
        Spatial model = assetManager.loadModel("Models/Blocks/" + (String)vb.properties.get("Model") + ".obj");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Blocks/" + (String)vb.properties.get("Texture") + ".jpg"));
        model.setMaterial(mat);
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot);
        pivot.attachChild(model);
        pivot.rotate((float)vb.properties.get("RotX"), (float)vb.properties.get("RotY"), (float)vb.properties.get("RotZ"));
        pivot.setLocalTranslation((float)vb.properties.get("PosX"), (float)vb.properties.get("PosY"), (float)vb.properties.get("PosZ"));
    }
    private void displayEntity(VictorumEntity ve)
    {
        Spatial model = assetManager.loadModel("Models/Blocks/" + (String)ve.properties.get("Model") + ".obj");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Blocks/" + (String)ve.properties.get("Texture") + ".jpg"));
        model.setMaterial(mat);
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot);
        pivot.attachChild(model);
        pivot.rotate((float)ve.properties.get("RotX"), (float)ve.properties.get("RotY"), (float)ve.properties.get("RotZ"));
        pivot.setLocalTranslation((float)ve.properties.get("PosX"), (float)ve.properties.get("PosY"), (float)ve.properties.get("PosZ"));
    }
    private void displayWindow(VictorumWindow vw)
    {
        Container window = new Container();
        guiNode.attachChild(window);
        window.setLocalTranslation((float)vw.properties.get("PosX"), (float)vw.properties.get("PosY"), (float)vw.properties.get("PosZ"));
        window.setLocalScale((float)vw.properties.get("SizeX"), (float)vw.properties.get("SizeY"), (float)vw.properties.get("SizeZ"));
        for(Panel control : (ArrayList<Panel>)vw.properties.get("Controls"))
        {
            Panel finalControl = window.addChild(control);
            finalControl.addMouseListener(new DefaultMouseListener()
            {
                @Override
                public void mouseButtonEvent(MouseButtonEvent evt, Spatial target, Spatial capture) {
                    if(evt.isReleased()) {

                    }
                }
            });
        }
    }
}