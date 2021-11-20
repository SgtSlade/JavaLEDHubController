package org.sgtslade.hue;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sgtslade.App;
import org.sgtslade.application.AppConfig;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class API {

    private static final String URL = "http://%s/api%s";
    private static final String DISCOVERY_URL = "https://discovery.meethue.com";
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void registerDevice(String bridgeAdress) throws HubButtonNotPressedException{
        if(!checkAuth(App.activeBridge.getBridgeAdress(),AppConfig.getConfigVal("username"))) {
            System.out.println("User not authorized, creating a user");
            try {
                String hostname = Inet4Address.getLocalHost().getHostName();
                JSONObject postData = new JSONObject(String.format("{\"devicetype\":\"LED-HUB-App#%s\"}", hostname));

                JSONObject responseData = postData(bridgeAdress, "", postData);
                if (responseData.has("error")) {
                    throw new HubButtonNotPressedException();
                }
                String username = responseData.getJSONObject("success").getString("username");
                AppConfig.setConfigVal("username", username);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("User authorized, skipping");
        }
    }

    public static boolean checkAuth(String bridgeAdress, String username){
        if(username.trim().length()>0) {
            try {
                JSONObject data = getData(bridgeAdress, String.format("/%s", username));
                return true;
            }catch (Exception e){
                return false;
            }
        }else {
            return false;
        }
    }

    public static String getDiscoveryData(){
        Request discoveryRequest = new Request.Builder().url(DISCOVERY_URL).get().build();
        try(Response dataResponse = CLIENT.newCall(discoveryRequest).execute()){
            return dataResponse.body().string();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static boolean needPairing(){
        if(AppConfig.getConfigVal("bridgeIP").equals("")){
            return true;
        }
        return !checkAuth(App.activeBridge.getBridgeAdress(),AppConfig.getConfigVal("username"));
    }

    public static String getFirstBridgeIP(){
        return JSONify(getDiscoveryData()).getString("internalipaddress");
    }

    public static JSONObject getData(String bridgeAddress, String endpoint) throws Exception{
        String formatted = String.format(URL,bridgeAddress,endpoint);
        Request dataRequest = new Request.Builder().url(formatted).get().build();

        try(Response dataResponse = CLIENT.newCall(dataRequest).execute()) {
            String stringResponse= dataResponse.body().string();
            JSONObject jsonResponse = JSONify(stringResponse);
            if (jsonResponse.has("error")){
                throw new Exception(jsonResponse.getJSONObject("error").getString("description"));
            }else {
                return jsonResponse;
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject postData(String bridgeAddress, String endpoint, JSONObject data){
        String formatted = String.format(URL,bridgeAddress,endpoint);
        Request dataRequest = new Request.Builder()
                .url(formatted)
                .post(RequestBody.create(data.toString(), MediaType.get("application/json")))
                .build();

        try(Response dataResponse = CLIENT.newCall(dataRequest).execute()){
            return JSONify(dataResponse.body().string());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject deleteData(String bridgeAddress, String endpoint){
        String formatted = String.format(URL,bridgeAddress,endpoint);
        Request dataRequest = new Request.Builder()
                .url(formatted)
                .delete()
                .build();

        try(Response dataResponse = CLIENT.newCall(dataRequest).execute()){
            return JSONify(dataResponse.body().string());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject putData(String bridgeAddress, String endpoint, JSONObject data){
        String formatted = String.format(URL,bridgeAddress,endpoint);
        Request dataRequest = new Request.Builder()
                .url(formatted)
                .put(RequestBody.create(data.toString(), MediaType.get("application/json")))
                .build();

        try(Response dataResponse = CLIENT.newCall(dataRequest).execute()){
            return JSONify(dataResponse.body().string());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getUserEndpoint(String bridgeAddress, String endpoint){
        String formattedEndpoint = String.format("/%s%s",AppConfig.getConfigVal("username"),endpoint);
        try{
            return getData(bridgeAddress,formattedEndpoint);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static JSONObject postUserEndpoint(String bridgeAddress, String endpoint, JSONObject data){
        String formattedEndpoint = String.format("/%s%s",AppConfig.getConfigVal("username"),endpoint);
        try{
            return postData(bridgeAddress,formattedEndpoint, data);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject deleteUserEndpoint(String bridgeAddress, String endpoint){
        String formattedEndpoint = String.format("/%s%s",AppConfig.getConfigVal("username"),endpoint);
        try{
            return deleteData(bridgeAddress,formattedEndpoint);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject putUserEndpoint(String bridgeAddress, String endpoint, JSONObject data){
        String formattedEndpoint = String.format("/%s%s",AppConfig.getConfigVal("username"),endpoint);
        try{
            return putData(bridgeAddress,formattedEndpoint, data);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    protected static JSONObject JSONify(String data){
        if(data.startsWith("[")){
            return new JSONArray(data).getJSONObject(0);
        }else {
            return new JSONObject(data);
        }
    }


}
