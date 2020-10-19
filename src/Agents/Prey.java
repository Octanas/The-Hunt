package Agents;


import jade.core.*;
import jade.core.behaviours.SimpleBehaviour;

public class Prey extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		System.out.println("I'm ready to run");
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
        	System.out.println("Started running");
        }

        @Override
        public void action() {
            System.out.println("I'm running");
            finished = true;
        }
        
        @Override
		public boolean done() {
			return finished;
		}       

    }
}
