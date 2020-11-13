package Environment;

public class Vertex {
    public int xCoordinate;
    public int yCoordinate;
    
    public Vertex(int xCoordinate, int yCoordinate) {
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
}