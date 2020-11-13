package Behaviours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import Agents.Predator;
import Agents.Prey;
import Environment.Maze;
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
	public void onStart() {
		
		int i = 0;
		Iterator iterator = agent.getAgentRolls().iterator();
		while (iterator.hasNext()) {
			if((int)iterator.next() == agent.getRolledValue()) break;
		}


		switch (i) {
			case 0:
				agent.setBiasX(3);
				agent.setBiasY(3);
				break;
			case 1:
				agent.setBiasX(13);
				agent.setBiasY(3);
				break;
			case 2:
				agent.setBiasX(3);
				agent.setBiasY(15);
				break;
			case 3:
				agent.setBiasX(13);
				agent.setBiasY(15);
				break;
			default:
				break;
		}
		
	}
	
	@Override
	protected void onTick() {
	        
		Maze maze = agent.getMaze();
		Maze.MazeEntity self = maze.getEntities().get(agent.getName());
		Maze.Direction direction = self.getDirection();
		
		// Calculate chances based in distance to Bias
		int chanceX = Math.abs(self.getXCoordinate() - agent.getBiasX());
		int chanceY = Math.abs(self.getYCoordinate() - agent.getBiasY());
		
		
		
		
		
		
		
		// Verify is prey is in vision
		Maze.MazeEntity visibleEntity = maze.getVisibleEntity(agent.getName());
		// If yes send info to other hunters
		if(visibleEntity != null && !visibleEntity.isHunter) {
			
			DFAgentDescription[] predators = agent.getPredators();
	        String message = "Prey " + String.valueOf(visibleEntity.getXCoordinate()) + " " + String.valueOf(visibleEntity.getXCoordinate());
	        
	        for(int i = 0; i < predators.length; i++) {
	        	sendMessageTo(predators[i].getName(), ACLMessage.INFORM, message);
	        }
	        agent.setPreyX(visibleEntity.getXCoordinate());
    		agent.setPreyY(visibleEntity.getXCoordinate());
    		agent.addBehaviour(agent.getChaseBehaviour());
			stop();
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
		System.out.println("Patrol Behaviour Stopped!");
		return 0;
	}

}
