package sik.client.user.services;

import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import sik.client.user.panes.BluetoothPane;

/**
 * Class which provides methods to discover bluetooth devices in range and a
 * bluetooth devices services
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class BTDiscoveryServices implements DiscoveryListener, Runnable {

	/**
	 * object used for waiting
	 */
	private static Object lock = new Object();

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private BluetoothPane BTPane;

	/**
	 * The URL to connect to a bluetooth device
	 */
	private static String connURL;

	/**
	 * An array list of bluetooth devices that have been discovered
	 */
	private static ArrayList<RemoteDevice> deviceArrayList = new ArrayList<RemoteDevice>();

	/**
	 * The bluetooth discovery agent
	 */
	private DiscoveryAgent agent;

	/**
	 * Boolean to determine and return whether or not a bluetooth device
	 * supports object push
	 */
	private boolean canObjectPush = false;

	/**
	 * Class constructor
	 * 
	 * @param BTPane
	 *            The object in which called an instance of this class
	 */
	public BTDiscoveryServices(BluetoothPane BTPane) {
		this.BTPane = BTPane;
	}

	/**
	 * Returns whether or not, upon discovering a bluetooth device's services,
	 * whether or not that device supports object push, which is required in
	 * this system implementation to send a bluetooth device a file
	 * 
	 * @return canObjectPush The boolean of if the bluetooth device supports
	 *         object push
	 */
	public boolean isObjectPushAvailable() {
		return canObjectPush;
	}

	/**
	 * Returns, upon discovering a device's services, the connection URL for a
	 * selected bluetooth device
	 * 
	 * @return connURL The connection URL for a particular bluetooth device
	 */
	public String getConnURL() {
		return connURL;
	}

	/**
	 * Used to discover bluetooth devices in range. In which it then informs the
	 * parent object (BT panel) of a discovered bluetooth device so it can add
	 * it to its panel. This method also retrieves a bluetooth device's friendly
	 * name to pass to the parent object and notifies the parent object when the
	 * discovery has finished (i.e. no more bluetooth devices are found) and
	 * also informs the parent object of any errors
	 * 
	 * @throws IOException
	 *             If any IO or connectivity problems occur
	 * 
	 */
	public void discoverDevices() throws IOException {
		// this bluetooth host
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("BT Services: This host address: "
				+ localDevice.getBluetoothAddress());

		System.out.println("BT Services: Searching For Devices");
		agent = localDevice.getDiscoveryAgent();
		agent.startInquiry(DiscoveryAgent.GIAC, this);

		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			BTPane.statusLabel.setText("Search Failed");
			System.err.println("BT Services: search Failed");
			e.printStackTrace();
		}

		// if there were not bluetooth enabled devices found
		if (deviceArrayList.size() <= 0) {
			BTPane.statusLabel.setText("No Devices Found");
			System.out.println("BT Services: No Devices Found");
		}

		else {
			System.out.println("BT Services: Found " + deviceArrayList.size()
					+ " Devices");

			boolean isLast = false;
			for (int i = 0; i < deviceArrayList.size(); i++) {

				// if discovery has finished, need something to notify parent
				// panel
				if (i == deviceArrayList.size() - 1) {
					isLast = true;
				}
				BTPane.addBTDevice(
						i,
						// sets name as bluetooth address if device name
						// unavailable
						determineName(
								deviceArrayList.get(i).getFriendlyName(true),
								deviceArrayList.get(i).getBluetoothAddress()),
						isLast);
				System.out.println(" - Device " + deviceArrayList.size()
						+ " Address: " + deviceArrayList.get(i));
			}

			// simple string manipulation for singular and plural amount of
			// devices
			String tmpStr = "";
			if (deviceArrayList.size() > 1) {
				tmpStr = "Device";
			} else {
				tmpStr = "Devices";
			}

			BTPane.statusLabel.setText("Found " + deviceArrayList.size() + " "
					+ tmpStr + ". Please Select Your Device");
		}
	}

	/**
	 * Returns the name of a blueooth device to be passed to the parent object.
	 * This will normally be the bluetooth device's friendly name, however this
	 * is not always available. If that's the case this method constructs a
	 * bluetooth physical address in the correct bluetooth address syntax and
	 * returns that instead
	 * 
	 * @param friendlyName
	 *            The supplied friendly name (if one exists)
	 * @param add
	 *            The address of the bluetooth device
	 * @return The name in which to give the bluetooth device
	 */
	public String determineName(String friendlyName, String add) {
		String determinedString = "";

		// if no name device name available, use device bluetooth address
		// need to manipulate address into proper address syntax
		if (friendlyName.equals("") || (friendlyName == null)) {
			determinedString = determinedString + add.substring(0, 2) + ":";
			determinedString = determinedString + add.substring(2, 4) + ":";
			determinedString = determinedString + add.substring(4, 6) + ":";
			determinedString = determinedString + add.substring(6, 8) + ":";
			determinedString = determinedString + add.substring(8, 10) + ":";
			determinedString = determinedString + add.substring(10, 12);
		} else {
			determinedString = friendlyName;
		}
		return determinedString;
	}

	/**
	 * Discovers the services of a given bluetooth device. More precisely it
	 * discovered whether or not a bluetooth device supports object push, which
	 * is required in this system implementation to be able to send a bluetooth
	 * device a file. This method will then inform the parent object if the push
	 * service exists in the given bluetooth devices or not.
	 * 
	 * @param index
	 *            The index of the device in the deviceArrayList to which to
	 *            attempt to discover services for
	 */
	public void discoverServices(int index) {
		RemoteDevice remoteDevice = deviceArrayList.get(index);
		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID("1105", true);

		connURL = null;

		System.out.println("BT Services: Retrieving Services List...");

		// try to discover bluetooth device services
		try {
			agent.searchServices(null, uuidSet, remoteDevice, this);
		} catch (BluetoothStateException e1) {
			BTPane.statusLabel.setText("Failure Discovering Services");
			System.err.println("BT Services: Failure Discovering Services");
		}

		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// if can't discover appropriate bluetooth services
		if (connURL == null) {
			BTPane.statusLabel
					.setText("The Device Does Not Support Object Push");
			System.out
					.println("BT Services: The Device Does Not Support Object Push");
			canObjectPush = false;
		} else {
			try {
				BTPane.statusLabel.setText("Using Device: "
						+ determineName(deviceArrayList.get(index)
								.getFriendlyName(false),
								deviceArrayList.get(index)
										.getBluetoothAddress()));
			} catch (IOException e) {
			}
			System.out.println("BT Services: The Device: "
					+ deviceArrayList.get(index).getBluetoothAddress()
					+ " Does Support Object Push");
			canObjectPush = true;
		}
	}

	/**
	 * Called when a bluetooth device is discovered. Used for device search.
	 */
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		if (!deviceArrayList.contains(btDevice)) {
			deviceArrayList.add(btDevice);
		}
	}

	/**
	 * Called when a bluetooth service is discovered. Used for service search.
	 */
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		if ((servRecord != null) && (servRecord.length > 0)) {
			connURL = servRecord[0].getConnectionURL(0, false);
		}
		synchronized (lock) {
			lock.notify();
		}
	}

	/**
	 * Called when the service search is over.
	 */
	public void serviceSearchCompleted(int transID, int respCode) {
		synchronized (lock) {
			lock.notify();
		}
	}

	/**
	 * Called when the device search is over.
	 */
	public void inquiryCompleted(int discType) {
		synchronized (lock) {
			lock.notify();
		}
	}

	/**
	 * Used when this class is used as a separate thread, as this class
	 * implements runnable
	 */
	public void run() {
		try {
			discoverDevices();
		} catch (IOException e) {

		}
	}
}