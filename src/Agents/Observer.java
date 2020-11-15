package Agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Observer extends SuperAgent {

	private static final long serialVersionUID = -3028740105116828299L;

	protected void setup() {
        super.setup();

        // Registration with the DF
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType("ObserverAgent"); // required
		serviceDescription.setName(getName()); // required

		agentDescription.setName(getAID()); // required
        agentDescription.addServices(serviceDescription); // required

        try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

        // Adding initial behaviours
        addBehaviour(getListenerBehaviour());
    }

}
