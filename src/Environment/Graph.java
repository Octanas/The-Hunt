package Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Graph {
	
	class Vertex {
	    public int xCoordinate;
	    public int yCoordinate;
	    private int heuristicCost;
	    private int finalCost; // G + H with 
	    // G(n) cost of the path from the star node to n
	    // H(n) the heuristic that estimates the cost of the cheapest path from n to the goal
	    public boolean solution = false; // if the vertex is part of the solution path
	    public boolean closed = false; // indicates if the vertex is already evaluated
	    
	    Vertex(int xCoordinate, int yCoordinate) {
	        this.xCoordinate = xCoordinate;
	        this.yCoordinate = yCoordinate;
	    }
	 
	    public int hashCode() {
	    	return this.xCoordinate * 10000 + this.yCoordinate;
	    }
	    
	    public boolean equals(Object o) {
	        if(!(o instanceof Vertex)) {
	        	return false;
	        }
	        
	        Vertex vert = (Vertex) o;
	        
	        if(vert.xCoordinate == this.xCoordinate && vert.yCoordinate == this.yCoordinate)
	        	return true;
	        else
	        	return false;
	    }
	    
	    public String toString() {
	    	return "(" + this.xCoordinate + ", " + this.yCoordinate + ")";
	    }
	    
	    int getFinalCost() {
			return this.finalCost;
		}
	    
	    void setFinalCost(int f) {
	    	this.finalCost = f;
	    }
	    
	    int getHeuristicCost() {
	    	return this.heuristicCost;
	    }
	    
	    void setHeuristicCost(int h) {
	    	this.heuristicCost = h;
	    }
	}
	
	private ConcurrentHashMap<Vertex, List<Vertex>> adjVertices;
	
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
