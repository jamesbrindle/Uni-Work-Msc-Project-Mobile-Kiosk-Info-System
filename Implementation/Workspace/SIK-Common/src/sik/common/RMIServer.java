package sik.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMI Server interface. It contains the methods that must be implemented
 * when a class implements this RMI server. In this case an interface must be
 * used to allow an RMI client and RMI server implementation to invoke one an
 * other's methods remotely
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 * 
 */
public interface RMIServer extends Remote {

	/**
	 * Used to return a student record object
	 * 
	 * @param uniqueInput
	 *            Defined a unique identifier of a student record which will
	 *            either be an RFID tag serial number or a student ID number
	 * @param computerName
	 *            The computer host name of the machine which invoked this
	 *            method
	 * @param isRFIDInput
	 *            Defines whether the uniqueInput is of type RFID serial or
	 *            student ID number and which corresponding operation to perform
	 * @return studentRecord The student record object in which to return
	 * @throws RemoteException
	 *             When some remote connectivity problems occur
	 */
	public StudentRecord getStudentRecord(String uniqueInput,
			String computerName, boolean isRFIDInput) throws RemoteException;

	/**
	 * Used to return a supplied message to the server logger, which displays
	 * and records any specified message from this RMI server or an RMI client
	 * 
	 * @param theMessage
	 *            The message to be displayed
	 */
	public void dispatchMessage(String theMessage) throws RemoteException;

	/**
	 * Saves a supplied student records store to a local file
	 * 
	 * @param store
	 *            The store object to save
	 * @param computerName
	 *            The name of the computer that invoked this message
	 * @throws RemoteException
	 *             When any remote connectivity problems occur
	 */
	public void saveStore(Store store, String computerName)
			throws RemoteException;

	/**
	 * Returns a student records store, while recording the details of the
	 * computer host which invoked this methods
	 * 
	 * @param computerName
	 *            The name of the computer which invoked this method
	 * @return store The student records store object to return
	 */
	public Store getStore(String computerName) throws RemoteException;

	/**
	 * Returns a student records store. It's similar to the getStore() method
	 * however it does not record any details of the remote method invoker
	 * 
	 * @return store The student records store object in which to return
	 * @see #getStore(String)
	 */
	public Store getStore_silent() throws RemoteException;
}