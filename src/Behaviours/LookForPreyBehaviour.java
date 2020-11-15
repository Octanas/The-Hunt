package Behaviours;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Agents.SuperAgent;
import Environment.Maze;
import Environment.Maze.Movement;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class LookForPreyBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -3323288587398781218L;
	private SuperAgent agent;
	private int ticks;

	public LookForPreyBehaviour(Agent a, long period, int ticks) {
		super(a, period);
		this.ticks = ticks;
		agent = (SuperAgent) a;

		System.out.println("Agent " + agent.getLocalName() + ": Starting looking for prey!");

		// Sets entity as looking
		if (agent.getMaze() != null && agent.getMaze().getEntities().get(agent.getName()) != null) {
			agent.getMaze().getEntities().get(agent.getName()).setAlert(false);
			agent.getMaze().getEntities().get(agent.getName()).setLooking(true);
		}
	}

	@Override
	protected void onTick() {

		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());

		// Calculate chances based on distance to Bias
		int chanceX = ((self.getXCoordinate() - agent.getPreyX()) * 7) / 100;
		int chanceY = ((self.getYCoordinate() - agent.getPreyY()) * 5) / 100;

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

		if (getTickCount() == ticks) {
			agent.removeCurrentBehaviour();
			agent.setCurrentBehaviour(agent.getPatrolBehaviour());

			// Sets entity as not looking
			self.setLooking(false);
		}
	}
}
