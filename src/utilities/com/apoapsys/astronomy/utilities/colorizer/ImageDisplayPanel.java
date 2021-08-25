package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageDisplayPanel extends JPanel {

	private BufferedImage image = null;
	
	public ImageDisplayPanel() {
		super();
		this.setBackground(Color.BLACK);

	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		} else {
			
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
}
