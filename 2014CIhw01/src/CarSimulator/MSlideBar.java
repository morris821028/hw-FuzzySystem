package CarSimulator;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

public class MSlideBar extends JPanel implements ItemListener,
		MouseMotionListener, MouseListener {
	JPanel slidePane;
	JPanel showPanel;
	Map<JToggleButton, Component> menuItemLaunch = new HashMap<JToggleButton, Component>();

	public MSlideBar() {
		slidePane = new JPanel();
		showPanel = new JPanel();

		slidePane.setBackground(new Color(0, 0, 0, 64));
		slidePane.setOpaque(false);
		BoxLayout boxLayout = new BoxLayout(slidePane, BoxLayout.Y_AXIS);
		slidePane.setLayout(boxLayout);

		showPanel.setBackground(new Color(0, 0, 0, 64));
		showPanel.setOpaque(false);
		showPanel.setLayout(new FlowLayout());

		this.setBackground(null);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(slidePane, BorderLayout.EAST);
		this.add(showPanel, BorderLayout.CENTER);
	}

	public void addSlideItem(JToggleButton btn, Component c) {
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setBackground(new Color(0, 0, 0, 64));
		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		btn.addItemListener(this);
		btn.addMouseMotionListener(this);
		btn.addMouseListener(this);
		menuItemLaunch.put(btn, c);
		if (c != null) {
			c.setMinimumSize(new Dimension(200, 1));
			// showPanel.add(c);
		}
		slidePane.add(btn);
	}

	public void itemStateChanged(ItemEvent ev) {
		if (ev.getStateChange() == ItemEvent.SELECTED) {
			if (menuItemLaunch.get(ev.getSource()) != null) {
				this.showPanel.add(menuItemLaunch.get(ev.getSource()));
			} else {
				CarControlPanel.getInstance().setVisible(false);
				Iterator iter = menuItemLaunch.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					if(key == ev.getSource())
						continue;
					JToggleButton b = ((JToggleButton) key);
					b.setSelected(false);
				}
			}
			this.repaint();
		} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
			if (menuItemLaunch.get(ev.getSource()) != null) 
				this.showPanel.remove(menuItemLaunch.get(ev.getSource()));
			else
				CarControlPanel.getInstance().setVisible(true);
			this.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Component c = arg0.getComponent();
		if (c instanceof AbstractButton) {
			BufferedImage img = new BufferedImage(48, 48,
					BufferedImage.TYPE_4BYTE_ABGR);
			((AbstractButton) c).getIcon();
			img.getGraphics().drawImage(
					((ImageIcon) ((AbstractButton) c).getIcon()).getImage(), 0,
					0, 48, 48, null);
			((AbstractButton) c).setIcon(new ImageIcon(img));
		}
		c.setSize(48, 48);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Component c = arg0.getComponent();
		if (c instanceof AbstractButton) {
			BufferedImage img = new BufferedImage(32, 32,
					BufferedImage.TYPE_4BYTE_ABGR);
			((AbstractButton) c).getIcon();
			img.getGraphics().drawImage(
					((ImageIcon) ((AbstractButton) c).getIcon()).getImage(), 0,
					0, 32, 32, null);
			((AbstractButton) c).setIcon(new ImageIcon(img));
		}
		c.setSize(32, 32);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
