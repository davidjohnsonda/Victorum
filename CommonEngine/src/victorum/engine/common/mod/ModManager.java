package victorum.engine.common.mod;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

import victorum.api.Engine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager extends BaseAppState{
    private final Engine engine;
    private final CopyOnWriteArrayList<ModHarness> mods = new CopyOnWriteArrayList<>();
    private final List<ModHarness> readOnlyModsList;

    public ModManager(Engine engine){
        this.engine = engine;
        readOnlyModsList = Collections.unmodifiableList(mods);
    }

    @Override
    protected void initialize(Application application){
        loadMods();
        initMods();
    }

    @Override
    protected void cleanup(Application application){
        for(ModHarness modHarness : mods){
            modHarness.getMod().onDeinit();
        }
    }

    private void loadMods(){
        ArrayList<URL> urlArrayList = new ArrayList<>();
        ArrayList<File> fileArrayList = new ArrayList<>();
        File modsFolder = new File("./mods");

        if(!modsFolder.exists()) modsFolder.mkdirs();
        if(modsFolder.isFile()) throw new RuntimeException("Mods folder is a file");

        for(File mod : modsFolder.listFiles()){
            try{
                if((!mod.isDirectory()) && mod.getName().toLowerCase().endsWith(".jar")){
                    urlArrayList.add(mod.toURI().toURL());
                    fileArrayList.add(mod);
                }
            }catch(MalformedURLException ex){
                ex.printStackTrace(); //skip over this mod, but continue loading
            }
        }

        URLClassLoader urlClassLoader = new URLClassLoader(urlArrayList.toArray(new URL[urlArrayList.size()]));

        for(File mod : fileArrayList){
            try{
                mods.add(new ModHarness(mod, urlClassLoader));
            }catch(IOException ex){
                ex.printStackTrace(); //skip over this mod, but continue loading
            }
        }
    }

    private void initMods(){
        for(ModHarness modHarness : mods){
            modHarness.getMod().onInit(engine);
        }
    }

    public List<ModHarness> getModHarnesses(){
        return readOnlyModsList;
    }

    @Override protected void onEnable(){}
    @Override protected void onDisable(){}
}
