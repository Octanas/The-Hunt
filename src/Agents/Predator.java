package Agents;

import jade.core.*;
import jade.core.behaviours.*;

public class Predator extends Agent {
	
	protected void setup() {
		System.out.println("I'm ready to hunt");
		addBehaviour(new Behaviour(this));
	}
	
	class Behaviour extends CyclicBehaviour {
        private boolean finished = false;

        public Behaviour(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            System.out.println("I'm hunting");
        }

    }

}
