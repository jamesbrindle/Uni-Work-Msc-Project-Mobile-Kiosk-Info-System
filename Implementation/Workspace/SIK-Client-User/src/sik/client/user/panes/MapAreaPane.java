package sik.client.user.panes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import sik.client.user.panels.HomePanel;

/**
 * Panel which is displayed as a content inside the home panel. As the home
 * panel displays a number of different complex panels corresponding to the
 * selected category this has been given the name 'pane' rather than 'panel'.
 * 
 * This particular pane is is displayed within the Map Panel when the Campus Map
 * button is selected in the Home Panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class MapAreaPane extends JPanel {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	private HomePanel homePanel;

	/**
	 * The image which this panel uses as the campus map image, this image is to
	 * be the image which get resized when executing zoom in and zoom out
	 * operations
	 */
	private ImageIcon workingMapImage;

	/**
	 * Mouse listener created to handle mouse events on a JScrollPane in which
	 * exists an image, in which the end result is to gain the ability to click
	 * and drag the image in the scoll pane to view different parts of the
	 * image. Much like a google maps app
	 */
	private mapImageMouseListener myMouseListener;

	/**
	 * When zooming in and out of an image, we need to re-calculate a number of
	 * co-ordinates in order to keep the the current centre of the image in
	 * view. This is partly achieved by working out the percentage of how far
	 * away the view port is to the edge of the image, and applying that
	 * percentage when scaling the image up or down. This double is a variable
	 * for the left to right margin of the view port from the edge of the image
	 */
	private double percentXmargin = 0.0;

	/**
	 * When zooming in and out of an image, we need to re-calculate a number of
	 * co-ordinates in order to keep the the current centre of the image in
	 * view. This is partly achieved by working out the percentage of how far
	 * away the view port is to the edge of the image, and applying that
	 * percentage when scaling the image up or down. This double is a variable
	 * for the top to button margin of the view port from the edge of the image
	 */
	private double percentYmargin = 0.0;

	// Panel properties
	private JScrollPane mapScrollPane;
	private JLabel mapLabel;

	//@formatter:off
	/**
	 * Emum for the different zoom levels that can be used when viewing the campus 
	 * map image. The zoom levels are in fact image dimensions.  
	 * I.e. the the image gets zoomed in, the image increases in size
	 */
	public static enum ZoomType {
		ZOOM_MINUS_4 (670, 606), 
		ZOOM_MINUS_3 (811, 733), 
		ZOOM_MINUS_2 (952, 861), 
		ZOOM_MINUS_1 (1093, 988), 
		ZOOM_ZERO (1235, 1117), 
		ZOOM_PLUS_1 (1376, 1244), 
		ZOOM_PLUS_2 (1518, 1372), 
		ZOOM_PLUS_3 (1659, 1500), 
		ZOOM_PLUS_4 (1800, 1627);

		private Dimension dimension;
		private int width, height;

		/**
		 * Sets the zoom type based on width and height dimension
		 * @param width The dimension width the image is to be set to
		 * @param height The dimension height the image is to be set to
		 */
		private ZoomType(int width, int height) {
			this.dimension = new Dimension(width, height);
			this.width = width;
			this.height = height;
		}

		/**
		 * Returns the dimesion of the zoom type
		 * @return dimension The dimension of the zoom type in which to return
		 */
		public Dimension getDimension() {
			return dimension;
		}

		/**
		 * Returns the width of the zoom type
		 * @return width The dimension width of the zoom type in which to return
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Returns the height of the zoom type
		 * @return height The dimension height of the zoom type in which to return
		 */
		public int getHeight() {
			return height;
		}
	}
	
	//@formatter:on
	/**
	 * Class constructor
	 * 
	 * @param homePanel
	 *            The parent class which called this class
	 */
	public MapAreaPane(HomePanel homePanel) {

		this.homePanel = homePanel;

		setPreferredSize(new Dimension(670, 390));
		setMaximumSize(new Dimension(670, 390));
		setMinimumSize(new Dimension(670, 390));

		setOpaque(false);

		workingMapImage = new ImageIcon();

		// set initial zoom type
		setZoom(ZoomType.ZOOM_MINUS_4);

		mapLabel = new JLabel(workingMapImage);
		mapScrollPane = new JScrollPane(mapLabel);

		mapScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mapScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		mapScrollPane.setPreferredSize(new Dimension(670, 390));
		mapScrollPane.getViewport().setViewPosition(new Point(600, 300));

		myMouseListener = new mapImageMouseListener();

		mapLabel.addMouseListener(myMouseListener);
		mapLabel.addMouseMotionListener(myMouseListener);
		mapScrollPane.addMouseMotionListener(myMouseListener);

		setLayout(new BorderLayout());
		add(mapScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Sets the zoom type of which to set the image at
	 * 
	 * @param zoomType
	 *            The zoomType (enum) in which to set the zoom type
	 */
	public void setZoom(ZoomType zoomType) {
		if (mapScrollPane != null) {
			setOldViewPortCentre();
		}

		// no need to re-scale as it's the size of the original image
		if (!(zoomType == ZoomType.ZOOM_PLUS_4)) {

			// using billinear due to performance / quality trade off
			Object renderingHint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

			workingMapImage.setImage(getScaledInstance(
					homePanel.mainGUI.bufferedImage, zoomType.getWidth(),
					zoomType.getHeight(), renderingHint, true));

		} else {
			workingMapImage.setImage(homePanel.mainGUI.bufferedImage);
		}

		if (mapScrollPane != null) {
			// need to re-set size of scrollbar area
			mapScrollPane.getViewport().setViewSize(zoomType.getDimension());

			// use to set the new view port to the centre from the previous view
			// port
			// i.e. when zooming in or out, it keeps what you was originally
			// looking at
			// in view
			mapScrollPane.getViewport().setViewPosition(getNewViewPortCentre());
		}

		homePanel.mainGUI.frame.pack();
	}

	/**
	 * Returns a re-scaled buffered image from a supplied buffered image. This
	 * method Provides extensive performance optimisation when dealing with
	 * re-scaling large images without compromising the quality of the image,
	 * which is ideal when the system is to run on a tablet PC with limited
	 * processing power and memory
	 * 
	 * @param img
	 *            The buffered image to be re-scaled
	 * @param targetWidth
	 *            The target width of the image to be returned
	 * @param targetHeight
	 *            The target height of the image to be returned
	 * @param hint
	 *            The rendering type of which to be types
	 * @param isHighQuality
	 *            Defines whether the re-produced image is to be of high quality
	 *            of not. This affects performance and has been included to test
	 *            the impact of large high quality image rendering on different
	 *            devices
	 */
	public BufferedImage getScaledInstance(BufferedImage img, int targetWidth,
			int targetHeight, Object hint, boolean isHighQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage returnImage = img;
		int width, height;
		if (isHighQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			width = img.getWidth();
			height = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			width = targetWidth;
			height = targetHeight;
		}

		// first run then test condition approach
		do {
			if (isHighQuality && (width > targetWidth)) {
				width /= 2;
				if (width < targetWidth) {
					width = targetWidth;
				}
			}

			if (isHighQuality && (height > targetHeight)) {
				height /= 2;
				if (height < targetHeight) {
					height = targetHeight;
				}
			}

			BufferedImage tempImage = new BufferedImage(width, height, type);
			Graphics2D g2 = tempImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(returnImage, 0, 0, width, height, null);
			g2.dispose();

			returnImage = tempImage;
			tempImage.flush();
		} while ((width != targetWidth) || (height != targetHeight));

		return returnImage;
	}

	/**
	 * Determines and sets a particular type of view port values from the
	 * current image zoom state
	 * 
	 * @see #percentXmargin
	 * @see #percentYmargin
	 */
	private void setOldViewPortCentre() {
		int viewPortCentrePointY;
		int viewPortCentrePointX;

		// get horizontal and vertical bounds of view port (for ease of code
		// reading)
		BoundedRangeModel horizontalModel = mapScrollPane
				.getHorizontalScrollBar().getModel();

		BoundedRangeModel verticalModel = mapScrollPane.getVerticalScrollBar()
				.getModel();

		// get the x and y coordinates of the centre of the view port
		// corresponding
		// to the image
		viewPortCentrePointX = horizontalModel.getValue()
				+ (horizontalModel.getExtent() / 2);

		viewPortCentrePointY = verticalModel.getValue()
				+ (verticalModel.getExtent() / 2);

		// work out the percentage of the view port coordinates corresponding to
		// the image
		// in order to apply this percentage when setting the coordinates of the
		// view
		// port when scaling the image
		percentXmargin = (viewPortCentrePointX / (double) workingMapImage
				.getIconWidth()) * 100;
		percentYmargin = (viewPortCentrePointY / (double) workingMapImage
				.getIconHeight()) * 100;
	}

	/**
	 * Returns a scroll position point in which a re-scalled image (zoom type)
	 * is to be set to, which depends on the old scoll positions
	 * 
	 * @see #percentXmargin
	 * @see #percentYmargin
	 */
	private Point getNewViewPortCentre() {
		BoundedRangeModel horizontalModel = mapScrollPane
				.getHorizontalScrollBar().getModel();

		BoundedRangeModel verticalModel = mapScrollPane.getVerticalScrollBar()
				.getModel();

		// set view port centre values to the percentage of that of the
		// view port on the previously scaled image (in order to keep same image
		// view)
		int viewPortCentreX = (workingMapImage.getIconWidth() / 100)
				* (int) percentXmargin;
		int viewPortCentreY = (workingMapImage.getIconHeight() / 100)
				* (int) percentYmargin;

		// as scrollbar coordinates work from top left, we can't use the centre
		// of the view port
		// as coordinates, therefore calculate top left values from this. We
		// haven't used
		// the top left values originally as when the view port is to the top
		// left, the return
		// values end up as 0 (i.e 0 * anything will always equal zero).
		// throwing off calculations
		int newScrollX = viewPortCentreX - (horizontalModel.getExtent() / 2);
		int newScrollY = viewPortCentreY - (verticalModel.getExtent() / 2);

		// if top left values go into minus values, we've gone beyond
		// the view of the image, therefore set to x0, y0;
		if (newScrollX < 0) {
			newScrollX = 0;
		}
		if (newScrollY < 0) {
			newScrollY = 0;
		}

		Point newScrollPosition = new Point(newScrollX, newScrollY);

		return newScrollPosition;
	}

	/**
	 * Class to deal with mouse events of this image scroll pane to allow the
	 * image to be moves around a scroll pane similar to that of the google maps
	 * app
	 */
	private class mapImageMouseListener extends MouseAdapter {
		private BoundedRangeModel horizontalModel = mapScrollPane
				.getHorizontalScrollBar().getModel();
		private BoundedRangeModel verticalModel = mapScrollPane
				.getVerticalScrollBar().getModel();
		private int x0, y0;
		private Point p0;

		/**
		 * Mouse pressed event handler
		 * 
		 * @param e
		 *            The mouse pressed event
		 */
		public void mousePressed(MouseEvent e) {
			homePanel.mainGUI.mapPanel.durationTimer.restart();
			x0 = horizontalModel.getValue();
			y0 = verticalModel.getValue();
			p0 = e.getLocationOnScreen();
		}

		/**
		 * Mouse dragged event handler
		 * 
		 * @param e
		 *            The mouse dragged event
		 */
		public void mouseDragged(MouseEvent e) {
			homePanel.mainGUI.mapPanel.durationTimer.restart();
			Point p1 = e.getLocationOnScreen();
			int x1 = p0.x - p1.x + x0;
			int y1 = p0.y - p1.y + y0;

			horizontalModel.setValue(x1);
			verticalModel.setValue(y1);
		}
	}
}
