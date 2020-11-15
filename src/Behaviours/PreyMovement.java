package Behaviours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Agents.Prey;
import Environment.Maze;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class PreyMovement extends TickerBehaviour {

	private static final long serialVersionUID = 9093704759233114476L;

	private Prey agent;

	public PreyMovement(Agent a, long period) {
		super(a, period);
		agent = (Prey) a;

		// Adds all the possible movements to the movementsToAvoid HashMap
		for (Maze.Movement movement : Maze.Movement.values()) {
			agent.getMovementsToAvoid().put(movement, 0);
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
						if (agent.getMovementsToAvoid().get(possibleMovements.get(j)) > agent.getMovementsToAvoid()
								.get(possibleMovements.get(j + 1))
								|| agent.getMovementsToAvoid().get(possibleMovements.get(j)) == agent
										.getMovementsToAvoid().get(possibleMovements.get(j + 1))
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

				// If not all ticks are at 0, the prey is in alert
				boolean inAlert = false;

				// Decrease a tick from each movement (the ones at 0 stay at 0)
				for (Maze.Movement movement : agent.getMovementsToAvoid().keySet()) {
					int currTick = agent.getMovementsToAvoid().get(movement);

					if (currTick != 0) {
						agent.getMovementsToAvoid().put(movement, agent.getMovementsToAvoid().get(movement) - 1);

						inAlert = true;
					}
				}

				// Toggle alert for entity
				self.setAlert(inAlert);
			} else {
				stop(); // Ticker Behaviour will stop repeating
			}
		}
	}

}
