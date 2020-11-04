package Behaviours;

import java.util.Random;

import Agents.Predator;
import Agents.Prey;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class PatrolBehaviour extends TickerBehaviour{
	
	private Predator agent;

	public PatrolBehaviour(Agent a, long period) {
		super(a, period);
		agent = (Predator) a;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
	        
        if(agent.getName().equals("Predator 1@192.168.1.188:1099/JADE")) {
        	System.out.println("We should chase! - " + agent.getName());
        	agent.addBehaviour(new ChaseBehaviour(agent, 1000));
        	
        	// search for other predators
        	DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription  = new ServiceDescription();
            serviceDescription.setType( "PredatorAgent" );
            agentDescription.addServices(serviceDescription);
            
            DFAgentDescription[] result = null;
			try {
				result = DFService.search(agent, agentDescription);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
            
            System.out.println(result.length + " results" );
            if (result.length>0) {
            	for(int i = 0; i < result.length; i++) {
            		sendMessageTo(result[i].getName(), ACLMessage.INFORM, "Prey was seen!");
            	}
            }

        	stop();
        }
        else {
        	System.out.println("I'm patroling - " + this.agent.getName());
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

}
