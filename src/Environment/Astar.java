package Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {
	class VertexInfo {
		private int hValue;
		private int gValue;
		private int fValue;
		private Vertex parent;
		private boolean closed = false;

		public VertexInfo(int h, int g, Vertex p) {
			this.hValue = h;
			this.gValue = g;
			this.updateFValue();
			this.parent = p;
		}

		public void setHValue(int hValue) {
			this.hValue = hValue;
		}

		public int getHValue() {
			return this.hValue;
		}

		public void setGValue(int gValue) {
			this.gValue = gValue;
		}

		public int getGValue() {
			return this.gValue;
		}

		public void updateFValue() {
			this.fValue = this.gValue + this.hValue;
		}

		public int getFValue() {
			return this.fValue;
		}

		public void setParent(Vertex parent) {
			this.parent = parent;
		}

		public Vertex getParent() {
			return this.parent;
		}

		public boolean isClosed() {
			return this.closed;
		}

		public void close() {
			this.closed = true;
		}
	}

	private Graph graph;
	private PriorityQueue<Vertex> openVertexes; // Vertexes not yet evaluated
	private Vertex initialVertex;
	private Vertex finalVertex;
	private HashMap<Vertex, VertexInfo> info;

	public Astar(Graph g, Vertex initialVertex, Vertex finalVertex) {
		this.graph = g;
		setInitialVertex(initialVertex);
		setFinalVertex(finalVertex);
		this.openVertexes = new PriorityQueue<Vertex>(new Comparator<Vertex>() {
			@Override
			public int compare(Vertex v1, Vertex v2) {
				return Integer.compare(info.get(v1).getFValue(), info.get(v2).getFValue());
			}
		});

		info.get(initialVertex).setGValue(0);
		info.get(initialVertex).setHValue(this.manhattanDistance(initialVertex, finalVertex));
		info.get(initialVertex).updateFValue();
	}

	public void process() {
		openVertexes.add(initialVertex);
		Vertex current;

		while (openVertexes.size() != 0) {
			current = openVertexes.poll();

			if (current == null)
				break;

			info.get(current).close();

			if (current.equals(finalVertex))
				return;

			for (int i = 0; i < graph.getAdjVertexes(current).size(); i++) {
				Vertex tmp = graph.getAdjVertexes(current).get(i);
				if (!info.get(tmp).isClosed()) {
					int newGCost = info.get(current).getGValue() + 1;
					int newHCost = this.manhattanDistance(tmp, finalVertex);
					int newFinalCost = newHCost + newGCost;
					if (openVertexes.contains(tmp)) {
						VertexInfo tmpInfo = info.get(tmp);
						if (info.get(tmp).getFValue() > newFinalCost) {
							tmpInfo.setParent(current);
							tmpInfo.setGValue(newGCost);
							tmpInfo.setHValue(newHCost);
							tmpInfo.updateFValue();
						}
					} else {
						VertexInfo tmpInfo = new VertexInfo(newHCost, newGCost, current);
						info.put(tmp, tmpInfo);
						openVertexes.add(tmp);
					}
				}
			}
		}
	}

	public List<Vertex> getPath() {
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex v = finalVertex;
		path.add(finalVertex);
		while (info.get(v).getParent() != null) {
			path.add(info.get(v).getParent());
			v = info.get(v).getParent();
		}

		Collections.reverse(path);

		return path;
	}

	public int getFValue() {
		return info.get(finalVertex).getFValue();
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
}

/*
 * PSEUDO CODE FOR A STAR ALGORYTHM
 * 
 * 
 * initialize open and closed lists make the start vertex current calculate
 * heuristic of start vertex to destination (h) calculate f value to start
 * vertex while current vertex in not the destination for each vertex adjacent
 * to current if vertex not in closed list and not in open list then add vertex
 * to open list calculate distance from start (g) calculate heuristic distance
 * to destination (h) calculate f value (f = g + h) if new f value < existing f
 * value or there is no existing f value then update f value set parent to be
 * the current vertex end if end if next adjacent vertex add current vertex to
 * closed list remove vertex with lowest f value from open list and make it
 * current end while
 */
