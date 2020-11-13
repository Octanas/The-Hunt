package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Environment.Maze;
import Environment.Maze.Direction;
import Environment.Maze.MazeEntity;
import Environment.Maze.Tile;

public class VisualizerWindow extends JPanel {

	final int frameRate = 30;
	final int animationFrames = 10;

	final int tileLength = 40;

	/**
	 * Copy of last values for each entity
	 */
	HashMap<String, MazeEntity> pastEntities;
	/**
	 * Current animation frame for each entity
	 */
	HashMap<String, Integer> currentAnimationFrame;
	/**
	 * If each entity is ready to receive a new positon for the animation
	 */
	HashMap<String, Boolean> waitingForNewPosition;

	// Upper left in 0, 0
	Shape tileShape = new Polygon(new int[] { 0, tileLength, tileLength, 0 },
			new int[] { tileLength, tileLength, 0, 0 }, 4);
	Shape entityShape = new Ellipse2D.Double(0, 0, tileLength, tileLength);
	Shape eliminatedShape = new Polygon(new int[] { 0, 5, 20, 35, 40, 25, 40, 35, 20, 5, 0, 15 },
			new int[] { 35, 40, 25, 40, 35, 20, 5, 0, 15, 0, 5, 20 }, 12);

	// Center in 0, 0
	Shape directionIndicator = new Polygon(new int[] { -8, 8, 0 }, new int[] { -12, -12, -18 }, 3);

	Maze maze;

