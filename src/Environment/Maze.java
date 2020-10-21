package Environment;

import java.util.HashMap;

public class Maze {
	private enum Direction { North, South, East, West };
	private enum Movement { Up, Down, Left, Right };
	private enum Tile { 
		W,	// WALL
		C, 	// CORRIDOR
		H	// HUNTER ZONE
	};
	
	// TODO: should the Maze class know who is the hunter and who is the beast?
	private class MazeEntity {
		Direction direction;
		int xCoordinate;
		int yCoordinate;
		
		public MazeEntity(int xCoordinate, int yCoordinate, Direction direction) {
			this.xCoordinate = xCoordinate;
			this.yCoordinate = yCoordinate;
			this.direction = direction;
		}
	}
	
	private static Tile[][] defaultMaze = new Tile[][] {
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.H, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.H, Tile.H, Tile.H, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.W, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W, Tile.C, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.C, Tile.W},
		{Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.C, Tile.W},
		{Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W, Tile.W}
	};
	
	private Tile[][] maze;
	private HashMap<String, MazeEntity> entities;
	
	/**
	 * Creates a Maze object with the default Pac-Man maze.
	 * @return The initialized maze object.
	 */
	public static Maze defaultMaze() {
		return new Maze(defaultMaze);
	}
	
	private Maze(Tile[][] maze) {
		this.maze = maze;
		
		entities = new HashMap<String, MazeEntity>();
	}
	
	/**
	 * Adds an entity to the maze.
	 * 
	 * If it fails, then there's already a entity with the chosen name.
	 * @param name			Name of the entity.
	 * @param xCoordinate	Initial X coordinate.
	 * @param yCoordinate	Initial Y coordinate.
	 * @return				If the entity was added.
	 */
	public boolean registerEntity(String name, int xCoordinate, int yCoordinate) {
		if(entities.containsKey(name))
			return false;
		
		entities.put(name, new MazeEntity(xCoordinate, yCoordinate, Direction.North));
		
		return true;
	}
	
	/**
	 * Moves the entity on the maze.
	 * 
	 * It can fail if there is no entity with the specified name or if there's an obstacle in the specified direction.
	 * @param name		Name of the entity.
	 * @param movement	Direction in which the entity will move.
	 * @return			If it succeeded.				
	 */
	public boolean moveEntity(String name, Movement movement) {
		MazeEntity entity = entities.get(name);
		
		if(entity == null)
			return false;
		
		if(movement.equals(Movement.Up)) {
			if(entity.yCoordinate > 0 && maze[entity.yCoordinate - 1][entity.xCoordinate].equals(Tile.C)) {
				entity.yCoordinate -= 1;
				entity.direction = Direction.North;
			}
			else
				return false;
		}
		else if(movement.equals(Movement.Down)) {
			if(entity.yCoordinate < maze.length - 1 && maze[entity.yCoordinate + 1][entity.xCoordinate].equals(Tile.C)) {
				entity.yCoordinate += 1;
				entity.direction = Direction.South;
			}
			else
				return false;
		}
		else if(movement.equals(Movement.Left)) {
			if(entity.xCoordinate > 0 && maze[entity.yCoordinate][entity.xCoordinate - 1].equals(Tile.C)) {
				entity.xCoordinate -= 1;
				entity.direction = Direction.West;
			}
			else
				return false;
		}
		else if(movement.equals(Movement.Right)) {
			if(entity.xCoordinate < maze[0].length && maze[entity.yCoordinate][entity.xCoordinate + 1].equals(Tile.C)) {
				entity.xCoordinate += 1;
				entity.direction = Direction.East;
			}
			else
				return false;
		}
		else
			return false;
		
		entities.put(name, entity);
		
		return true;
	}
	
	/**
	 * Rotates the entity in the maze.
	 * 
	 * It fails if there is no entity with the specified name.
	 * @param name		Name of the entity.
	 * @param direction	Direction the entity will rotate to.
	 * @return			If it succeeded.
	 */
	public boolean rotateEntity(String name, Direction direction) {
		MazeEntity entity = entities.get(name);
		
		if(entity == null)
			return false;
		
		entity.direction = direction;
		
		entities.put(name, entity);
		
		return true;
	}
	
	// TODO: implement sight method
	
	// TODO: represent entities in the toString() output
	public String toString() {
		String str = "";
		
		for(int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if(maze[i][j].equals(Tile.C))
					str += "C ";
				else if(maze[i][j].equals(Tile.W))
					str += "W ";
				else
					str += "H ";
			}
			
			str += "\n";
		}
		
		return str;
	}
}
