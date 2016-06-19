package sik.client.user.panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.panels.HomePanel;
import sik.client.user.services.BTDiscoveryServices;
import sik.client.user.services.BTObjectPushService;
import sik.client.user.services.TextFileBuilder;

/**
 * Panel which is displayed as a content inside the home panel. As the home
 * panel displays a number of different complex panels corresponding to the
 * selected category this has been given the name 'pane' rather than 'panel'.
 * 
 * This particular pane is is displayed when the 'Bluetooth Options' button is
 * pressed on the home panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class BluetoothPane extends JPanel implements ActionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private HomePanel homePanel;

	/**
	 * Class to handle bluetooth discovery services
	 */
	private BTDiscoveryServices btDiscoveryServices;

	/**
	 * Class to handler bluetooth object push services
	 */
	private BTObjectPushService btObjectPushService;

	/**
	 * A thread to run the BT discovery services class, which is required so
	 * that its execution doesn't halt the user interface
	 */
	private Thread newBTDiscoveryThread;

	/**
	 * A thread to run the BT object push services class, which is required so
	 * that its execution doesn't halt the user interface
	 */
	private Thread newBTObjectPushThread;

	/**
	 * A timer used to iterate through simple 'discovering' services
	 */
	private Timer statusAnimationTimer;

	/**
	 * A timer to 'cap' the amount of time which the bluetooth discovery class
	 * spends on discovering bluetooth devices, as this can occasionally
	 * deadlock
	 */
	private Timer BTDiscoveryTimeoutTimer;

	/**
	 * A device count for the number of bluetooth devices discovered
	 */
	private int BTDeviceCount;

	/**
	 * Used to define a 'reset' amount for the simple bluetooth discovery
	 * animation, in which is gets re-set in order to 'loop' the animation
	 */
	private int timerIncrementCount = 0;

	/**
	 * Defines whether a bluetooth discovery search has been made and if the
	 * panel is currently displaying the bluetooth menu
	 */
	private boolean isBTMenuVisible = false;

	// user messages
	private String status;
	private JLabel titleLabel;
	public JLabel statusLabel;

	// buttons
	private JButton searchButton, sendMessageButton, sendScheduleButton,
			sendResultsButton, sendMapButton;
	private JButton BT1, BT2, BT3, BT4, BT5, BT6, BT7, BT8, BT9, BT10;

	// panels and layouts
	private JPanel titleArea, BTDevicesArea, controlArea, BTMenuArea;
	private GridBagConstraints titleAreaLayout, BTDeviceAreaLayout,
			controlAreaLayout, BTMenuAreaLayout;

	// create runnable objects for the bluetooth discovery and bluetooth object
	// push objects
	private Runnable discoveryRunnable, objectPushRunnable;

	/**
	 * Enum for the file types in when to object push to a bluetooth device
	 */
	enum FileType {
		MESSAGES(1), SCHEDULE(2), RESULTS(3), MAP(4);

		private int type = 0;

		/**
		 * Sets the file type
		 * 
		 * @param fileType
		 *            The file type int
		 */
		private FileType(int fileType) {
			this.type = fileType;
		}

		/**
		 * Returns the file type
		 * 
		 * @return type The file type int in which to return
		 */
		public int getFileType() {
			return type;
		}
	}

	/**
	 * Class constructor
	 * 
	 * @param homePanel
	 *            The parent class which called this class
	 */
	public BluetoothPane(HomePanel homePanel) {
		this.homePanel = homePanel;

		// create new timer for the simple bluetooth discovery animation
		statusAnimationTimer = new Timer(1000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param evt
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {
				timerIncrementCount++;
				if (timerIncrementCount == 8) {
					status = "Searching for Bluetooth Devices";
					statusLabel.setText(status);
					timerIncrementCount = 0;
				} else {
					status = "." + status + ".";
					statusLabel.setText(status);
				}
				statusAnimationTimer.restart();
			}
		});

		// create new timer for the bluetooth discovery timeout timer
		BTDiscoveryTimeoutTimer = new Timer(35000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param evt
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {
				statusAnimationTimer.stop();
				searchButton.setText("Re-Search");
				searchButton.setEnabled(true);

				String tmpStr = "";
				if (BTDeviceCount > 1) {
					tmpStr = "Devices";
				} else {
					tmpStr = "Device";
				}

				if (BTDeviceCount == 0) {
					statusLabel.setText("No Devices Found");
				}

				else {
					statusLabel.setText("Found " + BTDeviceCount + " " + tmpStr
							+ ". Please Select Your Device");
					BTDiscoveryTimeoutTimer.stop();
				}
			}
		});

		setPreferredSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(650, 400));
		setMinimumSize(new Dimension(650, 400));

		setOpaque(false);

		// initialise panels and layouts
		BTDevicesArea = new JPanel(new GridBagLayout());
		BTDeviceAreaLayout = new GridBagConstraints();

		titleArea = new JPanel(new GridBagLayout());
		titleAreaLayout = new GridBagConstraints();

		controlArea = new JPanel(new GridBagLayout());
		controlAreaLayout = new GridBagConstraints();

		BTMenuArea = new JPanel(new GridBagLayout());
		BTMenuAreaLayout = new GridBagConstraints();

		// load panel elements
		loadTitleAreaElements();
		loadBTDeviceAreaElements();
		loadBTMenuElements();
		loadControlAreaElements();

		// add sub-panels to this panel
		add(titleArea, BorderLayout.NORTH);
		add(BTDevicesArea, BorderLayout.CENTER);
		add(controlArea, BorderLayout.SOUTH);

		// construct bluetooth discover thread
		btDiscoveryServices = new BTDiscoveryServices(this);
		discoveryRunnable = btDiscoveryServices;
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		ActionClips.playNumButton();

		if (e.getSource() == searchButton) {
			performSearch();
		}
		if (e.getSource() == BT1) {
			loadBTMenu(0);
		}
		if (e.getSource() == BT2) {
			loadBTMenu(1);
		}
		if (e.getSource() == BT3) {
			loadBTMenu(2);
		}
		if (e.getSource() == BT4) {
			loadBTMenu(3);
		}
		if (e.getSource() == BT5) {
			loadBTMenu(4);
		}
		if (e.getSource() == BT6) {
			loadBTMenu(5);
		}
		if (e.getSource() == BT7) {
			loadBTMenu(6);
		}
		if (e.getSource() == BT8) {
			loadBTMenu(7);
		}
		if (e.getSource() == BT9) {
			loadBTMenu(8);
		}
		if (e.getSource() == BT10) {
			loadBTMenu(9);
		}
		if (e.getSource() == sendMessageButton) {
			sendFileToBTDevice(FileType.MESSAGES);
		}
		if (e.getSource() == sendScheduleButton) {
			sendFileToBTDevice(FileType.SCHEDULE);
		}
		if (e.getSource() == sendResultsButton) {
			sendFileToBTDevice(FileType.RESULTS);
		}
		if (e.getSource() == sendMapButton) {
			sendFileToBTDevice(FileType.MAP);
		}
	}

	/**
	 * Prepares the title area panel elements
	 */
	private void loadTitleAreaElements() {
		titleArea.setOpaque(false);

		titleAreaLayout.insets = new Insets(0, 0, 10, 0);
		titleAreaLayout.gridy = 0;

		titleLabel = new JLabel("Bluetooth Options");
		titleLabel.setFont(FontStyles.extraLargeFont);
		titleLabel.setForeground(new Color(255, 255, 255));

		titleArea.add(titleLabel, titleAreaLayout);

		titleAreaLayout.gridy = 1;

		status = "Start By Searching for Available Bluetooth Devices";

		statusLabel = new JLabel(status);
		statusLabel.setFont(FontStyles.largeFont);
		statusLabel.setForeground(new Color(255, 255, 255));

		titleArea.add(statusLabel, titleAreaLayout);
	}

	/**
	 * Prepares the bluetooth device area panel elements
	 */
	private void loadBTDeviceAreaElements() {
		BTDevicesArea.setOpaque(false);
		BTDevicesArea.setPreferredSize(new Dimension(650, 216));
		BTDevicesArea.setMaximumSize(new Dimension(650, 216));
		BTDevicesArea.setMinimumSize(new Dimension(650, 216));

		prepareBTPhoneImages();
	}

	/**
	 * Prepares the control buttons area panel elements
	 */
	private void loadControlAreaElements() {

		controlArea.setOpaque(false);

		searchButton = new JButton("Search", new ImageIcon(
				"res/images/buttonNotSelected/search.png"));
		searchButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		searchButton.setHorizontalTextPosition(SwingConstants.CENTER);
		searchButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/search.png"));
		searchButton.setFont(FontStyles.smallerFont);
		searchButton.setContentAreaFilled(false);
		searchButton.setBorderPainted(false);
		searchButton.addActionListener(this);

		controlArea.add(searchButton, controlAreaLayout);
	}

	@SuppressWarnings("deprecation")
	/**
	 * Loads the bluetooth menu panel
	 * @param index 
	 * 			The bluetooth device index for the bluetooth device,
	 * 			which we'd be using in the bluetooth menu
	 */
	private void loadBTMenu(int index) {
		statusAnimationTimer.stop();
		BTDiscoveryTimeoutTimer.stop();
		newBTDiscoveryThread.stop();
		statusLabel.setText("Checking Device Services...");
		searchButton.setEnabled(true);
		btDiscoveryServices.discoverServices(index);

		// if the selected device supports object push, then load BT menu
		// else, show problem status
		if (btDiscoveryServices.isObjectPushAvailable()) {
			remove(BTDevicesArea);
			remove(controlArea);
			searchButton.setText("");
			searchButton.setIcon(new ImageIcon("res/images/backButton.png"));
			searchButton.setPressedIcon(new ImageIcon(
					"res/images/backButton.png"));
			add(BTMenuArea, BorderLayout.CENTER);
			add(controlArea, BorderLayout.SOUTH);
			repaint();
			homePanel.mainGUI.frame.pack();
			isBTMenuVisible = true;
		} else {
			statusLabel.setText("Selected Device Does Not Support Object Push");
		}
	}

	/**
	 * Prepares the bluetooth menu area panel elements
	 */
	private void loadBTMenuElements() {
		BTMenuArea.setOpaque(false);
		BTMenuArea.setPreferredSize(new Dimension(650, 216));
		BTMenuArea.setMaximumSize(new Dimension(650, 216));
		BTMenuArea.setMinimumSize(new Dimension(650, 216));

		sendMessageButton = new JButton("Send Messages", new ImageIcon(
				"res/images/buttonNotSelected/messagesToPhone.png"));
		sendScheduleButton = new JButton("Send Timetable", new ImageIcon(
				"res/images/buttonNotSelected/scheduleToPhone.png"));
		sendResultsButton = new JButton("Send Results", new ImageIcon(
				"res/images/buttonNotSelected/resultsToPhone.png"));
		sendMapButton = new JButton("Send Campus Map", new ImageIcon(
				"res/images/buttonNotSelected/mapToPhone.png"));

		sendMessageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sendMessageButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sendMessageButton.setFont(FontStyles.smallerFont);
		sendMessageButton.setContentAreaFilled(false);
		sendMessageButton.setBorderPainted(false);
		sendMessageButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/messagesToPhone.png"));
		sendMessageButton.addActionListener(this);

		sendScheduleButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sendScheduleButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sendScheduleButton.setFont(FontStyles.smallerFont);
		sendScheduleButton.setContentAreaFilled(false);
		sendScheduleButton.setBorderPainted(false);
		sendScheduleButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/scheduleToPhone.png"));
		sendScheduleButton.addActionListener(this);

		sendResultsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sendResultsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sendResultsButton.setFont(FontStyles.smallerFont);
		sendResultsButton.setContentAreaFilled(false);
		sendResultsButton.setBorderPainted(false);
		sendResultsButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/resultsToPhone.png"));
		sendResultsButton.addActionListener(this);

		sendMapButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sendMapButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sendMapButton.setFont(FontStyles.smallerFont);
		sendMapButton.setContentAreaFilled(false);
		sendMapButton.setBorderPainted(false);
		sendMapButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/mapToPhone.png"));
		sendMapButton.addActionListener(this);

		BTMenuAreaLayout.gridx = 0;
		BTMenuArea.add(sendMessageButton, BTMenuAreaLayout);

		BTMenuAreaLayout.gridx = 1;
		BTMenuArea.add(sendScheduleButton, BTMenuAreaLayout);

		BTMenuAreaLayout.gridx = 2;
		BTMenuArea.add(sendResultsButton, BTMenuAreaLayout);

		BTMenuAreaLayout.gridx = 3;
		BTMenuArea.add(sendMapButton, BTMenuAreaLayout);
	}

	/**
	 * Method which calls the appropriate methods to discovery bluetooth devices
	 * and add an image icon to the bluetooth devices area panel which
	 * represents a bluetooth device
	 */
	private void performSearch() {
		// if a search has already been made and the panel is currently showing
		// the bluetooth menu, then go back to the discovery devices panel
		if (isBTMenuVisible) {
			isBTMenuVisible = false;
			remove(BTMenuArea);
			remove(controlArea);
			repaint();
			add(BTDevicesArea, BorderLayout.CENTER);
			add(controlArea, BorderLayout.SOUTH);
			searchButton.setText("Re-Search");
			searchButton.setIcon(new ImageIcon(
					"res/images/buttonNotSelected/search.png"));
			searchButton.setPressedIcon(new ImageIcon(
					"res/images/buttonSelected/search.png.png"));
			homePanel.mainGUI.frame.pack();

		} else {
			// reset the bluetooth device icons
			clearBTButtons();

			status = "Searching for Bluetooth Devices";
			searchButton.setEnabled(false);
			statusAnimationTimer.start();

			newBTDiscoveryThread = new Thread(discoveryRunnable);
			newBTDiscoveryThread.start();

			BTDiscoveryTimeoutTimer.setDelay(25000);
			BTDiscoveryTimeoutTimer.start();
		}
	}

	/**
	 * Adds a discovered bluetooth device to the bluetooth device area panel,
	 * which adds an icon to the panel that represents a bluetooth device. This
	 * method also deals with finalising a discovery after all bluetooth devices
	 * in range have been discovered
	 * 
	 * @param index
	 *            The bluetooth device index that has been discovered
	 * @param name
	 *            The name of the bluetooth device that is to be presented
	 * @param isLast
	 *            Determines if the bluetooth device that has been discovered is
	 *            the last bluetooth device in range to be discovered
	 */
	public void addBTDevice(int index, String name, boolean isLast) {
		BTDiscoveryTimeoutTimer.setDelay(8000);
		BTDiscoveryTimeoutTimer.restart();
		BTDeviceCount++;

		// if BT discovery service has finished searching for devices
		if (isLast) {
			statusAnimationTimer.stop();
			searchButton.setText("Re-Search");
			searchButton.setEnabled(true);
			BTDiscoveryTimeoutTimer.stop();
		}

		// set appropriate bluetooth device buttons visible and set the button
		// name to the bluetooth device name so the user can access it
		int deviceIndex = index + 1;
		switch (deviceIndex) {
			case 1:
				BT1.setVisible(true);
				BT1.setText(name);
				break;
			case 2:
				BT2.setVisible(true);
				BT2.setText(name);
				break;
			case 3:
				BT3.setVisible(true);
				BT3.setText(name);
				break;
			case 4:
				BT4.setVisible(true);
				BT4.setText(name);
				break;
			case 5:
				BT5.setVisible(true);
				BT5.setText(name);
				break;
			case 6:
				BT6.setVisible(true);
				BT6.setText(name);
				break;
			case 7:
				BT7.setVisible(true);
				BT7.setText(name);
				break;
			case 8:
				BT8.setVisible(true);
				BT8.setText(name);
				break;
			case 9:
				BT9.setVisible(true);
				BT9.setText(name);
				break;
			case 10:
				BT10.setVisible(true);
				BT10.setText(name);
				break;
		}
	}

	/**
	 * Executes methods that sends a file to a bluetooth device and displays any
	 * error messages for errors that might occur, or if the transfer has been
	 * successful
	 * 
	 * @param fileType
	 *            The type of file (as enum) to send to the bluetooth device
	 */
	private void sendFileToBTDevice(FileType fileType) {
		// String to be sent back to this panel once sending is complete
		// The bluetooth object push class sends the string back and confirms
		// the file has been sent
		String replyString = "";

		// get the file type to determine which message to present to the user
		if (fileType.getFileType() == FileType.MESSAGES.getFileType()) {
			replyString = "Successfully Sent Messages File";
			statusLabel.setText("Sending Messages File...");

			// construct new BT object push service with appropriate parameters
			btObjectPushService = new BTObjectPushService(
					this,
					replyString,
					btDiscoveryServices.getConnURL(),
					TextFileBuilder
							.getMessageFile(homePanel.mainGUI.rmiClient.studentRecord
									.getMessages()),
					BTObjectPushService.FileType.TEXT);

			// create thread to be executed
			objectPushRunnable = btObjectPushService;
			newBTObjectPushThread = new Thread(objectPushRunnable);

			// //execute the thread (i.e. send the file)
			newBTObjectPushThread.start();

			// disable menu buttons until the file has been send or terminated
			// to prevent
			// problems of using multiple threads
			disableBTMenuButtons();
		}

		// get the file type to determine which message to present to the user
		if (fileType.getFileType() == FileType.SCHEDULE.getFileType()) {
			replyString = "Successfully Sent Timetable File";
			statusLabel.setText("Sending Timetable File...");

			// create new object push class instance
			btObjectPushService = new BTObjectPushService(this, replyString,
					btDiscoveryServices.getConnURL(),
					TextFileBuilder.getTimetableFile(
							homePanel.mainGUI.rmiClient.studentRecord
									.getTimetable(),
							homePanel.mainGUI.rmiClient.studentRecord
									.getTimestableName()),
					BTObjectPushService.FileType.TEXT);

			// create thread to be executed
			objectPushRunnable = btObjectPushService;
			newBTObjectPushThread = new Thread(objectPushRunnable);

			// execute the thread (i.e. send the file)
			newBTObjectPushThread.start();

			// disable menu buttons until the file has been send or terminated
			// to prevent
			// problems of using multiple threads
			disableBTMenuButtons();
		}

		// get the file type to determine which message to present to the user
		if (fileType.getFileType() == FileType.RESULTS.getFileType()) {
			replyString = "Successfully Sent Results File";
			statusLabel.setText("Sending Results File...");

			// create new object push class instance
			btObjectPushService = new BTObjectPushService(
					this,
					replyString,
					btDiscoveryServices.getConnURL(),
					TextFileBuilder
							.getResultsFile(homePanel.mainGUI.rmiClient.studentRecord
									.getResults()),
					BTObjectPushService.FileType.TEXT);

			// create thread to be executed
			objectPushRunnable = btObjectPushService;

			// execute the thread (i.e. send the file)
			newBTObjectPushThread = new Thread(objectPushRunnable);
			newBTObjectPushThread.start();

			// disable menu buttons until the file has been send or terminated
			// to prevent
			// problems of using multiple threads
			disableBTMenuButtons();
		}
		// get the file type to determine which message to present to the user
		if (fileType.getFileType() == FileType.MAP.getFileType()) {
			replyString = "Successfully Sent Map Image File";
			statusLabel.setText("Sending Map Image File...");

			// create new object push class instance
			btObjectPushService = new BTObjectPushService(this, replyString,
					btDiscoveryServices.getConnURL(), new File(
							"res/images/mapJPG.jpg"),
					BTObjectPushService.FileType.IMAGE);

			// create thread to be executed
			objectPushRunnable = btObjectPushService;
			newBTObjectPushThread = new Thread(objectPushRunnable);

			// execute the thread (i.e. send the file)
			newBTObjectPushThread.start();

			// disable menu buttons until the file has been send or terminated
			// to prevent
			// problems of using multiple threads
			disableBTMenuButtons();
		}
	}

	/**
	 * Disables the buttons within the bluetooth menu panel
	 */
	private void disableBTMenuButtons() {
		sendMessageButton.setEnabled(false);
		sendScheduleButton.setEnabled(false);
		sendResultsButton.setEnabled(false);
		sendMapButton.setEnabled(false);
	}

	/**
	 * Enables the buttons within the bluetooth menu panel
	 */
	public void enableBTMenuButtons() {
		sendMessageButton.setEnabled(true);
		sendScheduleButton.setEnabled(true);
		sendResultsButton.setEnabled(true);
		sendMapButton.setEnabled(true);
	}

	/**
	 * Clears the bluetooth device image icon on the bluetooth devices panel
	 */
	private void clearBTButtons() {
		BT1.setVisible(false);
		BT2.setVisible(false);
		BT3.setVisible(false);
		BT4.setVisible(false);
		BT5.setVisible(false);
		BT6.setVisible(false);
		BT7.setVisible(false);
		BT8.setVisible(false);
		BT9.setVisible(false);
		BT10.setVisible(false);
	}

	/**
	 * Prepares the bluetooth device image icons that may be displayed in the
	 * bluetooth devices panel
	 */
	private void prepareBTPhoneImages() {
		BT1 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT1.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT1.setHorizontalTextPosition(SwingConstants.CENTER);
		BT1.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT1.setFont(FontStyles.tinyFont);
		BT1.setContentAreaFilled(false);
		BT1.setBorderPainted(false);
		BT1.setVisible(false);
		BT1.addActionListener(this);

		BT2 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT2.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT2.setHorizontalTextPosition(SwingConstants.CENTER);
		BT2.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT2.setFont(FontStyles.tinyFont);
		BT2.setContentAreaFilled(false);
		BT2.setBorderPainted(false);
		BT2.setVisible(false);
		BT2.addActionListener(this);

		BT3 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT3.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT3.setHorizontalTextPosition(SwingConstants.CENTER);
		BT3.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT3.setFont(FontStyles.tinyFont);
		BT3.setContentAreaFilled(false);
		BT3.setBorderPainted(false);
		BT3.setVisible(false);
		BT3.addActionListener(this);

		BT4 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT4.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT4.setHorizontalTextPosition(SwingConstants.CENTER);
		BT4.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT4.setFont(FontStyles.tinyFont);
		BT4.setContentAreaFilled(false);
		BT4.setBorderPainted(false);
		BT4.setVisible(false);
		BT4.addActionListener(this);

		BT5 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT5.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT5.setHorizontalTextPosition(SwingConstants.CENTER);
		BT5.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT5.setFont(FontStyles.tinyFont);
		BT5.setContentAreaFilled(false);
		BT5.setBorderPainted(false);
		BT5.setVisible(false);
		BT5.addActionListener(this);

		BT6 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT6.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT6.setHorizontalTextPosition(SwingConstants.CENTER);
		BT6.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT6.setFont(FontStyles.tinyFont);
		BT6.setContentAreaFilled(false);
		BT6.setBorderPainted(false);
		BT6.setVisible(false);
		BT6.addActionListener(this);

		BT7 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT7.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT7.setHorizontalTextPosition(SwingConstants.CENTER);
		BT7.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT7.setFont(FontStyles.tinyFont);
		BT7.setContentAreaFilled(false);
		BT7.setBorderPainted(false);
		BT7.setVisible(false);
		BT7.addActionListener(this);

		BT8 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT8.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT8.setHorizontalTextPosition(SwingConstants.CENTER);
		BT8.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT8.setFont(FontStyles.tinyFont);
		BT8.setContentAreaFilled(false);
		BT8.setBorderPainted(false);
		BT8.setVisible(false);
		BT8.addActionListener(this);

		BT9 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT9.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT9.setHorizontalTextPosition(SwingConstants.CENTER);
		BT9.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT9.setFont(FontStyles.tinyFont);
		BT9.setContentAreaFilled(false);
		BT9.setBorderPainted(false);
		BT9.setVisible(false);
		BT9.addActionListener(this);

		BT10 = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetoothPhone.png"));
		BT10.setVerticalTextPosition(SwingConstants.BOTTOM);
		BT10.setHorizontalTextPosition(SwingConstants.CENTER);
		BT10.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/bluetoothPhone.png"));
		BT10.setFont(FontStyles.tinyFont);
		BT10.setContentAreaFilled(false);
		BT10.setBorderPainted(false);
		BT10.setVisible(false);
		BT10.addActionListener(this);

		BTDeviceAreaLayout.gridx = 0;
		BTDeviceAreaLayout.gridy = 0;
		BTDevicesArea.add(BT1, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 1;
		BTDeviceAreaLayout.gridy = 0;
		BTDevicesArea.add(BT2, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 2;
		BTDeviceAreaLayout.gridy = 0;
		BTDevicesArea.add(BT3, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 3;
		BTDeviceAreaLayout.gridy = 0;
		BTDevicesArea.add(BT4, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 4;
		BTDeviceAreaLayout.gridy = 0;
		BTDevicesArea.add(BT5, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 0;
		BTDeviceAreaLayout.gridy = 1;
		BTDevicesArea.add(BT6, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 1;
		BTDeviceAreaLayout.gridy = 1;
		BTDevicesArea.add(BT7, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 2;
		BTDeviceAreaLayout.gridy = 1;
		BTDevicesArea.add(BT8, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 3;
		BTDeviceAreaLayout.gridy = 1;
		BTDevicesArea.add(BT9, BTDeviceAreaLayout);

		BTDeviceAreaLayout.gridx = 4;
		BTDeviceAreaLayout.gridy = 1;
		BTDevicesArea.add(BT10, BTDeviceAreaLayout);
	}
}
