package sik.client.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A GUI to navigate through, edit and display the details of student record
 * objects which are stored in a store object
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class RecordsGUI {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class to handle the navigation control button styles
	 */
	protected RecordsGUIButtonManager recrdsGBM;

	/**
	 * Class to handle the action events of this class
	 */
	protected RecordsGUIListener recordsGUIListener;

	// frame properties
	private Container container;
	protected JFrame frame;

	// panels
	private JPanel recordsInfo, photoArea, searchBar, navigation;

	// layouts
	private GridBagConstraints recordsInfoLayout, photoAreaLayout,
			navigationLayout;

	// account information elements
	private JLabel studentIDLabel, titleLabel, firstNameLabel, lastNameLabel,
			firstLineAddrLabel, secondLineAddrLabel, cityLabel, countyLabel,
			postCodeLabel, telephoneLabel, pinNumberLabel, rfidTagIDLabel,
			courseLabel;

	protected JTextField studentIDTextField, titleTextField,
			firstNameTextField, lastNameTextField, firstLineAddrTextField,
			secondLineAddrTextField, cityTextField, countyTextField,
			postCodeTextField, telephoneTextField, pinNumberTextField,
			rfidTagIDTextField, courseTextField;

	// searchBarElements
	private JLabel searchBarLabel;
	protected JTextField searchBarTextField;
	protected JButton searchBarSearchButton;

	// navigationElements
	protected JButton firstNavButton, lastNavButton, nextNavButton,
			previousNavButton, newRecordNavButton, saveNavButton,
			editNavButton, removeNavButton;

	protected JLabel warningLabel, warningLabel2;
	protected String warningMessage, warningMessage2;
	protected boolean isEditing = false;

	// menu Elements
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenuItem menuItem_open, menuItem_saveAs, menuItem_exit,
			menuItem_newStore, menuItem_RMISave, menuItem_RMILoad,
			menuItem_changeRMILoc;

	// Photo Area Elements
	ImageIcon photo;
	protected String photoString = "images/default.jpg";
	protected JButton editImageButton, editMessages, editTimetable,
			editAssignmentResults, RFIDTagAssignButton;
	protected JLabel image;

	/**
	 * Builds and displays the elements and their attributes of this GUI
	 */
	public void run() {
		frame = new JFrame("Records Manager GUI");

		// add a window listener to this frame
		frame.addWindowListener(new WindowAdapter() {
			/**
			 * Creates a new window closing event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				System.exit(0);
			}
		});

		// create new instances of using objects
		recrdsGBM = new RecordsGUIButtonManager(this);
		recordsGUIListener = new RecordsGUIListener(this);

		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		recordsInfo = new JPanel(new GridBagLayout());
		recordsInfoLayout = new GridBagConstraints();

		photoArea = new JPanel(new GridBagLayout());
		photoAreaLayout = new GridBagConstraints();

		searchBar = new JPanel(new FlowLayout());

		navigation = new JPanel(new GridBagLayout());
		navigationLayout = new GridBagConstraints();

		// load GUI elements
		loadMenu();
		loadRecordInfoElements();
		loadPhotoAreaElements();

		// set original warning message
		warningMessage = "No File Loaded";
		warningMessage2 = "Click File Menu to Begin";

		loadSearchBarElements();
		loadNavigationElements();

		// set original GUI 'modes'
		setTextFieldsEditable(false);
		setNavButtonsEnabled(false);

		// add elements to the container
		container.add(searchBar, BorderLayout.NORTH);
		container.add(recordsInfo, BorderLayout.WEST);
		container.add(photoArea, BorderLayout.EAST);
		container.add(navigation, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setResizable(false);

		// call method to centre this GUI on the screen
		centreFrame();

		// prepare and display this GUI
		frame.pack();
		frame.setVisible(true);
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
	 * Prepares the photo area panel elements
	 */
	private void loadPhotoAreaElements() {
		photo = new ImageIcon(photoString);
		image = new JLabel(photo);
		image.setPreferredSize(new Dimension(100, 150));

		photoAreaLayout.insets = new Insets(5, 0, 0, 20);

		editImageButton = new JButton("Edit Image");
		editImageButton.addActionListener(recordsGUIListener);

		editMessages = new JButton("Messages");
		editMessages.addActionListener(recordsGUIListener);

		editTimetable = new JButton("Timetable");
		editTimetable.addActionListener(recordsGUIListener);

		editAssignmentResults = new JButton("Assignment Results");
		editAssignmentResults.addActionListener(recordsGUIListener);

		RFIDTagAssignButton = new JButton("Assign RFID Tag");
		RFIDTagAssignButton.addActionListener(recordsGUIListener);

		photoAreaLayout.gridy = 0;
		photoArea.add(image, photoAreaLayout);

		photoAreaLayout.gridy = 1;
		photoArea.add(editImageButton, photoAreaLayout);

		photoAreaLayout.gridy = 2;
		photoArea.add(editMessages, photoAreaLayout);

		photoAreaLayout.gridy = 3;
		photoArea.add(editTimetable, photoAreaLayout);

		photoAreaLayout.gridy = 4;
		photoArea.add(editAssignmentResults, photoAreaLayout);

		photoAreaLayout.gridy = 5;
		photoArea.add(RFIDTagAssignButton, photoAreaLayout);
	}

	/**
	 * Prepares the search bar area panel elements
	 */
	private void loadSearchBarElements() {
		searchBarLabel = new JLabel(
				"Please Enter Surname or Student ID Number to Search");
		searchBar.add(searchBarLabel);

		searchBarTextField = new JTextField(15);
		searchBar.add(searchBarTextField);

		searchBarSearchButton = new JButton("Search");
		searchBarSearchButton.addActionListener(recordsGUIListener);
		searchBar.add(searchBarSearchButton);

	}

	/**
	 * Prepares the record details area panel elements
	 */
	private void loadRecordInfoElements() {
		recordsInfoLayout.insets = new Insets(6, 13, 0, 0);
		recordsInfoLayout.anchor = GridBagConstraints.FIRST_LINE_START;

		loadRecordLabels();
		loadAccountTextFields();
	}

	/**
	 * Prepares the record details label elements
	 */
	private void loadRecordLabels() {
		studentIDLabel = new JLabel("Student ID");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 0;
		recordsInfo.add(studentIDLabel, recordsInfoLayout);

		pinNumberLabel = new JLabel("Pin Number");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 1;
		recordsInfo.add(pinNumberLabel, recordsInfoLayout);

		courseLabel = new JLabel("Course");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 2;
		recordsInfo.add(courseLabel, recordsInfoLayout);

		titleLabel = new JLabel("Title");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 3;
		recordsInfo.add(titleLabel, recordsInfoLayout);

		firstNameLabel = new JLabel("First Name");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 4;
		recordsInfo.add(firstNameLabel, recordsInfoLayout);

		lastNameLabel = new JLabel("Last Name");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 5;
		recordsInfo.add(lastNameLabel, recordsInfoLayout);

		firstLineAddrLabel = new JLabel("First Line Address");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 6;
		recordsInfo.add(firstLineAddrLabel, recordsInfoLayout);

		secondLineAddrLabel = new JLabel("Second Line Address");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 7;
		recordsInfo.add(secondLineAddrLabel, recordsInfoLayout);

		cityLabel = new JLabel("City");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 8;
		recordsInfo.add(cityLabel, recordsInfoLayout);

		countyLabel = new JLabel("County");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 9;
		recordsInfo.add(countyLabel, recordsInfoLayout);

		postCodeLabel = new JLabel("Post Code");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 10;
		recordsInfo.add(postCodeLabel, recordsInfoLayout);

		telephoneLabel = new JLabel("Telephone");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 11;
		recordsInfo.add(telephoneLabel, recordsInfoLayout);

		rfidTagIDLabel = new JLabel("RFID Tag ID");
		recordsInfoLayout.gridx = 0;
		recordsInfoLayout.gridy = 12;
		recordsInfo.add(rfidTagIDLabel, recordsInfoLayout);
	}

	/**
	 * Prepares the record details text field elements
	 */
	private void loadAccountTextFields() {
		recordsInfoLayout.insets = new Insets(3, 10, 0, 50);

		studentIDTextField = new JTextField(10);
		studentIDTextField.setEditable(false);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 0;
		recordsInfo.add(studentIDTextField, recordsInfoLayout);

		pinNumberTextField = new JTextField(10);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 1;
		recordsInfo.add(pinNumberTextField, recordsInfoLayout);

		courseTextField = new JTextField(20);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 2;
		recordsInfo.add(courseTextField, recordsInfoLayout);

		titleTextField = new JTextField(10);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 3;
		recordsInfo.add(titleTextField, recordsInfoLayout);

		firstNameTextField = new JTextField(15);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 4;
		recordsInfo.add(firstNameTextField, recordsInfoLayout);

		lastNameTextField = new JTextField(15);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 5;
		recordsInfo.add(lastNameTextField, recordsInfoLayout);

		firstLineAddrTextField = new JTextField(20);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 6;
		recordsInfo.add(firstLineAddrTextField, recordsInfoLayout);

		secondLineAddrTextField = new JTextField(20);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 7;
		recordsInfo.add(secondLineAddrTextField, recordsInfoLayout);

		cityTextField = new JTextField(15);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 8;
		recordsInfo.add(cityTextField, recordsInfoLayout);

		countyTextField = new JTextField(15);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 9;
		recordsInfo.add(countyTextField, recordsInfoLayout);

		postCodeTextField = new JTextField(10);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 10;
		recordsInfo.add(postCodeTextField, recordsInfoLayout);

		telephoneTextField = new JTextField(10);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 11;
		recordsInfo.add(telephoneTextField, recordsInfoLayout);

		rfidTagIDTextField = new JTextField(10);
		rfidTagIDTextField.setEditable(false);
		recordsInfoLayout.gridx = 1;
		recordsInfoLayout.gridy = 12;
		recordsInfo.add(rfidTagIDTextField, recordsInfoLayout);
	}

	/**
	 * Prepares the navigation control buttons area panel elements
	 */
	private void loadNavigationElements() {
		navigationLayout.insets = new Insets(10, 2, 5, 2);

		warningLabel = new JLabel(warningMessage);
		warningLabel.setForeground(new Color(255, 0, 0));

		warningLabel2 = new JLabel(warningMessage2);
		warningLabel2.setForeground(new Color(255, 0, 0));

		navigationLayout.gridx = 0;
		navigationLayout.gridy = 0;
		navigationLayout.gridwidth = 8;
		navigation.add(warningLabel, navigationLayout);

		navigationLayout.gridx = 0;
		navigationLayout.gridy = 1;
		navigationLayout.gridwidth = 8;
		navigation.add(warningLabel2, navigationLayout);

		navigationLayout.gridwidth = 1;
		firstNavButton = new JButton("<<");
		firstNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 0;
		navigationLayout.gridy = 2;
		navigation.add(firstNavButton, navigationLayout);

		previousNavButton = new JButton("<");
		previousNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 1;
		navigationLayout.gridy = 2;
		navigation.add(previousNavButton, navigationLayout);

		nextNavButton = new JButton(">");
		nextNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 2;
		navigationLayout.gridy = 2;
		navigation.add(nextNavButton, navigationLayout);

		lastNavButton = new JButton(">>");
		lastNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 3;
		navigationLayout.gridy = 2;
		navigation.add(lastNavButton, navigationLayout);

		newRecordNavButton = new JButton("New Record");
		newRecordNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 4;
		navigationLayout.gridy = 2;
		navigation.add(newRecordNavButton, navigationLayout);

		saveNavButton = new JButton("Save Record");
		saveNavButton.addActionListener(recordsGUIListener);
		saveNavButton.setEnabled(false);
		navigationLayout.gridx = 5;
		navigationLayout.gridy = 2;
		navigation.add(saveNavButton, navigationLayout);

		editNavButton = new JButton("Edit");
		editNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 6;
		navigationLayout.gridy = 2;
		navigation.add(editNavButton, navigationLayout);

		removeNavButton = new JButton("Remove");
		removeNavButton.addActionListener(recordsGUIListener);
		navigationLayout.gridx = 7;
		navigationLayout.gridy = 2;
		navigation.add(removeNavButton, navigationLayout);

	}

	/**
	 * Prepares the menu area panel elements
	 */
	private void loadMenu() {
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);

		menuItem_open = new JMenuItem("Open Store");
		menuItem_open.addActionListener(recordsGUIListener);

		menuItem_saveAs = new JMenuItem("Save Store As");
		menuItem_saveAs.addActionListener(recordsGUIListener);
		menuItem_saveAs.setEnabled(false);

		menuItem_exit = new JMenuItem("Exit GUI");
		menuItem_exit.addActionListener(recordsGUIListener);

		menuItem_newStore = new JMenuItem("New Store");
		menuItem_newStore.addActionListener(recordsGUIListener);

		menuItem_RMISave = new JMenuItem("Save to RMI Location");
		menuItem_RMISave.addActionListener(recordsGUIListener);
		menuItem_RMISave.setEnabled(false);

		menuItem_RMILoad = new JMenuItem("Load from RMI Location");
		menuItem_RMILoad.addActionListener(recordsGUIListener);

		menuItem_changeRMILoc = new JMenuItem("Change RMI Location");
		menuItem_changeRMILoc.addActionListener(recordsGUIListener);

		menu.add(menuItem_newStore);
		menu.add(menuItem_open);
		menu.add(menuItem_saveAs);
		menu.add(menuItem_RMILoad);
		menu.add(menuItem_RMISave);
		menu.add(menuItem_changeRMILoc);
		menu.add(menuItem_exit);
	}

	/**
	 * Sets the GUI's 1st warning message
	 * 
	 * @param message
	 *            The message to display as the warning
	 */
	public void setWarningMessage(String message) {
		warningMessage = message;
		warningLabel.setText(warningMessage);
	}

	/**
	 * Sets the GUI's 2nd warning message
	 * 
	 * @param message
	 *            The message to display as the warning
	 */
	public void setWarningMessage2(String message) {
		warningMessage2 = message;
		warningLabel2.setText(warningMessage2);
	}

	/**
	 * Sets the text fields to be in an editable state or not
	 * 
	 * @param b
	 *            What the text fields are to be set to (editiable or
	 *            uneditable)
	 */
	public void setTextFieldsEditable(boolean b) {
		if (b == false) {
			pinNumberTextField.setEditable(false);
			titleTextField.setEditable(false);
			firstNameTextField.setEditable(false);
			lastNameTextField.setEditable(false);
			firstLineAddrTextField.setEditable(false);
			secondLineAddrTextField.setEditable(false);
			cityTextField.setEditable(false);
			countyTextField.setEditable(false);
			postCodeTextField.setEditable(false);
			telephoneTextField.setEditable(false);
			searchBarTextField.setEnabled(true);
			courseTextField.setEditable(false);

		} else {
			pinNumberTextField.setEditable(true);
			titleTextField.setEditable(true);
			firstNameTextField.setEditable(true);
			lastNameTextField.setEditable(true);
			firstLineAddrTextField.setEditable(true);
			secondLineAddrTextField.setEditable(true);
			cityTextField.setEditable(true);
			countyTextField.setEditable(true);
			postCodeTextField.setEditable(true);
			telephoneTextField.setEditable(true);
			searchBarTextField.setEditable(true);
			courseTextField.setEditable(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		recrdsGBM.setNavButtonsEnabled(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is in 'edit record' mode
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setEditNavButtonMode(boolean b) {
		recrdsGBM.setEditNavButtonMode(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the last record in the student records
	 * array list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsLast(boolean b) {
		recrdsGBM.setIsLast(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the first record in the student records
	 * array list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsFirst(boolean b) {
		recrdsGBM.setIsFirst(b);
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the student records array list is empty
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsEmpty(boolean b) {
		recrdsGBM.setIsEmpty(b);
	}
}
