package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class FancyButton extends JButton {
	
	public FancyButton(Icon icon) {
		super(icon);
		
		final Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		final Border lined = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		
		this.setBorder(empty);
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				//setBorder(lined);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//setBorder(empty);
			}
			
		});
		
	}
	
	
}
