package sik.client.user.panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import sik.client.user.FontStyles;
import sik.client.user.panels.HomePanel;

/**
 * Panel which is displayed as a content inside the home panel. As the home
 * panel displays a number of different complex panels corresponding to the
 * selected category this has been given the name 'pane' rather than 'panel'.
 * 
 * This particular pane is is displayed when the 'Messages' button is pressed on
 * the home panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MessagesPane extends JPanel {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected HomePanel homePanel;

	/**
	 * Class to handle the action events of this panel
	 */
	private MessagesPaneListener messagesPL;

	/**
	 * Class to handle the button styling events of this panel
	 */
	private MessagesPaneButtonManager messagesPBM;

	/**
	 * Contains a list of messages from the student record
	 */
	protected ArrayList<String> messagesArrayList;

	/**
	 * Used to point to a particular element in the messages array list
	 */
	protected int arrayPointer = 0;

	// internal panels
	private JPanel messageArea, navigation;

	// layouts
	private GridBagConstraints messageAreaLayout, navigationLayout;

	// navigation properties
	protected JButton firstNavButton, lastNavButton, nextNavButton,
			previousNavButton, newRecordNavButton;

	// messages properties
	private JLabel titleLabel, messageNumberLabel;
	protected JTextArea theMessage;
	protected JTextField messageNumberTextField;
	private JScrollPane theMessageScrollPane;

	/**
	 * Class constructor
	 * 
	 * @param homePanel
	 *            The parent class which called this class
	 */
	public MessagesPane(HomePanel homePanel) {
		this.homePanel = homePanel;
		messagesArrayList = new ArrayList<String>();

		messagesPBM = new MessagesPaneButtonManager(this);
		messagesPL = new MessagesPaneListener(this);

		setPreferredSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(650, 400));
		setMinimumSize(new Dimension(650, 400));

		setOpaque(false);

		messageArea = new JPanel(new GridBagLayout());
		messageArea.setOpaque(false);
		messageAreaLayout = new GridBagConstraints();

		navigation = new JPanel(new GridBagLayout());
		navigation.setOpaque(false);
		navigationLayout = new GridBagConstraints();

		loadNavigationElements();
		loadMessagesArea();

		add(messageArea, BorderLayout.CENTER);
		add(navigation, BorderLayout.SOUTH);

		messagesPL.setNavButton();
	}

	/**
	 * Prepares the messages area panel elements
	 */
	private void loadMessagesArea() {
		messageAreaLayout.insets = new Insets(0, 0, 25, 0);

		titleLabel = new JLabel("Messages");
		titleLabel.setFont(FontStyles.extraLargeFont);
		titleLabel.setForeground(new Color(255, 255, 255));
		messageAreaLayout.gridy = 0;
		messageAreaLayout.gridx = 0;
		messageAreaLayout.anchor = GridBagConstraints.NORTHWEST;
		messageArea.add(titleLabel, messageAreaLayout);

		messageAreaLayout.insets = new Insets(0, 0, 10, 12);

		messageNumberLabel = new JLabel("Message Number");
		messageNumberLabel.setFont(FontStyles.largeFont);
		messageNumberLabel.setForeground(new Color(255, 255, 255));
		messageAreaLayout.gridy = 1;
		messageAreaLayout.gridx = 0;
		messageArea.add(messageNumberLabel, messageAreaLayout);

		messageAreaLayout.insets = new Insets(0, 0, 4, 0);

		messageNumberTextField = new JTextField(10);
		messageNumberTextField.setFont(FontStyles.largeFont);
		messageNumberTextField.setEditable(false);
		Border border = LineBorder.createGrayLineBorder();
		messageNumberTextField.setBorder(border);
		messageAreaLayout.gridy = 1;
		messageAreaLayout.gridx = 1;
		messageArea.add(messageNumberTextField, messageAreaLayout);

		theMessage = new JTextArea(9, 38);
		theMessage.setFont(FontStyles.largeFont);
		theMessage.setEditable(false);
		theMessageScrollPane = new JScrollPane(theMessage);
		messageAreaLayout.gridy = 2;
		messageAreaLayout.gridx = 0;
		messageAreaLayout.gridwidth = 2;
		messageArea.add(theMessageScrollPane, messageAreaLayout);
		
		// if there are no messages
		if (homePanel.mainGUI.rmiClient.studentRecord.getMessages() == null) {
			messageNumberTextField.setText("0 / 0");
		} else {
			messagesArrayList = homePanel.mainGUI.rmiClient.studentRecord
					.getMessages();
			messageNumberTextField.setText("1 / " + messagesArrayList.size());
			if (messagesArrayList.isEmpty()) {
				theMessage.setText("");
			} else {
				theMessage.setText(messagesArrayList.get(0).toString());
			}
		}
	}

	/**
	 * Prepares the navigation control buttons area panel elements
	 */
	private void loadNavigationElements() {
		navigationLayout.insets = new Insets(10, 2, 0, 2);

		navigationLayout.gridwidth = 1;
		firstNavButton = new JButton("<<");
		firstNavButton.setFont(FontStyles.largeFont);
		firstNavButton.addActionListener(messagesPL);
		navigationLayout.gridx = 0;
		navigationLayout.gridy = 0;
		navigation.add(firstNavButton, navigationLayout);

		previousNavButton = new JButton("<");
		previousNavButton.setFont(FontStyles.largeFont);
		previousNavButton.addActionListener(messagesPL);
		navigationLayout.gridx = 1;
		navigationLayout.gridy = 0;
		navigation.add(previousNavButton, navigationLayout);

		nextNavButton = new JButton(">");
		nextNavButton.setFont(FontStyles.largeFont);
		nextNavButton.addActionListener(messagesPL);
		navigationLayout.gridx = 2;
		navigationLayout.gridy = 0;
		navigation.add(nextNavButton, navigationLayout);

		lastNavButton = new JButton(">>");
		lastNavButton.setFont(FontStyles.largeFont);
		lastNavButton.addActionListener(messagesPL);
		navigationLayout.gridx = 3;
		navigationLayout.gridy = 0;
		navigation.add(lastNavButton, navigationLayout);

	}

	/**
	 * Sets the navigation control buttons enabled or disabled
	 * 
	 * @param b
	 *            The boolean value which determines of the buttons are enabled
	 *            or disabled
	 */
	public void setNavButtonsEnabled(boolean b) {
		messagesPBM.setNavButtonsEnabled(b);
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
		messagesPBM.setIsLast(b);
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
		messagesPBM.setIsFirst(b);
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
		messagesPBM.setIsEmpty(b);
	}
}
