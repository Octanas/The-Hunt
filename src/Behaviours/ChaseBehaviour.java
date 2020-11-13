package Behaviours;

import java.util.List;

import Agents.Predator;
import Environment.Astar;
import Environment.Graph;
import Environment.Vertex;
import Environment.Maze;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class ChaseBehaviour extends TickerBehaviour{
	
	private Predator agent;

	public ChaseBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		
		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());
		Vertex init = new Vertex(self.getXCoordinate(), self.getYCoordinate());
		Vertex prey = new Vertex(13, 3);
		Astar astar = new Astar(maze.getGraph(), init, prey);
		
		List<Vertex> path = astar.getPath();
		
	}

}
