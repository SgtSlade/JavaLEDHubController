package org.sgtslade.hue;

import org.json.JSONObject;
import org.sgtslade.hue.lights.Light;
import org.sgtslade.hue.lights.LightState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Scene {
    private final String sceneName;
    private final Bridge parentBridge;
    private final String sceneId;
    private final String sceneType;
    private final String parentGroup;
    private final Map<Light,LightState> lightstates = new HashMap<>();

    public Scene(String sceneId, JSONObject sceneObject, Bridge parentBridge) {
        this.parentBridge = parentBridge;
        this.sceneName = sceneObject.getString("name");
        this.sceneId = sceneId;
        this.sceneType = sceneObject.getString("type");
        this.parentGroup = sceneObject.optString("group");

        JSONObject pulledLightstates = sceneObject.getJSONObject("lightstates");
        Iterator<String> lightIds = pulledLightstates.keys();

        lightIds.forEachRemaining(s -> {lightstates.put(parentBridge.getLights().get(s),new LightState(pulledLightstates.getJSONObject(s)));});

    }

    public String getSceneName() {
        return sceneName;
    }

    public Bridge getParentBridge() {
        return parentBridge;
    }

    public String getSceneId() {
        return sceneId;
    }

    public Map<Light, LightState> getLightstates() {
        return lightstates;
    }

    @Override
    public String toString() {
        return String.format("%s\nLightstate:\n%s",sceneName,lightstates);
    }

    public String getSceneType() {
        return sceneType;
    }

    public String getParentGroup() {
        return parentGroup;
    }
}
