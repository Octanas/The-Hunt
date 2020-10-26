package Behaviours;

import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class PreyMovement extends TickerBehaviour{

	public PreyMovement(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		System.out.println("I'm moving randomly - " + this.getAgent().getName());
		
	}

}
