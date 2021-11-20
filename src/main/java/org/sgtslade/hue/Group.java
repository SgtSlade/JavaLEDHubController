package org.sgtslade.hue;

import org.json.JSONObject;
import org.sgtslade.hue.lights.Light;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Group {
    private List<Light> lights = new ArrayList<>();
    private final Bridge groupParent;
    private final String groupId;
    private String groupName;
    private final String groupType;
    private final String groupClass;

    public Group(String id,JSONObject groupObject, Bridge parentBridge){
        groupId = id;
        groupParent = parentBridge;
        groupName = groupObject.getString("name");
        groupType = groupObject.getString("type");
        groupClass = groupObject.getString("class");
        for(Object lightId: groupObject.getJSONArray("lights")){
            lights.add(parentBridge.getLights().get(lightId));
        }
    }

    public void setAttrVar(String var, Object val){
        JSONObject object = new JSONObject();
        object.put(var,val);
        System.out.println(API.putUserEndpoint(getGroupParent().getBridgeAdress(),String.format("/groups/%s", getGroupId()),object));
    }

    public void setGroupState(String arg, Object val){
        JSONObject object = new JSONObject();
        object.put(arg,val);
        System.out.println(API.putUserEndpoint(getGroupParent().getBridgeAdress(),String.format("/groups/%s/action", getGroupId()),object));
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append(String.format("%s - %s (%s)\n",groupName,groupType,groupClass));
        lights.forEach(light -> toReturn.append(light).append("\n"));
        return toReturn.toString();
    }

    public List<Light> getLights() {
        return lights;
    }

    public void addLight(Light toAdd){
        getLights().add(toAdd);
        setLights(getLights());
    }

    public void removeLight(Light toRemove){
        getLights().remove(toRemove);
        setLights(getLights());
    }

    public void setLights(List<Light> lights) {
        setAttrVar("lights",lights.stream().map(Light::getLightId).collect(Collectors.toList()));
        this.lights = lights;
    }

    public Bridge getGroupParent() {
        return groupParent;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        setAttrVar("name",groupName);
    }

    public String getGroupType() {
        return groupType;
    }

    public String getGroupClass() {
        return groupClass;
    }

    public void setScene(Scene toSet){
        setGroupState("scene",toSet.getSceneId());
    }
}
