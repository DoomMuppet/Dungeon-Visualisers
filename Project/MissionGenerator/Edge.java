package MissionGenerator;

public class Edge {
	Vertex source; //where the edge starts
	Vertex target; //where the edge ends
	public Boolean key; //if the edge connects a key to its lock
	String name; //the name of this edge
	
	public Edge (Vertex source, Vertex target, Boolean key, String name) {
		this.source = source;
		this.target = target;
		this.key = key;
		this.name = name;
	}
	
	public Edge (Boolean key, String name) {
		this.key = key;
		this.name = name;
	}
	//getters and setters for source and target
	public String getName() {
		return name;
	}
	
	public Vertex getSource() {
		return source;
	}
	public Vertex getTarget() {
		return target;
	}
	public void setSource(Vertex newSource) {
		source = newSource;
	}
	public void setTarget(Vertex newTarget) {
		target = newTarget;
	}
	
	//the toString method
	public String toString() {
		String data = "Name: " +name + "\n";
		if (source != null) {
			data = data +"Source: "+ source.getName() + "\n";
		}
		if (target != null) {
			data = data +"Target: " +target.getName() +"\n";
		}
		
		//String data = "Hello";
		if (key) {
			data = data + " is a key";
		}
		return data;
	}
}
