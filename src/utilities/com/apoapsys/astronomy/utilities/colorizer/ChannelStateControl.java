package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class ChannelStateControl extends JPanel {
	
	private static Color ACTIVE_COLOR = Color.LIGHT_GRAY;
	private static Color INACTIVE_COLOR = Color.DARK_GRAY;
	
	private Color channelColor = Color.RED;
	
	private JCheckBox chkVisible;
	private boolean isActive = false;
	
	private List<StateChangedListener> stateChangedListeners = Lists.newArrayList();
	

	public ChannelStateControl(Color channelColor) {
		chkVisible = new JCheckBox();
		chkVisible.setSelected(true);
		chkVisible.setToolTipText("Toggle color channel visibility");
		chkVisible.setBackground(new Color(0, 0, 0, 0));
		chkVisible.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireVisibilityStateChangedListeners(chkVisible.isSelected());
			}
			
		});
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(chkVisible);
		
		setActive(false);
	}
	
	public void setActive(boolean active) {
		this.isActive = active;
		
		this.setBackground(active ? ACTIVE_COLOR : INACTIVE_COLOR);
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setChannelVisibility(boolean visible) {
		chkVisible.setSelected(visible);
	}
	
	public boolean getChannelVisibility() {
		return chkVisible.isSelected();
	}
	
	protected void fireVisibilityStateChangedListeners(boolean visible) {
		for (StateChangedListener l : stateChangedListeners) {
			l.onVisibilityChanged(visible);
		}
	}
	
	public void addStateChangedListener(StateChangedListener l) {
		stateChangedListeners.add(l);
	}
	
	public boolean removeStateChangedListener(StateChangedListener l) {
		return stateChangedListeners.remove(l);
	}
	
	
	public interface StateChangedListener {
		public void onVisibilityChanged(boolean visible);
	}
}
