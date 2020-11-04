package Agents;

import Behaviours.ListenerBehaviour;
import Behaviours.PatrolBehaviour;
import Environment.Maze;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Predator extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Behaviour defaultBehaviour = new PatrolBehaviour(this, 1000);
	private Maze maze;

	protected void setup() {
		
		// Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("PredatorAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
		agentDescription.addServices(serviceDescription); // required
		
		//Retrieve arguments
		Object[] args = getArguments();
		maze = (Maze) args[0];
		
		
		maze.registerEntity(getName(), true);
		
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		
		// Adding initial behaviours
		addBehaviour(defaultBehaviour);
		addBehaviour(new ListenerBehaviour(this, 1000));
	}
	
	public void removeDefaultBehaviour(){
		
		this.removeBehaviour(defaultBehaviour);
		
	}
	
	public Maze getMaze() {
		return maze;
	}

}
