package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class ImageDropHandler extends TransferHandler implements Transferable {
	private static final DataFlavor flavors[] = { DataFlavor.imageFlavor };

	private BufferedImage image;
	private int imageIndex = 0;
	private ImageDroppedListener dropListener;

	public ImageDropHandler(int imageIndex, ImageDroppedListener dropListener) {
		this.imageIndex = imageIndex;
		this.dropListener = dropListener;
	}

	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] flavor) {
		if (!(comp instanceof ImageThumbnail)) {
			return false;
		}
		for (int i = 0, n = flavor.length; i < n; i++) {
			if (flavor[i].isFlavorJavaFileListType()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t) {
		try {
			List<File> droppedFiles = (List<File>) t
					.getTransferData(DataFlavor.javaFileListFlavor);
			if (droppedFiles.size() != 1) {
				return false;
			}
			System.err.println("Dropped "
					+ droppedFiles.get(0).getAbsolutePath());
			image = ImageUtil.convertToStandardFormat(ImageUtil.loadImage(droppedFiles.get(0)));

			ImageThumbnail imgComp = (ImageThumbnail) comp;
			imgComp.setImage(image);
			imgComp.setPath(droppedFiles.get(0).getName());

			imgComp.repaint();

			fireDropListener();

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.imageFlavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return image;
		} else {
			return null;
		}
	}

	private void fireDropListener() {
		if (dropListener != null) {
			dropListener.onImageDropped(image, imageIndex);
		}
	}
}