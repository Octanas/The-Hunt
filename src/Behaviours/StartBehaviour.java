package Behaviours;

import java.util.Random;

import Agents.Predator;
import Agents.Prey;
import jade.core.*;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class StartBehaviour extends SimpleBehaviour{
	
	private Predator agent;

	public StartBehaviour(Agent a) {
		agent = (Predator) a;
		// TODO Auto-generated constructor stub
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
        DFAgentDescription[] predators = agent.getPredators();
        
        String message = "Roll " + String.valueOf(agent.getRolledValue());
        
        for(int i = 0; i < predators.length; i++) {
        	sendMessageTo(predators[i].getName(), ACLMessage.INFORM, message);
        }
        
        
	        
	}
	
	public void sendMessageTo(AID predatorID, int performative, String content) {
		
		ACLMessage message = new ACLMessage(performative);

        message.addReceiver(predatorID);
        message.setContent(content);
        message.addReplyTo(agent.getAID());

        agent.send(message);
        
	}
	
	public int onEnd() {
		return 0;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
