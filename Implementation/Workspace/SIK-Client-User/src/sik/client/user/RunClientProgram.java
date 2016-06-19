package sik.client.user;

import java.io.Serializable;
import java.rmi.RemoteException;

import com.phidgets.PhidgetException;

/**
 * Class which creates a new instance of the student information kiosk GUI and
 * runs (displays) it
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class RunClientProgram implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The main GUI instance
	 */
	protected static MainDisplayGUI mainGUI;

	public static void main(String[] args) throws RemoteException,
			PhidgetException {
		mainGUI = new MainDisplayGUI();
		mainGUI.run();
	}
}
