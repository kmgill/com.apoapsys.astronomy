package com.apoapsys.astronomy.utilities.hama;

import javax.swing.JFrame;

public class VisualizerFrame extends JFrame {
	
	public VisualizerFrame() {
		super("Test");
		
		int width = 600;
		int height = 300;
		
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.add(new HamaProblemVisualizer(width, height));
	}
	
	
	public static void main(String[] args) {
		VisualizerFrame frame = new VisualizerFrame();
		frame.setVisible(true);
	}
}
