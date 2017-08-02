package view;

import utilities.Settings;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * This class permits to use an image as background of a panel.
 */
public class ImagePanel extends JPanel {
    protected Image imagePanel;

    public ImagePanel() {
        this.imagePanel = LoadImage.load(Settings.Images$.MODULE$.PANELS_FOLDER() + "menu.png");
    }

    @Override
    protected void paintComponent(final Graphics g) {
        setOpaque(false);
        g.drawImage(this.imagePanel, 0, 0, this.getWidth(), this.getHeight(), this);
        super.paintComponent(g);
    }


}
