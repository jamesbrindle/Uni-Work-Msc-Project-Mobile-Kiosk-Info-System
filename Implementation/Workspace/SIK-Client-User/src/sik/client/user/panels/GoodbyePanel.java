package sik.client.user.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;

import com.phidgets.PhidgetException;

/**
 * Panel displayed when a user logs out from a session
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class GoodbyePanel extends JPanel {

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
	 * A timer which will re load the intro panel after a given duration of time
	 */
	private Timer displayDurationTimer;

	// panel properties
	private GridBagConstraints goodbyeLayout;
	private JLabel goodbyeMessage;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 */
	public GoodbyePanel(MainDisplayGUI mainGUI) {
		this.mainGUI = mainGUI;

		// create new timer to load the intro panel after a given time
		displayDurationTimer = new Timer(6000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			private Timer displayDurationTimer;

			public void actionPerformed(ActionEvent evt) {
				try {
					restartClientInterface();
				} catch (PhidgetException e) {
				}
				displayDurationTimer.stop();
			}
		});

		// load panel elements
		setLayout(new GridBagLayout());
		goodbyeLayout = new GridBagConstraints();

		setOpaque(false);

		goodbyeMessage = new JLabel("Goodbye...");
		goodbyeMessage.setFont(FontStyles.extraExtraLargeFont);
		goodbyeMessage.setForeground(new Color(255, 255, 255));

		add(goodbyeMessage, goodbyeLayout);
		displayDurationTimer.start();
	}

	/**
	 * Method to re-load the intro panel
	 * 
	 * @throws PhidgedException
	 *             When there are any RFID scanner or connectivity problems
	 */
	public void restartClientInterface() throws PhidgetException {
		mainGUI.mainDisplayPanel.remove(this);
		mainGUI.mainDisplayPanel.repaint();
		mainGUI.loadIntroPanel();
		mainGUI.frame.pack();
	}
}