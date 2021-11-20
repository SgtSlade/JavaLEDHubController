package org.sgtslade.application.widgets;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import org.sgtslade.App;
import org.sgtslade.application.AppData;
import org.sgtslade.application.layouts.GroupsTab;
import org.sgtslade.application.scenes.GroupDisplayScene;
import org.sgtslade.application.stages.GroupRenamingStage;
import org.sgtslade.hue.Group;

import java.io.FileInputStream;

public class GroupTile extends StackPane {
    private ImageView iconCanvas = new ImageView();
    private Image iconImg;
    private Label groupLabel = new Label();
    private Group parentGroup;
    private HBox hBox = new HBox();

    public GroupTile(Group parent){
        parentGroup = parent;
        setImage();
        setLayout();
    }

    private void setImage(){
        if(!AppData.resourceExists(String.format("img/grp/%s",parentGroup.getGroupName().toLowerCase()+".png"))){
            try {
                iconImg = new Image(new FileInputStream("./data/img/grp/testimg.jpg"));
            }catch (Exception e){
                e.printStackTrace();
            }
            iconCanvas.setImage(iconImg);
            Circle clip = new Circle();
            clip.setRadius(25);
            clip.centerXProperty().bind(iconCanvas.fitWidthProperty().divide(2));
            clip.centerYProperty().bind(iconCanvas.fitHeightProperty().divide(2));
            iconCanvas.setClip(clip);
            iconCanvas.setFitWidth(50);
            iconCanvas.setFitHeight(50);
        }
    }

    private void setLayout(){
        setPadding(new Insets(5));
        hBox.setSpacing(5);

        groupLabel.setText(parentGroup.getGroupName());
        groupLabel.maxHeightProperty().bind(heightProperty());
        groupLabel.setWrapText(true);
        groupLabel.setTextAlignment(TextAlignment.CENTER);
        groupLabel.setFont(AppData.getFont("ARIAL.ttf",35));
        groupLabel.setTextFill(Color.valueOf("#4d3e40"));
        getChildren().add(hBox);

        setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY))
            {
                ContextMenu renameMenu = new ContextMenu();
                MenuItem renameItem = new MenuItem("Rename");
                MenuItem deleteItem = new MenuItem("Delete");
                renameItem.setOnAction(actionEvent -> {
                    new GroupRenamingStage(parentGroup).show();
                });
                deleteItem.setOnAction(actionEvent -> {
                    App.activeBridge.deleteGroup(parentGroup);
                    App.hueControlScene.refreshData();
                });
                renameMenu.getItems().addAll(renameItem,deleteItem);
                renameMenu.show(getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                App.mainStage.setScene(new GroupDisplayScene(parentGroup));
            }
        });

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        dropShadow.setRadius(3);

        prefWidthProperty().bind(((GroupsTab)App.hueControlScene.getTabPane().getGroupsTab().getContent()).getScrollPane().widthProperty().subtract(25));

        setEffect(dropShadow);

        LinearGradient lg = new LinearGradient(0.5,0,0.5,1,true, CycleMethod.NO_CYCLE,new Stop(0,Color.rgb(249,249,249)),new Stop(1,Color.rgb(220,220,220)));
        Background bg = new Background(new BackgroundFill(lg,new CornerRadii(5), null));
        setBackground(bg);

        hBox.getChildren().addAll(iconCanvas,groupLabel);
    }

    public Image getIconImg() {
        return iconImg;
    }

    public void setIconImg(Image iconImg) {
        this.iconImg = iconImg;
    }

    public Label getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(Label groupLabel) {
        this.groupLabel = groupLabel;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }
}
