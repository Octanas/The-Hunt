package Behaviours;

import java.util.Random;

import Agents.SuperAgent;
import jade.core.*;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class StartBehaviour extends SimpleBehaviour{
	
	private SuperAgent agent;

	private boolean done;

	public StartBehaviour(Agent a) {
		agent = (SuperAgent) a;
		done = false;
	}

	@Override
	public void action() {
	        
		// create instance of Random class 
        Random rand = new Random();
		
        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);
	
        // Associate rand with agent
        agent.setRolledValue(rand_int1);
        
        // Send info to other Predators
        DFAgentDescription[] observers = agent.getObservers();
        
		String message = "Roll " + String.valueOf(agent.getRolledValue());
        
        for(int i = 0; i < observers.length; i++) {
			agent.sendMessageTo(observers[i].getName(), ACLMessage.INFORM, message);
		}
		
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}
}
