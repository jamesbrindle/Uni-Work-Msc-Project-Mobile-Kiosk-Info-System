package sik.client.admin;

/**
 * Used to handle the complexity of the navigation control buttons of the
 * EditMessages GUI, which generally involves setting specific buttons enabled
 * or disabled depending on the mode and record pointer of the parent GUI
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class EditMessagesGUIButtonManager {

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private EditMessagesGUI editMessagesGUI;

	/**
	 * Class constructor
	 * 
	 * @param editMessagesGUI
	 *            The supplied parent class was called this class
	 * 
	 */
	public EditMessagesGUIButtonManager(EditMessagesGUI editMessagesGUI) {
		this.editMessagesGUI = editMessagesGUI;
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		if (b == false) {
			editMessagesGUI.firstNavButton.setEnabled(false);
			editMessagesGUI.lastNavButton.setEnabled(false);
			editMessagesGUI.nextNavButton.setEnabled(false);
			editMessagesGUI.previousNavButton.setEnabled(false);
			editMessagesGUI.removeNavButton.setEnabled(false);

		} else {
			editMessagesGUI.firstNavButton.setEnabled(true);
			editMessagesGUI.lastNavButton.setEnabled(true);
			editMessagesGUI.nextNavButton.setEnabled(true);
			editMessagesGUI.previousNavButton.setEnabled(true);
			editMessagesGUI.newRecordNavButton.setEnabled(true);
			editMessagesGUI.removeNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is in 'edit record' mode
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setEditNavButtonMode(boolean b) {
		if (b == false) {
			editMessagesGUI.newRecordNavButton.setText("New Message");
			editMessagesGUI.saveNavButton.setEnabled(false);
			editMessagesGUI.editNavButton.setEnabled(true);
			editMessagesGUI.removeNavButton.setEnabled(true);

		} else {
			editMessagesGUI.firstNavButton.setEnabled(false);
			editMessagesGUI.lastNavButton.setEnabled(false);
			editMessagesGUI.nextNavButton.setEnabled(false);
			editMessagesGUI.previousNavButton.setEnabled(false);
			editMessagesGUI.newRecordNavButton.setText("Cancel");
			editMessagesGUI.saveNavButton.setEnabled(true);
			editMessagesGUI.editNavButton.setEnabled(false);
			editMessagesGUI.removeNavButton.setEnabled(false);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the first record in the messages array
	 * list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsFirst(boolean b) {
		if (b == true) {
			editMessagesGUI.firstNavButton.setEnabled(false);
			editMessagesGUI.previousNavButton.setEnabled(false);
		} else {
			editMessagesGUI.firstNavButton.setEnabled(true);
			editMessagesGUI.previousNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the last record in the messages array
	 * list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsLast(boolean b) {
		if (b == true) {
			editMessagesGUI.lastNavButton.setEnabled(false);
			editMessagesGUI.nextNavButton.setEnabled(false);
		} else {
			editMessagesGUI.lastNavButton.setEnabled(true);
			editMessagesGUI.nextNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the messages array list is empty
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsEmpty(boolean b) {
		if (b == true) {
			editMessagesGUI.removeNavButton.setEnabled(false);
			editMessagesGUI.firstNavButton.setEnabled(false);
			editMessagesGUI.previousNavButton.setEnabled(false);
			editMessagesGUI.lastNavButton.setEnabled(false);
			editMessagesGUI.nextNavButton.setEnabled(false);
			editMessagesGUI.newRecordNavButton.setEnabled(true);
			editMessagesGUI.editNavButton.setEnabled(false);
		} else {

			editMessagesGUI.removeNavButton.setEnabled(true);
		}
	}
}
