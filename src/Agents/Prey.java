package Agents;

import Behaviours.PreyMovement;
import Behaviours.RandomMovement;
import Environment.Maze;
import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Prey extends Agent {

	private static final long serialVersionUID = 2160457764102179509L;
	
	private Maze maze;

	protected void setup() {

		// Registration with the DF
		// Registration with the DF
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

		maze.registerEntity(getName(), false);

		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// Adding initial behaviours
		addBehaviour(new PreyMovement(this, 500));
		// addBehaviour(new RandomMovement(this, 500));
	}

	public Maze getMaze() {
		return maze;
	}

}
