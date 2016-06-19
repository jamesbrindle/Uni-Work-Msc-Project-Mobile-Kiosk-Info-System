package sik.client.user.panes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sik.client.user.ActionClips;

/**
 * Panel which is displayed inside the manual pin entry panel. It contains a
 * number pad which inserts the selected number into a parameter supplied text
 * field. This panel was originally designed to work as a stand alone class
 * which could be re-used in different areas of the main GUI. however this
 * didn't end up being the case and it only gets used once, however the
 * implementation is there if it's ever needed again in the future.
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class NumPadPane extends JPanel implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access it
	 */
	protected JPanel parent;

	// panel properties
	private GridBagConstraints numPadLayout;

	public JButton numEnter, numCancel, numClear, numEmpty1, numEmpty2,
			numEmpty3, num0, num1, num2, num3, num4, num5, num6, num7, num8,
			num9;

	public JTextField aTextField;
	public JPasswordField aPasswordField;
	public String textFieldType = "";
	private Dimension buttonDimension1 = new Dimension(60, 60);
	private Dimension buttonDimension2 = new Dimension(125, 60);

	/**
	 * Class constructor - Constructs class set to use a password text field
	 * 
	 * @param parent
	 *            The JPanel which called this class
	 * @param font
	 *            The font in which to set the num pad buttons to use
	 * @param passwordField
	 *            The supplied password text field in which to return an input
	 *            to
	 */
	public NumPadPane(JPanel parent, Font font, JPasswordField passwordField) {
		this.parent = parent;
		this.aPasswordField = passwordField;
		textFieldType = "JPasswordField";
		buildNumPad(font);
	}

	/**
	 * Class constructor - Constructs class set to use a standard text field
	 * 
	 * @param parent
	 *            The JPanel which called this class
	 * @param font
	 *            The font in which to set the num pad buttons to use
	 * @param textField
	 *            The supplied text field in which to return an input to
	 */
	public NumPadPane(JPanel parent, Font font, JTextField textField) {
		this.parent = parent;
		this.aTextField = textField;
		textFieldType = "JTextField";
		buildNumPad(font);
	}

	@SuppressWarnings("deprecation")
	/**
	 * Action event handler
	 * @param e The action event
	 */
	public void actionPerformed(ActionEvent e) {
		// Enter, clear and cancel action performed methods are unique to the
		// parent.
		// The action performed methods are written in the parent class.
		JButton tempButton = new JButton("");
		if ((e.getSource() == num1) || (e.getSource() == num2)
				|| (e.getSource() == num3) || (e.getSource() == num4)
				|| (e.getSource() == num5) || (e.getSource() == num6)
				|| (e.getSource() == num7) || (e.getSource() == num8)
				|| (e.getSource() == num9) || (e.getSource() == num0)) {

			ActionClips.playNumButton();
			tempButton = (JButton) e.getSource();

			if (textFieldType.equalsIgnoreCase("JTextField")) {
				aTextField.setText(aTextField.getText() + tempButton.getText());
				aTextField.requestFocusInWindow();
			}
			if (textFieldType.equalsIgnoreCase("JPasswordField")) {
				aPasswordField.setText(aPasswordField.getText()
						+ tempButton.getText());
				aPasswordField.requestFocusInWindow();
			}
		}
	}

	/**
	 * Prepare the numPad panel elements
	 */
	public void buildNumPad(Font font) {
		setLayout(new GridBagLayout());
		setOpaque(false);
		numPadLayout = new GridBagConstraints();

		numPadLayout.ipady = 8;

		numPadLayout.insets = new Insets(4, 0, 0, 0);

		num1 = new JButton("1");
		num1.setFont(font);
		num1.addActionListener(this);
		num1.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 0;
		numPadLayout.gridy = 0;
		add(num1, numPadLayout);

		num2 = new JButton("2");
		num2.setFont(font);
		num2.addActionListener(this);
		num2.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 1;
		numPadLayout.gridy = 0;
		add(num2, numPadLayout);

		num3 = new JButton("3");
		num3.setFont(font);
		num3.addActionListener(this);
		num3.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 2;
		numPadLayout.gridy = 0;
		add(num3, numPadLayout);

		numPadLayout.insets = new Insets(4, 10, 0, 0);

		// action listener for this button on parent
		numClear = new JButton("Clear");
		numClear.setFont(font);
		numClear.setPreferredSize(buttonDimension2);
		numPadLayout.fill = GridBagConstraints.HORIZONTAL;
		numPadLayout.gridx = 3;
		numPadLayout.gridy = 0;
		add(numClear, numPadLayout);

		numPadLayout.insets = new Insets(0, 0, 0, 0);

		num4 = new JButton("4");
		num4.setFont(font);
		num4.addActionListener(this);
		num4.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 0;
		numPadLayout.gridy = 1;
		add(num4, numPadLayout);

		num5 = new JButton("5");
		num5.setFont(font);
		num5.addActionListener(this);
		num5.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 1;
		numPadLayout.gridy = 1;
		add(num5, numPadLayout);

		num6 = new JButton("6");
		num6.setFont(font);
		num6.addActionListener(this);
		num6.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 2;
		numPadLayout.gridy = 1;
		add(num6, numPadLayout);

		numPadLayout.insets = new Insets(0, 10, 0, 0);

		// action listener for this button on parent
		numCancel = new JButton("Cancel");
		numCancel.setFont(font);
		numCancel.setPreferredSize(buttonDimension2);
		numPadLayout.fill = GridBagConstraints.HORIZONTAL;
		numPadLayout.gridx = 3;
		numPadLayout.gridy = 1;
		add(numCancel, numPadLayout);

		numPadLayout.insets = new Insets(0, 0, 0, 0);

		num7 = new JButton("7");
		num7.setFont(font);
		num7.addActionListener(this);
		num7.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 0;
		numPadLayout.gridy = 2;
		add(num7, numPadLayout);

		num8 = new JButton("8");
		num8.setFont(font);
		num8.addActionListener(this);
		num8.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 1;
		numPadLayout.gridy = 2;
		add(num8, numPadLayout);

		num9 = new JButton("9");
		num9.setFont(font);
		num9.addActionListener(this);
		num9.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 2;
		numPadLayout.gridy = 2;
		add(num9, numPadLayout);

		numPadLayout.insets = new Insets(0, 10, 0, 0);

		// action listener for this button on parent
		numEnter = new JButton("Enter");
		numEnter.setFont(font);
		numEnter.setPreferredSize(buttonDimension2);
		numPadLayout.gridx = 3;
		numPadLayout.gridy = 2;
		add(numEnter, numPadLayout);

		numPadLayout.insets = new Insets(0, 0, 0, 0);

		numEmpty1 = new JButton("");
		numEmpty1.setFont(font);
		numEmpty1.setPreferredSize(buttonDimension2);
		numEmpty1.setVisible(false);
		numPadLayout.fill = GridBagConstraints.BOTH;
		numPadLayout.gridx = 0;
		numPadLayout.gridy = 3;
		add(numEmpty1, numPadLayout);

		num0 = new JButton("0");
		num0.setFont(font);
		num0.addActionListener(this);
		num0.setPreferredSize(buttonDimension1);
		numPadLayout.gridx = 1;
		numPadLayout.gridy = 3;
		add(num0, numPadLayout);

		numEmpty2 = new JButton("");
		numEmpty2.setFont(font);
		numEmpty2.setPreferredSize(buttonDimension2);
		numEmpty2.setVisible(false);
		numPadLayout.fill = GridBagConstraints.BOTH;
		numPadLayout.gridx = 2;
		numPadLayout.gridy = 3;
		add(numEmpty2, numPadLayout);

		numPadLayout.insets = new Insets(0, 10, 0, 0);

		numEmpty3 = new JButton("");
		numEmpty3.setFont(font);
		numEmpty3.setPreferredSize(buttonDimension2);
		numEmpty3.setVisible(false);
		numPadLayout.fill = GridBagConstraints.BOTH;
		numPadLayout.gridx = 3;
		numPadLayout.gridy = 3;
		add(numEmpty3, numPadLayout);

		numPadLayout.insets = new Insets(0, 0, 0, 0);
	}

	/**
	 * Sets an object to be the Focused object, in order to input values to it
	 * 
	 * @param textField
	 *            The text field to be the Focused object
	 */
	public void setFocusedObject(JTextField textField) {
		this.aTextField = textField;
		textFieldType = "JTextField";
	}

	/**
	 * Sets an object to be the Focused object, in order to input values to it
	 * 
	 * @param textField
	 *            The text field to be the Focused object
	 */
	public void setFocusedObject(JPasswordField textField) {
		this.aPasswordField = textField;
		textFieldType = "JPasswordField";
	}
}
