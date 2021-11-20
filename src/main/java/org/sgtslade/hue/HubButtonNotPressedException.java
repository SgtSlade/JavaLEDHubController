package org.sgtslade.hue;

public class HubButtonNotPressedException extends Exception{
    public HubButtonNotPressedException(){
        super("Hub button has not been pressed");
    }
}
