package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.utilities.colorizer.ChannelStateControl.StateChangedListener;
import com.apoapsys.astronomy.utilities.colorizer.transforms.ReverseHorizontal;
import com.apoapsys.astronomy.utilities.colorizer.transforms.ReverseVertical;
import com.apoapsys.astronomy.utilities.colorizer.transforms.RotateClockwise;
import com.apoapsys.astronomy.utilities.colorizer.transforms.RotateCounterClockwise;
import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class ImageDocumentPanel extends JPanel {

	private ImageThumbnail redThumbnail;
	private ImageThumbnail greenThumbnail;
	private ImageThumbnail blueThumbnail;

	private boolean redChannelVisible = true;
	private boolean greenChannelVisible = true;
	private boolean blueChannelVisible = true;

	private ImageDisplayPanel imageDisplay;

	private BufferedImage composite;

	private List<Transform> transforms = Lists.newArrayList();

	private List<BufferedImage> frames = Lists.newArrayList();

	public ImageDocumentPanel() {

		redThumbnail = new ImageThumbnail(Color.RED);
		greenThumbnail = new ImageThumbnail(Color.GREEN);
		blueThumbnail = new ImageThumbnail(Color.BLUE);

		ImageDroppedListener dropListener = new ImageDroppedListener() {
			@Override
			public void onImageDropped(BufferedImage image, int index) {
				frames.set(index, image);
				buildComposite();
				repaint();
			}
		};

		redThumbnail.setTransferHandler(new ImageDropHandler(0, dropListener));
		greenThumbnail.setTransferHandler(new ImageDropHandler(1, dropListener));
		blueThumbnail.setTransferHandler(new ImageDropHandler(2, dropListener));

		redThumbnail.getChannelStateControl().addStateChangedListener(new StateChangedListener() {
			@Override
			public void onVisibilityChanged(boolean visible) {
				redChannelVisible = visible;
				buildComposite();
			}
		});

		greenThumbnail.getChannelStateControl().addStateChangedListener(new StateChangedListener() {
			@Override
			public void onVisibilityChanged(boolean visible) {
				greenChannelVisible = visible;
				buildComposite();
			}
		});

		blueThumbnail.getChannelStateControl().addStateChangedListener(new StateChangedListener() {
			@Override
			public void onVisibilityChanged(boolean visible) {
				blueChannelVisible = visible;
				buildComposite();
			}
		});

		MouseAdapter activeClickListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				redThumbnail.getChannelStateControl().setActive(redThumbnail.getChannelStateControl() == e.getComponent());
				greenThumbnail.getChannelStateControl().setActive(greenThumbnail.getChannelStateControl() == e.getComponent());
				blueThumbnail.getChannelStateControl().setActive(blueThumbnail.getChannelStateControl() == e.getComponent());
			}
		};

		redThumbnail.getChannelStateControl().addMouseListener(activeClickListener);
		greenThumbnail.getChannelStateControl().addMouseListener(activeClickListener);
		blueThumbnail.getChannelStateControl().addMouseListener(activeClickListener);

		JPanel thumbnailsPanel = new JPanel();
		thumbnailsPanel.setLayout(new GridLayout(3, 1));
		thumbnailsPanel.add(redThumbnail);
		thumbnailsPanel.add(greenThumbnail);
		thumbnailsPanel.add(blueThumbnail);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				System.out.println("Got key event!");
				return false;
			}
		});

		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				System.err.println("Typed: " + e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.err.println("Pressed: " + e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.err.println("Released: " + e.getKeyCode());
			}

		});

		imageDisplay = new ImageDisplayPanel();

		JToolBar toolBar = new JToolBar();

		JButton btnCounterClockwise = new JButton("\u21B6");
		btnCounterClockwise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transforms.add(new RotateCounterClockwise());
				buildComposite();
			}
		});
		toolBar.add(btnCounterClockwise);

		JButton btnClockwise = new JButton("\u21B7");
		btnClockwise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transforms.add(new RotateClockwise());
				buildComposite();
			}
		});
		toolBar.add(btnClockwise);

		JButton btnFlipVertical = new JButton("\u21C5");
		btnFlipVertical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transforms.add(new ReverseVertical());
				buildComposite();
			}
		});
		toolBar.add(btnFlipVertical);

		JButton btnFlipHorizontal = new JButton("\u21C6");
		btnFlipHorizontal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transforms.add(new ReverseHorizontal());
				buildComposite();
			}
		});
		toolBar.add(btnFlipHorizontal);

		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.NORTH);
		this.add(thumbnailsPanel, BorderLayout.EAST);
		this.add(imageDisplay, BorderLayout.CENTER);

		composite = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
		imageDisplay.setImage(composite);

		loadTestData();
	}

	public void save() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showSaveDialog(getRootPane());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			doSave(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void doSave(String path) {
		try {
			System.err.println("Saving file to: " + path);
			ImageWriter.saveImage(composite, path);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void loadTestData() {
		try {
			BufferedImage bl1 = ImageUtil.convertToStandardFormat(ImageUtil
					.loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229230_1.IMG.Saturn.CL1.BL1.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage grn = ImageUtil.convertToStandardFormat(ImageUtil
					.loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229263_1.IMG.Saturn.CL1.GRN.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage red = ImageUtil.convertToStandardFormat(ImageUtil
					.loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229296_1.IMG.Saturn.CL1.RED.s1.1024x1024x8.2011-02-25.PNG"));

			redThumbnail.setImage(red);
			redThumbnail.setPath("W1677229296_1.IMG.Saturn.CL1.RED.s1.1024x1024x8.2011-02-25.PNG");

			greenThumbnail.setImage(grn);
			greenThumbnail.setPath("W1677229263_1.IMG.Saturn.CL1.GRN.s1.1024x1024x8.2011-02-25.PNG");

			blueThumbnail.setImage(bl1);
			blueThumbnail.setPath("W1677229230_1.IMG.Saturn.CL1.BL1.s1.1024x1024x8.2011-02-25.PNG");

			frames.add(red);
			frames.add(grn);
			frames.add(bl1);

			buildComposite();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void buildComposite() {

		ImageUtil.buildComposite(redChannelVisible ? frames.get(0) : null, greenChannelVisible ? frames.get(1) : null, blueChannelVisible ? frames.get(2) : null, composite, transforms);

		repaint();
	}

}
