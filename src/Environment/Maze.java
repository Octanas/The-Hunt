package Environment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Maze {
	public enum Direction {
		North, South, East, West
	};

	public enum Movement {
		Up, Down, Left, Right
	};

	public enum Tile {
		W, // WALL
		C, // CORRIDOR
		H // HUNTER ZONE
	};

	public class MazeEntity {
		Direction direction;
		int xCoordinate;
		int yCoordinate;
		public boolean isHunter;
		private boolean caught;

		public MazeEntity(boolean isHunter, int xCoordinate, int yCoordinate, Direction direction) {
			this.isHunter = isHunter;
			this.xCoordinate = xCoordinate;
			this.yCoordinate = yCoordinate;
			this.direction = direction;
			this.caught = false;
		}

		public Direction getDirection() {
			return direction;
		}

		public int getXCoordinate() {
			return xCoordinate;
		}

		public int getYCoordinate() {
			return yCoordinate;
		}
		
		/**
		 * 
		 * @param caught
		 * @return			If it was set (only works on beasts)
		 */
		public boolean setCaught(boolean caught) {
			if(isHunter)
				return false;
			
			this.caught = caught;
			
			return true;
		}
		
		public boolean isCaught() {
			return !isHunter && caught;
		}
	}

	private static Tile[][] defaultMaze = new Tile[][] {
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W,
					Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C,
					Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C,
					Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C,
					Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.H, Tile.W, Tile.W, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.H, Tile.H, Tile.H, Tile.W, Tile.C,
					Tile.C, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C,
					Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W,
					Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C,
					Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C,
					Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C,
					Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W,
					Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W },
			{ Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C,
					Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W },
			{ Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W,
					Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W } };

	private int mazeWidth;
	private int mazeHeight;
	private Tile[][] maze;
	private Graph mazeGraph;
	private HashMap<Tile, Integer> numTiles;
	private ConcurrentHashMap<String, MazeEntity> entities;
	private LocalDateTime startingTime;
	private LocalDateTime endingTime;

	/**
	 * Creates a Maze object with the default Pac-Man maze.
	 * 
	 * @return The initialized maze object.
	 */
	public static Maze defaultMaze() {
		return new Maze(defaultMaze);
	}

	private Maze(Tile[][] maze) {
		this.maze = maze;

		this.mazeHeight = maze.length;
		this.mazeWidth = maze.length != 0 ? maze[0].length : 0;

		this.createGraph();

		entities = new ConcurrentHashMap<String, MazeEntity>();
		numTiles = new HashMap<Tile, Integer>();

		for (int y = 0; y < this.mazeHeight; y++) {
			for (int x = 0; x < this.mazeWidth; x++) {
				int currValue = numTiles.getOrDefault(this.maze[y][x], 0);
				numTiles.put(this.maze[y][x], currValue + 1);
			}
		}
		
		startingTime = null;
		endingTime = null;
	}

	private void createGraph() {
		mazeGraph = new Graph();

		for (int y = 0; y < this.mazeHeight; y++) {
			for (int x = 0; x < this.mazeWidth; x++) {
				if (this.maze[y][x].equals(Tile.C) || this.maze[y][x].equals(Tile.H)) {
					mazeGraph.addVertex(x, y);

					mazeGraph.addEdge(x, y, x - 1, y);
					mazeGraph.addEdge(x, y, x + 1, y);
					mazeGraph.addEdge(x, y, x, y - 1);
					mazeGraph.addEdge(x, y, x, y + 1);
				}
			}
		}
	}

	/**
	 * Adds an entity to the maze in a random location.
	 * 
	 * If it fails, then there's already a entity with the chosen name or it
	 * couldn't calculate a location.
	 * 
	 * @param name     Name of the entity.
	 * @param isHunter If the entity is a hunter (false if is a beast).
	 * @return If the entity was added.
	 */
	public boolean registerEntity(String name, boolean isHunter) {
		if (entities.containsKey(name))
			return false;

		int[] coordinates;

		if (isHunter)
			coordinates = getRandomCoordinate(Tile.H);
		else
			coordinates = getRandomCoordinate(Tile.C);

		if (coordinates == null)
			return false;

		return registerEntity(name, isHunter, coordinates[0], coordinates[1]);
	}

	/**
	 * Adds an entity to the maze.
	 * 
	 * If it fails, then there's already a entity with the chosen name.
	 * 
	 * @param name        Name of the entity.
	 * @param isHunter    If the entity is a hunter (false if is a beast).
	 * @param xCoordinate Initial X coordinate.
	 * @param yCoordinate Initial Y coordinate.
	 * @return If the entity was added.
	 */
	public boolean registerEntity(String name, boolean isHunter, int xCoordinate, int yCoordinate) {
		if (entities.containsKey(name))
			return false;

		entities.put(name, new MazeEntity(isHunter, xCoordinate, yCoordinate, Direction.North));

		return true;
	}

	/**
	 * Returns the list of possible movements in a specified tile.
	 * @param xCoordinate	X coordinate of the current tile;
	 * @param yCoordinate	Y coordinate of the current tile;
	 * @return				List of valid moves to make from the current tile.
	 */
	public List<Movement> possibleMovements(int xCoordinate, int yCoordinate) {
		if(maze[yCoordinate][xCoordinate].equals(Tile.W))
			return Collections.emptyList();

		ArrayList<Movement> validMoves = new ArrayList<Movement>();

		if (yCoordinate > 0 && maze[yCoordinate - 1][xCoordinate].equals(Tile.C)) {
			validMoves.add(Movement.Up);
		}

		if (yCoordinate < maze.length - 1 && maze[yCoordinate + 1][xCoordinate].equals(Tile.C)) {
			validMoves.add(Movement.Down);
		}

		if (xCoordinate > 0 && maze[yCoordinate][xCoordinate - 1].equals(Tile.C)) {
			validMoves.add(Movement.Left);
		}

		if (xCoordinate < maze[0].length && maze[yCoordinate][xCoordinate + 1].equals(Tile.C)) {
			validMoves.add(Movement.Right);
		}

		return validMoves;
	}

	/**
	 * Moves the entity on the maze.
	 * 
	 * It can fail if there is no entity with the specified name or if there's an
	 * obstacle in the specified direction.
	 * 
	 * @param name     Name of the entity.
	 * @param movement Direction in which the entity will move.
	 * @return If it succeeded.
	 */
	public boolean moveEntity(String name, Movement movement) {
		MazeEntity entity = entities.get(name);

		if (entity == null)
			return false;

		if (movement.equals(Movement.Up)) {
			if (entity.yCoordinate > 0 && maze[entity.yCoordinate - 1][entity.xCoordinate].equals(Tile.C)) {
				entity.yCoordinate -= 1;
				entity.direction = Direction.North;
			} else
				return false;
		} else if (movement.equals(Movement.Down)) {
			if (entity.yCoordinate < maze.length - 1
					&& maze[entity.yCoordinate + 1][entity.xCoordinate].equals(Tile.C)) {
				entity.yCoordinate += 1;
				entity.direction = Direction.South;
			} else
				return false;
		} else if (movement.equals(Movement.Left)) {
			if (entity.xCoordinate > 0 && maze[entity.yCoordinate][entity.xCoordinate - 1].equals(Tile.C)) {
				entity.xCoordinate -= 1;
				entity.direction = Direction.West;
			} else
				return false;
		} else if (movement.equals(Movement.Right)) {
			if (entity.xCoordinate < maze[0].length
					&& maze[entity.yCoordinate][entity.xCoordinate + 1].equals(Tile.C)) {
				entity.xCoordinate += 1;
				entity.direction = Direction.East;
			} else
				return false;
		} else
			return false;
		
		// Check if with this move, a beast was caught
		wasBeastCaught(entity.xCoordinate, entity.yCoordinate);

		// When the first move is made, the clock starts
		if(startingTime == null)
			startingTime = LocalDateTime.now();
		
		return true;
	}

	/**
	 * Rotates the entity in the maze.
	 * 
	 * It fails if there is no entity with the specified name.
	 * 
	 * @param name      Name of the entity.
	 * @param direction Direction the entity will rotate to.
	 * @return If it succeeded.
	 */
	public boolean rotateEntity(String name, Direction direction) {
		MazeEntity entity = entities.get(name);

		if (entity == null)
			return false;

		entity.direction = direction;

		entities.put(name, entity);

		return true;
	}

	/**
	 * Returns the entity visible to the specified entity. If there are two entities
	 * in a row, only the closest entity is returned.
	 * 
	 * @param name Name of the seeing entity.
	 * @return The entity that is being seen or null if no entity is being seen.
	 */
	public MazeEntity getVisibleEntity(String name) {
		MazeEntity entity = entities.get(name);

		if (entity == null)
			return null;

		int diffX = 0;
		int diffY = 0;

		if (entity.direction.equals(Direction.North)) {
			diffY = -1;
		} else if (entity.direction.equals(Direction.South)) {
			diffY = 1;
		} else if (entity.direction.equals(Direction.West)) {
			diffX = -1;
		} else if (entity.direction.equals(Direction.East)) {
			diffX = 1;
		}

		int x = entity.xCoordinate;
		int y = entity.yCoordinate;
		boolean stop = false;

		MazeEntity entityFound = null;

		while (!stop) {
			x += diffX;
			y += diffY;

			for (String entityKey : entities.keySet()) {
				if (entities.get(entityKey).xCoordinate == x && entities.get(entityKey).yCoordinate == y) {
					entityFound = entities.get(entityKey);
					break;
				}
			}

			stop = !this.maze[y][x].equals(Tile.C) || entityFound != null;
		}

		return entityFound;
	}
	
	/**
	 * Check if all beasts have been caught.
	 * @return
	 */
	public boolean isOver() {
		for(String entityName : entities.keySet()) {
			MazeEntity entity = entities.get(entityName);
			
			if(entity != null && !entity.isHunter && !entity.isCaught())
				return false;
		}
		
		// Clock stops when all beasts have been caught
		if(endingTime == null)
			endingTime = LocalDateTime.now();
		
		return true;
	}
	
	public String getClock() {
		if(startingTime == null)
			return "00:00";
		
		LocalDateTime endTime = endingTime == null ? LocalDateTime.now() : endingTime;
		
		Duration duration = Duration.between(startingTime, endTime);
		
		return String.format("%02d", duration.toMinutes()) + ":" + String.format("%02d", duration.toSecondsPart());
	}

	/**
	 * Check if in the specified coordinates there is a catched beast.
	 * @param xCoordinate
	 * @param yCoordinate
	 */
	private void wasBeastCaught(int xCoordinate, int yCoordinate) {
		ArrayList<MazeEntity> beastsInCoordinates = new ArrayList<MazeEntity>();
		boolean hunterInCoordinates = false;
		
		for(String entityName : entities.keySet()) {
			MazeEntity entity = entities.get(entityName);
			
			if(entity != null && entity.xCoordinate == xCoordinate && entity.yCoordinate == yCoordinate) {
				if(entity.isHunter)
					hunterInCoordinates = true;
				else {
					beastsInCoordinates.add(entity);
				}
			}
		}
		
		if(hunterInCoordinates) {
			// Sets each beast in the coordinates as having been caught
			for(MazeEntity beast : beastsInCoordinates) {
				beast.setCaught(true);
			}
		}
	}

	/**
	 * Gets a random coordinate with the specified tile.
	 * 
	 * @param tile Tile in the coordinates.
	 * @return Array with the coordinates [x, y] or null if it can't find a
	 *         location.
	 */
	private int[] getRandomCoordinate(Tile tile) {
		Random rand = new Random();

		int numTile = this.numTiles.getOrDefault(tile, 0);

		int tileNumber = rand.nextInt(numTile);

		if (numTile == 0)
			return null;

		int numFound = 0;

		for (int y = 0; y < this.mazeHeight; y++) {
			for (int x = 0; x < this.mazeWidth; x++) {
				if (this.maze[y][x].equals(tile)) {
					if (numFound == tileNumber)
						return new int[] { x, y };

					numFound++;
				}
			}
		}

		return null;
	}

	public int getWidth() {
		return this.mazeWidth;
	}

	public int getHeight() {
		return this.mazeHeight;
	}

	public Tile[][] getMazeArrayRepresentation() {
		return this.maze;
	}

	public ConcurrentHashMap<String, MazeEntity> getEntities() {
		return this.entities;
	}
	
	public Graph getGraph() {
		return mazeGraph;
	}

	/**
	 * Returns the maze representation in a String object.
	 */
	public String toString() {
		String str = "";

		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				boolean foundEntity = false;

				for (String entityKey : entities.keySet()) {
					if (entities.get(entityKey).xCoordinate == x && entities.get(entityKey).yCoordinate == y) {
						str += "E ";

						foundEntity = true;

						break;
					}
				}

				if (!foundEntity) {
					if (maze[y][x].equals(Tile.C))
						str += "C ";
					else if (maze[y][x].equals(Tile.W))
						str += "W ";
					else
						str += "H ";
				}
			}

			str += "\n";
		}

		return str;
	}
}
