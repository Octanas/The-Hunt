package Behaviours;

import java.util.List;

import Agents.Predator;
import Environment.Astar;
import Environment.Maze;
import Environment.Vertex;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ChaseBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -8035807564820299915L;

	private Predator agent;

	private List<Maze.Movement> movementsToTake;

	public ChaseBehaviour(Agent a, long period) {
		super(a, period);
		
		agent = (Predator) a;
		movementsToTake = null;
	}

	@Override
	protected void onTick() {

		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());

		// movementsToTake is null, so it's should get new path to follow
		if (movementsToTake == null) {
			Vertex init = new Vertex(self.getXCoordinate(), self.getYCoordinate());
			Vertex prey = new Vertex(agent.getPreyX(), agent.getPreyY());

			Astar astar = new Astar(maze.getGraph(), init, prey);
			astar.process();
			List<Vertex> path = astar.getPath();

			movementsToTake = Maze.convertVertexPathToMovements(path);

			if (movementsToTake == null) {
				System.out.println("Agent "  + agent.getName() + ": Something is wrong, path is invalid");
			}
		}

		if(movementsToTake != null) {
			if (!movementsToTake.isEmpty()) {
				boolean success = maze.moveEntity(agent.getName(), movementsToTake.get(0));
	
				if (success) {
					movementsToTake.remove(0);
				} else {
					System.out.println("Agent "  + agent.getName() + ": Something is wrong, path does not work");
	
					// Path does not work, so a new path should be calculated
					movementsToTake = null;
				}
			}
	
			// Every move has been made, get new path
			if(movementsToTake.isEmpty())
				movementsToTake = null;
		}
	}

}
