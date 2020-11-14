package Behaviours;

import java.util.Iterator;
import java.util.Random;

import Agents.SuperAgent;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class ListenerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 861411093335605709L;

	private SuperAgent agent;

	public ListenerBehaviour(Agent a) {
		agent = (SuperAgent) a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {

		// Waiting for a message
		ACLMessage message = agent.receive();

		if (message != null) {
			analyseMessage(message);
		}

	}

	private void analyseMessage(ACLMessage message) {

		int type = message.getPerformative();
		// System.out.println("Content: " + message.getContent() + "\nSender" +
		// message.getSender());

		String[] messageArray = message.getContent().split(" ");

		switch (type) {
			case ACLMessage.INFORM:
				switch (messageArray[0]) {

					case "Roll":
						if (agent.getAgentRolls().contains(Integer.parseInt(messageArray[1]))) {
							agent.sendMessageTo(message.getSender(), ACLMessage.INFORM, "Reroll");
						} else {
							agent.addAgentRoll(Integer.parseInt(messageArray[1]));
						}
						if (agent.getAgentRolls().size() == 4) {
							// Send info to Predators
							DFAgentDescription[] predators = agent.getPredators();

							String patrolMessage = "Patrol";

							Iterator iterator = agent.getAgentRolls().iterator();
							while (iterator.hasNext()) {
								patrolMessage += " " + iterator.next();
							}

							for (int i = 0; i < predators.length; i++) {
								agent.sendMessageTo(predators[i].getName(), ACLMessage.INFORM, patrolMessage);
							}
						}
						break;
					case "Prey":
						agent.setPreyX(Integer.parseInt(messageArray[1]));
						agent.setPreyY(Integer.parseInt(messageArray[2]));
						agent.removeCurrentBehaviour();
						agent.setCurrentBehaviour(agent.getChaseBehaviour());
						break;
					case "Reroll":
						// Roll a new random value
						Random rand = new Random();
						int rand_int1 = rand.nextInt(1000);

						agent.setRolledValue(rand_int1);

						// Send info to other Predators
						String newMessage = "Roll " + String.valueOf(agent.getRolledValue());

						agent.sendMessageTo(message.getSender(), ACLMessage.INFORM, newMessage);
						break;
					case "Patrol":
						for (int i = 1; i < messageArray.length; i++) {
							agent.addAgentRoll(Integer.parseInt(messageArray[i]));
						}
						agent.removeCurrentBehaviour();
						agent.setCurrentBehaviour(agent.getPatrolBehaviour());
						break;
					default:
						break;
				}
				break;
			case ACLMessage.PROPOSE:
				break;
			case ACLMessage.ACCEPT_PROPOSAL:
				break;
			case ACLMessage.REJECT_PROPOSAL:
				break;
			default:
				break;
			// do nothing
		}

	}

}
