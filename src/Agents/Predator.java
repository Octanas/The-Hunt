package Agents;

import Environment.Maze;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Predator extends SuperAgent {

	private static final long serialVersionUID = -8218305308654229995L;

	protected void setup() {
		super.setup();

		// Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("PredatorAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
		agentDescription.addServices(serviceDescription); // required

		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		//Retrieve arguments
		Object[] args = getArguments();
		setMaze((Maze) args[0]);
	
		getMaze().registerEntity(getName(), true);		
		
		// Adding initial behaviours
		setCurrentBehaviour(getStartBehaviour());
		addBehaviour(getListenerBehaviour());
	}

}
