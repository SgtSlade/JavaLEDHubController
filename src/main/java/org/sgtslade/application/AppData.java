package org.sgtslade.application;

import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AppData {

    private static final File configFile = new File("./userdata/config.sld");

    public static Font getFont(String fontname,int size){
        try{
            Font toReturn = Font.loadFont(new FileInputStream(String.format("data/font/%s",fontname)),size);
            return toReturn;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean resourceExists(String resourcePath){
        return new File(String.format("./data/%s",resourcePath)).exists();
    }



    public static byte[] openFile(File toOpen,boolean create){
        try {
            if(create) {
                toOpen.createNewFile();
            }
            FileInputStream stream = new FileInputStream(toOpen);
            return stream.readAllBytes();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToFile(File toOpen,byte[] toAppend, boolean create){
        try{
            if(create) {
                toOpen.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(toOpen);
            stream.write(toAppend);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static File getConfigFile() {
        return configFile;
    }
}
