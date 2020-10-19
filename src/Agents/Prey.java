package Agents;

import Agents.Predator.Behaviour;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;

public class Prey extends Agent{

	protected void setup() {
		System.out.println("I'm ready to run");
		addBehaviour(new Behaviour(this));
	}
	
	class Behaviour extends CyclicBehaviour {
        private boolean finished = false;

        public Behaviour(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            System.out.println("I'm running");
        }

    }
}
