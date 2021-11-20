package org.sgtslade.application.scenes;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import org.sgtslade.App;
import org.sgtslade.application.CSSHelpers;
import org.sgtslade.application.widgets.SceneTile;
import org.sgtslade.hue.Group;
import org.sgtslade.hue.lights.Light;

import java.util.stream.Collectors;

public class GroupDisplayScene extends Scene {
    private Group parentGroup;
    private final ScrollPane mainPane = new ScrollPane();
    private final GridPane gridPane = new GridPane();
    private final Button exitButton = new Button("Back");
    private final Label groupLabel = new Label("");
    private final ListView<Light> groupMembers = new ListView<>();
    private final ListView<Light> availableMembers = new ListView<>();
    private final Button moveLeftButton = new Button("<");
    private final Button moveRightButton = new Button(">");
    private final Label groupMembersLabel = new Label("Group lights");
    private final Label availableMembersLabel = new Label("Available lights");
    private final Label scenesLabel = new Label("Scenes");
    private final TilePane scenesTilePane = new TilePane();

    public GroupDisplayScene(Group parentGroup) {
        super(new StackPane());
        this.parentGroup = parentGroup;
        createLayout();
        setUpListeners();
        refreshData();
    }

    private void createLayout(){
        StackPane root = (StackPane)getRoot();

        mainPane.setStyle(CSSHelpers.disableFocusHighlight);


        gridPane.setPadding(new Insets(5,0,0,10));
        gridPane.maxWidthProperty().bind(mainPane.widthProperty().subtract(15));
        gridPane.minWidthProperty().bind(mainPane.widthProperty().subtract(15));

        GridPane.setMargin(moveLeftButton,new Insets(5));
        GridPane.setValignment(moveLeftButton, VPos.BOTTOM);
        GridPane.setVgrow(moveLeftButton, Priority.ALWAYS);
        moveLeftButton.setMinSize(25,25);
        moveLeftButton.setOnAction(actionEvent -> {
            if(availableMembers.getSelectionModel().getSelectedItem() != null){
                parentGroup.addLight(availableMembers.getSelectionModel().getSelectedItem());
                refreshData();
            }
        });

        moveRightButton.setMinSize(25,25);
        GridPane.setMargin(moveRightButton,new Insets(5));
        GridPane.setValignment(moveRightButton, VPos.TOP);
        GridPane.setVgrow(moveRightButton, Priority.ALWAYS);
        moveRightButton.setOnAction(actionEvent -> {
            if(groupMembers.getSelectionModel().getSelectedItem() != null){
                parentGroup.removeLight(groupMembers.getSelectionModel().getSelectedItem());
                refreshData();
            }
        });

        groupLabel.setTextAlignment(TextAlignment.CENTER);
        groupLabel.setText(parentGroup.getGroupName());
        groupLabel.setFont(org.sgtslade.application.AppData.getFont("arial.ttf",15));
        GridPane.setHalignment(groupLabel, HPos.CENTER);

        GridPane.setHalignment(groupMembersLabel,HPos.CENTER);
        groupMembers.setMaxHeight(190);

        GridPane.setHalignment(availableMembersLabel,HPos.CENTER);
        availableMembers.setMaxHeight(190);

        GridPane.setHalignment(scenesLabel,HPos.CENTER);
        scenesLabel.setFont(org.sgtslade.application.AppData.getFont("arial.ttf",15));

        App.activeBridge.getScenes().values()
                .stream()
                .filter(scene -> scene.getParentGroup().equals(parentGroup.getGroupId()))
                .forEach(scene -> scenesTilePane.getChildren().add(new SceneTile(scene,parentGroup)));
        scenesTilePane.setVgap(5);
        scenesTilePane.setHgap(5);
        scenesTilePane.setPrefTileWidth(300);
        scenesTilePane.setAlignment(Pos.CENTER);

        gridPane.setVgap(5);
        gridPane.add(exitButton,0,0,1,1);
        gridPane.add(groupLabel,0,0,3,1);
        gridPane.add(groupMembersLabel,0,1,1,1);
        gridPane.add(availableMembersLabel,2,1,1,1);
        gridPane.add(groupMembers,0,2,1,2);
        gridPane.add(availableMembers,2,2,1,2);
        gridPane.add(moveLeftButton,1,2,1,1);
        gridPane.add(moveRightButton,1,3,1,1);
        gridPane.add(scenesLabel,0,4,3,1);
        gridPane.add(scenesTilePane,0,5,3,1);

        mainPane.setPrefViewportWidth(500);
        mainPane.setContent(gridPane);
        root.getChildren().add(mainPane);
    }

    private void setUpListeners(){
        exitButton.setOnAction(actionEvent -> App.mainStage.setScene(App.hueControlScene));
    }

    private void refreshData(){
        groupMembers.getItems().clear();
        availableMembers.getItems().clear();
        groupMembers.getItems().addAll(parentGroup.getLights());
        availableMembers.getItems().addAll(App.activeBridge.getLights().values().stream().filter(light -> !parentGroup.getLights().contains(light)).collect(Collectors.toList()));
    }
}
