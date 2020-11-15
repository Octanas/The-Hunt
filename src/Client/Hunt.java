package Client;

import Environment.Maze;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Hunt {

	// JADE profile and runtime variables
	static Runtime runt;
	static Profile profile;
	static Maze maze;

	public static void main(String[] args) {

		int numHunters = 4;
		int numPrey = 1;
		boolean fixedPrey = false;

		if (args.length >= 1) {
			try {
				numHunters = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				System.out.println("Invalid number of hunters, terminating");
				return;
			}
		}

		if (args.length >= 2) {
			try {
				numPrey = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				System.out.println("Invalid number of preys, terminating");
				return;
			}
		}

		if (args.length >= 3) {
			fixedPrey = Boolean.parseBoolean(args[2]);
		}

		// Start JADE
		runt = Runtime.instance();
		profile = new ProfileImpl();
		profile.setParameter(Profile.GUI, "false");

		// Initialize maze
		maze = Maze.defaultMaze();

		VisualizerWindow.run(maze);

		// Add agents
		AgentContainer mainContainer = runt.createMainContainer(profile);

		System.out.println("Starting with " + numHunters + (numHunters == 1 ? " hunter and " : " hunters and ")
				+ numPrey + " prey");

		addPredators(mainContainer, numHunters);
		addPreys(mainContainer, numPrey, fixedPrey);
		addObserver(mainContainer);

		System.out.println("Agents created...");

		System.out.println("Container Running....");

	}

	public static void createEnvironment(AgentContainer container) {

		try {
			AgentController mazeController = container.createNewAgent("Maze", "Environment.Maze", null);
			mazeController.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

	public static void addPredators(AgentContainer container, int n) {

		for (int i = 0; i < n; i++) {
			AgentController predatorController;
			try {
				predatorController = container.createNewAgent("Predator " + i, "Agents.Predator",
						new Object[] { maze });
				predatorController.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

		}

	}

	public static void addPreys(AgentContainer container, int n, boolean fixedPrey) {

		AgentController preyController;

		for (int i = 0; i < n; i++) {
			try {
				preyController = container.createNewAgent("Prey " + i, "Agents.Prey", new Object[] { maze, fixedPrey });
				preyController.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

		}

	}

	public static void addObserver(AgentContainer container) {

		AgentController observerController;

		try {
			observerController = container.createNewAgent("Observer", "Agents.Observer", null);
			observerController.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

}
