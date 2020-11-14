package Agents;

import java.util.TreeSet;

import Behaviours.ChaseBehaviour;
import Behaviours.ListenerBehaviour;
import Behaviours.PatrolBehaviour;
import Behaviours.StartBehaviour;
import Environment.Maze;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SuperAgent extends Agent {

    private Behaviour patrolBehaviour = new PatrolBehaviour(this, 750);
	private Behaviour startBehaviour = new StartBehaviour(this);
	private Behaviour chaseBehaviour = new ChaseBehaviour(this, 750, 6);
    private Behaviour listenerBehaviour = new ListenerBehaviour(this);
    
    private TreeSet<Integer> agentRolls;
    private int rolledValue;
	private int biasX;
	private int biasY;
	private int preyX;
    private int preyY;
    
    private Maze maze;

    protected void setup() {

        // Initialize agent rolls 
        agentRolls = new TreeSet<Integer>();

	}
    

    // Methods

	public void sendMessageTo(AID predatorID, int performative, String content) {
		
		ACLMessage message = new ACLMessage(performative);

        message.addReceiver(predatorID);
        message.setContent(content);
        message.addReplyTo(this.getAID());

        this.send(message);
        
	}
	
	public DFAgentDescription[] getPredators() {
		
		DFAgentDescription agentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType( "PredatorAgent" );
        agentDescription.addServices(serviceDescription);
        
        DFAgentDescription[] result = null;
		try {
			result = DFService.search(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public DFAgentDescription[] getObservers() {
		
		DFAgentDescription agentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType( "ObserverAgent" );
        agentDescription.addServices(serviceDescription);
        
        DFAgentDescription[] result = null;
		try {
			result = DFService.search(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		return result;
		
    }
    
    public void removeBehaviour(String behaviour){
		
		switch (behaviour) {
			case "start": 
				this.removeBehaviour(startBehaviour);
				return;
			case "patrol": 
				this.removeBehaviour(patrolBehaviour);
				return;
			case "chase": 
				this.removeBehaviour(chaseBehaviour);
				return;
			default: return;
		}
		
	}
	
    // Getters

    public Behaviour getStartBehaviour() {
		return startBehaviour;
	}

	public Behaviour getListenerBehaviour() {
		return listenerBehaviour;
	}
	
	public Behaviour getPatrolBehaviour() {
		return patrolBehaviour;
	}
	
	public Behaviour getChaseBehaviour() {
		return chaseBehaviour;
	}
    
    public TreeSet<Integer> getAgentRolls(){
		return this.agentRolls;
    }
    
    public Maze getMaze() {
		return maze;
	}
	
	public int getRolledValue() {
		return rolledValue;
	}

	public int getBiasX() {
		return biasX;
	}
	
	public int getBiasY() {
		return biasY;
	}
	
	public int getPreyX() {
		return preyX;
	}
	
	public int getPreyY() {
		return preyY;
    }
    
    // Setters

    public void addAgentRoll(int agentRoll) {
		this.agentRolls.add(agentRoll);
    }
    
    public void setRolledValue(int rolledValue) {
		this.rolledValue = rolledValue;
	}
	
	public void setBiasX(int biasX) {
		this.biasX = biasX;
	}
	
	public void setBiasY(int biasY) {
		this.biasY = biasY;
	}
	
	public void setPreyX(int preyX) {
		this.preyX = preyX;
	}
	
	public void setPreyY(int preyY) {
		this.preyY = preyY;
    }
    
    public void setMaze(Maze maze){
        this.maze = maze;
    }

}
