package sik.client.user.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;
import sik.client.user.panes.NumPadPane;

import com.phidgets.PhidgetException;

/**
 * Panel displayed to allow a user to log into the system via supplying their
 * unique student ID and pin number rather than using the typical RFID tag to
 * log in. This panel is navigated to via the intro panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ManPinEntryPanel extends JPanel implements ActionListener,
		FocusListener, Serializable, KeyListener, CaretListener,
		MouseMotionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private MainDisplayGUI mainGUI;

	/**
	 * Panel which contains a number pad, which can be used to insert values
	 * into a supplied text field
	 */
	private NumPadPane numPad;

	/**
	 * Timer which will load the intro panel when a pre-defined time has been
	 * met
	 */
	public Timer durationTimer;

	// internal panels
	private JPanel manPinEntryPanelNorth, manPinEntryPanelEast,
			manPinEntryPanelWest, manPinEntryPanelSouth;

	// layouts
	private GridBagConstraints manPinEntryDisplayLayout,
			manPinEntryLayoutNorth, manPinEntryLayoutEast,
			manPinEntryLayoutWest, manPinEntryLayoutSouth;

	// labels and text fields
	private JLabel manPinEntryMessage, studentIDLabel, pinNoLabel,
			errorMessage;
	public JTextField studentIDTextField;
	private JPasswordField pinNoTextField;
	private boolean isStudentIDTextFieldFocused = true;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 */
	public ManPinEntryPanel(MainDisplayGUI mainGUI) {

		this.mainGUI = mainGUI;

		// create new timer to display this panel for a given time
		durationTimer = new Timer(30000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {

				// displays the intro panel
				performCancelLogin();
				durationTimer.stop();
			}
		});

		addMouseMotionListener(this);

		setLayout(new GridBagLayout());
		setOpaque(false);
		manPinEntryDisplayLayout = new GridBagConstraints();

		// load this panels elements
		loadManPinEntryPanelElements();

		durationTimer.start();
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		durationTimer.restart();
		if (e.getSource() == numPad.numClear) {
			this.performTextFieldClear();
		}

		if (e.getSource() == numPad.numCancel) {
			durationTimer.stop();
			ActionClips.playCancelButton();
			this.performCancelLogin();
		}

		if (e.getSource() == numPad.numEnter) {
			durationTimer.restart();
			this.performLogin();
		}
	}

	/**
	 * Loads panel elements
	 */
	private void loadManPinEntryPanelElements() {
		loadNorthPanelElements();
		loadEastPanelElements();
		loadWestPanelElements();
		loadSouthPanelElements();
	}

	/**
	 * Loads north panel area elements
	 */
	private void loadNorthPanelElements() {
		manPinEntryPanelNorth = new JPanel(new GridBagLayout());
		manPinEntryPanelNorth.setOpaque(false);
		manPinEntryLayoutNorth = new GridBagConstraints();

		manPinEntryMessage = new JLabel(
				"Please Enter Your Student ID and Pin Number");
		manPinEntryMessage.setFont(FontStyles.extraExtraLargeFont);
		manPinEntryMessage.setForeground(new Color(255, 255, 255));
		manPinEntryPanelNorth.add(manPinEntryMessage, manPinEntryLayoutNorth);

		manPinEntryDisplayLayout.insets = new Insets(30, 30, 30, 30);

		manPinEntryDisplayLayout.gridwidth = 2;
		manPinEntryDisplayLayout.gridy = 0;
		add(manPinEntryPanelNorth, manPinEntryDisplayLayout);
	}

	/**
	 * Loads east panel area elements
	 */
	private void loadEastPanelElements() {
		manPinEntryPanelEast = new JPanel(new GridBagLayout());
		manPinEntryPanelEast.setOpaque(false);
		manPinEntryLayoutEast = new GridBagConstraints();

		manPinEntryLayoutEast.insets = new Insets(2, 5, 2, 5);
		manPinEntryLayoutEast.anchor = GridBagConstraints.WEST;
		manPinEntryLayoutEast.gridy = 0;

		studentIDLabel = new JLabel("Student ID");
		studentIDLabel.setFont(FontStyles.largeFont);
		studentIDLabel.setForeground(new Color(255, 255, 255));

		studentIDTextField = new JTextField(12);
		studentIDTextField.addFocusListener(this);
		studentIDTextField.setFocusable(true);
		studentIDTextField.addKeyListener(this);
		studentIDTextField.addCaretListener(this);
		studentIDTextField.setFont(FontStyles.largeFont);

		manPinEntryPanelEast.add(studentIDLabel, manPinEntryLayoutEast);
		manPinEntryPanelEast.add(studentIDTextField, manPinEntryLayoutEast);

		manPinEntryLayoutEast.gridy = 1;

		pinNoLabel = new JLabel("Pin Numeber");
		pinNoLabel.setFont(FontStyles.largeFont);
		pinNoLabel.setForeground(new Color(255, 255, 255));

		pinNoTextField = new JPasswordField(12);
		pinNoTextField.addFocusListener(this);
		pinNoTextField.addKeyListener(this);
		pinNoTextField.setFocusable(true);
		pinNoTextField.addCaretListener(this);
		pinNoTextField.setFont(FontStyles.largeFont);

		manPinEntryPanelEast.add(pinNoLabel, manPinEntryLayoutEast);
		manPinEntryPanelEast.add(pinNoTextField, manPinEntryLayoutEast);

		manPinEntryDisplayLayout.gridwidth = 1;
		manPinEntryDisplayLayout.gridy = 1;
		add(manPinEntryPanelEast, manPinEntryDisplayLayout);

	}

	/**
	 * Loads west panel area elements
	 */
	private void loadWestPanelElements() {
		manPinEntryDisplayLayout.gridx = 1;
		manPinEntryDisplayLayout.gridy = 1;

		manPinEntryPanelWest = new JPanel(new GridBagLayout());
		manPinEntryPanelWest.setOpaque(false);
		manPinEntryLayoutWest = new GridBagConstraints();

		numPad = new NumPadPane(this, FontStyles.buttonFont1,
				studentIDTextField);
		addExtraNumPadActionListeners();

		manPinEntryPanelWest.add(numPad, manPinEntryLayoutWest);

		add(manPinEntryPanelWest, manPinEntryDisplayLayout);
	}

	/**
	 * Loads south panel area elements
	 */
	private void loadSouthPanelElements() {
		manPinEntryPanelSouth = new JPanel(new GridBagLayout());
		manPinEntryPanelSouth.setOpaque(false);
		manPinEntryLayoutSouth = new GridBagConstraints();

		errorMessage = new JLabel("Your Pin Number Should Be 4 Digits Long");
		errorMessage.setFont(FontStyles.largeFont);

		manPinEntryPanelSouth.add(errorMessage, manPinEntryLayoutSouth);

		manPinEntryDisplayLayout.gridwidth = 2;
		manPinEntryDisplayLayout.gridy = 3;
		manPinEntryDisplayLayout.gridx = 0;

		add(manPinEntryPanelSouth, manPinEntryDisplayLayout);

		studentIDTextField.requestFocusInWindow();
	}

	/**
	 * Adds additional listeners to the numpad panel
	 */
	private void addExtraNumPadActionListeners() {
		numPad.numCancel.addActionListener(this);
		numPad.numEnter.addActionListener(this);
		numPad.numClear.addActionListener(this);
		numPad.addMouseMotionListener(this);
	}

	/**
	 * When a text field gains focus the numpad panel needs to be notified so it
	 * knows which text fields to place the input
	 */
	public void focusGained(FocusEvent e) {
		if (e.getSource() == studentIDTextField) {
			numPad.setFocusedObject(studentIDTextField);
			isStudentIDTextFieldFocused = true;
		}
		if (e.getSource() == pinNoTextField) {
			numPad.setFocusedObject(pinNoTextField);
			isStudentIDTextFieldFocused = false;
		}
	}

	/**
	 * Clears this panels text fields
	 */
	public void clearTextFields() {
		studentIDTextField.setText("");
		pinNoTextField.setText("");
	}

	@SuppressWarnings("deprecation")
	/**
	 * Validates the users input and connects to the RMI server via the RMI client
	 * and displays the home panel and starts a user session. Thi method also
	 * displays any problem messages such as wrong pin entered or connection problems
	 */
	public void validateInputAndConnection() {
		// Makes sure the use has actually entered a student id and pin number
		if (pinNoTextField.getText().equalsIgnoreCase("")
				|| pinNoTextField.getText().isEmpty()) {
			if (studentIDTextField.getText().equalsIgnoreCase("")
					|| studentIDTextField.getText().isEmpty()) {

				errorMessage.setText("Please Enter Your Student ID number");
				studentIDTextField.requestFocusInWindow();
			} else {
				errorMessage.setText("Please Enter Your Pin Number");
				pinNoTextField.requestFocusInWindow();
			}
			ActionClips.playError();
			// if required inputs exists, then proceed with login
		} else {
			try {
				mainGUI.rmiClient.connectToServer();
				mainGUI.rmiClient.loadStudentRecord(
						studentIDTextField.getText(), false);
				System.out.println("Connected...");

				// if the RMI server doesn't match the student ID number
				if (mainGUI.rmiClient.getOperationMessage().equalsIgnoreCase(
						"nullPointer")) {
					ActionClips.playWrongPassword();
					errorMessage
							.setText("Student ID invalid. Please try again");
					mainGUI.rmiClient.setOperationMessage("");
					studentIDTextField.requestFocusInWindow();
					studentIDTextField.setText("");
					pinNoTextField.setText("");
				} else {

					// if the entered pin number is correct, then load the home
					// panel
					if (mainGUI.rmiClient.studentRecord.getPinNo()
							.equalsIgnoreCase(pinNoTextField.getText())) {
						clearTextFields();
						durationTimer.stop();
						mainGUI.mainDisplayPanel.remove(this);
						mainGUI.frame.repaint();
						mainGUI.loadHomePanel("messages");
						mainGUI.frame.setFocusable(isFocusOwner());
						mainGUI.frame.pack();

						// if the entered pin number is incorrect, then display
						// error
					} else {
						ActionClips.playWrongPassword();
						errorMessage.setText("Pin Incorrect. Please try again");
						pinNoTextField.setText("");
						pinNoTextField.requestFocusInWindow();
					}
				}
			} catch (ConnectException e) {
				ActionClips.playConnectionFailure();
				errorMessage
						.setText("Connection failure, please try again later");
				System.err.println("Connection failure - Connection exception");
				numPad.numClear.doClick();
				studentIDTextField.requestFocusInWindow();
			} catch (MalformedURLException e) {
				ActionClips.playConnectionFailure();
				errorMessage
						.setText("Connection failure, please try again later");
				clearTextFields();
				System.err.println("Connection failure - malformed url exception");
				numPad.numClear.doClick();
				studentIDTextField.requestFocusInWindow();
			} catch (NotBoundException e) {
				ActionClips.playConnectionFailure();
				errorMessage
						.setText("Connection failure, please try again later");
				clearTextFields();
				System.err.println("Connection failure - not bound exception");
				numPad.numClear.doClick();
				studentIDTextField.requestFocusInWindow();
			} catch (RemoteException e) {
				ActionClips.playConnectionFailure();
				clearTextFields();
				errorMessage
						.setText("Connection failure, please try again later");
				System.err.println("Connection failure - general remote exception ");
				numPad.numClear.doClick();
				studentIDTextField.requestFocusInWindow();
			}
		}
	}

	/**
	 * Clears the text fields. This method is generally executed when the
	 * 'clear' button is pressed on the numPad panel
	 */
	private void performTextFieldClear() {
		ActionClips.playClearButton();
		clearTextFields();
		studentIDTextField.requestFocusInWindow();
	}

	/**
	 * Method which cancels the login, which removes this panel and reloads the
	 * intro panel. This method is generally executed when the 'cancel' button
	 * on the numPad panel is pressed
	 */
	private void performCancelLogin() {
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.frame.repaint();
		try {
			mainGUI.loadIntroPanel();
		} catch (PhidgetException e1) {
		}
		mainGUI.frame.pack();
	}

	/**
	 * Method which is executed when the user presses the 'enter' button on the
	 * numPad panel. If the current focus is on the 'studentID' text field then
	 * the focus is then moved to the 'pinNo' text field. If the focus is on the
	 * pin number text field then a login attempt is made.
	 */
	private void performLogin() {
		if (isStudentIDTextFieldFocused) {
			if (studentIDTextField.getText().equalsIgnoreCase("")
					|| studentIDTextField.getText().isEmpty()) {

				errorMessage.setText("Please Enter Your Student ID number");
				ActionClips.playError();
				studentIDTextField.setText("");
				studentIDTextField.requestFocusInWindow();
			}

			else {
				ActionClips.playClick();
				errorMessage.setText("Now Enter Your Pin Number");
				pinNoTextField.setText("");
				pinNoTextField.requestFocusInWindow();
			}

		} else {
			if (studentIDTextField.getText().equalsIgnoreCase("")
					|| studentIDTextField.getText().isEmpty()) {

				errorMessage.setText("Please Enter Your Student ID number");
				ActionClips.playError();
				studentIDTextField.setText("");
				pinNoTextField.setText("");
				studentIDTextField.requestFocusInWindow();
			} else {
				validateInputAndConnection();
			}
		}
	}

	@SuppressWarnings("deprecation")
	/**
	 * Key pressed event handler
	 * @param e The key pressed event
	 */
	public void keyPressed(KeyEvent e) {
		// attempy to log in
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			durationTimer.restart();
			if (studentIDTextField.isFocusOwner()) {
				pinNoTextField.requestFocusInWindow();
				pinNoTextField.select(0, pinNoTextField.getText().length());
			} else {
				validateInputAndConnection();
			}
		}
		// reload intro panel
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			durationTimer.stop();
			mainGUI.mainDisplayPanel.remove(this);
			mainGUI.frame.repaint();
			try {
				mainGUI.loadIntroPanel();
			} catch (PhidgetException e1) {
			}
			mainGUI.frame.pack();
		}
	}

	/**
	 * Caret update event handler
	 * 
	 * @param e
	 *            The caret update event
	 */
	public void caretUpdate(CaretEvent e) {
		durationTimer.restart();
	}

	/**
	 * Mouse dragged event handler
	 * 
	 * @param e
	 *            The mouse dragged event
	 */
	public void mouseDragged(MouseEvent e) {
		durationTimer.restart();
	}

	/**
	 * Mouse moved event handler
	 * 
	 * @param e
	 *            The mouse moved event
	 */
	public void mouseMoved(MouseEvent e) {
		durationTimer.restart();
	}

	//@formatter:off
	/**
	 * Focus lost event handler
	 * @param e The focus event
	 */
	public void focusLost(FocusEvent e) {}
	
	/**
	 * Key typed event handler
	 * @param e The key typed event
	 */
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Key released event handler
	 * @param e The key released event
	 */
	public void keyReleased(KeyEvent e) {}
}
