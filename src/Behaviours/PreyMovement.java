package Behaviours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import Agents.Prey;
import Environment.Maze;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class PreyMovement extends TickerBehaviour {

	private static final long serialVersionUID = 9093704759233114476L;
	
	private int ticksToAvoid = 3;

	private Prey agent;
	/**
	 * Keeps track of movements the prey should avoid to not run into hunters (for a
	 * short period of time).
	 * 
	 * The Integer represents the number of ticks that a movement should be avoided.
	 * It decreases for each tick.
	 */
	private HashMap<Maze.Movement, Integer> movementsToAvoid;

	public PreyMovement(Agent a, long period) {
		super(a, period);
		agent = (Prey) a;
		movementsToAvoid = new HashMap<Maze.Movement, Integer>();

		// Adds all the possible movements to the movementsToAvoid HashMap
		for (Maze.Movement movement : Maze.Movement.values()) {
			movementsToAvoid.put(movement, 0);
		}
	}

	@Override
	protected void onTick() {	
		Maze maze = agent.getMaze();

		if (maze != null) {
			Random rand = new Random();

			Maze.MazeEntity self = maze.getEntities().get(agent.getName());

			if (!self.isCaught()) {
				Maze.Direction direction = self.getDirection();
				Maze.MazeEntity visibleEntity = maze.getVisibleEntity(agent.getName());

				// If there is a visible hunter, mark the movement in the direction it was seen
				// to be avoided for ticksToAvoid ticks
				if (visibleEntity != null && visibleEntity.isHunter) {
					if (direction.equals(Maze.Direction.North)) {
						movementsToAvoid.put(Maze.Movement.Up, ticksToAvoid);
					} else if (direction.equals(Maze.Direction.South)) {
						movementsToAvoid.put(Maze.Movement.Down, ticksToAvoid);
					} else if (direction.equals(Maze.Direction.East)) {
						movementsToAvoid.put(Maze.Movement.Right, ticksToAvoid);
					} else if (direction.equals(Maze.Direction.West)) {
						movementsToAvoid.put(Maze.Movement.Left, ticksToAvoid);
					}
				}

				// Get list with all the possible movements
				ArrayList<Maze.Movement> possibleMovements = new ArrayList<Maze.Movement>(
						Arrays.asList(Maze.Movement.values()));

				// Order the movements from best to worst, according to the number of ticks that
				// it should be avoided
				//
				// Movements with the same number of ticks that they should be avoided are
				// randomised in the order between them
				// A movement to go backwards will always be worse than another movement with
				// the same number of ticks that it should be avoided
				for (int i = 0; i < possibleMovements.size() - 1; i++) {
					for (int j = 0; j < possibleMovements.size() - i - 1; j++) {
						if (movementsToAvoid.get(possibleMovements.get(j)) > movementsToAvoid
								.get(possibleMovements.get(j + 1))
								|| movementsToAvoid.get(possibleMovements.get(j)) == movementsToAvoid
										.get(possibleMovements.get(j + 1))
										&& (direction.equals(Maze.Direction.North)
												&& possibleMovements.get(j).equals(Maze.Movement.Down)
												|| direction.equals(Maze.Direction.South)
														&& possibleMovements.get(j).equals(Maze.Movement.Up)
												|| direction.equals(Maze.Direction.East)
														&& possibleMovements.get(j).equals(Maze.Movement.Left)
												|| direction.equals(Maze.Direction.West)
														&& possibleMovements.get(j).equals(Maze.Movement.Right)
												|| rand.nextBoolean())
										&& !(direction.equals(Maze.Direction.North)
												&& possibleMovements.get(j + 1).equals(Maze.Movement.Down)
												|| direction.equals(Maze.Direction.South)
														&& possibleMovements.get(j + 1).equals(Maze.Movement.Up)
												|| direction.equals(Maze.Direction.East)
														&& possibleMovements.get(j + 1).equals(Maze.Movement.Left)
												|| direction.equals(Maze.Direction.West)
														&& possibleMovements.get(j + 1).equals(Maze.Movement.Right))) {
							Maze.Movement movement = possibleMovements.remove(j + 1);
							possibleMovements.add(j, movement);
						}
					}
				}

				int i = 0;

				// Follow the list until a movement is valid (the worst movement will always be
				// the last one to be tried)
				while (!maze.moveEntity(agent.getName(), possibleMovements.get(i)) && i != possibleMovements.size())
					i++;

				// Decrease a tick from each movement (the ones at 0 stay at 0)
				for (Maze.Movement movement : movementsToAvoid.keySet()) {
					movementsToAvoid.put(movement, Math.max(0, movementsToAvoid.get(movement) - 1));
				}
			}
			else {
				stop();	// Ticker Behaviour will stop repeating
			}
		}
	}

}
