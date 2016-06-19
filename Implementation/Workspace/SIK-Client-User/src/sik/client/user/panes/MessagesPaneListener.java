package sik.client.user.panes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import sik.client.user.ActionClips;

/**
 * Class which creates a new instance of the student information kiosk GUI and
 * runs (displays) it
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MessagesPaneListener implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected MessagesPane messagesPanel;
	protected int messageNumber;

	/**
	 * Class constructor
	 * 
	 * @param messagesPanel
	 *            The parent class which called this class
	 */
	public MessagesPaneListener(MessagesPane messagesPanel) {
		this.messagesPanel = messagesPanel;
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {

		messagesPanel.homePanel.durationTimer.restart();
		ActionClips.playClick();

		if (e.getSource() == messagesPanel.nextNavButton) {
			nextPointer();
		}
		if (e.getSource() == messagesPanel.previousNavButton) {
			previousPointer();
		}
		if (e.getSource() == messagesPanel.firstNavButton) {
			firstPointer();
		}
		if (e.getSource() == messagesPanel.lastNavButton) {
			lastPointer();
		}
	}

	/**
	 * Set panel to point to the next element in messages array list
	 */
	private void nextPointer() {
		messagesPanel.arrayPointer++;
		setNavButton();
		setRecord();
	}

	/**
	 * Set panel to point to the previous element in messages array list
	 */
	private void previousPointer() {
		messagesPanel.arrayPointer--;
		setNavButton();
		setRecord();
	}

	/**
	 * Set panel to point to the first element in messages array list
	 */
	private void firstPointer() {
		messagesPanel.arrayPointer = 0;
		setNavButton();
		setRecord();
	}

	/**
	 * Set panel to point to the last element in messages array list
	 */
	private void lastPointer() {
		messagesPanel.arrayPointer = messagesPanel.messagesArrayList.size() - 1;
		setNavButton();
		setRecord();
	}

	/**
	 * Sets panel navigation control buttons to appropriate style based on mode
	 * of GUI and messages array pointer
	 */
	public void setNavButton() {
		if (messagesPanel.messagesArrayList.isEmpty()) {
			messagesPanel.setIsEmpty(true);
			messagesPanel.messageNumberTextField.setText("0 / 0");
		} else {

			messageNumber = messagesPanel.arrayPointer + 1;

			messagesPanel.messageNumberTextField.setText("" + messageNumber
					+ " / " + messagesPanel.messagesArrayList.size());

			if ((messagesPanel.messagesArrayList.size() > 1)
					&& (messagesPanel.arrayPointer == 0)) {
				messagesPanel.setIsFirst(true);
				messagesPanel.setIsLast(false);
			} else if (messagesPanel.messagesArrayList.size() == 1) {
				messagesPanel.setIsFirst(true);
				messagesPanel.setIsLast(true);
			} else if ((messagesPanel.messagesArrayList.size() == messagesPanel.arrayPointer + 1)
					&& (messagesPanel.messagesArrayList.size() > 0)) {
				messagesPanel.setIsLast(true);
				messagesPanel.setIsFirst(false);
			} else if ((messagesPanel.messagesArrayList.size() > 1)
					&& (messagesPanel.arrayPointer != messagesPanel.messagesArrayList
							.size())) {
				messagesPanel.setIsLast(false);
				messagesPanel.setIsFirst(false);
			}
		}
	}

	/**
	 * Sets the fields of the messages panel according to the student record
	 * object details
	 */
	private void setRecord() {
		if (messagesPanel.messagesArrayList.isEmpty()) {
			messagesPanel.theMessage.setText("");
			messagesPanel.setIsEmpty(true);
		} else {
			messagesPanel.theMessage.setText(messagesPanel.messagesArrayList
					.get(messagesPanel.arrayPointer).toString());
		}
	}
}
