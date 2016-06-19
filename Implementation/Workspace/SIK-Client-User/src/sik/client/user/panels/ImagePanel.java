package sik.client.user.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel which is created with a background image
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ImagePanel extends JPanel implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The image to be displayed as a background
	 */
	private Image img;

	/**
	 * Class constructor - Which will use a string as a parameter for the image
	 * name and location of an image to use as a background image of this panel
	 * 
	 * @param img
	 *            The image name and location string
	 */
	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	/**
	 * Class constructor - Which will use a an image as parameter to use as a
	 * background image of this panel
	 * 
	 * @param img
	 *            The image
	 */
	public ImagePanel(Image img) {
		this.img = img;

		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
	}

	/**
	 * Writes the actual image to a drawable graphics object
	 * 
	 * @param g
	 *            The supplied graphics in which to draw on
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}