package Agents;

import java.util.TreeSet;

import Behaviours.ChaseBehaviour;
import Behaviours.ListenerBehaviour;
import Behaviours.PatrolBehaviour;
import Behaviours.StartBehaviour;
import Environment.Maze;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SuperAgent extends Agent {

	int agentSpeed = 750;
	int ticksToGiveUpChase = 6;

	Behaviour currentBehaviour;
 
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
		currentBehaviour = null;
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

    // Getters

    public Behaviour getStartBehaviour() {
		return new StartBehaviour(this);
	}

	public Behaviour getListenerBehaviour() {
		return new ListenerBehaviour(this);
	}
	
	public Behaviour getPatrolBehaviour() {
		return new PatrolBehaviour(this, agentSpeed);
	}
	
	public Behaviour getChaseBehaviour() {
		return new ChaseBehaviour(this, agentSpeed, ticksToGiveUpChase);
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

	public void removeCurrentBehaviour() {
		if(currentBehaviour instanceof TickerBehaviour) {
			System.out.println(this.getName());
			((TickerBehaviour) currentBehaviour).stop();
		}

		this.removeBehaviour(currentBehaviour);
		currentBehaviour = null;
	}
	
	public void setCurrentBehaviour(Behaviour behaviour) {
		currentBehaviour = behaviour;
		this.addBehaviour(behaviour);
	}

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
