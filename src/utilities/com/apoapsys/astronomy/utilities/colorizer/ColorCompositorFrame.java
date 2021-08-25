package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public class ColorCompositorFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	
	
	public ColorCompositorFrame() {
		super("RGB Compositor");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		JToolBar toolbar = new JToolBar();
		this.add(toolbar, BorderLayout.NORTH);
		
		ImageIcon imgNew = null;
		ImageIcon imgSave = null;
		try {
			imgNew = new ImageIcon(ImageUtil.loadImage("/com/apoapsys/astronomy/utilities/colorizer/icons/project_new.gif"));
			imgSave = new ImageIcon(ImageUtil.loadImage("/com/apoapsys/astronomy/utilities/colorizer/icons/project_save.gif"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		JButton btnNew = new FancyButton(imgNew);
		toolbar.add(btnNew);
		btnNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.add(new ImageDocumentPanel(), "Image " + tabbedPane.getComponentCount() + 1);
			}
			
		});
		
		JButton btnSave = new FancyButton(imgSave);
		btnSave.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		toolbar.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabbedPane.getSelectedComponent() instanceof ImageDocumentPanel) {
					ImageDocumentPanel imgDoc = (ImageDocumentPanel) tabbedPane.getSelectedComponent();
					imgDoc.save();
				}
			}
		});
		toolbar.add(btnSave);
		
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		
		
		
	}


}
