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

public class Predator extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Behaviour patrolBehaviour = new PatrolBehaviour((Predator)this, 1000);
	private Behaviour startBehaviour = new StartBehaviour((Predator)this);
	private Behaviour chaseBehaviour = new ChaseBehaviour((Predator)this, 1000);
	private Behaviour listenerBehaviour = new ListenerBehaviour((Predator)this);
	private Maze maze;
	
	TreeSet<Integer> agentRolls;  
	private int rolledValue;
	private int biasX;
	private int biasY;
	private int preyX;
	private int preyY;
	

	protected void setup() {
		
		agentRolls = new TreeSet<Integer>();
		
		// Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("PredatorAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
		agentDescription.addServices(serviceDescription); // required
		
		//Retrieve arguments
		Object[] args = getArguments();
		maze = (Maze) args[0];
		
		
		maze.registerEntity(getName(), true);
		
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		
		// Adding initial behaviours
		addBehaviour(startBehaviour);
		addBehaviour(new ListenerBehaviour((Predator)this));
	}
	
	
	// Methods
	
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
	
	// Getters
	
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
	
	public TreeSet<Integer> getAgentRolls(){
		return this.agentRolls;
	}
	
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
	
	
	
	// Setters
	
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
	
	public void addAgentRoll(int agentRoll) {
		this.agentRolls.add(agentRoll);
	}











	

}
