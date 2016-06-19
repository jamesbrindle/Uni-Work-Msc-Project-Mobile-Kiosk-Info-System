package sik.client.user.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;
import sik.client.user.panes.BluetoothPane;
import sik.client.user.panes.MessagesPane;
import sik.client.user.panes.ResultsPane;
import sik.client.user.panes.TimetablePane;

/**
 * Panel displayed as a the main panel when a user succesfully logs in and
 * creates a session
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class HomePanel extends JPanel implements ActionListener, Serializable,
		MouseMotionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	public MainDisplayGUI mainGUI;

	/**
	 * A string to define the current pane that is displayed within this home
	 * panel
	 */
	private String currentView;

	/**
	 * A timeout timer, which when a given time is met the GUI will load the
	 * logout prompt panel, which will request if the user wants more time. This
	 * is part of a security technique which will automatically log a user out
	 * if user input has been made in a given time frame
	 */
	public Timer durationTimer;

	// layouts
	private GridBagConstraints mainPanelLayout, greatingsPanelLayout,
			buttonPanelLayout;

	// internal panels
	private JPanel greetingsPanel, buttonPanel, viewPane;

	// external panes (panels within 'viewPane')
	private MessagesPane messagesPane;
	private TimetablePane timetablePane;
	private ResultsPane resultsPane;
	private BluetoothPane bluetoothPane;

	// buttons
	private JButton timesTableButton, messagesButton, campusGuideButton,
			BluetoothButton, assignmentResultsButton, logoutButton;
	private Dimension iconButtonSize = new Dimension(120, 120);

	// photo area
	private ImageIcon photo;
	private JLabel photoLabel;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 * @param currentView
	 *            The current view to define which pane the home panel is to
	 *            display when it's loaded
	 */
	public HomePanel(MainDisplayGUI mainGUI, String currentView) {
		this.currentView = currentView;

		// create new timer to laod the logout panel after a given time
		// this timer is re-set every time a mouse dragged or moved event occurs
		// is when user input exists
		durationTimer = new Timer(50000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {

				loadTimedLogoutPanel();
				durationTimer.stop();
			}
		});

		this.mainGUI = mainGUI;

		addMouseMotionListener(this);

		ActionClips.playWelcome();

		setLayout(new GridBagLayout());
		mainPanelLayout = new GridBagConstraints();

		setOpaque(false);

		// load internal panels
		greetingsPanel = new JPanel(new GridBagLayout());
		greatingsPanelLayout = new GridBagConstraints();
		greetingsPanel.setOpaque(false);

		viewPane = new JPanel(new BorderLayout());
		viewPane.setOpaque(false);
		viewPane.addMouseMotionListener(this);

		buttonPanel = new JPanel(new GridBagLayout());
		buttonPanelLayout = new GridBagConstraints();
		buttonPanel.setOpaque(false);

		loadGreetingPanelElements();
		loadButtonPanelElements();

		// add sub panels to this frame
		positionPanels();

		// pre-load external panes
		messagesPane = new MessagesPane(this);
		timetablePane = new TimetablePane(this);
		resultsPane = new ResultsPane(this);
		bluetoothPane = new BluetoothPane(this);

		// load the first pane and add it to the view pane
		loadMessagesPane();
		add(viewPane, mainPanelLayout);
	}

	/**
	 * Action event handler
	 * 
	 * @exception The
	 *                action event
	 */
	public void actionPerformed(ActionEvent e) {
		durationTimer.restart();

		if (e.getSource() == messagesButton) {
			ActionClips.playNumButton();

			if (!(currentView.equalsIgnoreCase("messages"))) {
				currentView = "messages";
				loadMessagesPane();
			}
		}
		if (e.getSource() == timesTableButton) {
			ActionClips.playNumButton();

			if (!(currentView.equalsIgnoreCase("timetable"))) {
				currentView = "timetable";
				loadTimetablePane();
			}
		}
		if (e.getSource() == assignmentResultsButton) {
			ActionClips.playNumButton();

			if (!(currentView.equalsIgnoreCase("results"))) {
				currentView = "results";
				loadResultsPane();
			}
		}
		if (e.getSource() == campusGuideButton) {
			ActionClips.playNumButton();
			loadMapPanel();
		}
		if (e.getSource() == BluetoothButton) {
			ActionClips.playNumButton();

			if (!(currentView.equalsIgnoreCase("bluetooth"))) {
				currentView = "bluetooth";
				loadBluetoothPane();
			}
		}
		if (e.getSource() == logoutButton) {
			ActionClips.playCancelButton();
			loadManualLogoutPanel();
		}
	}

	/**
	 * Prepare the greeting area panel elements
	 */
	private void loadGreetingPanelElements() {

		// get the name of the user to display in the greeting from the student
		// records
		// store located in the RMI client object
		JLabel welcomeMessage = new JLabel("Welcome "
				+ mainGUI.rmiClient.studentRecord.getFirstName() + " "
				+ mainGUI.rmiClient.studentRecord.getLastName());
		welcomeMessage.setFont(FontStyles.extraExtraLargeFont);
		welcomeMessage.setForeground(new Color(255, 255, 255));

		// get the photo of the user to display in the greeting from the student
		// records
		// store located in the RMI client object
		photo = mainGUI.rmiClient.studentRecord.getImage();
		photoLabel = new JLabel(photo);
		photoLabel.setPreferredSize(new Dimension(100, 150));
		Border border = LineBorder.createGrayLineBorder();
		photoLabel.setBorder(border);

		greatingsPanelLayout.gridx = 0;
		greetingsPanel.add(welcomeMessage, greatingsPanelLayout);

		greatingsPanelLayout.gridx = 1;
		greatingsPanelLayout.insets = new Insets(0, 300, 0, 0);
		greetingsPanel.add(photoLabel, greatingsPanelLayout);
	}

	/**
	 * Prepare the control button area panel elements
	 */
	private void loadButtonPanelElements() {
		messagesButton = new JButton("", new ImageIcon(
				"res/images/buttonSelected/message.png"));
		messagesButton.setPreferredSize(iconButtonSize);
		messagesButton.setBorderPainted(false);
		messagesButton.setContentAreaFilled(false);
		messagesButton.addActionListener(this);

		timesTableButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/schedule.png"));
		timesTableButton.setPreferredSize(iconButtonSize);
		timesTableButton.setBorderPainted(false);
		timesTableButton.setContentAreaFilled(false);
		timesTableButton.addActionListener(this);

		assignmentResultsButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/results.png"));
		assignmentResultsButton.setPreferredSize(iconButtonSize);
		assignmentResultsButton.setBorderPainted(false);
		assignmentResultsButton.setContentAreaFilled(false);
		assignmentResultsButton.addActionListener(this);

		campusGuideButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/map.png"));
		campusGuideButton.setPreferredSize(iconButtonSize);
		campusGuideButton.setBorderPainted(false);
		campusGuideButton.setContentAreaFilled(false);
		campusGuideButton.addActionListener(this);

		BluetoothButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/bluetooth.png"));
		BluetoothButton.setPreferredSize(iconButtonSize);
		BluetoothButton.setBorderPainted(false);
		BluetoothButton.setContentAreaFilled(false);
		BluetoothButton.addActionListener(this);

		logoutButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/logout.png"));
		logoutButton.setPreferredSize(iconButtonSize);
		logoutButton.setBorderPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.addActionListener(this);

		buttonPanelLayout.insets = new Insets(10, 10, 10, 10);
		buttonPanelLayout.gridy = 0;
		buttonPanelLayout.gridx = 0;

		buttonPanel.add(messagesButton, buttonPanelLayout);

		buttonPanelLayout.gridy = 0;
		buttonPanelLayout.gridx = 1;

		buttonPanel.add(timesTableButton, buttonPanelLayout);

		buttonPanelLayout.gridy = 1;
		buttonPanelLayout.gridx = 0;

		buttonPanel.add(assignmentResultsButton, buttonPanelLayout);

		buttonPanelLayout.gridy = 1;
		buttonPanelLayout.gridx = 1;

		buttonPanel.add(campusGuideButton, buttonPanelLayout);

		buttonPanelLayout.gridy = 2;
		buttonPanelLayout.gridx = 0;

		buttonPanel.add(BluetoothButton, buttonPanelLayout);

		buttonPanelLayout.gridy = 2;
		buttonPanelLayout.gridx = 1;

		buttonPanel.add(logoutButton, buttonPanelLayout);
	}

	/**
	 * Adds sub panels to this panel
	 */
	private void positionPanels() {
		mainPanelLayout.gridy = 0;
		mainPanelLayout.gridx = 0;
		mainPanelLayout.gridwidth = 2;

		mainPanelLayout.insets = new Insets(0, 0, 0, 0);
		mainPanelLayout.anchor = GridBagConstraints.EAST;

		add(greetingsPanel, mainPanelLayout);

		mainPanelLayout.gridy = 1;
		mainPanelLayout.gridx = 0;
		mainPanelLayout.gridwidth = 1;

		mainPanelLayout.insets = new Insets(0, 15, 0, 35);

		add(buttonPanel, mainPanelLayout);

		mainPanelLayout.gridy = 1;
		mainPanelLayout.gridx = 1;

		mainPanelLayout.insets = new Insets(0, 0, 7, 0);
	}

	/**
	 * Method to de-select the control buttons, in which when a control button
	 * is selected it displays a separate button image, therefore when a
	 * different button is pressed, each other button must be de-selected first
	 */
	private void resetButtonIcons() {
		messagesButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/message.png"));

		timesTableButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/schedule.png"));

		assignmentResultsButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/results.png"));

		campusGuideButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/map.png"));

		BluetoothButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/bluetooth.png"));

		logoutButton.setIcon(new ImageIcon(
				"res/images/buttonNotSelected/logout.png"));
	}

	/**
	 * Hides this panel and displays the logout prompt panel
	 */
	private void loadTimedLogoutPanel() {
		ActionClips.playCancelButton();
		setVisible(false);
		mainGUI.loadLogoutPromptPanel(currentView);
		mainGUI.frame.pack();
	}

	/**
	 * Removes this panel and displays the goodbye panel
	 */
	private void loadManualLogoutPanel() {
		resetButtonIcons();
		messagesButton.setIcon(new ImageIcon(
				"res/images/buttonSelected/map.png"));
		durationTimer.stop();
		viewPane.removeAll();
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.frame.repaint();
		mainGUI.loadGoodbyePanel();
		mainGUI.frame.pack();
	}

	/**
	 * Removes the current panel in the view pane and displays the messages pane
	 */
	private void loadMessagesPane() {
		resetButtonIcons();
		messagesButton.setIcon(new ImageIcon(
				"res/images/buttonSelected/message.png"));
		viewPane.removeAll();
		viewPane.add(messagesPane, BorderLayout.CENTER);
		repaint();
		mainGUI.frame.pack();
		mainGUI.exitProgramButton.requestFocus();
		mainGUI.exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Removes the current panel in the view pane and displays the timetable
	 * pane
	 */
	private void loadTimetablePane() {
		resetButtonIcons();
		timesTableButton.setIcon(new ImageIcon(
				"res/images/buttonSelected/schedule.png"));
		viewPane.removeAll();
		viewPane.add(timetablePane, BorderLayout.CENTER);
		repaint();
		mainGUI.frame.pack();
		mainGUI.exitProgramButton.requestFocus();
		mainGUI.exitProgramButton.requestFocusInWindow();

	}

	/**
	 * Hides this panel and displays the campus map panel
	 */
	private void loadMapPanel() {
		durationTimer.stop();
		this.setVisible(false);
		mainGUI.frame.repaint();
		mainGUI.loadMapPanel();
		mainGUI.frame.pack();
	}

	/**
	 * Removes the current panel in the view pane and displays the assignment
	 * results pane
	 */
	private void loadResultsPane() {
		resetButtonIcons();
		assignmentResultsButton.setIcon(new ImageIcon(
				"res/images/buttonSelected/results.png"));
		viewPane.removeAll();
		viewPane.add(resultsPane, BorderLayout.CENTER);
		repaint();
		mainGUI.frame.pack();
		mainGUI.exitProgramButton.requestFocus();
		mainGUI.exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Removes the current panel in the view pane and displays the bluetooth
	 * pane
	 */
	private void loadBluetoothPane() {
		resetButtonIcons();
		BluetoothButton.setIcon(new ImageIcon(
				"res/images/buttonSelected/bluetooth.png"));
		viewPane.removeAll();
		viewPane.add(bluetoothPane);
		repaint();
		mainGUI.frame.pack();
		mainGUI.exitProgramButton.requestFocus();
		mainGUI.exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Mouse dragged action event handler
	 * 
	 * @param e
	 *            The mouse dragged event
	 */
	public void mouseDragged(MouseEvent e) {
		durationTimer.restart();
	}

	/**
	 * Mouse moved action event handler
	 * 
	 * @param e
	 *            The mouse moved event
	 */
	public void mouseMoved(MouseEvent e) {
		durationTimer.restart();
	}
}
