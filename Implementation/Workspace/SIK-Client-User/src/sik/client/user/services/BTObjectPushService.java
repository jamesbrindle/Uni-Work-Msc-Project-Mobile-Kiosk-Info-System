package sik.client.user.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.swing.Timer;

import sik.client.user.panes.BluetoothPane;

/**
 * Class to send a file to a bluetooth devices via bluetooth's 'Object Push'
 * technology
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class BTObjectPushService implements Runnable {

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private BluetoothPane BTPane;

	/**
	 * A connection object, to connect to a bluetooth device
	 */
	private Connection conn;

	/**
	 * The connection URL for the bluetooth device in which to send a file to
	 */
	private String connURL;

	/**
	 * The file to send to the bluetooth device
	 */
	private File file;

	/**
	 * A supplied string in which will be returned to the parent object
	 * informing the parent of the status of a file send attempt
	 */
	private String replyString;

	/**
	 * A timer to add a timeout operation when attempting to run file. When sending 
	 * a file to a device, if the device/user doesn't decide to accept the file or not, this
	 * timer will insure other operations can continue
	 */
	private Timer timeoutTimer;
	
	/**
	 * Specified file type enum of the file type in which to send to the
	 * bluetooth device
	 */
	private String FILE_TYPE;

	/**
	 * File type enum of the type of file which is to be sent to the bluetooth
	 * deivce
	 */
	public enum FileType {
		IMAGE("image/jpg"), TEXT("text");

		private String type;

		/**
		 * Sets the file type
		 * 
		 * @param type
		 *            The type in which to set the file type to
		 */
		private FileType(String type) {
			this.type = type;
		}

		/**
		 * Returns the file type of an enum
		 * 
		 * @return the file type in which to return
		 */
		public String getType() {
			return type;
		}
	}

	/**
	 * Class constructor
	 * 
	 * @param BTPane
	 *            The parent object which called an instance of this class
	 * @param replyString
	 *            The string in which to return as status message
	 * @param connURL
	 *            The bluetooth connection URL of where to send a file
	 * @param type
	 *            The file type enum of the file to send to the bluetooth device
	 */
	public BTObjectPushService(BluetoothPane BTPane, String replyString,
			String connURL, File file, FileType type) {
		this.BTPane = BTPane;
		this.replyString = replyString;
		this.connURL = connURL;
		this.file = file;
		this.FILE_TYPE = type.getType();
		
		// create new object push timeout timer
		timeoutTimer = new Timer(30000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param evt
			 *            action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {
				enableBTPaneMenuButtons();
				setBTPaneStatusLabel("File Send Operation Timed Out");
				timeoutTimer.stop();
			}
		});
	}

	/**
	 * Sends a file to a bluetooth device via the object push bluetooth
	 * implementation. This method also sends a status message of the success of
	 * the file send to the bluetooth device and calls a parents method to
	 * enable buttons which allow further bluetooth file send requests
	 */
	public void sendFile() {
		
		timeoutTimer.start();
		
		try {
			// convert file to bytes
			FileInputStream stream = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			stream.read(data);

			// connect to bluetooth device
			conn = Connector.open(connURL);

			ClientSession cs = (ClientSession) conn;
			HeaderSet hs = cs.createHeaderSet();

			cs.connect(hs);

			// set object push attributes
			hs.setHeader(HeaderSet.NAME, file.getName());
			hs.setHeader(HeaderSet.TYPE, FILE_TYPE);
			hs.setHeader(HeaderSet.LENGTH, new Long(data.length));

			Operation putOperation = cs.put(hs);

			// send bytes to bluetooth device
			OutputStream out = putOperation.openOutputStream();
			out.write(data);

			// close connections and streams
			out.close();
			putOperation.close();
			cs.disconnect(null);
			conn.close();

			// notify parent panel of successful file send
			System.out.println(replyString);
			setBTPaneStatusLabel(replyString);
			enableBTPaneMenuButtons();
			
			timeoutTimer.stop();

		} catch (Exception e) {
			timeoutTimer.stop();
			enableBTPaneMenuButtons();
			setBTPaneStatusLabel("Failure Sending File");
			System.err.println("BT Services: Failure Sending File");
		}
	}
	
	/**
	 * Enables the bluetooth menu buttons in the parent class. It's
	 * also in a method so to be accessed by the action performed method
	 * in the timer in this class
	 */
	public void enableBTPaneMenuButtons() {
		BTPane.enableBTMenuButtons();	
	}
	
	/**
	 * Sets the parent bluetooth panel's status text. It's
	 * also in a method so to be accessed by the action performed method
	 * in the timer in this class
	 * @param theStatus The status message in which to set the status label
	 */
	public void setBTPaneStatusLabel(String theStatus) {
		BTPane.statusLabel.setText(theStatus);
	}

	/**
	 * Used when this class is used as a separate thread, as this class
	 * implements runnable
	 */
	public void run() {
		sendFile();
	}
}
