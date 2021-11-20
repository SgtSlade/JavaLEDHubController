package org.sgtslade.application;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AppConfig {
    public static File getResource(String internalResourcePath){
        try{
            return new File(String.format("/resources/%s",internalResourcePath));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getConfigVal(String valKey){
        List<String[]> configSet = getConfigSet();
        try {
            return configSet.get(getIndexByKey(valKey))[1];
        }catch (ConfigKeyNotFound e){
            return null;
        }
    }

    public static void setConfigVal(String valKey, String value){
        List<String[]> configSet = getConfigSet();
        try {
            configSet.get(getIndexByKey(valKey))[1] = value;
        }catch (ConfigKeyNotFound e){
            configSet.add(new String[]{valKey,value});
        }
        writeConfigSet(configSet);
    }

    public static void writeConfigSet(List<String[]> configSet){
        StringBuilder builder = new StringBuilder();
        configSet.stream().forEach(strings -> {
            builder.append(String.format("%s = %s\n",strings[0],strings[1]));
        });
        AppData.writeToFile(AppData.getConfigFile(),builder.toString().getBytes(StandardCharsets.UTF_8),true);
    }

    public static List<String[]> getConfigSet(){
        try {
            Scanner configScanner = new Scanner(AppData.getConfigFile());
            List<String[]> pairList = new ArrayList<>();
            while(configScanner.hasNextLine()){
                String line = configScanner.nextLine();
                if(line.length()>0){
                    String[] splitLine = line.split("=",2);
                    if(splitLine.length==2) {
                        splitLine[0] = splitLine[0].trim();
                        splitLine[1] = splitLine[1].trim();
                        pairList.add(splitLine);
                    }
                }
            }
            return pairList;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void displayConfig(){
        List<String[]> configSet = AppConfig.getConfigSet();
        configSet.stream().forEach(strings -> System.out.println(Arrays.asList(strings)));
    }

    public static int getIndexByKey(String key) throws ConfigKeyNotFound{
        List<String[]> configSet = getConfigSet();
        for(int i=0;i<configSet.size();i++){
            if(configSet.get(i)[0].trim().equals(key)){
                return i;
            }
        }
        throw new ConfigKeyNotFound();
    }

    public static boolean keyExists(String key){
        try{
            getIndexByKey(key);
            return true;
        }catch (ConfigKeyNotFound e){
            return false;
        }
    }
}
