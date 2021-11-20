package org.sgtslade.application.layouts;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import org.sgtslade.App;
import org.sgtslade.application.CSSHelpers;
import org.sgtslade.application.AppData;
import org.sgtslade.application.widgets.GroupTile;
import org.sgtslade.hue.Group;

public class GroupsTab extends TabLayout{

    private final VBox mainBox = new VBox();
    private final Button addGroupButton = new Button("Add group");
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox groupBox = new VBox();

    public GroupsTab(){
        scrollPane.prefHeightProperty().bind(mainBox.heightProperty());
        scrollPane.setContent(groupBox);
        scrollPane.setStyle(CSSHelpers.disableFocusHighlight);

        groupBox.setSpacing(10);
        groupBox.setPadding(new Insets(5));

        mainBox.getChildren().addAll(addGroupButton,scrollPane);
        mainBox.setSpacing(5);
        mainBox.setPadding(new Insets(5));

        addGroupButton.setFont(AppData.getFont("arial.ttf",15));
        addGroupButton.setOnAction(actionEvent -> {
            App.activeBridge.createEmptyGroup();
            refresh();
        });


        getChildren().add(mainBox);
    }

    @Override
    public void refresh() {
        groupBox.getChildren().clear();
        for(Group g : App.activeBridge.getGroups().values()){
            groupBox.getChildren().add(new GroupTile(g));
        }
    }

    public VBox getMainBox() {
        return mainBox;
    }

    public Button getAddGroupButton() {
        return addGroupButton;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public VBox getGroupBox() {
        return groupBox;
    }
}
