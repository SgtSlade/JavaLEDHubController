package org.sgtslade.application;

public class ConfigKeyNotFound extends RuntimeException{
    public ConfigKeyNotFound(){
        super("Config key has not been found");
    }
}
