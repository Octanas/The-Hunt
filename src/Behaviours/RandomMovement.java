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

				// This do while makes sure the chosen movement is not a backwards movement
				do {
					chosenMovement = Maze.Movement.values()[rand.nextInt(Maze.Movement.values().length)];
				} while (direction.equals(Maze.Direction.North) && chosenMovement.equals(Maze.Movement.Down)
						|| direction.equals(Maze.Direction.South) && chosenMovement.equals(Maze.Movement.Up)
						|| direction.equals(Maze.Direction.East) && chosenMovement.equals(Maze.Movement.Left)
						|| direction.equals(Maze.Direction.West) && chosenMovement.equals(Maze.Movement.Right));

				success = maze.moveEntity(agent.getName(), chosenMovement);
			}
		}
	}
}
