package sik.client.admin;

/**
 * Used to handle the complexity of the navigation control buttons of the
 * Records GUI, which generally involves setting specific buttons enabled or
 * disabled depending on the mode and record pointer of the parent GUI
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class RecordsGUIButtonManager {

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private RecordsGUI recordsGUI;

	/**
	 * Class constructor
	 * 
	 * @param recordsGUI
	 *            The parent class which called this class
	 */
	public RecordsGUIButtonManager(RecordsGUI recordsGUI) {
		this.recordsGUI = recordsGUI;
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		if (b == false) {
			recordsGUI.firstNavButton.setEnabled(false);
			recordsGUI.lastNavButton.setEnabled(false);
			recordsGUI.nextNavButton.setEnabled(false);
			recordsGUI.previousNavButton.setEnabled(false);
			recordsGUI.newRecordNavButton.setEnabled(false);
			recordsGUI.saveNavButton.setEnabled(false);
			recordsGUI.editNavButton.setEnabled(false);
			recordsGUI.removeNavButton.setEnabled(false);
			recordsGUI.searchBarSearchButton.setEnabled(false);
			recordsGUI.editImageButton.setEnabled(false);
			recordsGUI.editMessages.setEnabled(false);
			recordsGUI.editAssignmentResults.setEnabled(false);
			recordsGUI.editTimetable.setEnabled(false);
			recordsGUI.RFIDTagAssignButton.setEnabled(false);

		} else {
			recordsGUI.firstNavButton.setEnabled(true);
			recordsGUI.lastNavButton.setEnabled(true);
			recordsGUI.nextNavButton.setEnabled(true);
			recordsGUI.previousNavButton.setEnabled(true);
			recordsGUI.newRecordNavButton.setEnabled(true);
			recordsGUI.editNavButton.setEnabled(true);
			recordsGUI.removeNavButton.setEnabled(true);
			recordsGUI.searchBarSearchButton.setEnabled(true);
			recordsGUI.editImageButton.setEnabled(true);
			recordsGUI.editMessages.setEnabled(true);
			recordsGUI.editAssignmentResults.setEnabled(true);
			recordsGUI.editTimetable.setEnabled(true);
			recordsGUI.RFIDTagAssignButton.setEnabled(true);
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
			recordsGUI.firstNavButton.setEnabled(true);
			recordsGUI.lastNavButton.setEnabled(true);
			recordsGUI.nextNavButton.setEnabled(true);
			recordsGUI.previousNavButton.setEnabled(true);
			recordsGUI.newRecordNavButton.setText("New Record");
			recordsGUI.isEditing = false;
			recordsGUI.editNavButton.setEnabled(true);
			recordsGUI.saveNavButton.setEnabled(false);
			recordsGUI.removeNavButton.setEnabled(true);
			recordsGUI.searchBarSearchButton.setEnabled(true);
			recordsGUI.editMessages.setEnabled(true);
			recordsGUI.editAssignmentResults.setEnabled(true);
			recordsGUI.editTimetable.setEnabled(true);
			recordsGUI.editImageButton.setEnabled(true);
			recordsGUI.RFIDTagAssignButton.setEnabled(true);
		} else {
			recordsGUI.firstNavButton.setEnabled(false);
			recordsGUI.lastNavButton.setEnabled(false);
			recordsGUI.nextNavButton.setEnabled(false);
			recordsGUI.previousNavButton.setEnabled(false);
			recordsGUI.newRecordNavButton.setText("Cancel");
			recordsGUI.isEditing = true;
			recordsGUI.editNavButton.setEnabled(false);
			recordsGUI.saveNavButton.setEnabled(true);
			recordsGUI.removeNavButton.setEnabled(false);
			recordsGUI.searchBarSearchButton.setEnabled(false);
			recordsGUI.editMessages.setEnabled(false);
			recordsGUI.editAssignmentResults.setEnabled(false);
			recordsGUI.editTimetable.setEnabled(false);
			recordsGUI.editImageButton.setEnabled(false);
			recordsGUI.RFIDTagAssignButton.setEnabled(false);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the first record in the student records
	 * array list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsFirst(boolean b) {
		if (b == true) {
			recordsGUI.firstNavButton.setEnabled(false);
			recordsGUI.previousNavButton.setEnabled(false);
		} else {
			recordsGUI.firstNavButton.setEnabled(true);
			recordsGUI.previousNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the GUI is pointing at the last record in the student records
	 * array list
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsLast(boolean b) {
		if (b == true) {
			recordsGUI.lastNavButton.setEnabled(false);
			recordsGUI.nextNavButton.setEnabled(false);
		} else {
			recordsGUI.lastNavButton.setEnabled(true);
			recordsGUI.nextNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation controls (buttons) to enabled or disabled on the
	 * basis that the student records array list is empty
	 * 
	 * @param b
	 *            whether to set the buttons enabled or disabled
	 */
	public void setIsEmpty(boolean b) {
		if (b == true) {
			recordsGUI.removeNavButton.setEnabled(false);
			recordsGUI.firstNavButton.setEnabled(false);
			recordsGUI.previousNavButton.setEnabled(false);
			recordsGUI.lastNavButton.setEnabled(false);
			recordsGUI.nextNavButton.setEnabled(false);
			recordsGUI.editNavButton.setEnabled(false);
			recordsGUI.newRecordNavButton.setEnabled(true);
		} else {

			recordsGUI.removeNavButton.setEnabled(true);
			recordsGUI.editNavButton.setEnabled(true);
		}
	}
}
