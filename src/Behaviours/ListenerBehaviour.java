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
	}

	@Override
	public void action() {

		// Waiting for a message
		ACLMessage message = agent.receive();

		if (message != null) {
			analyseMessage(message);
		}

		if (agent.getMaze() != null && agent.getMaze().isOver()) {
			agent.removeCurrentBehaviour();

			// Take entity off alert
			if (agent.getMaze() != null && agent.getMaze().getEntities().get(agent.getName()) != null)
				agent.getMaze().getEntities().get(agent.getName()).setAlert(false);

			// Put entity as not looking
			if (agent.getMaze() != null && agent.getMaze().getEntities().get(agent.getName()) != null)
				agent.getMaze().getEntities().get(agent.getName()).setLooking(false);
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
					case "Prey":
						agent.setPreyX(Integer.parseInt(messageArray[1]));
						agent.setPreyY(Integer.parseInt(messageArray[2]));

						if (!(agent.getCurrentBehaviour() instanceof ChaseBehaviour)) {
							agent.removeCurrentBehaviour();
							agent.setCurrentBehaviour(agent.getChaseBehaviour());
						}

						break;
					default:
						break;
				}
				break;
			case ACLMessage.PROPOSE:
				switch (messageArray[0]) {
					case "Roll":
						if (agent.getAgentRolls().contains(Integer.parseInt(messageArray[1]))) {
							agent.sendMessageTo(message.getSender(), ACLMessage.REJECT_PROPOSAL, "Reroll");
						} else {
							agent.addAgentRoll(Integer.parseInt(messageArray[1]));
						}

						if (agent.getAgentRolls().size() == agent.getPredators().length) {
							// Send info to Predators
							DFAgentDescription[] predators = agent.getPredators();

							String patrolMessage = "Patrol";

							Iterator<Integer> iterator = agent.getAgentRolls().iterator();
							while (iterator.hasNext()) {
								patrolMessage += " " + iterator.next();
							}

							for (int i = 0; i < predators.length; i++) {
								agent.sendMessageTo(predators[i].getName(), ACLMessage.ACCEPT_PROPOSAL, patrolMessage);
							}
						}
						break;
					default:
						break;
				}
				break;
			case ACLMessage.ACCEPT_PROPOSAL:
				switch (messageArray[0]) {
					case "Patrol":
						for (int i = 1; i < messageArray.length; i++) {
							agent.addAgentRoll(Integer.parseInt(messageArray[i]));
						}
						agent.removeCurrentBehaviour();
						agent.setCurrentBehaviour(agent.getGoToBehaviour());
						break;
					default:
						break;
				}
				break;
			case ACLMessage.REJECT_PROPOSAL:
				switch (messageArray[0]) {
					case "Reroll":
						// Roll a new random value
						Random rand = new Random();
						int rand_int1 = rand.nextInt(1000);

						agent.setRolledValue(rand_int1);

						// Send info to other Predators
						String newMessage = "Roll " + String.valueOf(agent.getRolledValue());

						agent.sendMessageTo(message.getSender(), ACLMessage.PROPOSE, newMessage);
						break;
					default:
						break;
				}
				break;
			default:
				break;
			// do nothing
		}

	}

}