	public VisualizerWindow(Maze maze) {
		this.maze = maze;

		pastEntities = new HashMap<String, MazeEntity>();
		currentAnimationFrame = new HashMap<String, Integer>();
		waitingForNewPosition = new HashMap<String, Boolean>();

		Thread animationThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					repaint(); // Refresh the display
					try {
						Thread.sleep(1000 / frameRate); // delay and yield to other threads
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		animationThread.start(); // start the thread to run animation

		setPreferredSize(new Dimension(tileLength * maze.getWidth(), tileLength * maze.getHeight()));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background
		setBackground(Color.GRAY);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Save the current transform of the graphics contexts.
		AffineTransform saveTransform = g2d.getTransform();
		// Create a identity affine transform, and apply to the Graphics2D context
		AffineTransform identity = new AffineTransform();
		g2d.setTransform(identity);

		Tile[][] mazeArray = maze.getMazeArrayRepresentation();

		// Draw walls and hunter zone
		for (int y = 0; y < maze.getHeight(); y++) {
			for (int x = 0; x < maze.getWidth(); x++) {
				if (mazeArray[y][x].equals(Tile.W)) {
					g2d.setColor(Color.DARK_GRAY);
				} else if (mazeArray[y][x].equals(Tile.C)) {
					g2d.setColor(Color.GRAY);
				} else {
					g2d.setColor(Color.RED);
				}

				g2d.fill(tileShape);
				g2d.translate(tileLength, 0);
			}

			g2d.translate(-maze.getWidth() * tileLength, tileLength);
		}

		g2d.setTransform(saveTransform);
		g2d.setTransform(identity);

		ArrayList<MazeEntity> entities = new ArrayList<MazeEntity>();
		ArrayList<String> entityNames = new ArrayList<String>();

		for (String entityName : maze.getEntities().keySet()) {
			MazeEntity entity = maze.getEntities().get(entityName);

			if (entity != null) {
				if (entity.isHunter) {
					entities.add(entity);
					entityNames.add(entityName);
				} else {
					entities.add(0, entity);
					entityNames.add(0, entityName);
				}
			}
		}

		// Draw entities
		for (int i = 0; i < entities.size(); i++) {
			MazeEntity entity = entities.get(i);
			String entityName = entityNames.get(i);
			MazeEntity pastEntity = pastEntities.get(entityName);

			// First time this entity is on the screen, just initialize the values
			// No animation will be done
			if (pastEntity == null) {
				pastEntities.put(entityName, entity.clone());
				currentAnimationFrame.put(entityName, 0);
				waitingForNewPosition.put(entityName, true);

				pastEntity = entity;
			}

			// Got a new position, time to animate it
			// Which means, go to the first frame of animation (1)
			// It is set to 0 but it will be incremented right after this if block
			if (waitingForNewPosition.get(entityName) && (entity.getXCoordinate() != pastEntity.getXCoordinate()
					|| entity.getYCoordinate() != pastEntity.getYCoordinate()
					|| entity.getDirection() != pastEntity.getDirection())) {
				waitingForNewPosition.put(entityName, false);
				currentAnimationFrame.put(entityName, 0);
			}

			// Animation frame incremented here!
			currentAnimationFrame.put(entityName, currentAnimationFrame.get(entityName) + 1);

			// Calculate the position of the entity in the current frame
			int currentXTranslation = currentAnimationFrame.get(entityName)
					* (entity.getXCoordinate() * tileLength - pastEntity.getXCoordinate() * tileLength)
					/ animationFrames + pastEntity.getXCoordinate() * tileLength;
			int currentYTranslation = currentAnimationFrame.get(entityName)
					* (entity.getYCoordinate() * tileLength - pastEntity.getYCoordinate() * tileLength)
					/ animationFrames + pastEntity.getYCoordinate() * tileLength;

			// This will draw the entity body
			// Hunters are Black, beasts are white
			if (entity.isHunter) {
				g2d.setColor(Color.BLACK);
			} else {
				g2d.setColor(Color.WHITE);
			}

			g2d.translate(currentXTranslation, currentYTranslation);
			g2d.fill(entityShape);

			g2d.setTransform(saveTransform);
			g2d.setTransform(identity);

			// Now this will draw the direction indicator
			// The colors are reversed
			if (entity.isHunter) {
				g2d.setColor(Color.WHITE);
			} else {
				g2d.setColor(Color.BLACK);
			}

			g2d.translate(20, 20);
			g2d.translate(currentXTranslation, currentYTranslation);

			if (entity.getDirection().equals(Direction.East)) {
				g2d.rotate(Math.toRadians(90));
			} else if (entity.getDirection().equals(Direction.South)) {
				g2d.rotate(Math.toRadians(180));
			} else if (entity.getDirection().equals(Direction.West)) {
				g2d.rotate(Math.toRadians(270));
			}

			g2d.fill(directionIndicator);

			g2d.setTransform(saveTransform);
			g2d.setTransform(identity);

			// This will draw the RED X over the entity, if it is a prey and it has been
			// caught
			if (entity.isCaught()) {
				g2d.setColor(Color.RED);

				g2d.translate(currentXTranslation, currentYTranslation);
				g2d.fill(eliminatedShape);

				g2d.setTransform(saveTransform);
				g2d.setTransform(identity);
			}

			// Last animation frame reached, which means the animation is completed
			// The last entity values are now the current ones
			// and the entity is ready to receive a new movement to animate
			if (currentAnimationFrame.get(entityName) == animationFrames) {
				pastEntities.put(entityName, entity.clone());
				waitingForNewPosition.put(entityName, true);
			}
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

		String msg = maze.getClock();
		int msgWidth = g.getFontMetrics().stringWidth(msg);
		int msgAscent = g.getFontMetrics().getAscent();

		int msgX = getWidth() / 2 - msgWidth / 2;
		int msgY = tileLength / 2 + msgAscent / 2;

		g.drawString(msg, msgX, msgY);

		if (maze.isOver()) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));

			msg = "END";
			msgWidth = g.getFontMetrics().stringWidth(msg);
			msgAscent = g.getFontMetrics().getAscent();

			msgX = getWidth() / 2 - msgWidth / 2;
			msgY = getHeight() / 2 + msgAscent / 2;

			g.drawString(msg, msgX, msgY);

			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

			msg = "All beasts have been caught";
			msgWidth = g.getFontMetrics().stringWidth(msg);
			msgAscent = g.getFontMetrics().getAscent();

			msgX = getWidth() / 2 - msgWidth / 2;
			msgY = getHeight() - tileLength / 2 + msgAscent / 2;

			g.drawString(msg, msgX, msgY);
		}
	}

	public static void run(Maze maze) {
		// Run the GUI codes on the Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("The Hunt");
				frame.setContentPane(new VisualizerWindow(maze));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack(); // "this" JFrame packs its components
				frame.setLocationRelativeTo(null); // center the application window
				frame.setVisible(true); // show it
			}
		});
	}
}
