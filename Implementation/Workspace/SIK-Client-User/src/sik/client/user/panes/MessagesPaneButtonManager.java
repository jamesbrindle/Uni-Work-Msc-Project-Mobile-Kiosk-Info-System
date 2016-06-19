package sik.client.user.panes;

/**
 * Class which creates a new instance of the student information kiosk GUI and
 * runs (displays) it
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MessagesPaneButtonManager {

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private MessagesPane messagesPanel;

	/**
	 * Class constructor
	 * 
	 * @param messagesPanel
	 *            The parent class which called this class
	 */
	public MessagesPaneButtonManager(MessagesPane messagesPanel) {
		this.messagesPanel = messagesPanel;
	}

	/**
	 * Sets the navigation control buttons enabled or disabled
	 * 
	 * @param b
	 *            The boolean value which determines of the buttons are enabled
	 *            or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		if (b == false) {
			messagesPanel.firstNavButton.setEnabled(false);
			messagesPanel.lastNavButton.setEnabled(false);
			messagesPanel.nextNavButton.setEnabled(false);
			messagesPanel.previousNavButton.setEnabled(false);

		} else {
			messagesPanel.firstNavButton.setEnabled(true);
			messagesPanel.lastNavButton.setEnabled(true);
			messagesPanel.nextNavButton.setEnabled(true);
			messagesPanel.previousNavButton.setEnabled(true);
			messagesPanel.newRecordNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation control buttons to 'first' mode, which enables and
	 * disables the corresponding buttons
	 * 
	 * @param b
	 *            The boolean value to determine what mode to set the navigation
	 *            buttons to
	 */
	public void setIsFirst(boolean b) {
		if (b == true) {
			messagesPanel.firstNavButton.setEnabled(false);
			messagesPanel.previousNavButton.setEnabled(false);
		} else {
			messagesPanel.firstNavButton.setEnabled(true);
			messagesPanel.previousNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation control buttons to 'last' mode, which enables and
	 * disables the corresponding buttons
	 * 
	 * @param b
	 *            The boolean value to determine what mode to set the navigation
	 *            buttons to
	 */
	public void setIsLast(boolean b) {
		if (b == true) {
			messagesPanel.lastNavButton.setEnabled(false);
			messagesPanel.nextNavButton.setEnabled(false);
		} else {
			messagesPanel.lastNavButton.setEnabled(true);
			messagesPanel.nextNavButton.setEnabled(true);
		}
	}

	/**
	 * Sets the navigation control buttons to 'empty' mode, which enables and
	 * disables the corresponding buttons
	 * 
	 * @param b
	 *            The boolean value to determine what mode to set the navigation
	 *            buttons to
	 */
	public void setIsEmpty(boolean b) {
		if (b == true) {
			messagesPanel.firstNavButton.setEnabled(false);
			messagesPanel.previousNavButton.setEnabled(false);
			messagesPanel.lastNavButton.setEnabled(false);
			messagesPanel.nextNavButton.setEnabled(false);
			messagesPanel.newRecordNavButton.setEnabled(true);
		}
	}
}
