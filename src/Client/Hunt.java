package Client;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Hunt {

	public static void main(String[] args) {
		
		//Start JADE
        Runtime runt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        //Add agents
        AgentContainer mainContainer = runt.createMainContainer(profile);
        try {
            AgentController predatorController = mainContainer.createNewAgent("Predator1", "Agents.Predator", null);
            AgentController preyController = mainContainer.createNewAgent("Prey1", "Agents.Prey", null);
            mainContainer.start();
            predatorController.start();
            preyController.start();
        } catch (StaleProxyException e) {
            System.err.println("\nThere was an error creating the agent!");
            e.printStackTrace();
        } catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Agent created...");

        System.out.println("Container Running....");
	    
	}
	 
}
