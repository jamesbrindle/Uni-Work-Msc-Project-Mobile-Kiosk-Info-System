package sik.client.user.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;

import com.phidgets.PhidgetException;

/**
 * Panel which is displayed after a pre-defined timeout period to confirm if a
 * user wishes to end their current session or request more time to continue
 * with their session. If no user input is made the panel will again time out
 * and re-load the intro panel, thus terminating the users session. This is part
 * of a security technique to automatically log a user out of system if no user
 * input is made in a given time period
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class LogoutPromptPanel extends JPanel implements ActionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected MainDisplayGUI mainGUI;

	/**
	 * Timer which is used to display this panel for a pre-defined amount of
	 * time before loading the intro panel if no user input occurs
	 */
	private Timer durationTimer;

	// panel properties
	private GridBagConstraints logoutPromptLayout;
	private JLabel messageLabel;
	private JButton yesButton, noButton;
	private String currentView;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 * @param currentView
	 *            The current view to define which pane the home panel is to
	 *            display when it's loaded
	 */
	public LogoutPromptPanel(MainDisplayGUI mainGUI, String currentView) {
		this.currentView = currentView;
		this.mainGUI = mainGUI;

		// create new timer to display this panel for a given time
		durationTimer = new Timer(12000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {
				try {
					restartClientInterface();
				} catch (PhidgetException e) {
				}

				durationTimer.stop();
			}
		});

		mainGUI.homePanel.durationTimer.stop();

		setLayout(new GridBagLayout());
		logoutPromptLayout = new GridBagConstraints();
		loadLogoutPromptElements();

		setOpaque(false);

		durationTimer.start();
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == yesButton) {
			performYesOperation();

		}
		if (e.getSource() == noButton) {
			performNoOperation();
		}
	}

	/**
	 * Loads the logout prompt panel element
	 */
	public void loadLogoutPromptElements() {
		logoutPromptLayout.gridwidth = 2;
		logoutPromptLayout.gridy = 0;
		logoutPromptLayout.gridx = 0;

		logoutPromptLayout.insets = new Insets(0, 0, 70, 35);

		logoutPromptLayout.anchor = GridBagConstraints.CENTER;

		messageLabel = new JLabel("Would You Like More Time?");
		messageLabel.setFont(FontStyles.extraExtraLargeFont);
		messageLabel.setForeground(new Color(255, 255, 255));

		add(messageLabel, logoutPromptLayout);

		logoutPromptLayout.insets = new Insets(0, 35, 0, 70);
		logoutPromptLayout.gridwidth = 1;
		logoutPromptLayout.gridy = 1;
		logoutPromptLayout.gridx = 0;
		logoutPromptLayout.anchor = GridBagConstraints.WEST;

		yesButton = new JButton("", new ImageIcon("res/images/okbutton.png"));
		yesButton.setBorderPainted(false);
		yesButton.setContentAreaFilled(false);
		yesButton.addActionListener(this);

		add(yesButton, logoutPromptLayout);

		logoutPromptLayout.gridy = 1;
		logoutPromptLayout.gridx = 1;
		logoutPromptLayout.insets = new Insets(0, 0, 0, 100);
		logoutPromptLayout.anchor = GridBagConstraints.EAST;

		noButton = new JButton("", new ImageIcon("res/images/cancelbutton.png"));
		noButton.setBorderPainted(false);
		noButton.setContentAreaFilled(false);
		noButton.addActionListener(this);

		add(noButton, logoutPromptLayout);
	}

	/**
	 * Will remove this panel and display the intro panel
	 * 
	 * @throws PhidgetException
	 *             If any RFID scanner or connectivity problems exist
	 */
	public void restartClientInterface() throws PhidgetException {
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.mainDisplayPanel.repaint();
		mainGUI.loadIntroPanel();
		mainGUI.frame.pack();
	}

	/**
	 * The method that is called when the user clicks the 'yes' button, which
	 * will re-load the home panel, as the user has requested more time in their
	 * current session
	 */
	public void performYesOperation() {
		ActionClips.playWelcome();
		durationTimer.stop();
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.mainDisplayPanel.repaint();

		if (currentView.equalsIgnoreCase("map")) {
			mainGUI.mapPanel.setVisible(true);
		} else {
			mainGUI.homePanel.setVisible(true);
			mainGUI.homePanel.durationTimer.restart();
		}
		mainGUI.frame.pack();
	}

	/**
	 * The method that is called when the user clicks the 'no' button, which
	 * will re-load the intro, as the user has requested they want no more time
	 * in the current session
	 */
	public void performNoOperation() {
		durationTimer.stop();
		ActionClips.playCancelButton();
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.mainDisplayPanel.repaint();

		try {
			mainGUI.loadIntroPanel();

		} catch (PhidgetException e1) {
		}

		mainGUI.frame.pack();
	}

}
