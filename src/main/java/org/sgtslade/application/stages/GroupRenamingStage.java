package org.sgtslade.application.stages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.sgtslade.App;
import org.sgtslade.hue.Group;

public class GroupRenamingStage extends Stage {

    private Group parentGroupTile;
    private VBox root = new VBox();
    private Scene mainScene = new Scene(root);
    private Label newNameLabel = new Label("Choose new name");
    private TextField groupNameInputField = new TextField();
    private Button confirmButton = new Button("Confirm");

    public GroupRenamingStage(Group groupTile){
        setResizable(false);
        parentGroupTile = groupTile;

        setScene(mainScene);

        root.getChildren().add(newNameLabel);
        root.getChildren().add(groupNameInputField);
        root.getChildren().add(confirmButton);
        confirmButton.setOnAction(actionEvent -> {
            parentGroupTile.setGroupName(groupNameInputField.getText());
            App.hueControlScene.refreshData();
            this.close();
        });
    }

}
