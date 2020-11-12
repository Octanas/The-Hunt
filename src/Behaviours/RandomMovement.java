package Behaviours;

import java.util.Random;

import Agents.Prey;
import Environment.Maze;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class RandomMovement extends TickerBehaviour {
	
	private Prey agent;

	public RandomMovement(Agent a, long period) {
		super(a, period);
		agent = (Prey) a;
	}

	@Override
	protected void onTick() {		
		Maze maze = agent.getMaze();

		if (maze != null) {
			Random rand = new Random();
			
			Maze.Direction direction = maze.getEntities().get(agent.getName()).getDirection();

			boolean success = false;

			while (!success) {
				Maze.Movement chosenMovement;

				do {
					chosenMovement = Maze.Movement.values()[rand.nextInt(Maze.Movement.values().length)];
				} while ((direction == Maze.Direction.North && chosenMovement == Maze.Movement.Down)
						|| (direction == Maze.Direction.South && chosenMovement == Maze.Movement.Up)
						|| (direction == Maze.Direction.East && chosenMovement == Maze.Movement.Left)
						|| (direction == Maze.Direction.West && chosenMovement == Maze.Movement.Right));
				
				success = maze.moveEntity(agent.getName(), chosenMovement);
			}
		}
	}
}
