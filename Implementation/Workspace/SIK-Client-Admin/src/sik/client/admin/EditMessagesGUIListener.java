package sik.client.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * A class solely for handling the events made in the Edit Messages GUI
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class EditMessagesGUIListener implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private EditMessagesGUI editMessagesGUI;

	// GUI 'Mode' properties
	protected boolean isNewRecord = false;
	protected boolean isEditing = false;
	protected boolean isCreating = false;

	private int messageNumber;

	/**
	 * Class constructor
	 * 
	 * @param editMessagesGUI
	 *            The supplied parent class which called with class
	 */
	public EditMessagesGUIListener(EditMessagesGUI editMessagesGUI) {
		this.editMessagesGUI = editMessagesGUI;
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == editMessagesGUI.menuItem3) {
			editMessagesGUI.frame.dispose();
		}
		if (e.getSource() == editMessagesGUI.nextNavButton) {
			nextPointer();
		}
		if (e.getSource() == editMessagesGUI.previousNavButton) {
			previousPointer();
		}
		if (e.getSource() == editMessagesGUI.firstNavButton) {
			firstPointer();
		}
		if (e.getSource() == editMessagesGUI.lastNavButton) {
			lastPointer();
		}
		if (e.getSource() == editMessagesGUI.removeNavButton) {
			remove();
		}
		if (e.getSource() == editMessagesGUI.editNavButton) {
			setToEditMode();
		}
		if (e.getSource() == editMessagesGUI.newRecordNavButton) {
			setNewMessageMode();
		}
		if (e.getSource() == editMessagesGUI.saveNavButton) {
			save();
		}
	}

	/**
	 * Removes a message from the editMessagesGUI class's messages array list
	 */
	private void remove() {
		if (editMessagesGUI.arrayPointer == editMessagesGUI.messagesArrayList
				.size() - 1) {
			if (editMessagesGUI.arrayPointer == 0) {
				editMessagesGUI.messagesArrayList
						.remove(editMessagesGUI.arrayPointer);
				editMessagesGUI.messageNumberTextField.setText("0 / 0");
			} else {
				editMessagesGUI.messagesArrayList
						.remove(editMessagesGUI.arrayPointer);
				editMessagesGUI.arrayPointer--;
			}
		} else {
			editMessagesGUI.messagesArrayList
					.remove(editMessagesGUI.arrayPointer);
		}
		editMessagesGUI.recordsGL.store.currentRecord().setMessages(
				editMessagesGUI.messagesArrayList);
		editMessagesGUI.recordsGL.saveFile();
		setNavButtons();
		setRecord();
	}

	/**
	 * Sets the EditMessagesGUI to be in 'edit' mode
	 */
	private void setToEditMode() {
		isEditing = true;
		editMessagesGUI.setEditNavButtonMode(true);
		editMessagesGUI.theMessage.setEditable(true);
	}

	/**
	 * Sets the EditMessagesGUI to be in 'new' message mode
	 */
	private void setNewMessageMode() {
		if (isCreating || isEditing) {
			isCreating = false;
			isEditing = false;
			setNavButtons(); // may need to alter navigation control style
			setRecord(); // adds another element to messages array list and
							// points to it
			editMessagesGUI.setEditNavButtonMode(false);
			editMessagesGUI.theMessage.setEditable(false);

			if (editMessagesGUI.messagesArrayList.isEmpty()) {
				editMessagesGUI.editNavButton.setEnabled(false);
				editMessagesGUI.removeNavButton.setEnabled(false);
			}
		} else {
			isCreating = true;
			editMessagesGUI.theMessage.setText("");
			editMessagesGUI.setEditNavButtonMode(true);
			editMessagesGUI.theMessage.setEditable(true);
		}
	}

	/**
	 * Saves a message to the editMessagesGUI class's messages array list
	 */
	private void save() {
		if (isEditing) {
			isEditing = false;
			editMessagesGUI.messagesArrayList.set(editMessagesGUI.arrayPointer,
					editMessagesGUI.theMessage.getText());
		} else if (isCreating) {
			isCreating = false;
			editMessagesGUI.messagesArrayList.add(editMessagesGUI.theMessage
					.getText());
			editMessagesGUI.arrayPointer = editMessagesGUI.messagesArrayList
					.size() - 1;

		}
		// save the changes to the student record object via store object in
		// the records GUI listener class
		editMessagesGUI.recordsGL.store.currentRecord().setMessages(
				editMessagesGUI.messagesArrayList);
		editMessagesGUI.recordsGL.saveFile();
		editMessagesGUI.setEditNavButtonMode(false);
		editMessagesGUI.theMessage.setEditable(false);
		setNavButtons();
		setRecord();
	}

	/**
	 * Set GUI to point to the next element in messages array list
	 */
	private void nextPointer() {
		editMessagesGUI.arrayPointer++;
		setNavButtons();
		setRecord();
	}

	/**
	 * Set GUI to point to the previous element in messages array list
	 */
	private void previousPointer() {
		editMessagesGUI.arrayPointer--;
		setNavButtons();
		setRecord();
	}

	/**
	 * Set GUI to point to the first element in messages array list
	 */
	private void firstPointer() {
		editMessagesGUI.arrayPointer = 0;
		setNavButtons();
		setRecord();
	}

	/**
	 * Set GUI to point to the last element in messages array list
	 */
	private void lastPointer() {
		editMessagesGUI.arrayPointer = editMessagesGUI.messagesArrayList.size() - 1;
		setNavButtons();
		setRecord();
	}

	/**
	 * Sets GUI navigation control buttons to appropriate style based on mode of
	 * GUI and messages array pointer
	 */
	public void setNavButtons() {
		if (editMessagesGUI.messagesArrayList.isEmpty()) {
			editMessagesGUI.setIsEmpty(true);
			editMessagesGUI.messageNumberTextField.setText("0 / 0");
		} else {
			messageNumber = editMessagesGUI.arrayPointer + 1;

			editMessagesGUI.messageNumberTextField.setText("" + messageNumber
					+ " / " + editMessagesGUI.messagesArrayList.size());

			if ((editMessagesGUI.messagesArrayList.size() > 1)
					&& (editMessagesGUI.arrayPointer == 0)) {
				editMessagesGUI.setIsFirst(true);
				editMessagesGUI.setIsLast(false);
			} else if (editMessagesGUI.messagesArrayList.size() == 1) {
				editMessagesGUI.setIsFirst(true);
				editMessagesGUI.setIsLast(true);
			} else if ((editMessagesGUI.messagesArrayList.size() == editMessagesGUI.arrayPointer + 1)
					&& (editMessagesGUI.messagesArrayList.size() > 0)) {
				editMessagesGUI.setIsLast(true);
				editMessagesGUI.setIsFirst(false);
			} else if ((editMessagesGUI.messagesArrayList.size() > 1)
					&& (editMessagesGUI.arrayPointer != editMessagesGUI.messagesArrayList
							.size())) {
				editMessagesGUI.setIsLast(false);
				editMessagesGUI.setIsFirst(false);
			}
		}
	}

	/**
	 * Sets the fields of the messages GUI according to the student record
	 * object details
	 */
	private void setRecord() {
		if (editMessagesGUI.messagesArrayList.isEmpty()) {
			editMessagesGUI.theMessage.setText("");
			editMessagesGUI.setIsEmpty(true);
		} else {
			editMessagesGUI.theMessage
					.setText(editMessagesGUI.messagesArrayList.get(
							editMessagesGUI.arrayPointer).toString());
		}
	}
}
