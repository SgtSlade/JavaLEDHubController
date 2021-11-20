package org.sgtslade.hue.lights;

import org.json.JSONObject;
import org.sgtslade.hue.Bridge;

public class Light {
    private final Bridge parentBridge;
    private final LightState state;
    private final String type;
    private String name;
    private final String modelId;
    private final String manufacturerName;
    private final String productName;
    private final String uniqueId;
    private final String swversion;
    private final String lightId;

    public Light(JSONObject lightObject,Bridge parent, String id){
        state = createLightState(lightObject.getJSONObject("state"));

        type = lightObject.getString("type");
        name = lightObject.getString("name");
        modelId = lightObject.getString("modelid");
        manufacturerName = lightObject.getString("manufacturername");
        productName = lightObject.getString("productname");
        uniqueId = lightObject.getString("uniqueid");
        swversion = lightObject.getString("swversion");
        parentBridge = parent;
        lightId = id;
    }

    private LightState createLightState(JSONObject lsObject){
        return new LightState(lsObject,this);
    }

    public LightState getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getModelId() {
        return modelId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getProductName() {
        return productName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getSwversion() {
        return swversion;
    }

    @Override
    public String toString() {
        return name;
    }

    public Bridge getParentBridge() {
        return parentBridge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLightId() {
        return lightId;
    }
}
