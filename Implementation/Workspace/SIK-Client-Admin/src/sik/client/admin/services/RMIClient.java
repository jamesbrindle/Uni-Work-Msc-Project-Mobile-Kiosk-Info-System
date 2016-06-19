package sik.client.admin.services;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import sik.common.RMIServer;
import sik.common.Store;

/**
 * Class to act as an RMI Client which uses an RMI Server Interface to connect
 * to a remote RMI server implementation and gain the ability to call remote
 * methods as the the using data will be stored on a remote server
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 * 
 */
public class RMIClient implements Serializable, Remote {

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
	private ClientIOServices clientServices;

	/**
	 * the bindName URL to where the RMI server code base in located
	 */
	private String bindName;

	/**
	 * Returns this computer name
	 * 
	 * @return computerName The computer name
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

		clientServices = new ClientIOServices();
		bindName = clientServices.getRMIAddress();

		// Check bindings - A lot of errors can occur here, could do with
		// quickly knowing why
		System.out.println("Checking server binding... "
				+ Naming.lookup(bindName));
		System.out.println("Checking server binding... "
				+ LocateRegistry.getRegistry("RMIServer"));
		System.out.println("Looking up " + bindName);

		// Create instance of RMI server interface from the server's bound
		// object
		// which allows us to invoke remote methods
		rmiServer = (RMIServer) Naming.lookup(bindName);
	}

	/**
	 * Invokes the RMIServer to send a student record store (it 'gets' a remote
	 * student record store)
	 * 
	 * @param silent
	 *            Defines which RMIServer method to invoke. Silent or not.
	 *            Silent prevents the recording of information which is
	 *            sometimes preferred depending on the operation
	 * @return Store The student records store
	 * @throws RemoteException
	 *             when any remote connectivity problems occur
	 */
	public Store loadStoreFromRMI(boolean silent) throws RemoteException,
			ConnectException {

		// The RMI server includes a server log and displays transaction
		// information, sometimes we don't want it to show this information
		// so it includes two methods
		if (silent) {
			return rmiServer.getStore_silent();
		} else {
			return rmiServer.getStore(getComputerName());
		}
	}

	/**
	 * Saves a student records store to the already connected RMI server
	 * 
	 * @param store
	 *            The student records store to save to remote location
	 * @throws RemoteException
	 *             when any remote connectivity problems occur
	 */
	public void saveStoreToRMI(Store store) throws RemoteException,
			ConnectException {
		rmiServer.saveStore(store, getComputerName());
	}
}
