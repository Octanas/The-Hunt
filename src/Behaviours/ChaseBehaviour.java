package Behaviours;

import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class ChaseBehaviour extends TickerBehaviour{

	public ChaseBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		System.out.println("I'm chasing - " + this.getAgent().getName());
		
	}

}
