package org.sgtslade.application.layouts;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    Tab lightsTab = new Tab("Lights",new LightsTab());
    Tab groupsTab = new Tab("Groups", new GroupsTab());

    public MainTabPane(){
        lightsTab.setClosable(false);
        groupsTab.setClosable(false);
        getTabs().addAll(lightsTab, groupsTab);
    }

    public Tab getLightsTab() {
        return lightsTab;
    }

    public Tab getGroupsTab() {
        return groupsTab;
    }
}
