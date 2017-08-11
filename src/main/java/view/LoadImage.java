package view;

import java.awt.Image;

import javax.swing.ImageIcon;

public final class LoadImage {

    private LoadImage() { }

    public static Image load(final String path) {
        return new ImageIcon(LoadImage.class.getResource(path)).getImage();
    }

}