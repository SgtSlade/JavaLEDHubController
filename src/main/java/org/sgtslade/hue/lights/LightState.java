package org.sgtslade.hue.lights;

import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sgtslade.hue.API;

import java.math.BigDecimal;

public class LightState {
    private Light parentLight;
    private boolean isOn;
    private int brightness;
    private int hue;
    private int saturation;
    private float[] xy = new float[2];
    private int colorTemperature;
    private String effect;
    private boolean reachable;

    public LightState(JSONObject lsObject){
        isOn = lsObject.optBoolean("on");
        brightness = lsObject.optInt("bri");
        hue = lsObject.optInt("hue");
        saturation = lsObject.optInt("sat");
        JSONArray xyObj = lsObject.optJSONArray("xy");
        if(xyObj != null) {
            xy[0] = (xyObj.optBigDecimal(0, BigDecimal.ZERO)).floatValue();
            xy[1] = (xyObj.optBigDecimal(1, BigDecimal.ZERO)).floatValue();
        }
        reachable = lsObject.optBoolean("reachable");
        colorTemperature = lsObject.optInt("ct");
        effect = lsObject.optString("effect");
    }

    public LightState(JSONObject lsObject, Light parentLight){
        isOn = lsObject.getBoolean("on");
        brightness = lsObject.getInt("bri");
        hue = lsObject.getInt("hue");
        saturation = lsObject.getInt("sat");
        JSONArray xyObj = lsObject.getJSONArray("xy");
        if(xyObj != null) {
            xy[0] = (xyObj.optBigDecimal(0, BigDecimal.ZERO)).floatValue();
            xy[1] = (xyObj.optBigDecimal(1, BigDecimal.ZERO)).floatValue();
        }
        reachable = lsObject.getBoolean("reachable");
        colorTemperature = lsObject.getInt("ct");
        effect = lsObject.getString("effect");
        this.parentLight = parentLight;
    }

    public void setStateVar(String var, Object val){
        JSONObject object = new JSONObject();
        object.put(var,val);
        API.putUserEndpoint(parentLight.getParentBridge().getBridgeAdress(),String.format("/lights/%s/state", parentLight.getLightId()),object);
    }

    public void setColor(Color toSet){
        int briVal = (int)(toSet.getBrightness()*254);
        int satVal = (int)(toSet.getSaturation()*254);
        int hueVal = (int)(toSet.getHue()*182);

        setBrightness(briVal);
        setSaturation(satVal);
        setHue(hueVal);
    }

    public static Color getColor(LightState ls){
        return Color.hsb((double)ls.getHue()/182,(double)ls.getSaturation()/254,(double)ls.getBrightness()/254);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
        setStateVar("on",on);
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
        setStateVar("bri",brightness);
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
        setStateVar("hue",hue);
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
        setStateVar("sat",saturation);
    }

    public float[] getXy() {
        return xy;
    }

    public void setXy(float[] xy) {
        this.xy = xy;
        setStateVar("xy",new JSONArray(xy));
    }

    public int getColorTemperature() {
        return colorTemperature;
    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
        setStateVar("ct",colorTemperature);
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
        setStateVar("effect",effect);
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public Light getParentLight() {
        return parentLight;
    }

    public void setParentLight(Light parentLight) {
        this.parentLight = parentLight;
    }
}
