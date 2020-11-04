package Behaviours;

import Agents.Prey;
import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class PreyMovement extends TickerBehaviour{
	
	private Prey agent;

	public PreyMovement(Agent a, long period) {
		super(a, period);
		agent = (Prey) a;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		System.out.println("I'm moving randomly - " + this.agent.getName());
		System.out.println(agent.getMaze().toString());
		
	}

}
