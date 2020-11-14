package Behaviours;

import java.util.List;

import Agents.SuperAgent;
import Environment.Astar;
import Environment.Maze;
import Environment.Vertex;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

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

		System.out.println("Agent " + agent.getLocalName() + ": Starting chase!");
	}

	@Override
	protected void onTick() {
		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());

		// Verify is prey is in vision
		Maze.MazeEntity visibleEntity = maze.getVisibleEntity(agent.getName());
		// If yes send info to other hunters
		if (visibleEntity != null && !visibleEntity.isHunter && !visibleEntity.isCaught()) {

			DFAgentDescription[] predators = agent.getPredators();
			String message = "Prey " + String.valueOf(visibleEntity.getXCoordinate()) + " "
					+ String.valueOf(visibleEntity.getYCoordinate());

			for (int i = 0; i < predators.length; i++) {
				agent.sendMessageTo(predators[i].getName(), ACLMessage.INFORM, message);
			}
			agent.setPreyX(visibleEntity.getXCoordinate());
			agent.setPreyY(visibleEntity.getYCoordinate());
		}

		// New position for prey was communicated, so get a new path
		if (previousPreyX != agent.getPreyX() || previousPreyY != agent.getPreyY()) {
			movementsToTake = null;
			ticksWithoutUpdate = 0;
		} else {
			ticksWithoutUpdate++;
		}

		// No updates in a long time, giving up
		if (ticksWithoutUpdate >= ticksToGiveUp) {
			System.out.println("Agent " + agent.getLocalName() + ": Giving up on chase");

			agent.removeCurrentBehaviour();
			agent.setCurrentBehaviour(agent.getPatrolBehaviour());
			return;
		}

		// movementsToTake is null, so it's should get new path to follow
		if (movementsToTake == null) {
			Vertex init = new Vertex(self.getXCoordinate(), self.getYCoordinate());
			Vertex prey = new Vertex(agent.getPreyX(), agent.getPreyY());
			previousPreyX = agent.getPreyX();
			previousPreyY = agent.getPreyY();

			Astar astar = new Astar(maze.getGraph(), init, prey);
			int proc = astar.process();

			if (proc == 0) {
				List<Vertex> path = astar.getPath();

				movementsToTake = Maze.convertVertexPathToMovements(path);
			} else {
				if (proc == 1) {
					System.out.println("Unreachable Vertex...");
				} else if (proc == 2) {
					System.out.println("Error...");
				}

				movementsToTake = null;
			}

			if (movementsToTake == null) {
				System.out.println("Agent " + agent.getLocalName() + ": Something is wrong, path is invalid");
			}
		}

		if (movementsToTake != null) {
			if (!movementsToTake.isEmpty()) {
				boolean success = maze.moveEntity(agent.getName(), movementsToTake.get(0));

				if (success) {
					movementsToTake.remove(0);
				} else {
					System.out.println("Agent " + agent.getLocalName() + ": Something is wrong, path does not work");

					// Path does not work, so a new path should be calculated
					movementsToTake = null;
				}
			}

			// Every move has been made, get new path
			if (movementsToTake != null && movementsToTake.isEmpty())
				movementsToTake = null;
		}
	}

}
