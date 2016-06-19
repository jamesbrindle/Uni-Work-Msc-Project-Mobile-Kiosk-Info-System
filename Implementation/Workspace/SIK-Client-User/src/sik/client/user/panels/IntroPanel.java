package sik.client.user.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;

/**
 * Panel which is shown as the main panel before a user logs in and creates a
 * session. This panel also utilises the an RFID phidget connector so a user can
 * log in using an RFID tag. From this panel, a user can navigate to the 'Manual
 * Pin Entry Panel' where a user may log in using their unique student ID and
 * pin number rather than using an RFID tag to log in
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class IntroPanel extends JPanel implements ActionListener, Serializable,
		AttachListener, DetachListener, ErrorListener, TagGainListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected MainDisplayGUI mainGUI;

	// panel properties
	private GridBagConstraints introDisplayLayout;
	private JLabel introMessageLn1, introMessageLn2;
	private JButton keyEntryButton;
	private String rfidTagID;
	public RFIDPhidget rfid;
	private Timer validateTimer, messageDurationTimer1, messageDurationTimer2;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 * @throws PhidgetException
	 *             when there is a problem with the RFID scanner hardware or
	 *             connecting to it
	 */
	public IntroPanel(final MainDisplayGUI mainGUI) throws PhidgetException {
		this.mainGUI = mainGUI;

		// create new timer to validate the user after a given time once an RFID
		// tag serial
		// number has been gained
		validateTimer = new Timer(50, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {
				validateInputAndConnection();
			}
		});

		// create new timer to display a message after a given amount of time
		messageDurationTimer1 = new Timer(5000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {

				introMessageLn1
						.setText("Please Hold Your Fob Key or Card Up to the Reader");
				introMessageLn2.setText("In Order to Access This Console");

				messageDurationTimer1.stop();
			}
		});

		// create new timer to display a message after a given amount of time
		messageDurationTimer2 = new Timer(5000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param The
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {

				introMessageLn1
						.setText("Please Hold Your Fob Key or Card Up to the Reader");
				introMessageLn2.setText("In Order to Access This Console");
				messageDurationTimer2.stop();
			}
		});

		// create new RFIDphidget
		rfid = new RFIDPhidget();
		addRFIDActionListeners();

		setLayout(new GridBagLayout());
		introDisplayLayout = new GridBagConstraints();
		loadIntroPanelElements();

		setOpaque(false);

		 //open the RFID scanner to accept connections
		rfid.openAny();
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == keyEntryButton) {

			ActionClips.playNumButton();
			rfidDisconnect();
			mainGUI.mainDisplayPanel.remove(this);
			mainGUI.frame.repaint();
			mainGUI.loadManualPinEntryPanel();
			mainGUI.frame.pack();
		}
	}

	/**
	 * Adds RFID Phidget listeners to this panel
	 */
	private void addRFIDActionListeners() {
		rfid.addAttachListener(this);
		rfid.addDetachListener(this);
		rfid.addErrorListener(this);
		rfid.addTagGainListener(this);
	}

	/**
	 * Prepares the intro panel elements
	 */
	private void loadIntroPanelElements() {
		introDisplayLayout.insets = new Insets(5, 5, 5, 5);

		introDisplayLayout.gridy = 0;

		introMessageLn1 = new JLabel("Fob Input Not Yet Ready");
		introMessageLn1.setFont(FontStyles.extraExtraLargeFont);
		introMessageLn1.setForeground(new Color(255, 255, 255));
		add(introMessageLn1, introDisplayLayout);

		introDisplayLayout.gridy = 1;

		introMessageLn2 = new JLabel("Waiting For RFID Scanner Connection");
		introMessageLn2.setFont(FontStyles.extraExtraLargeFont);
		introMessageLn2.setForeground(new Color(255, 255, 255));
		add(introMessageLn2, introDisplayLayout);

		introDisplayLayout.insets = new Insets(60, 5, 5, 5);

		introDisplayLayout.gridy = 2;

		keyEntryButton = new JButton("Manual Pin Entry", new ImageIcon(
				"res/images/login-icon.png"));
		keyEntryButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		keyEntryButton.setHorizontalTextPosition(SwingConstants.CENTER);
		keyEntryButton.setContentAreaFilled(false);
		keyEntryButton.setBorderPainted(false);

		keyEntryButton.setFont(FontStyles.buttonFont2);
		keyEntryButton.addActionListener(this);

		add(keyEntryButton, introDisplayLayout);
	}

	/**
	 * Validates a user input when an RFID tag gain event occurs and will load
	 * the home panel if the RMI client can connect to the RMI server and starts
	 * a log in session if the validation is successfully. This method also
	 * informs a user of any problem message that might occur such as wrong RFID
	 * tag serial number gained or connectivity problems
	 */
	private void validateInputAndConnection() {
		try {
			mainGUI.rmiClient.connectToServer();
			System.out.println("Connecting");
			mainGUI.rmiClient.loadStudentRecord(rfidTagID, true);
			validateTimer.stop();

			// if the supplied tag isn't assigned in that student's student
			// record
			if (mainGUI.rmiClient.getOperationMessage().equalsIgnoreCase(
					"nullPointer")) {
				rfidTagID = "";

				introMessageLn1.setText("RFID Tag Unassigned");
				ActionClips.playError();
				introMessageLn2
						.setText("Please Contact A Technician To Re-Assign Your Tag");
				messageDurationTimer1.start();

				// log in
			} else {
				this.rfidDisconnect();
				ActionClips.playWelcome();
				mainGUI.mainDisplayPanel.remove(this);
				mainGUI.frame.repaint();
				mainGUI.loadHomePanel("messages");
				mainGUI.frame.pack();
			}

			// any connection problems
		} catch (MalformedURLException e2) {
			validateTimer.stop();
			ActionClips.playConnectionFailure();
			introMessageLn1.setText("Connection Failure");
			introMessageLn2.setText("Please Try Again Later");
			System.err.println("Connection failure");
			messageDurationTimer2.start();
		} catch (NotBoundException e2) {
			validateTimer.stop();
			ActionClips.playConnectionFailure();
			introMessageLn1.setText("Connection Failure");
			introMessageLn2.setText("Please Try Again Later");
			System.err.println("Connection failure");
			messageDurationTimer2.start();
		} catch (ConnectException e) {
			validateTimer.stop();
			ActionClips.playConnectionFailure();
			introMessageLn1.setText("Connection Failure");
			introMessageLn2.setText("Please Try Again Later");
			System.err.println("Connection failure");
			messageDurationTimer2.start();
		} catch (RemoteException e2) {
			validateTimer.stop();
			ActionClips.playConnectionFailure();
			introMessageLn1.setText("Connection Failure");
			introMessageLn2.setText("Please Try Again Later");
			System.err.println("Connection failure");
			messageDurationTimer2.start();
		}
	}

	/**
	 * Close RFID phidget connections. This is generally required as problems
	 * may be encountered when a user wishes to log in via their RFID tag after
	 * a short amount of time. It will also turn the RFID phidget LEDs off to so
	 * as to inform a user that the RFID scanner is now non functional
	 */
	public void rfidDisconnect() {
		try {
			if (rfid.getAntennaOn()) {
				rfid.setAntennaOn(false);
			}
			if (rfid.getLEDOn()) {
				rfid.setLEDOn(false);
			}

			rfid.close();
		} catch (PhidgetException e) {

		}
	}

	/**
	 * RFID tag serial number has been received handler
	 * 
	 * @param e
	 *            The tag serial number
	 */
	public void tagGained(TagGainEvent e) {
		ActionClips.playClick();
		rfidTagID = e.getValue();
		System.out.println("Fob Serial Input: " + rfidTagID);
		validateTimer.start();
	}

	/**
	 * RFID tag attachment event handler
	 * 
	 * @param e
	 *            The attachment event
	 */
	public void attached(AttachEvent e) {
		try {
			rfid.setAntennaOn(true);
			rfid.setLEDOn(true);

			introMessageLn1
					.setText("Please Hold Your Fob Key or Card Up to the Reader");
			introMessageLn2.setText("In Order to Access This Console");
		} catch (PhidgetException e1) {
		}
	}

	/**
	 * RFID error event hander
	 * 
	 * @param e
	 *            The error event
	 */
	public void error(ErrorEvent e) {
		ActionClips.playWrongPassword();
		introMessageLn1.setText("RFID Interface Error");
		introMessageLn2.setText("Please Consult A Technician");
	}

	/**
	 * RFID detached event handler
	 * 
	 * @param e
	 *            The detachment event
	 */
	public void detached(DetachEvent e) {
		ActionClips.playError();
		introMessageLn1.setText("RFID Interface Detached");
		introMessageLn2.setText("Please Consult A Technician");
	}
}