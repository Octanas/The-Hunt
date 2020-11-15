package Behaviours;

import Agents.Prey;
import Agents.SuperAgent;
import Environment.Maze;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class SightBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = 1L;

    private Agent agent;
    private boolean isHunter;

    public SightBehaviour(Agent a, int period, boolean isHunter) {
        super(a, period);

        agent = (Agent) a;
        this.isHunter = isHunter;
    }

    @Override
    protected void onTick() {

        if (isHunter) {
            SuperAgent thisAgent = (SuperAgent) agent;

            Maze maze = thisAgent.getMaze();

            if (maze != null && !maze.isOver()) {
                // Verify is prey is in vision
                Maze.MazeEntity visibleEntity = maze.getVisibleEntity(thisAgent.getName());
                // If yes send info to other hunters
                if (visibleEntity != null && !visibleEntity.isHunter && !visibleEntity.isCaught()) {

                    DFAgentDescription[] predators = thisAgent.getPredators();
                    String message = "Prey " + String.valueOf(visibleEntity.getXCoordinate()) + " "
                            + String.valueOf(visibleEntity.getYCoordinate());

                    for (int i = 0; i < predators.length; i++) {
                        thisAgent.sendMessageTo(predators[i].getName(), ACLMessage.INFORM, message);
                    }
                    thisAgent.setPreyX(visibleEntity.getXCoordinate());
                    thisAgent.setPreyY(visibleEntity.getYCoordinate());

                    if (!(thisAgent.getCurrentBehaviour() instanceof ChaseBehaviour)) {
                        thisAgent.removeCurrentBehaviour();
                        thisAgent.setCurrentBehaviour(thisAgent.getChaseBehaviour());
                    }
                }
            }
        } else {
            Prey thisAgent = (Prey) agent;

            Maze maze = thisAgent.getMaze();
            Maze.MazeEntity self = maze.getEntities().get(thisAgent.getName());

            if (maze != null && !maze.isOver() && !self.isCaught()) {
                Maze.Direction direction = self.getDirection();
                Maze.MazeEntity visibleEntity = maze.getVisibleEntity(thisAgent.getName());

                // If there is a visible hunter, mark the movement in the direction it was seen
                // to be avoided for ticksToAvoid ticks
                if (visibleEntity != null && visibleEntity.isHunter) {
                    if (direction.equals(Maze.Direction.North)) {
                        thisAgent.getMovementsToAvoid().put(Maze.Movement.Up, thisAgent.getTicksToAvoid());
                    } else if (direction.equals(Maze.Direction.South)) {
                        thisAgent.getMovementsToAvoid().put(Maze.Movement.Down, thisAgent.getTicksToAvoid());
                    } else if (direction.equals(Maze.Direction.East)) {
                        thisAgent.getMovementsToAvoid().put(Maze.Movement.Right, thisAgent.getTicksToAvoid());
                    } else if (direction.equals(Maze.Direction.West)) {
                        thisAgent.getMovementsToAvoid().put(Maze.Movement.Left, thisAgent.getTicksToAvoid());
                    }

                    // Put entity on alert
                    self.setAlert(true);
                }
            }
        }
    }
}
