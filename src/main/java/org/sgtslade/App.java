package org.sgtslade;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sgtslade.application.AppConfig;
import org.sgtslade.application.scenes.BridgePairingScene;
import org.sgtslade.application.scenes.HueControlPanel;
import org.sgtslade.hue.API;
import org.sgtslade.hue.Bridge;


/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene bridgePairingScene;
    public static HueControlPanel hueControlScene;
    public static Stage mainStage;
    public static Bridge activeBridge;
    public static final String defaultFont = "arial.ttf";

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        setStartingConfig();
        mainStage = stage;

        stage.setTitle("LED Controller hub");
        if(API.checkAuth(activeBridge.getBridgeAdress(),AppConfig.getConfigVal("username"))){
            activeBridge.setDevices();
            hueControlScene = new HueControlPanel();
            hueControlScene.refreshData();
            stage.setWidth(500);
            stage.setHeight(300);
            stage.setScene(hueControlScene);
        }else {
            bridgePairingScene = new BridgePairingScene();
            stage.setScene(bridgePairingScene);
        }
        stage.show();
    }

    private void setStartingConfig(){
        activeBridge = Bridge.getClosestBridge();
        if(!AppConfig.keyExists("bridgeIP")) {
            AppConfig.setConfigVal("bridgeIP", activeBridge.getBridgeAdress());
        }
        if(!AppConfig.keyExists("username")){
            AppConfig.setConfigVal("username","");
        }
    }

    public static void main(String[] args) {
        launch();
    }

}