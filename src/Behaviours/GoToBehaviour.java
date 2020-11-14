package Behaviours;

import java.util.Iterator;
import java.util.List;

import Agents.SuperAgent;
import Environment.Astar;
import Environment.Maze;
import Environment.Vertex;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class GoToBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -8035807564820299915L;

    private SuperAgent agent;

    private List<Maze.Movement> movementsToTake;

    public GoToBehaviour(Agent a, long period) {
        super(a, period);

        agent = (SuperAgent) a;

        movementsToTake = null;

        System.out.println("Agent " + agent.getLocalName() + ": Starting go to!");
    }

    @Override
    public void onStart() {
        int i = 0;
        Iterator<Integer> iterator = agent.getAgentRolls().iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == agent.getRolledValue()) {
                break;
            }

            i++;
        }

        switch (i) {
            case 0:
                agent.setBiasX(4);
                agent.setBiasY(4);
                break;
            case 1:
                agent.setBiasX(14);
                agent.setBiasY(4);
                break;
            case 2:
                agent.setBiasX(4);
                agent.setBiasY(16);
                break;
            case 3:
                agent.setBiasX(14);
                agent.setBiasY(16);
                break;
            default:
                break;
        }

        super.onStart();
    }

    @Override
    protected void onTick() {
        Maze maze = agent.getMaze();
        Maze.MazeEntity self = maze.getEntities().get(agent.getName());

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

            agent.removeCurrentBehaviour();
            agent.setCurrentBehaviour(agent.getChaseBehaviour());
        } else {
            // movementsToTake is null, so it's should get new path to follow
            if (movementsToTake == null) {
                Vertex init = new Vertex(self.getXCoordinate(), self.getYCoordinate());
                Vertex prey = new Vertex(agent.getBiasX(), agent.getBiasY());

                Astar astar = new Astar(maze.getGraph(), init, prey);
                int proc = astar.process();

                if (proc == 0) {
                    List<Vertex> path = astar.getPath();

                    movementsToTake = Maze.convertVertexPathToMovements(path);
                } else {
                    if (proc == 1) {
                        System.out.println("Unreachable Vertex...");
                    } else if (proc == 2) {
                        System.out.println("Error...");
                    }

                    movementsToTake = null;
                }

                if (movementsToTake == null) {
                    System.out.println("Agent " + agent.getLocalName() + ": Something is wrong, path is invalid");
                }
            }

            if (movementsToTake != null) {
                if (!movementsToTake.isEmpty()) {
                    boolean success = maze.moveEntity(agent.getName(), movementsToTake.get(0));

                    if (success) {
                        movementsToTake.remove(0);
                    } else {
                        System.out
                                .println("Agent " + agent.getLocalName() + ": Something is wrong, path does not work");

                        // Path does not work, so a new path should be calculated
                        movementsToTake = null;
                    }
                }

                // Every move has been made, start patrolling
                if (movementsToTake != null && movementsToTake.isEmpty()) {
                    agent.removeCurrentBehaviour();
                    agent.setCurrentBehaviour(agent.getPatrolBehaviour());
                }
            }
        }
    }
}
