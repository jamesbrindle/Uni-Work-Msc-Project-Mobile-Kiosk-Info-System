package sik.client.admin.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

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
 * Class which to manage RFID scanner connection and events
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class PhidgetConnector implements AttachListener, DetachListener,
		ErrorListener, TagGainListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The RFIDPhidget object
	 */
	private RFIDPhidget rfid;

	/**
	 * Text field to be used to pass the RFID tag serial
	 */
	private JTextField tagID;

	/**
	 * Label property to be used to pass the RFID scanner status messags
	 */
	private JLabel outMessage;

	/**
	 * RFID scanner control timer
	 */
	private Timer controlTimer;

	// control timer counter and settings
	private int timeoutAmount = 20;
	public int timeCounter;

	/**
	 * Class constructor
	 * 
	 * @param tagID
	 *            The supplied text field to be updated of the RFID tag serial
	 * @param forOutMessage
	 *            The supplied label to be updated of the RFID scanner status
	 */
	public PhidgetConnector(JTextField tagID, JLabel forOutMessage)
			throws PhidgetException {
		this.tagID = tagID;
		this.outMessage = forOutMessage;

		// create new RFIDPhidget instance
		rfid = new RFIDPhidget();

		// add phidget action listeners
		rfid.addAttachListener(this);
		rfid.addDetachListener(this);
		rfid.addErrorListener(this);
		rfid.addTagGainListener(this);

		// initialise timeCounter
		timeCounter = 0;

		// create new control timer
		controlTimer = new Timer(1000, new ActionListener() {
			/**
			 * Create new action event handler
			 * 
			 * @param evt
			 *            The action event
			 */
			public void actionPerformed(ActionEvent evt) {

				timeCounter++;

				// if timer reaches the timout amount
				if (timeCounter == timeoutAmount) {

					outMessage
							.setText("Please ensure the device is properly connected");
				}

				// restart the timeout counter if not attached
				try {
					if (rfid.isAttached() == false) {
						controlTimer.restart();
					} else if (rfid.isAttached() == true) {
						controlTimer.stop();

					}
				} catch (PhidgetException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Sets the timeout amount timeOutInSeconds The supplied timeout amount
	 */
	public void setTimeout(int timeOutInSeconds) {
		this.timeoutAmount = timeOutInSeconds;
	}

	/**
	 * Connect to the RFID phidget
	 * 
	 * @throws PhidgetException
	 *             If there are any connectivity problems
	 */
	public void connect() throws PhidgetException {

		// set RFID phidget to accept any connections
		rfid.openAny();

		if (rfid.isAttached() == false) {
			outMessage.setText("Waiting for RFID connection");
			controlTimer.start();
		}
	}

	/**
	 * Disconnect from the RFID phidget
	 * 
	 * @throws PhidgetException
	 *             If there are any connectivity problems
	 */
	public void disconnect() throws PhidgetException {
		controlTimer.stop();

		// turn off the RFID scanner LED if it's on
		if (rfid.getAntennaOn()) {
			rfid.setAntennaOn(false);
		}
		if (rfid.getLEDOn()) {
			rfid.setLEDOn(false);
		}

		rfid.close();
	}

	/**
	 * Phidget attachment event handler
	 * 
	 * @param e
	 *            The attachment event
	 */
	public void attached(AttachEvent e) {
		timeCounter = 0;
		outMessage
				.setText("RFID scanner connection established. Ready to read RFID fob or card");
		try {
			rfid.setAntennaOn(true);
			rfid.setLEDOn(true);
		} catch (PhidgetException e1) {
		}
	}

	/**
	 * RFID tag gain (gets an RFID tag serial number) event handler
	 * 
	 * @param e
	 *            The tag gained event
	 */
	public void tagGained(TagGainEvent e) {
		tagID.setText(e.getValue());
	}

	/**
	 * Phidget error event handler
	 * 
	 * @param e
	 *            The error event
	 */
	public void error(ErrorEvent e) {
		outMessage
				.setText("There was an error with the RFID scanner. Please reset the connection");
	}

	/**
	 * Phidget detached event handler
	 * 
	 * @param e
	 *            The detachment event
	 */
	public void detached(DetachEvent e) {
		outMessage.setText("The RFID scanner has been detached");
		controlTimer.start();
	}
}
