package Behaviours;

import java.util.List;

import Agents.SuperAgent;
import Environment.Astar;
import Environment.Maze;
import Environment.Vertex;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ChaseBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -8035807564820299915L;

	private SuperAgent agent;

	private List<Maze.Movement> movementsToTake;

	private int ticksToGiveUp;
	private int ticksWithoutUpdate;

	private int previousPreyX;
	private int previousPreyY;

	public ChaseBehaviour(Agent a, long period, int ticksToGiveUp) {
		super(a, period);

		agent = (SuperAgent) a;

		movementsToTake = null;

		this.ticksToGiveUp = ticksToGiveUp;
		ticksWithoutUpdate = 0;

		previousPreyX = -1;
		previousPreyY = -1;
	}

	@Override
	protected void onTick() {
		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());

		// No updates in a long time, giving up
		if (ticksWithoutUpdate >= ticksToGiveUp) {
			System.out.println("Agent " + agent.getName() + ": Giving up on chase");

			agent.addBehaviour(agent.getPatrolBehaviour());
			stop();
			return;
		}

		// New position for prey was communicated, so get a new path
		if (previousPreyX != agent.getPreyX() || previousPreyY != agent.getPreyY()) {
			movementsToTake = null;
			ticksWithoutUpdate = 0;
		} else {
			ticksWithoutUpdate++;
		}

		// movementsToTake is null, so it's should get new path to follow
		if (movementsToTake == null) {
			Vertex init = new Vertex(self.getXCoordinate(), self.getYCoordinate());
			Vertex prey = new Vertex(agent.getPreyX(), agent.getPreyY());
			previousPreyX = agent.getPreyX();
			previousPreyY = agent.getPreyY();

			Astar astar = new Astar(maze.getGraph(), init, prey);
			int proc = astar.process();
			if(proc != 0) {
				if (proc == 1) {
					System.out.println("Unreachable Vertex...");
					return;
				}
				else if (proc == 2) {
					System.out.println("Error...");
					return;
				}
			}
			List<Vertex> path = astar.getPath();

			movementsToTake = Maze.convertVertexPathToMovements(path);

			if (movementsToTake == null) {
				System.out.println("Agent " + agent.getName() + ": Something is wrong, path is invalid");
			}
		}

		if (movementsToTake != null) {
			if (!movementsToTake.isEmpty()) {
				boolean success = maze.moveEntity(agent.getName(), movementsToTake.get(0));

				if (success) {
					movementsToTake.remove(0);
				} else {
					System.out.println("Agent " + agent.getName() + ": Something is wrong, path does not work");

					// Path does not work, so a new path should be calculated
					movementsToTake = null;
				}
			}

			// Every move has been made, get new path
			if (movementsToTake.isEmpty())
				movementsToTake = null;
		}
	}

}
