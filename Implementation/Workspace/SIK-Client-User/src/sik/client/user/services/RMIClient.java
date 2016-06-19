package sik.client.user.services;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import sik.common.RMIServer;
import sik.common.StudentRecord;

/**
 * Class to act as an RMI Client which uses an RMI Server Interface to connect
 * to a remote RMI server implementation and gain the ability to call remote
 * methods as the the using data will be stored on a remote server
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 * 
 */
public class RMIClient implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This remote server interface object
	 */
	public RMIServer rmiServer;

	/**
	 * Class to perform text file IO operations, in which the text file contains
	 * attribute values and settings to be used by this and other classes
	 */
	private ClientIOServices clientOptions;

	/**
	 * the bindName URL to where the RMI server code base in located
	 */
	private String bindName;

	/**
	 * Student record object, which contains details of the students using the
	 * user client
	 */
	public StudentRecord studentRecord;

	/**
	 * String used by a parent class to determine whether or not a student ID
	 * exists in the RMI server's student records store
	 * 
	 */
	private String operationMessage = "";

	/**
	 * Class constructor
	 * 
	 */
	public RMIClient() {
		studentRecord = new StudentRecord();
	}

	/**
	 * Returns the computer name from which this client is running
	 * 
	 * @return computerName The name of the computer from which this client is
	 *         running
	 * 
	 */
	public String getComputerName() {
		String computerName = "Unknown";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}

		return computerName;
	}

	/**
	 * Makes a connection to the RMI server
	 * 
	 * @throws RemoteException
	 *             when any remote connectivity problems occur
	 */
	public void connectToServer() throws RemoteException,
			MalformedURLException, NotBoundException {

		// create a new security manager if one isn't set (generally most of the
		// time)
		if (System.getSecurityManager() == null) {
			// set up the security policy programmatically so a user doesn't
			// have to and make it easier to export the project to a runnable
			// jar file
			// that is set up and ready to make RMI connections
			System.setProperty("java.security.policy",
					"policy/AllSecurity.policy");
			System.setSecurityManager(new RMISecurityManager());
		}

		clientOptions = new ClientIOServices();
		bindName = clientOptions.getRMIAddress();

		// Check bindings - A lot of errors can occur here, could do with
		// quickly knowing why
		System.out.println("Checking server binding... "
				+ Naming.lookup(bindName));
		System.out.println("Checking server binding... "
				+ LocateRegistry.getRegistry("RMIServer"));

		// Create instance of RMI server interface from the server's bound
		// object
		// which allows us to invoke remote methods
		rmiServer = (RMIServer) Naming.lookup(bindName);

	}

	/**
	 * Invokes the RMIServer to send a student record object (it 'gets' a remote
	 * student record object)
	 * 
	 * @throws RemoteException
	 *             when any remote connectivity problems occur
	 */
	public void loadStudentRecord(String studentID, boolean isRFIDInput)
			throws RemoteException, ConnectException {

		operationMessage = "NotYetNull";

		if (isRFIDInput) {
			studentRecord = rmiServer.getStudentRecord(studentID,
					getComputerName(), true);

		} else {
			studentRecord = rmiServer.getStudentRecord(studentID,
					getComputerName(), false);
		}

		if (studentRecord.getFirstName().equalsIgnoreCase("nullArrayList")) {
			operationMessage = "nullPointer";
		}
	}

	/**
	 * Returns the operation message string
	 * 
	 * @return operationMessage The Operation Message
	 * @see RMIClient#operationMessage
	 */
	public String getOperationMessage() {
		return operationMessage;
	}

	/**
	 * Sets the operation message string
	 * 
	 * @param opMessage
	 *            The value in which to set the operationMessage string to
	 * 
	 * @see RMIClient#operationMessage
	 */
	public void setOperationMessage(String opMessage) {
		this.operationMessage = opMessage;
	}
}
