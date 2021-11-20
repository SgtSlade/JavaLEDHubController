package org.sgtslade.application.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sgtslade.App;
import org.sgtslade.application.AppConfig;
import org.sgtslade.application.AppData;
import org.sgtslade.hue.API;
import org.sgtslade.hue.Bridge;
import org.sgtslade.hue.HubButtonNotPressedException;

public class BridgePairingScene extends Scene {

    private final StackPane root;
    private final Label bridgePairingInfoLabel = new Label("Click the button on your hub and then the one below to pair with your closest HUE bridge");
    private final Button bridgePairingButton = new Button("Pair");
    private final Label bridgePairingErrorLabel = new Label();
    private final VBox bridgePairingBox = new VBox();

    public BridgePairingScene() {
        super(new StackPane());
        root = (StackPane)getRoot();
        root.setPadding(new Insets(5));

        bridgePairingInfoLabel.setFont(AppData.getFont(App.defaultFont,15));

        bridgePairingButton.setFont(AppData.getFont(App.defaultFont,13));
        bridgePairingButton.setOnAction(actionEvent -> {
            try {
                pairWithClosestBridge();
                App.activeBridge.setDevices();
                App.hueControlScene = new HueControlPanel();
                App.hueControlScene.refreshData();

                bridgePairingErrorLabel.setVisible(false);

                Stage parentStage = (Stage)getWindow();

                parentStage.setWidth(500);
                parentStage.setHeight(300);

                parentStage.setScene(App.hueControlScene);
            }catch (HubButtonNotPressedException e){
                bridgePairingErrorLabel.setText("Error! Press the hub button first");
                bridgePairingErrorLabel.setVisible(true);
            }
        });

        bridgePairingErrorLabel.setVisible(false);
        bridgePairingErrorLabel.setFont(AppData.getFont(App.defaultFont,13));
        bridgePairingErrorLabel.setTextFill(Color.RED);

        bridgePairingBox.getChildren().addAll(bridgePairingInfoLabel,bridgePairingButton, bridgePairingErrorLabel);
        bridgePairingBox.setAlignment(Pos.CENTER);
        bridgePairingBox.setSpacing(5);

        root.getChildren().add(bridgePairingBox);
    }

    public void pairWithClosestBridge() throws HubButtonNotPressedException {
        App.activeBridge = Bridge.getClosestBridge();
        AppConfig.setConfigVal("bridgeIP", App.activeBridge.getBridgeAdress());
        API.registerDevice(App.activeBridge.getBridgeAdress());
    }
}
