package view;

import utilities.Settings;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    protected Image imagePanel;

    public ImagePanel() {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "menu.png");
    }

    @Override
    protected void paintComponent(final Graphics g) {
        setOpaque(false);
        g.drawImage(this.imagePanel, 0, 0, this.getWidth(), this.getHeight(), this);
        super.paintComponent(g);
    }


}
