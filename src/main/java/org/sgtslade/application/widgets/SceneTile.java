package org.sgtslade.application.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import org.sgtslade.App;
import org.sgtslade.application.AppData;
import org.sgtslade.hue.Group;
import org.sgtslade.hue.Scene;

import java.io.FileInputStream;

public class SceneTile extends StackPane {

    private final Group parentDisplayedGroup;
    private final HBox mainPane = new HBox();
    private final Scene parentScene;
    private Image iconImg;
    private final Label sceneNameLabel;
    private final ImageView iconCanvas = new ImageView();

    public SceneTile(org.sgtslade.hue.Scene parentScene, Group group){
        this.parentScene = parentScene;
        this.parentDisplayedGroup = group;
        this.sceneNameLabel = new Label(parentScene.getSceneName());
        setImage();

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,new CornerRadii(5),null)));
        sceneNameLabel.setFont(AppData.getFont(App.defaultFont,30));
        sceneNameLabel.setWrapText(true);
        sceneNameLabel.setTextAlignment(TextAlignment.CENTER);

        mainPane.setPadding(new Insets(5));
        mainPane.setSpacing(10);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        dropShadow.setRadius(3);
        LinearGradient lg = new LinearGradient(0.5,0,0.5,1,true, CycleMethod.NO_CYCLE,new Stop(0,Color.rgb(249,249,249)),new Stop(1,Color.rgb(220,220,220)));
        Background bg = new Background(new BackgroundFill(lg,new CornerRadii(5), null));
        mainPane.setBackground(bg);
        mainPane.setEffect(dropShadow);
        mainPane.getChildren().add(iconCanvas);
        mainPane.getChildren().add(sceneNameLabel);
        mainPane.setAlignment(Pos.CENTER_LEFT);

        getChildren().add(mainPane);

        setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton()== MouseButton.PRIMARY){
                parentDisplayedGroup.setScene(parentScene);
            }
        });
    }

    private void setImage(){
        if(!AppData.resourceExists(String.format("img/scn/%s", parentScene.getSceneName().toLowerCase()+".png"))){
            try {
                iconImg = new Image(new FileInputStream("./data/img/scn/testimg.jpg"));
            }catch (Exception e){
                e.printStackTrace();
            }
            iconCanvas.setImage(iconImg);
            iconCanvas.setFitWidth(50);
            iconCanvas.setFitHeight(50);
        }
    }
}
