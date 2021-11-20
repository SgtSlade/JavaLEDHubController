package org.sgtslade.application.layouts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import org.sgtslade.App;
import org.sgtslade.application.AppData;
import org.sgtslade.hue.lights.Light;
import org.sgtslade.hue.lights.LightState;

public class LightsTab extends TabLayout {

    private final GridPane mainPane = new GridPane();
    private final ListView<Light> lightList = new ListView<>();
    private final VBox lightData = new VBox();
    int lastSelectedIndex = -1;

    public LightsTab(){
        getChildren().add(mainPane);

        addAllLightsToList();

        mainPane.add(lightList,0,0);
        mainPane.add(lightData,1,0);

        lightList.setPrefWidth(300);
        lightData.setPrefWidth(300);

        lightList.setOnMouseClicked(mouseEvent -> {
            int selectedIndex = lightList.getSelectionModel().getSelectedIndex();
            if(selectedIndex!=lastSelectedIndex){
                lastSelectedIndex = selectedIndex;
                displayLight(getSelectedLight());
            }
        });
    }


    private void addAllLightsToList(){
        lightList.getItems().clear();
        lightList.getItems().addAll(App.activeBridge.getLights().values());
    }

    private void displayLight(Light light){
        lightData.getChildren().clear();
        lightData.setSpacing(5);
        lightData.setPadding(new Insets(0,0,0,5));

        Label lightNameLabel = new Label(light.getName());
        lightNameLabel.setPrefWidth(300);
        lightNameLabel.setAlignment(Pos.CENTER);

        Label onLabel = new Label(light.getState().isOn() ? "Status - On" : "Status - Off");
        onLabel.setOnMouseClicked(mouseEvent -> {
            light.getState().setOn(!light.getState().isOn());
            onLabel.setText(light.getState().isOn() ? "Status - On" : "Status - Off");
        });

        Label hueLabel = new Label("Hue - " + light.getState().getHue());
        Label brightnessLabel = new Label("Brightness - " + light.getState().getBrightness());
        Label saturationLabel = new Label("Saturation - " + light.getState().getSaturation());
        Label effectLabel = new Label("Current effect - " + light.getState().getEffect());

        ColorPicker picker = new ColorPicker();
        picker.setValue(LightState.getColor(light.getState()));
        picker.setOnAction(actionEvent -> {
            light.getState().setColor(picker.getValue());
            hueLabel.setText(Integer.toString(light.getState().getBrightness()));
            hueLabel.setText(Integer.toString(light.getState().getSaturation()));
            hueLabel.setText(Integer.toString(light.getState().getHue()));
        });


        lightData.getChildren().addAll(lightNameLabel,onLabel,hueLabel,brightnessLabel,saturationLabel,effectLabel,picker);

        for(Node n : lightData.getChildren()){
            ((Region) n).setPadding(new Insets(0, 0, 0, 5));
            if(n.getClass() == Label.class) {
                ((Label) n).setFont(AppData.getFont(App.defaultFont,15));
            }
        }
    }

    public Light getSelectedLight(){
        return lightList.getSelectionModel().getSelectedItem();
    }

    @Override
    public void refresh() {
        addAllLightsToList();
    }
}
