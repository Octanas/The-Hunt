package Agents;

import java.util.HashMap;

import Behaviours.PreyMovement;
import Behaviours.SightBehaviour;
import Environment.Maze;
import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Prey extends Agent {

	private static final long serialVersionUID = 2160457764102179509L;

	private Maze maze;

	/**
	 * Keeps track of movements the prey should avoid to not run into hunters (for a
	 * short period of time).
	 * 
	 * The Integer represents the number of ticks that a movement should be avoided.
	 * It decreases for each tick.
	 * 
	 * This is only used with the PreyMovement behaviour
	 */
	private HashMap<Maze.Movement, Integer> movementsToAvoid;

	private int ticksToAvoid = 3;

	protected void setup() {

		// Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("PreyAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
		agentDescription.addServices(serviceDescription); // required

		// Retrieve arguments
		Object[] args = getArguments();
		maze = (Maze) args[0];
		boolean fixedPrey = (boolean) args[1];

		if (fixedPrey)
			maze.registerEntity(getName(), false, 9, 16);
		else
			maze.registerEntity(getName(), false);

		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		movementsToAvoid = new HashMap<Maze.Movement, Integer>();

		// Add sight behaviour
		addBehaviour(new SightBehaviour(this, 100, false));

		// Add movement behavior
		addBehaviour(new PreyMovement(this, 500));
		// addBehaviour(new RandomMovement(this, 500));
	}

	public Maze getMaze() {
		return maze;
	}

	public HashMap<Maze.Movement, Integer> getMovementsToAvoid() {
		return movementsToAvoid;
	}

	public int getTicksToAvoid() {
		return ticksToAvoid;
	}
}
