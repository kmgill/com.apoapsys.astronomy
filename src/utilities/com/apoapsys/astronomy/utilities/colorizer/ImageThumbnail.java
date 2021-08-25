package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageThumbnail extends JPanel {
	
	private ImageDisplayPanel imageDisplay;
	private ChannelStateControl stateControl;
	private JPanel colorSwatch;
	private String path;
	
	public ImageThumbnail(Color c) {
		this.setSize(200, 200);
		this.setPreferredSize(new Dimension(200, 200));

		this.setLayout(new BorderLayout());
		
		colorSwatch = new JPanel();
		colorSwatch.setBackground(c);
		this.add(colorSwatch, BorderLayout.EAST);
		
		imageDisplay = new ImageDisplayPanel();
		this.add(imageDisplay, BorderLayout.CENTER);
		
		stateControl = new ChannelStateControl(c);
		this.add(stateControl, BorderLayout.WEST);
	}
	
	public ChannelStateControl getChannelStateControl() {
		return stateControl;
	}
	
	public void setChannelColor(Color c) {
		colorSwatch.setBackground(c);
	}
	
	public BufferedImage getImage() {
		return imageDisplay.getImage();
	}

	public void setImage(BufferedImage image) {
		imageDisplay.setImage(image);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		this.setToolTipText(path);
	}
	
}
