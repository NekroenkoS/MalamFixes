package com.sergeysolutions.malamassistant.FrontEnd;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImagePanel extends JPanel {
    private final Image img;

    public ImagePanel(URL imgPath) {
        this(new ImageIcon(imgPath).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;
       setSize(400,600);
        setLayout(new BorderLayout());
    }



    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
