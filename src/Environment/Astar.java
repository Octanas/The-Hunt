package Environment;

import java.util.*;
import Environment.Graph.Vertex;

public class Astar 
{
	private PriorityQueue<Vertex> openVertexes; //Vertexes not yet evaluated
	//private Vertex[][] searchArea;
	private Vertex initialvertex;
	private Vertex finalVertex;
	
	public Astar(Graph g, Vertex initialVertex, Vertex finalVertex) {
        setInitialVertex(initialVertex);
        setFinalVertex(finalVertex);
        this.openVertexes = new PriorityQueue<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                return Integer.compare(v1.getFinalCost(), v2.getFinalCost());
            }
        });
        
        initialVertex.setHeuristicCost(manhattanDistance(initialVertex, finalVertex));
        initialVertex.setFinalCost(initialVertex.getHeuristicCost());
        finalVertex.setHeuristicCost(0);
        
        
    }
	
	public void setInitialVertex(Vertex vertex) {
		this.initialvertex = vertex;
	}
	
	public void setFinalVertex(Vertex vertex) {
		this.finalVertex = vertex;
	}
	
	/*
	 * function to be used as heuristic
	 */
	public int manhattanDistance(Vertex v1, Vertex v2) {
		return Math.abs(v1.xCoordinate - v2.xCoordinate) + Math.abs(v1.yCoordinate - v2.yCoordinate);
	}
	
	/*
	 * PSEUDO CODE FOR A STAR ALGORYTHM
	 * 
	 * 	
	 * 	initialize open and closed lists
	 * 	make the start vertex current
	 * 	calculate heuristic of start vertex to destination (h)
	 * 	calculate f value to start vertex
	 * 	while current vertex in not the destination
	 *		for each vertex adjacent to current
	 *			if vertex not in closed list and not in closed list then
	 *				add vertex to open list
	 *				calculate distance from start (g)
	 *				calculate heuristic distance to destination (h)
	 *				calculate f value (f = g + h)
	 *				if new f value < existing f value or there is no existing f value then
	 *					update f value
	 *					set parent to be the current vertex
	 *				end if
	 *			end if
	 *		next adjacent vertex
	 *		add current vertex to closed list
	 *		remove vertex with lowest f value from open list and make it current
	 *	end while
	 */
}
