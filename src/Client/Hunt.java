package Client;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Hunt {
	
	//JADE profile and runtime variables
    static Runtime runt;
    static Profile profile;

	public static void main(String[] args) {
		
		//Start JADE
        runt = Runtime.instance();
        profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        //Add agents
        AgentContainer mainContainer = runt.createMainContainer(profile);
        
        addPredators(mainContainer, 4);
        addPreys(mainContainer, 1);
        
        System.out.println("Agents created...");

        System.out.println("Container Running....");
	    
	}
	
	public static void addPredators(AgentContainer container, int n) {
					
		for(int i = 0; i < n; i++) {
			AgentController predatorController;
			try {
				predatorController = container.createNewAgent("Predator " + i, "Agents.Predator", null);
				predatorController.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void addPreys(AgentContainer container, int n) {
		
		AgentController preyController;
		
		for(int i = 0; i < n; i++) {
			try {
				preyController = container.createNewAgent("Prey " + i, "Agents.Prey", null);
				preyController.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	 
}
