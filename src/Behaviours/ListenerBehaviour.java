package Behaviours;

import Agents.Predator;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class ListenerBehaviour extends CyclicBehaviour{

	private Predator agent;
	
	public ListenerBehaviour(Agent a) {
		agent = (Predator) a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		
		//Temporary print for debug/testing purposes
				//System.out.println("I'm listening - " + this.getAgent().getName());
				
				//Waiting for a message
				ACLMessage message = agent.receive();
				
				if(message != null) {
					analyseMessage(message);
				}
		
	}

	private void analyseMessage(ACLMessage message) {

		int type = message.getPerformative();
		//System.out.println("Content:" + message.getContent());
		
		String[] messageArray = message.getContent().split(" ");

        switch (type) {
            case ACLMessage.INFORM:
            	switch (messageArray[0]) {
                	case "Roll":
                		agent.addAgentRoll(Integer.parseInt(messageArray[1]));
                		if(agent.getAgentRolls().size() == 4) {
                			agent.addBehaviour(agent.getPatrolBehaviour());
                		}
                		break;
                	case "Prey": 
                		agent.setPreyX(Integer.parseInt(messageArray[1]));
                		agent.setPreyY(Integer.parseInt(messageArray[2]));
                		agent.addBehaviour(agent.getChaseBehaviour());
                		break;
                	default:
                		break;
            	}
                break;
            case ACLMessage.PROPOSE:
                break;
            case ACLMessage.ACCEPT_PROPOSAL:
                break;
            case ACLMessage.REJECT_PROPOSAL:
                break;
            default:
            	break;
                //do nothing
        }
		
	}

	

}
