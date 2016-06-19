package sik.client.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sik.client.admin.services.PhidgetConnector;

import com.phidgets.PhidgetException;

/**
 * GUI for assigning and un-assigning an RFID tag to a given student
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 * 
 */
public class AssignRFIDTagGUI implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PhidgetConnector class which is used to communicate with an RFID scanner
	 * in order to obtain an RFID tag serial number
	 */
	public PhidgetConnector rfid;

	// GUI elements
	private JButton unAssignTag, saveButton, cancelButton, connectButton;
	private JTextField rfidTagField;
	private JLabel inputMessageLabel;
	private JPanel messagePane, tagPane, buttonPane;
	private Container container;
	private JFrame frame;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private RecordsGUIListener recordsGL;

	/**
	 * Class constructor
	 * 
	 * @param recordsGL
	 *            The parent class which called this class
	 */
	public AssignRFIDTagGUI(RecordsGUIListener recordsGL)
			throws PhidgetException {

		this.recordsGL = recordsGL;

		rfidTagField = new JTextField(20);
		rfidTagField.setEditable(false);
		inputMessageLabel = new JLabel("");

		rfid = new PhidgetConnector(rfidTagField, inputMessageLabel);
	}

	/**
	 * Action Event handler for this class
	 * 
	 * @param e
	 *            The action event
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectButton) {
			connecToPhidget();
		}
		if (e.getSource() == unAssignTag) {
			unAssignTag();
		}
		if (e.getSource() == saveButton) {
			saveRFIDTag();
		}
		if (e.getSource() == cancelButton) {
			cancelAssignment();
		}
	}

	/**
	 * Builds and displays the elements and their attributes of this GUI
	 */
	public void run() {

		frame = new JFrame("RFID Tag Assignment");

		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				frame.dispose();
			}
		});

		// so it listens for key events
		frame.setFocusable(true);
		frame.requestFocus();

		// create new key listener
		frame.addKeyListener(new KeyAdapter() {

			/**
			 * Creates a new key event pressed handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					unAssignTag.doClick();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					saveButton.doClick();
				}
			}
		});

		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		// load GUI elements
		loadMessagePane();
		loadTagFieldPane();
		loadButtonPane();

		// add GUI elements to this frame
		frame.add(messagePane, BorderLayout.NORTH);
		frame.add(tagPane, BorderLayout.CENTER);
		frame.add(buttonPane, BorderLayout.SOUTH);

		// calls method to centre this frame on the screen
		centreFrame();

		// prepare and display the frame
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Load the elements for the message panel of this frame
	 */
	private void loadMessagePane() {
		messagePane = new JPanel();

		inputMessageLabel
				.setText("Click 'Connect to RFID Scanner' button to begin");
		inputMessageLabel.setForeground(new Color(0, 0, 0));

		messagePane.add(inputMessageLabel);
	}

	/**
	 * Load the elements for the RFID tag serial panel of this frame
	 */
	private void loadTagFieldPane() {
		tagPane = new JPanel();

		tagPane.add(new JLabel("Tag Serial Number:"));
		tagPane.add(rfidTagField);
	}

	/**
	 * Load the elements for the control buttons panel of this frame
	 */
	private void loadButtonPane() {
		buttonPane = new JPanel();

		connectButton = new JButton("Connect To Scanner");
		connectButton.addActionListener(this);

		unAssignTag = new JButton("Unassign RFID Tag");
		unAssignTag.addActionListener(this);

		saveButton = new JButton("Save User's RFID Tag");
		saveButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		buttonPane.add(connectButton);
		buttonPane.add(unAssignTag);
		buttonPane.add(saveButton);
		buttonPane.add(cancelButton);
	}

	/**
	 * Positions this class's frame to appear centred on the screen
	 */
	private void centreFrame() {
		frame.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Calls the PhidgetConnector class to connect to the RFID scanner hardware
	 */
	private void connecToPhidget() {
		try {
			rfid.connect();
		} catch (PhidgetException e1) {
			inputMessageLabel.setText("Problem connecting to RFID Scanner");
		}
	}

	/**
	 * Saves the RFID tag gained serial number via the parent class's instance
	 * of a store object which contains a list of student record objects
	 */
	private void saveRFIDTag() {
		recordsGL.store.currentRecord().setRFIDTagID(rfidTagField.getText());
		recordsGL.recordsGUI.rfidTagIDTextField.setText(rfidTagField.getText());
		recordsGL.saveFile();
		frame.dispose();
		try {
			rfid.disconnect();
		} catch (PhidgetException e1) {
		}
	}

	/**
	 * Closes this frame, no user changes will be saves
	 */
	private void cancelAssignment() {
		try {
			rfid.disconnect();
		} catch (PhidgetException e1) {
		}

		frame.dispose();
	}

	/**
	 * Clears the RFID gained tag serial number from the RFID tag text field
	 * object
	 */
	private void unAssignTag() {
		rfidTagField.setText("Unassigned");
	}
}
