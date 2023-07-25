package MissionGenerator;

import java.util.ArrayList;
//all of the edges except from start and goal use this to get their names
//this code means there cannot be duplicate names
public class NameStorage {
	ArrayList<String> usednames;
	int numVertexs;
	int numEdges;
	public NameStorage() {
		usednames = new ArrayList<String>();
		numVertexs = 0;
		numEdges = 0;
	}
	public String getEdgename() {
		String name = "e" +numEdges;
		numEdges += 1;
		return name;
	}
	public String getVertexname() {
		String name = "v" + numVertexs;
		numVertexs +=1;
		return name;
	}
}
