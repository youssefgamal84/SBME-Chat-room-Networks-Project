package com.kfatsa.networks;

import javafx.scene.image.Image;

import java.io.Serializable;

public class ImageTransfer implements Serializable {

    private Image image;

    public ImageTransfer(Image image){
        super();
        this.image = image;
    }

    public Image getImage(){
        return this.image;
    }
}
