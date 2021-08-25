package com.apoapsys.astronomy.utilities.perlin;

import javax.swing.JFrame;

public class PerlinViewer extends JFrame {
	
	PerlinPanel perlinPanel;
	
	public PerlinViewer(int width, int height) {
		super("Perlin");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		
		perlinPanel = new PerlinPanel(width, height);
		
		this.add(perlinPanel);
		perlinPanel.writeToFile("D:/tmp/perlin-" + System.currentTimeMillis() + ".jpg");
		
	}
	
	
	public static void main(String[] args) {
		PerlinViewer viewer =  new PerlinViewer(8192, 8192);
		viewer.setVisible(true);
	}
	
	
}
