package Agents;


import Behaviours.PreyMovement;
import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Prey extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		
		// Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("PreyrAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
		agentDescription.addServices(serviceDescription); // required
		
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
				
		// Adding initial behaviours
		addBehaviour(new PreyMovement(this, 1000));
	}
	
}
