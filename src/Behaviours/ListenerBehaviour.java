package Behaviours;

import Agents.Predator;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class ListenerBehaviour extends TickerBehaviour{

	public ListenerBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick() {
		//Temporary print for debug/testing purposes
		//System.out.println("I'm listening - " + this.getAgent().getName());
		
		//Waiting for a message
		ACLMessage message = getAgent().receive();
		
		if(message != null) {
			analyseMessage(message);
		}
		
		
	}

	private void analyseMessage(ACLMessage message) {

		int type = message.getPerformative();

        switch (type) {
            case ACLMessage.INFORM:
            	System.out.println("Performative:" + message.getPerformative());
                System.out.println("Content:" + message.getContent());
                System.out.println("Sender:" + message.getSender());
                getAgent().addBehaviour(new ChaseBehaviour(getAgent(), 2000));
                Predator predator = (Predator) getAgent();
                predator.removeDefaultBehaviour();
                break;
            case ACLMessage.PROPOSE:
                break;
            case ACLMessage.ACCEPT_PROPOSAL:
                break;
            case ACLMessage.REJECT_PROPOSAL:
                break;
            default:
                //do nothing
        }
		
	}

}
