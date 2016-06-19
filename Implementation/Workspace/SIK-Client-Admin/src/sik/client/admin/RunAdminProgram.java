package sik.client.admin;

/**
 * Creates an instance of the Records GUI and runs it
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class RunAdminProgram {

	/**
	 * The main GUI
	 */
	protected static RecordsGUI recordsGUI;

	public static void main(String[] args) {
		recordsGUI = new RecordsGUI();
		recordsGUI.run();
	}
}