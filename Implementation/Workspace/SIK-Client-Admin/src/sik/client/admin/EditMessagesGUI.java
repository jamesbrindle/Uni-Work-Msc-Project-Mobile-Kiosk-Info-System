package sik.client.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * GUI for displaying and editing messages from a student record object
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 * 
 */
public class EditMessagesGUI {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;
	private Container container;
	protected JFrame frame;

	/**
	 * Class which deals with the actions associated with the control buttons of
	 * this GUI
	 */
	protected EditMessagesGUIButtonManager editMessagesGBM;

	/**
	 * Class which deals with the actions associated with this class
	 */
	protected EditMessagesGUIListener editMessagesGL;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected RecordsGUIListener recordsGL;

	// panels
	private JPanel messageArea, navigation;

	// layouts
	private GridBagConstraints messageAreaLayout, navigationLayout;

	// navigation properties
	protected JButton firstNavButton, lastNavButton, nextNavButton,
			previousNavButton, newRecordNavButton, saveNavButton,
			editNavButton, removeNavButton;

	protected JLabel warningLabel;
	protected String warningMessage;
	protected boolean isEditing = false;

	// menu properties
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenuItem menuItem3;

	// messages properties
	private JLabel messageNumberLabel;
	protected JTextArea theMessage;
	protected JTextField messageNumberTextField;
	private JScrollPane theMessageScrollPane;
	protected ArrayList<String> messagesArrayList;
	protected int arrayPointer = 0;

	/**
	 * Class constructor
	 * 
	 * @param recordsGL
	 *            The parent class which called this class
	 */
	public EditMessagesGUI(RecordsGUIListener recordsGL) {
		this.recordsGL = recordsGL;
		messagesArrayList = new ArrayList<String>();
	}

	/**
	 * Builds and displays the elements and their attributes of this GUI
	 */
	public void run() {
		frame = new JFrame("Messages Editor GUI");

		// create new window listener
		frame.addWindowListener(new WindowAdapter() {
			/**
			 * Creates a new window closing event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void windowClosing(WindowEvent e) {

				frame.dispose();
			}
		});

		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		// create new instances of the objects used in this class

		editMessagesGBM = new EditMessagesGUIButtonManager(this);
		editMessagesGL = new EditMessagesGUIListener(this);

		messageArea = new JPanel(new GridBagLayout());
		messageAreaLayout = new GridBagConstraints();

		navigation = new JPanel(new GridBagLayout());
		navigationLayout = new GridBagConstraints();

		// load GUI elements
		loadMenu();
		loadNavigationElements();
		loadMessagesArea();

		// add elements to the container
		container.add(messageArea, BorderLayout.CENTER);
		container.add(navigation, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setResizable(false);

		// calls method to centre this GUIs frame on the screen
		centreFrame();

		// prepare and show this GUI
		frame.pack();
		frame.setVisible(true);

		// prepare the navigation style control buttons of this GUI
		editMessagesGL.setNavButtons();
	}

	/**
	 * Positions this class's frame to appear centred on the screen
	 */
	private void centreFrame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Prepares the messages Area panel of this GUI
	 */
	private void loadMessagesArea() {

		messageAreaLayout.insets = new Insets(5, 5, 5, 5);

		messageNumberLabel = new JLabel("Message Number");
		messageAreaLayout.gridy = 0;
		messageAreaLayout.gridx = 0;
		messageAreaLayout.anchor = GridBagConstraints.WEST;
		messageArea.add(messageNumberLabel, messageAreaLayout);

		messageNumberTextField = new JTextField(10);
		messageNumberTextField.setEditable(false);
		messageAreaLayout.gridy = 0;
		messageAreaLayout.gridx = 1;
		messageArea.add(messageNumberTextField, messageAreaLayout);

		theMessage = new JTextArea(10, 50);
		theMessage.setEditable(false);
		theMessageScrollPane = new JScrollPane(theMessage);
		messageAreaLayout.gridy = 1;
		messageAreaLayout.gridx = 0;
		messageAreaLayout.gridwidth = 2;
		messageArea.add(theMessageScrollPane, messageAreaLayout);

		// if there are no messages to display
		if (recordsGL.store.currentRecord().getMessages() == null) {
			messageNumberTextField.setText("0 / 0");
		} else {
			messagesArrayList = recordsGL.store.currentRecord().getMessages();
			messageNumberTextField.setText("1 / " + messagesArrayList.size());
			if (messagesArrayList.isEmpty()) {
				theMessage.setText("");
			} else {
				theMessage.setText(messagesArrayList.get(0).toString());
			}
		}
	}

	/**
	 * Prepares the navigation controls panel (buttons) of this GUI
	 */
	private void loadNavigationElements() {

		navigationLayout.insets = new Insets(10, 2, 5, 2);

		warningLabel = new JLabel(warningMessage);
		warningLabel.setForeground(new Color(255, 0, 0));
		navigationLayout.gridx = 0;
		navigationLayout.gridy = 0;
		navigationLayout.gridwidth = 8;
		navigation.add(warningLabel, navigationLayout);

		navigationLayout.gridwidth = 1;
		firstNavButton = new JButton("<<");
		firstNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 0;
		navigationLayout.gridy = 1;
		navigation.add(firstNavButton, navigationLayout);

		previousNavButton = new JButton("<");
		previousNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 1;
		navigationLayout.gridy = 1;
		navigation.add(previousNavButton, navigationLayout);

		nextNavButton = new JButton(">");
		nextNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 2;
		navigationLayout.gridy = 1;
		navigation.add(nextNavButton, navigationLayout);

		lastNavButton = new JButton(">>");
		lastNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 3;
		navigationLayout.gridy = 1;
		navigation.add(lastNavButton, navigationLayout);

		newRecordNavButton = new JButton("New Message");
		newRecordNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 4;
		navigationLayout.gridy = 1;
		navigation.add(newRecordNavButton, navigationLayout);

		saveNavButton = new JButton("Save");
		saveNavButton.addActionListener(editMessagesGL);
		saveNavButton.setEnabled(false);
		navigationLayout.gridx = 5;
		navigationLayout.gridy = 1;
		navigation.add(saveNavButton, navigationLayout);

		editNavButton = new JButton("Edit");
		editNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 6;
		navigationLayout.gridy = 1;
		navigation.add(editNavButton, navigationLayout);

		removeNavButton = new JButton("Remove Message");
		removeNavButton.addActionListener(editMessagesGL);
		navigationLayout.gridx = 7;
		navigationLayout.gridy = 1;
		navigation.add(removeNavButton, navigationLayout);
	}

	/**
	 * Prepares the menu elements of this GUI
	 */
	private void loadMenu() {
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);

		menuItem3 = new JMenuItem("Exit GUI");
		menuItem3.addActionListener(editMessagesGL);

		menu.add(menuItem3);
	}

	/**
	 * Sets the GUI's displayed warning message
	 * 
	 * @param message
	 *            The message to be displayed as the warning
	 */
	public void setWarningMessage(String message) {
		warningMessage = message;
		warningLabel.setText(warningMessage);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		editMessagesGBM.setNavButtonsEnabled(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is in 'edit record' mode
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setEditNavButtonMode(boolean b) {
		editMessagesGBM.setEditNavButtonMode(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the last record in the messages array
	 * list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsLast(boolean b) {
		editMessagesGBM.setIsLast(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the first record in the messages array
	 * list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsFirst(boolean b) {
		editMessagesGBM.setIsFirst(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the messages array list is empty
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsEmpty(boolean b) {
		editMessagesGBM.setIsEmpty(b);
	}
}
