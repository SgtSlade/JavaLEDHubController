package org.sgtslade.application.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.sgtslade.application.Refreshable;
import org.sgtslade.application.layouts.MainTabPane;

public class HueControlPanel extends Scene {

    private StackPane root;
    private VBox menuTabsBox = new VBox();
    private MainTabPane tabPane = new MainTabPane();

    public HueControlPanel() {
        super(new StackPane());
        root = (StackPane) getRoot();

        menuTabsBox.getChildren().add(tabPane);


        root.getChildren().add(menuTabsBox);
    }

    public void refreshData(){
        for(Tab t:tabPane.getTabs()){
            ((Refreshable)t.getContent()).refresh();
        }
    }

    public StackPane getMyRoot() {
        return root;
    }

    public VBox getMenuTabsBox() {
        return menuTabsBox;
    }

    public MainTabPane getTabPane() {
        return tabPane;
    }
}
