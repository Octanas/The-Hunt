package Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Graph {
	
	private ConcurrentHashMap<Vertex, List<Vertex>> adjVertices;
	
	@SuppressWarnings("unlikely-arg-type")
	public List<Vertex> getAdjVertexes(Vertex v){
		return this.adjVertices.get(v);
	}
	
	public Graph() {
		adjVertices = new ConcurrentHashMap<Vertex, List<Vertex>>();
	}
	
	void addVertex(int xCoordinate, int yCoordinate) {
	    adjVertices.putIfAbsent(new Vertex(xCoordinate, yCoordinate), new ArrayList<>());
	}
	 
	void removeVertex(int xCoordinate, int yCoordinate) {
	    Vertex v = new Vertex(xCoordinate, yCoordinate);
	    adjVertices.values().stream().forEach(e -> e.remove(v));
	    adjVertices.remove(v);
	}
	
	boolean addEdge(int xCoordinate1, int yCoordinate1, int xCoordinate2, int yCoordinate2) {
	    Vertex v1 = new Vertex(xCoordinate1, yCoordinate1);
	    Vertex v2 = new Vertex(xCoordinate2, yCoordinate2);
	    
	    List<Vertex> v1List = adjVertices.get(v1);
	    List<Vertex> v2List = adjVertices.get(v2);
	    
	    if(v1List != null && v2List != null) {
	    	v1List.add(v2);
	    	v2List.add(v1);
	    	
	    	return true;
	    }
	    
	    return false;
	}
	
	void removeEdge(int xCoordinate1, int yCoordinate1, int xCoordinate2, int yCoordinate2) {
	    Vertex v1 = new Vertex(xCoordinate1, yCoordinate1);
	    Vertex v2 = new Vertex(xCoordinate2, yCoordinate2);
	    List<Vertex> eV1 = adjVertices.get(v1);
	    List<Vertex> eV2 = adjVertices.get(v2);
	    if (eV1 != null)
	        eV1.remove(v2);
	    if (eV2 != null)
	        eV2.remove(v1);
	}
	
	List<Vertex> getAdjVertices(int xCoordinate, int yCoordinate) {
	    return adjVertices.get(new Vertex(xCoordinate, yCoordinate));
	}
}
