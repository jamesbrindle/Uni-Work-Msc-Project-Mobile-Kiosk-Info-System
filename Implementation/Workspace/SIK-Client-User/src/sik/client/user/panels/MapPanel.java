package sik.client.user.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import sik.client.user.ActionClips;
import sik.client.user.FontStyles;
import sik.client.user.MainDisplayGUI;
import sik.client.user.panes.MapAreaPane;
import sik.client.user.panes.MapAreaPane.ZoomType;

/**
 * Panel which is displayed to show a user an interactive campus map of
 * Manchester metropolitan university. This panel is navigated to via the home
 * panel, which can be accessed once the user has logged into the system
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MapPanel extends JPanel implements ActionListener,
		MouseMotionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private MainDisplayGUI mainGUI;

	/**
	 * Defines the zoom level of the image in the map pane
	 */
	private int currentZoom = 1;

	/**
	 * Timer which will load the logout prompt panel when a pre-defined time has
	 * been met
	 */
	public Timer durationTimer;

	// Panel properties
	private MapAreaPane mapAreaPane;
	private JPanel titlePanel, legendPanel, navigationPanel;

	private GridBagConstraints mainLayout, titleLayout, legendLayout,
			navigationLayout;

	private JLabel titleLabel;
	private JButton backButton, zoomInButton, zoomOutButton;
	private JTextArea legendTextArea;

	/**
	 * Class constructor
	 * 
	 * @param mainGUI
	 *            The parent class which called this class
	 */
	public MapPanel(MainDisplayGUI mainGUI) {
		this.mainGUI = mainGUI;

		// create new timer to display this panel for a given time
		durationTimer = new Timer(50000, new ActionListener() {
			/**
			 * Action event handler for the timer
			 * 
			 * @param evt
			 *            The action event. In this case a time reached action
			 */
			public void actionPerformed(ActionEvent evt) {

				loadTimedLogoutPanel();
				durationTimer.stop();
			}
		});

		durationTimer.start();

		setLayout(new GridBagLayout());
		setOpaque(false);

		// create new sub panel instances
		titlePanel = new JPanel(new GridBagLayout());
		legendPanel = new JPanel(new GridBagLayout());
		navigationPanel = new JPanel(new GridBagLayout());
		mapAreaPane = new MapAreaPane(mainGUI.homePanel);

		mapAreaPane.addMouseMotionListener(this);
		addMouseMotionListener(this);

		// create new layout instances for the panels
		mainLayout = new GridBagConstraints();
		titleLayout = new GridBagConstraints();
		legendLayout = new GridBagConstraints();
		navigationLayout = new GridBagConstraints();

		loadMapPanelElements();

		// add sub panels to this panel
		positionSubPanels();
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			ActionClips.playWelcome();
			loadHomePanel();
		}
		if (e.getSource() == zoomInButton) {
			ActionClips.playNumButton();
			this.executeZoomIn();
		}
		if (e.getSource() == zoomOutButton) {
			ActionClips.playNumButton();
			this.executeZoomOut();
		}
	}

	/**
	 * Prepares map panel elements
	 */
	private void loadMapPanelElements() {
		loadTitlePanelElements();
		loadLegendElements();
		loadNavigationElements();
	}

	/**
	 * Adds and positions this panels sub panel on this panel
	 */
	private void positionSubPanels() {
		mainLayout.gridy = 0;
		mainLayout.gridy = 0;
		mainLayout.gridwidth = 2;

		mainLayout.insets = new Insets(35, 10, 25, 10);
		add(titleLabel, mainLayout);

		mainLayout.gridwidth = 1;
		mainLayout.gridx = 0;
		mainLayout.gridy = 1;

		mainLayout.insets = new Insets(0, 10, 0, 20);
		add(mapAreaPane, mainLayout);

		mainLayout.gridheight = 2;
		mainLayout.gridy = 1;
		mainLayout.gridx = 1;

		mainLayout.insets = new Insets(0, 10, 0, 10);
		add(legendPanel, mainLayout);

		mainLayout.gridheight = 1;
		mainLayout.gridx = 0;
		mainLayout.gridy = 2;

		mainLayout.anchor = GridBagConstraints.CENTER;

		mainLayout.insets = new Insets(0, 10, 0, 10);
		add(navigationPanel, mainLayout);
	}

	/**
	 * Prepares title area panel elements
	 */
	private void loadTitlePanelElements() {
		titleLabel = new JLabel("All Saints Campus Map");
		titleLabel.setFont(FontStyles.extraExtraLargeFont);
		titleLabel.setForeground(new Color(255, 255, 255));
		titlePanel.add(titleLabel, titleLayout);
	}

	/**
	 * Prepares legend area panel elements
	 */
	private void loadLegendElements() {
		legendTextArea = new JTextArea(8, 5);
		legendTextArea.setEditable(false);
		legendTextArea.setText(mainGUI.clientOptions.getCampusLegendText());
		legendTextArea.setFont(FontStyles.smallerFont);

		legendTextArea.setOpaque(false);
		legendPanel.setOpaque(false);

		legendLayout.insets = new Insets(0, 5, 0, 5);
		legendPanel.add(legendTextArea, legendLayout);
	}

	/**
	 * Prepares navigation control button area panel elements
	 */
	private void loadNavigationElements() {
		navigationPanel.setOpaque(false);

		backButton = new JButton("", new ImageIcon("res/images/backButton.png"));
		zoomInButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/zoom_in.png"));
		zoomOutButton = new JButton("", new ImageIcon(
				"res/images/buttonNotSelected/zoom_out.png"));

		backButton.setContentAreaFilled(false);
		zoomInButton.setContentAreaFilled(false);
		zoomOutButton.setContentAreaFilled(false);
		zoomOutButton.setEnabled(false);

		backButton.setBorderPainted(false);
		zoomInButton.setBorderPainted(false);
		zoomOutButton.setBorderPainted(false);

		backButton.addActionListener(this);
		zoomInButton.addActionListener(this);
		zoomOutButton.addActionListener(this);

		zoomInButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/zoom_in.png"));
		zoomOutButton.setPressedIcon(new ImageIcon(
				"res/images/buttonSelected/zoom_out.png"));

		navigationLayout.insets = new Insets(5, 0, 0, 90);

		navigationLayout.anchor = GridBagConstraints.WEST;
		navigationLayout.gridx = 0;
		navigationPanel.add(backButton, navigationLayout);

		navigationLayout.insets = new Insets(5, 0, 0, 90);

		navigationLayout.gridx = 1;
		navigationPanel.add(zoomInButton, navigationLayout);

		navigationLayout.insets = new Insets(5, 0, 0, 0);

		navigationLayout.gridx = 2;
		navigationPanel.add(zoomOutButton, navigationLayout);
	}

	/**
	 * Loads the logout prompt panel
	 */
	private void loadTimedLogoutPanel() {
		ActionClips.playCancelButton();
		setVisible(false);
		mainGUI.loadLogoutPromptPanel("map");
		mainGUI.frame.pack();
	}

	/**
	 * Loads the home panel
	 */
	private void loadHomePanel() {
		durationTimer.stop();
		this.setVisible(false);
		mainGUI.frame.repaint();
		mainGUI.homePanel.setVisible(true);
		mainGUI.frame.pack();
	}

	/**
	 * Makes the map area pane zoom into the image. It does this by executing
	 * methods that increase the size of the image
	 */
	private void executeZoomIn() {
		currentZoom++;
		if (currentZoom >= 9) {
			currentZoom = 9;
			selectZoomEnum(currentZoom);
			zoomInButton.setEnabled(false);
		} else {
			zoomOutButton.setEnabled(true);
			selectZoomEnum(currentZoom);
		}
	}

	/**
	 * Makes the map area pane zoom out of the image. It does this by executing
	 * methods that decrease the size of the image
	 */
	private void executeZoomOut() {
		currentZoom--;
		if (currentZoom <= 1) {
			currentZoom = 1;
			selectZoomEnum(currentZoom);
			zoomOutButton.setEnabled(false);
		} else {
			zoomInButton.setEnabled(true);
			selectZoomEnum(currentZoom);
		}
	}

	/**
	 * Sets the zoom level of the map area pane
	 * 
	 * @param zoomInt
	 *            The zoom level in which to set the map area pane
	 */
	private void selectZoomEnum(int zoomInt) {
		switch (zoomInt) {
			case 1:
				mapAreaPane.setZoom(ZoomType.ZOOM_MINUS_4);
				break;
			case 2:
				mapAreaPane.setZoom(ZoomType.ZOOM_MINUS_3);
				break;
			case 3:
				mapAreaPane.setZoom(ZoomType.ZOOM_MINUS_2);
				break;
			case 4:
				mapAreaPane.setZoom(ZoomType.ZOOM_MINUS_1);
				break;
			case 5:
				mapAreaPane.setZoom(ZoomType.ZOOM_ZERO);
				break;
			case 6:
				mapAreaPane.setZoom(ZoomType.ZOOM_PLUS_1);
				break;
			case 7:
				mapAreaPane.setZoom(ZoomType.ZOOM_PLUS_2);
				break;
			case 8:
				mapAreaPane.setZoom(ZoomType.ZOOM_PLUS_3);
				break;
			case 9:
				mapAreaPane.setZoom(ZoomType.ZOOM_PLUS_4);
				break;
			default:
				mapAreaPane.setZoom(ZoomType.ZOOM_MINUS_4);
				break;
		}
	}

	/**
	 * Mouse dragged event handler
	 * 
	 * @param e
	 *            The mouse dragged event
	 */
	public void mouseDragged(MouseEvent e) {
		durationTimer.restart();
	}

	/**
	 * Mouse moved event handler
	 * 
	 * @param e
	 *            The mouse moved event
	 */
	public void mouseMoved(MouseEvent e) {
		durationTimer.restart();
	}
}
