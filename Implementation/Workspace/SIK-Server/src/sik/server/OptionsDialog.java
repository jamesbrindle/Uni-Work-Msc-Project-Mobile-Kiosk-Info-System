package sik.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A Dialog for displaying either message or a prompt to the user
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class OptionsDialog extends JDialog implements ActionListener,
		Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class that provides text file IO services, in which the text file
	 * contains attribute values and setting to be used by this and other
	 * classes
	 */
	private ServerIOServices clientOptions;

	private JButton yesButton, noButton;
	private JTextField textField;
	private int choice;
	private JPanel messagePane, buttonPane, textFieldPane;

	/**
	 * Class constructor
	 * 
	 * @param parent
	 *            The calling class which called this class
	 * @param title
	 *            The title this GUI is to be set to
	 * @param message
	 *            The message to be displayed in this dialogue
	 */
	public OptionsDialog(JFrame parent, String title, String message,
			String type) {

		// Inherit implementations of JDialog
		super(parent, title, true);

		// So can listen to particular key events
		setFocusable(true);
		requestFocus();

		// create new key listener
		addKeyListener(new KeyAdapter() {
			/**
			 * Creates a new key pressed event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					yesButton.doClick();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					noButton.doClick();
				}
			}
		});

		setResizable(false);

		// method call to centre this dialog on the screen
		centreFrame();

		// create new instances of using objects
		messagePane = new JPanel();
		messagePane.add(new JLabel(message));
		getContentPane().add(messagePane, BorderLayout.NORTH);
		buttonPane = new JPanel();
		textFieldPane = new JPanel();

		// display appropriate elements based on the 'mode' or 'type' of dialog
		// for what this dialog is intended
		if (type.equalsIgnoreCase("option")) {
			loadOptionTypeDialog();
		}

		if (type.equalsIgnoreCase("changeRMILocation")) {
			loadChangeRMITypeDialog();
		}

		else if (type.equalsIgnoreCase("message")) {
			loadMessageTypeDialog();
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// prepare and display this dialog
		pack();
		setVisible(true);
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == yesButton) {
			this.setChoice(1);
			setVisible(false);
			this.dispose();
		}

		if (e.getSource() == noButton) {
			this.setChoice(0);
			setVisible(false);
			this.dispose();
		}
	}

	/**
	 * Load dialog elements for the 'message' type of dialog
	 */
	private void loadMessageTypeDialog() {
		yesButton = new JButton("OK");
		buttonPane.add(yesButton);
		yesButton.addActionListener(this);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	/**
	 * Load dialog elements for the 'option' type of dialog
	 */
	private void loadOptionTypeDialog() {
		yesButton = new JButton("Yes");
		noButton = new JButton("No");
		buttonPane.add(yesButton);
		buttonPane.add(noButton);
		yesButton.addActionListener(this);
		noButton.addActionListener(this);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	/**
	 * Load dialog elements for the 'change RMI location' type of dialog
	 */
	private void loadChangeRMITypeDialog() {
		clientOptions = new ServerIOServices();
		yesButton = new JButton("Commit");
		noButton = new JButton("Cancel");

		textField = new JTextField(20);
		textField.setText(clientOptions.getRMIAddress());

		textFieldPane.add(textField);

		buttonPane.add(yesButton);
		buttonPane.add(noButton);

		yesButton.addActionListener(this);
		noButton.addActionListener(this);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		getContentPane().add(textFieldPane, BorderLayout.CENTER);
	}

	/**
	 * Positions this class's frame to appear centred on the screen
	 */
	private void centreFrame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Returns the choice that the user selected from this dialog
	 * 
	 * @return choice The selected choice the user chose
	 */
	public int getChoice() {
		return choice;
	}

	/**
	 * Sets this dialogs choice, which is retrieved from the user input of
	 * selected the corresponding button
	 * 
	 * @param c
	 *            The selected choice to be set to
	 */
	public void setChoice(int c) {
		this.choice = c;
	}

	/**
	 * Returns the input RMI address
	 * 
	 * @return textField The RMI address String
	 */
	public String getNewRMILocation() {
		return textField.getText();
	}
}
