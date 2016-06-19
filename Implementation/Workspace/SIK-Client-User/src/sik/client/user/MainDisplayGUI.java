package sik.client.user;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sik.client.user.dialogs.AdminDialog;
import sik.client.user.dialogs.OptionsDialog;
import sik.client.user.panels.GoodbyePanel;
import sik.client.user.panels.HomePanel;
import sik.client.user.panels.ImagePanel;
import sik.client.user.panels.IntroPanel;
import sik.client.user.panels.LogoutPromptPanel;
import sik.client.user.panels.ManPinEntryPanel;
import sik.client.user.panels.MapPanel;
import sik.client.user.services.ClientIOServices;
import sik.client.user.services.RMIClient;

import com.phidgets.PhidgetException;

/**
 * The main GUI class which is displayed as the student information kiosk user
 * GUI which loads and handles numerous panels to be contained within it
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MainDisplayGUI implements ActionListener, Serializable,
		KeyListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Boolean to define whether or not the GUI is displayed in 'debug mode' or
	 * not. When the GUI is not in debug mode the mouse pointer is hidden and
	 * the frame decorations are removed, as the GUI is to be in full screen
	 * mode in live use. When in debug mode it eases control when developing
	 */
	private boolean debugMode = false;

	/**
	 * A dialog to display messages and prompts to the user - Generally used to
	 * gain administrative controls to the GUI when in 'live' mode
	 */
	private AdminDialog adminDialog;

	/**
	 * A dialog to display messages and prompts to the user - generally used to
	 * be able to change the RMI server binding location
	 */
	private OptionsDialog changeRMILocationDialog;

	/**
	 * Class to perform text file IO services. The text file contains attribute
	 * values and settings to be used by this and other classes
	 */
	public ClientIOServices clientOptions;

	/**
	 * An RMI client class, to enable connectivity to a remote RMI server and
	 * invoke its methods
	 */
	public RMIClient rmiClient;

	// GUI properties
	public Container container;
	public JFrame frame;
	private JLabel typicalJLabel = new JLabel("");
	public JButton exitProgramButton;
	private JButton changeRMILocButton;
	protected ImageIcon image;
	protected JLabel rfidMessageInput;
	protected JTextField rfidTagField;

	// internal panel and layout
	public JPanel mainDisplayPanel;
	private GridBagConstraints mainDisplayLayout;

	// external Panels
	protected IntroPanel introPanel;
	protected ManPinEntryPanel manPinEntryPanel;
	public HomePanel homePanel;
	protected GoodbyePanel goodbyePanel;
	protected LogoutPromptPanel logoutPrompt;
	public MapPanel mapPanel;

	// campus map image - Loaded when the main GUI starts to keep it in memory
	// and use later
	private ImageIcon originalMapImage;
	public BufferedImage bufferedImage;

	/**
	 * Class constructor
	 * 
	 * @throws RemoteException
	 *             is any remote connectivity problems occur
	 */
	public MainDisplayGUI() throws RemoteException {
		clientOptions = new ClientIOServices();
		rmiClient = new RMIClient();

		// Get whether or not the GUI should be in debug made from the attribute
		// in the client options text file
		if (clientOptions.getDebugOption().equalsIgnoreCase("true")) {
			debugMode = true;
		} else {
			debugMode = false;
		}

		// pre-load image on GUI startup - smoother results when using GUI
		originalMapImage = new ImageIcon("res/images/mapSmall.gif");

		// We want to create buffered image for optimisation. The image will
		// undergo
		// several changes in the future and it needs to be a buffered image to
		// allow
		// image alteration optimisation techniques to work effectively
		bufferedImage = new BufferedImage(originalMapImage.getImage().getWidth(
				null), originalMapImage.getImage().getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(originalMapImage.getImage(), 0, 0, null);
		g.dispose();
	}

	/**
	 * The action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitProgramButton) {
			performExitProgram();
		}
		if (e.getSource() == changeRMILocButton) {
			performChangeRMILoc();
		}
	}

	/**
	 * Prepares and displays this GUI
	 */
	public void run() throws PhidgetException {
		image = new ImageIcon(clientOptions.getBgImageLocation());

		frame = new JFrame("Student Informatio Kiosk");

		// Create a new window listener
		frame.addWindowListener(new WindowAdapter() {
			/**
			 * Creates a new window closing event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});

		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		frame.setResizable(false);

		// if the GUI is set to debug mode, don't include frame decoration and
		// hide the mouse
		if (!(debugMode)) {
			frame.setUndecorated(true);
			frame.setCursor(frame.getToolkit().createCustomCursor(
					new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), "null"));
		}

		typicalJLabel = new JLabel("");
		System.out.println(typicalJLabel.getText().toString());

		// prepare this GUI with a background image and create a new panel and
		// layout
		mainDisplayPanel = new ImagePanel(image.getImage());
		mainDisplayPanel.setLayout(new GridBagLayout());
		mainDisplayLayout = new GridBagConstraints();

		rfidMessageInput = new JLabel();
		rfidTagField = new JTextField();

		// add hot keys to the frame. This will mainly be used when in live mode
		// to give an administrator the ability to quickly quit the GUI, it
		// would normally be difficult as the frame doesn't have decorations, so
		// there's no 'quit' button, it's full screen so you can't access a task
		// bar and there's no visible mouse cursor
		addExitHotKey();
		addChangeRMILocHotkey();

		// load the first panel to be displayed's elements
		loadIntroPanel();

		container.add(mainDisplayPanel, BorderLayout.CENTER);

		// if it's not in debug mode, the frame doesn't need to be centred as
		// it's full screen
		if (debugMode) {
			centreFrame();

		}

		// prepare and display the GUI
		frame.setVisible(true);
		frame.pack();
	}

	/**
	 * Method to position the frame centred on the screen
	 */
	private void centreFrame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Adds an 'exit' GUI prompt hot key. This is done by creating an invisible
	 * button, in which later can be set to gain window focus and listen to key
	 * presses
	 */
	public void addExitHotKey() {
		exitProgramButton = new JButton("");
		exitProgramButton.addActionListener(this);
		exitProgramButton.setMnemonic(KeyEvent.VK_Q);
		exitProgramButton.addKeyListener(this);
		frame.add(exitProgramButton);
	}

	/**
	 * Adds an 'change RMI server binding' GUI prompt hot key. This is done by
	 * creating an invisible button, in which later can be set to gain window
	 * focus and listen to key presses
	 */
	public void addChangeRMILocHotkey() {
		changeRMILocButton = new JButton("");
		changeRMILocButton.addActionListener(this);
		changeRMILocButton.setMnemonic(KeyEvent.VK_R);
		changeRMILocButton.addKeyListener(this);
		frame.add(changeRMILocButton);
	}

	/**
	 * Adds the intro panel to the frame
	 */
	public void loadIntroPanel() throws PhidgetException {
		introPanel = new IntroPanel(this);
		mainDisplayPanel.add(introPanel, mainDisplayLayout);
		exitProgramButton.requestFocus();
		exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Adds the manual pin entry panel to the frame
	 */
	public void loadManualPinEntryPanel() {
		manPinEntryPanel = new ManPinEntryPanel(this);
		mainDisplayPanel.add(manPinEntryPanel, mainDisplayLayout);
		manPinEntryPanel.studentIDTextField.requestFocusInWindow();
	}

	/**
	 * Adds the home panel to the frame
	 */
	public void loadHomePanel(String currentView) {
		homePanel = new HomePanel(this, currentView);
		mainDisplayPanel.add(homePanel, mainDisplayLayout);
		homePanel.durationTimer.start();
		exitProgramButton.requestFocus();
		exitProgramButton.requestFocusInWindow();
	}

	public void loadGoodbyePanel() {
		goodbyePanel = new GoodbyePanel(this);
		mainDisplayPanel.add(goodbyePanel, mainDisplayLayout);
	}

	/**
	 * Adds the logout prompt panel to the frame
	 */
	public void loadLogoutPromptPanel(String currentView) {
		logoutPrompt = new LogoutPromptPanel(this, currentView);
		mainDisplayPanel.add(logoutPrompt);
		exitProgramButton.requestFocus();
		exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Adds the compus map panel to the frame
	 */
	public void loadMapPanel() {
		mapPanel = new MapPanel(this);
		mainDisplayPanel.add(mapPanel);
		exitProgramButton.requestFocus();
		exitProgramButton.requestFocusInWindow();
	}

	/**
	 * Loads the administration prompt when a user attempts to exit the GUI.
	 * Depending on the outcome the GUI will dispose or return back to the
	 * frame. A password must be entered to quit the GUI
	 */
	private void performExitProgram() {
		adminDialog = new AdminDialog(new JFrame(), "Exit Attempt");

		if (adminDialog.getChoice() == 1) {
			frame.dispose();
			System.exit(0);
		}
	}

	/**
	 * Loads the administration prompt when a user attempts to change the RMI
	 * location binding. Depending on the outcome the GUI will display the
	 * change RMI location prompt. A password must be entered to change the RMI
	 * location
	 */
	private void performChangeRMILoc() {
		adminDialog = new AdminDialog(new JFrame(),
				"Change RMI Location Attempt");

		if (adminDialog.getChoice() == 1) {
			this.changeRMILocationDialog = new OptionsDialog(new JFrame(),
					"Change RMI Location",
					"Please specify the URL of the RMI Server to connect to",
					"changeRMILocation");

			if (changeRMILocationDialog.getChoice() == 1) {
				clientOptions.changeRMILocation(changeRMILocationDialog
						.getNewRMILocation());
			}
		}
	}

	/**
	 * Key pressed action handler
	 * 
	 * @param e
	 *            The key pressed event
	 */
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyChar() == 'q') || (e.getKeyChar() == 'Q')) {
			this.performExitProgram();
		}
		if ((e.getKeyChar() == 'r') || (e.getKeyChar() == 'R')) {
			this.performChangeRMILoc();
		}
	}

	//@formatter:off
	/**
	 * Key released action handler
	 * @param e The key released event
	 */
	public void keyReleased(KeyEvent e) {}
	/**
	 * Key typed action handler
	 * @param e The key typed event
	 */
	public void keyTyped(KeyEvent e) {}
}
