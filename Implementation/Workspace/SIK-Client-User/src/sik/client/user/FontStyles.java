package sik.client.user;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * Contains a list of font styles
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class FontStyles {

	/**
	 * Provides a generic system font in which to retrieve particular properties
	 * in other methods so each font property doesn't have to be set
	 */
	public static JLabel typicalJLabel = new JLabel("");

	public static final Font extraExtraLargeFont = new Font(typicalJLabel
			.getFont().getName(), typicalJLabel.getFont().getStyle(), 38);

	public static final Font extraLargeFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 26);

	public static final Font largerFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 24);

	public static final Font largeFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 20);

	public static final Font mediumFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 20);

	public static final Font smallFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 18);

	public static final Font smallerFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 14);

	public static final Font smallerFont2 = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 14);

	public static final Font tinyFont = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 12);

	public static final Font buttonFont1 = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 22);

	public static final Font buttonFont2 = new Font(typicalJLabel.getFont()
			.getName(), typicalJLabel.getFont().getStyle(), 16);
}
