package Behaviours;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Agents.SuperAgent;
import Environment.Maze;
import Environment.Maze.Movement;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class PatrolBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -8739555362845174914L;

	private SuperAgent agent;

	public PatrolBehaviour(Agent a, long period) {
		super(a, period);
		agent = (SuperAgent) a;
	}

	@Override
	protected void onTick() {

		// System.out.println("Patrolling");

		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());

		// Calculate chances based on distance to Bias
		int chanceX = ((self.getXCoordinate() - agent.getBiasX()) * 7) / 100;
		int chanceY = ((self.getYCoordinate() - agent.getBiasY()) * 5) / 100;

		List<Movement> possibleMovements = maze.possibleMovements(self.getXCoordinate(), self.getYCoordinate());

		if (possibleMovements.size() == 2) {
			for (Movement movement : possibleMovements) {
				if (!(self.getDirection().equals(Maze.Direction.North) && movement.equals(Maze.Movement.Down)
						|| self.getDirection().equals(Maze.Direction.South) && movement.equals(Maze.Movement.Up)
						|| self.getDirection().equals(Maze.Direction.East) && movement.equals(Maze.Movement.Left)
						|| self.getDirection().equals(Maze.Direction.West) && movement.equals(Maze.Movement.Right))) {
					maze.moveEntity(agent.getName(), movement);
				}
			}
		} else {
			int movementChance = 100 / possibleMovements.size();

			HashMap<Movement, Integer> moveChances = new HashMap<Movement, Integer>();

			for (Movement movement : Maze.Movement.values()) {
				if (possibleMovements.contains(movement)) {
					moveChances.put(movement, movementChance);
				} else {
					moveChances.put(movement, 0);
				}
			}

			moveChances.put(Movement.Left,
					Math.round(moveChances.get(Movement.Left) - moveChances.get(Movement.Left) * chanceX));
			moveChances.put(Movement.Right,
					Math.round(moveChances.get(Movement.Right) + moveChances.get(Movement.Right) * chanceX));
			moveChances.put(Movement.Up,
					Math.round(moveChances.get(Movement.Up) - moveChances.get(Movement.Up) * chanceY));
			moveChances.put(Movement.Down,
					Math.round(moveChances.get(Movement.Down) + moveChances.get(Movement.Down) * chanceY));

			movementChance = moveChances.get(Movement.Left) + moveChances.get(Movement.Right)
					+ moveChances.get(Movement.Up) + moveChances.get(Movement.Down);

			Random rand = new Random();
			int randMovement = rand.nextInt(movementChance);

			if (randMovement < moveChances.get(Movement.Left)) {
				maze.moveEntity(agent.getName(), Movement.Left);
			} else if (randMovement < (moveChances.get(Movement.Left) + moveChances.get(Movement.Right))) {
				maze.moveEntity(agent.getName(), Movement.Right);
			} else if (randMovement < (moveChances.get(Movement.Left) + moveChances.get(Movement.Right)
					+ moveChances.get(Movement.Up))) {
				maze.moveEntity(agent.getName(), Movement.Up);
			} else if (randMovement < (moveChances.get(Movement.Left) + moveChances.get(Movement.Right)
					+ moveChances.get(Movement.Up) + moveChances.get(Movement.Down))) {
				maze.moveEntity(agent.getName(), Movement.Down);
			}
		}
	}
}
