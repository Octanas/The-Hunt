package Behaviours;

import Agents.SuperAgent;
import Environment.Maze;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class SightBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = 1L;

    private SuperAgent agent;
    private boolean isHunter;

    public SightBehaviour(Agent a, int period, boolean isHunter) {
        super(a, period);

        agent = (SuperAgent) a;
        this.isHunter = isHunter;
    }

    @Override
    protected void onTick() {
        Maze maze = agent.getMaze();

        if (maze != null) {
            if (isHunter) {
                // Verify is prey is in vision
                Maze.MazeEntity visibleEntity = maze.getVisibleEntity(agent.getName());
                // If yes send info to other hunters
                if (visibleEntity != null && !visibleEntity.isHunter && !visibleEntity.isCaught()) {

                    DFAgentDescription[] predators = agent.getPredators();
                    String message = "Prey " + String.valueOf(visibleEntity.getXCoordinate()) + " "
                            + String.valueOf(visibleEntity.getYCoordinate());

                    for (int i = 0; i < predators.length; i++) {
                        agent.sendMessageTo(predators[i].getName(), ACLMessage.INFORM, message);
                    }
                    agent.setPreyX(visibleEntity.getXCoordinate());
                    agent.setPreyY(visibleEntity.getYCoordinate());

                    if (!(agent.getCurrentBehaviour() instanceof ChaseBehaviour)) {
                        agent.removeCurrentBehaviour();
                        agent.setCurrentBehaviour(agent.getChaseBehaviour());
                    }
                }
            }
        }
    }
}
