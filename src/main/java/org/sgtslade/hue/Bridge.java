package org.sgtslade.hue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sgtslade.hue.lights.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bridge {
    private final String bridgeId;
    private final String bridgeAdress;
    private final Map<String,Light> lights = new HashMap<>();
    private final Map<String,Group> groups = new HashMap<>();
    private final Map<String,Scene> scenes = new HashMap<>();

    public static Bridge getClosestBridge(){
        String response = API.getDiscoveryData();
        return new Bridge(API.JSONify(response));
    }

    public Bridge(JSONObject bridgeObject){
        bridgeAdress = bridgeObject.getString("internalipaddress");
        bridgeId = bridgeObject.getString("id");
    }

    public void createEmptyGroup(){
        int cgInt = 1;
        String name = "Created group %s";
        while (true){
            if(groups.values().stream().map(Group::getGroupName).collect(Collectors.toList()).contains(String.format(name,cgInt))){
                cgInt++;
            }else {
                break;
            }
        }
        JSONObject data = new JSONObject();
        data.put("lights",new JSONArray());
        data.put("name",String.format(name,cgInt));
        data.put("type","Zone");
        System.out.println(API.postUserEndpoint(bridgeAdress,"/groups",data));
        setGroups();
    }

    public void deleteGroup(Group g){
        API.deleteUserEndpoint(bridgeAdress,String.format("/groups/%s",g.getGroupId()));
        setGroups();
    }

    public String getBridgeId() {
        return bridgeId;
    }

    public String getBridgeAdress() {
        return bridgeAdress;
    }

    public void setDevices(){
        setLights();
        setGroups();
        setScenes();
    }

    private void setLights(){
        lights.clear();
        JSONObject activeLights = API.getUserEndpoint(bridgeAdress,"/lights");
        activeLights.keys().forEachRemaining(s -> lights.put(s,new Light(activeLights.getJSONObject(s),this,s)));
    }

    private void setGroups(){
        groups.clear();
        JSONObject activeGroups = API.getUserEndpoint(bridgeAdress,"/groups");
        activeGroups.keys().forEachRemaining(s -> groups.put(s,new Group(s,activeGroups.getJSONObject(s),this)));
    }

    private void setScenes(){
        scenes.clear();
        JSONObject activeScenes = API.getUserEndpoint(bridgeAdress,"/scenes");
        Map<String,JSONObject> detailedScenes = new HashMap<>();
        activeScenes.keys().forEachRemaining(s->{detailedScenes.put(s,API.getUserEndpoint(bridgeAdress,String.format("/scenes/%s",s)));});
        for(Map.Entry<String,JSONObject> entry:detailedScenes.entrySet()){
            scenes.put(entry.getKey(),new Scene(entry.getKey(),entry.getValue(),this));
        }
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public Map<String, Light> getLights() {
        return lights;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }
}
