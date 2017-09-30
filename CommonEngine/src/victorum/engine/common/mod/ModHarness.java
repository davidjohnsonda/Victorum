package victorum.engine.common.mod;

import victorum.api.Mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModHarness{
    private final Mod mod;
    private final String modName;
    private final String modVersion;

    public ModHarness(File file, ClassLoader classLoader) throws IOException{
        Properties modProperties = new Properties();

        //load properties
        try(
                FileInputStream fileInputStream = new FileInputStream(file);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ){
            //get properties file from zip
            ZipEntry entry;
            do{
                entry = zipInputStream.getNextEntry();
                if(entry == null){ //null happens when we run out of entries, which should not happen since the loop should exit first
                    throw new IOException("Failed to load mod " + file.getAbsolutePath() + " because there is no mod.properties file.");
                }
            }while(!entry.getName().equalsIgnoreCase("mod.properties"));

            modProperties.load(zipInputStream);
        }

        modName = modProperties.getProperty("name");
        modVersion = modProperties.getProperty("version");

        try{
            Class<? extends Mod> clazz = ((Class<? extends Mod>)classLoader.loadClass(modProperties.getProperty("mainClass")));
            mod = clazz.newInstance();
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex){
            throw new IOException("Failed to load mod " + modName + ".", ex);
        }
    }

    public Mod getMod(){
        return mod;
    }

    public String getModName(){
        return modName;
    }

    public String getModVersion(){
        return modVersion;
    }

}
