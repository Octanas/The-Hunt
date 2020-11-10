package Environment;

import java.util.*;
import Environment.Graph.Vertex;

public class Astar 
{
	private Graph graph;
	private PriorityQueue<Vertex> openVertexes; //Vertexes not yet evaluated
	//private Vertex[][] searchArea;
	private Vertex initialVertex;
	private Vertex finalVertex;
	
	public Astar(Graph g, Vertex initialVertex, Vertex finalVertex) {
		this.graph = g;
        setInitialVertex(initialVertex);
        setFinalVertex(finalVertex);
        this.openVertexes = new PriorityQueue<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                return Integer.compare(v1.getFinalCost(), v2.getFinalCost());
            }
        });
        
        initialVertex.setG(0);
        initialVertex.setFinalCost(this.manhattanDistance(initialVertex, finalVertex));
    }
	
	public void process() {
		openVertexes.add(initialVertex);
		Vertex current;
		
		while(openVertexes.size() != 0)
		{
			current = openVertexes.poll();
			
			if(current == null) 
				break;
			
			current.close();
			
			if(current.equals(finalVertex))
				return;
			
			for(int i = 0; i < graph.getAdjVertexes(current).size(); i++) {
				Vertex tmp = graph.getAdjVertexes(current).get(i);
				if(!tmp.closed) {
					int newGCost = current.getG() + 1;
					int newFinalCost = this.manhattanDistance(tmp, finalVertex) + newGCost;
					if (openVertexes.contains(tmp)) {
						if(tmp.getFinalCost() > newFinalCost) {
							tmp.setParent(current);
							tmp.setG(newGCost);
							tmp.setFinalCost(newFinalCost);
						}
					}
					else {
						tmp.setParent(current);
						tmp.setG(newGCost);
						tmp.setFinalCost(newFinalCost);
						openVertexes.add(tmp);
					}
				}
			}
		}
	}
	
	public List<Vertex> getPath(){
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex v = finalVertex;
		path.add(finalVertex);
		while(v.getParent() != null) {
			path.add(v.getParent());
			v = v.getParent();
		}
		
		Collections.reverse(path);
		
		return path;
	}
	
	public void setInitialVertex(Vertex vertex) {
		this.initialVertex = vertex;
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
	 *			if vertex not in closed list and not in open list then
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
