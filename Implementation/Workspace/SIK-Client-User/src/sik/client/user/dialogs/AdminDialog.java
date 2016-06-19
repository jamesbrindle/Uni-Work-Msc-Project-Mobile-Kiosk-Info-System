package sik.client.user.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPasswordField;

import sik.client.user.ActionClips;
import sik.client.user.services.ClientIOServices;

/**
 * Dialog to display messages or prompts to the user. This would generally be
 * used to prompt a user for password when they attempt to exit a GUI or make
 * any settings changes. The password to compare the user's input to is stored
 * in a text file, which is retrieved via a client IO services class to read in
 * that password.
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class AdminDialog extends JDialog implements ActionListener,
		Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class to perform text file IO services. The text file contains attribute
	 * values and settings to be used by this and other classes
	 */
	private ClientIOServices clientOptions;

	// dialog elements
	private JButton OKButton, cancelButton;
	private JPasswordField passwordTextField;
	private JLabel messageLabel;
	private String password;
	private JPanel messagePane, passwordPane, buttonPane;
	private int choice;

	/**
	 * Class constructor
	 * 
	 * @param parent
	 *            The calling class which called this class
	 * @param title
	 *            The title this GUI is to be set to
	 */
	public AdminDialog(JFrame parent, String title) {

		// Inherit implementations of JDialog
		super(parent, title, true);

		ActionClips.playAdminButton();

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
					OKButton.doClick();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cancelButton.doClick();
				}
			}
		});

		clientOptions = new ClientIOServices();
		password = clientOptions.getAdminPassword();

		// load GUI elements
		loadMessagePane();
		loadPasswordPane();
		loadButtonPane();

		// add elements to the content pane
		getContentPane().add(messagePane, BorderLayout.NORTH);
		getContentPane().add(passwordPane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// display attributes
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		// method called to centre the dialog on the screen
		centreFrame();

		// prepare and display the dialog
		pack();
		setVisible(true);
	}

	@SuppressWarnings("deprecation")
	/**
	 * Action event handler
	 * 
	 * @param e
	 *          The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == OKButton) {

			// if passwors entered is correct
			if (passwordTextField.getText().equalsIgnoreCase(password)) {
				ActionClips.playClick();
				this.setChoice(1);
				setVisible(false);
				dispose();
			} else {
				ActionClips.playWrongPassword();
				messageLabel.setText("Wrong password. Please try again");
				messageLabel.setForeground(new Color(255, 0, 0));
			}
		}

		if (e.getSource() == cancelButton) {
			ActionClips.playCancelButton();
			this.setChoice(0);
			setVisible(false);
			dispose();
		}
	}

	/**
	 * Prepares the message area panel elements
	 */
	private void loadMessagePane() {
		messagePane = new JPanel();
		messageLabel = new JLabel(
				"Please enter admin password to close program");
		messageLabel.setForeground(new Color(0, 0, 0));

		messagePane.add(messageLabel);
	}

	/**
	 * Prepares the password area panel elements
	 */
	private void loadPasswordPane() {
		passwordPane = new JPanel();
		passwordTextField = new JPasswordField(20);

		passwordPane.add(new JLabel("Password:"));
		passwordPane.add(passwordTextField);
	}

	/**
	 * Prepares the control button area panel elements
	 */
	private void loadButtonPane() {
		buttonPane = new JPanel();

		OKButton = new JButton("OK");
		OKButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		buttonPane.add(OKButton);
		buttonPane.add(cancelButton);
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
}
