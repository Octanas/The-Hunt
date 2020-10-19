package Agents;

import jade.core.*;
import jade.core.behaviours.*;

public class Predator extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		System.out.println("I'm ready to hunt");
		addBehaviour(new Behaviour(this));
	}
	
	class Behaviour extends SimpleBehaviour {
		
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean finished = false;

        public Behaviour(Agent agent) {
            super(agent);
        }
        
        public void onStart() {
        	System.out.println("Started hunting");
        }

        @Override
        public void action() {
            System.out.println("I'm hunting");
            finished = true;
        }

		@Override
		public boolean done() {
			return finished;
		}
        

    }

}
