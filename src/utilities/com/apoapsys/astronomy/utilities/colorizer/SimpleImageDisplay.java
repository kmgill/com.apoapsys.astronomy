package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SimpleImageDisplay extends JFrame {
	
	private Image image;
	
	public SimpleImageDisplay(Image image) {
		super("Image Display");
		this.setSize(image.getWidth(null), image.getHeight(null));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.image = image;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
	}
	
}
