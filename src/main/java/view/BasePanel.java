package view;

import utilities.Settings;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BasePanel extends ImagePanel {

    private static final long serialVersionUID = 1L;
    private static final int INSETS = 10;
    protected JButton backButton;
    protected JPanel centralPanel;
    protected JPanel downPanel;
    protected GridBagConstraints k;

    public BasePanel() {

        this.setLayout(new BorderLayout());
        this.centralPanel = new JPanel(new GridBagLayout());
        this.downPanel = new JPanel(new BorderLayout());

        final ImageIcon icon = new ImageIcon(BasePanel.class.getResource(Settings.IMAGES_FOLDER()
                + "arrow-back.png"));
        this.backButton = new JButton(icon);
        this.backButton.setContentAreaFilled(false);
        this.backButton.setBorder(null);
        downPanel.add(this.backButton, BorderLayout.WEST);
        downPanel.setOpaque(false);
        this.add(downPanel, BorderLayout.SOUTH);

        this.k = new GridBagConstraints();
        this.k.gridy = 0;
        this.k.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        this.k.fill = GridBagConstraints.VERTICAL;
        this.centralPanel.setOpaque(false);
        this.add(centralPanel, BorderLayout.CENTER);
    }


}