package MissionGenerator;

import java.util.ArrayList;

public class Vertex {
	String name;
	ArrayList<Edge> incoming;
	ArrayList<Edge> outgoing;
	String type;
	boolean critical;
	public Vertex(String name, String type, boolean critical) {
		this.name = name;
		this.type = type;
		this.critical = critical;
		incoming = new ArrayList<Edge>();
		outgoing = new ArrayList<Edge>();
	}
	
	//getters and setters
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String newType) {
		type = newType;
	}
	public ArrayList<Edge> getIncoming() {
		return incoming;
	}
	public ArrayList<Edge> getOutgoing(){
		return outgoing;
	}
	
	public boolean isCritical() {
		return critical;
	}
	//adds an edge to the list of incoming edges, returns false if the edge is already present
	public boolean AddIncoming (Edge newEdge) {
		if(!(incoming.isEmpty())) {
			if (incoming.contains(newEdge)) {
				return false;
			}
			else {
				incoming.add(newEdge);
				newEdge.setTarget(this);
				return true;
			}
		}
		else {
			incoming.add(newEdge);
			newEdge.setTarget(this);
			return true;
		}
	}
	
	//adds an edge to the list of incoming edges, returns false if the edge is already present
	public boolean AddOutgoing (Edge newEdge) {
		if (!(outgoing.isEmpty())) {
			if (outgoing.contains(newEdge)) {
				return false;
			}
			else {
				outgoing.add(newEdge);
				newEdge.setSource(this);
				return true;
			}
		}
		else {
			outgoing.add(newEdge);
			newEdge.setSource(this);
			return true;
		}
	}
	
	//removes an edge from the list of edges, returns false if the edge is not present
	public boolean RemoveIncoming (Edge newEdge) {
		if (!incoming.isEmpty()) {
			if (incoming.contains(newEdge)) {
				incoming.remove(newEdge);
				newEdge.setTarget(null);
				return true;
			}
			else {
				return false;
			}
		} else {
			return false;
		}
	}
	//removes an edge from the list of edges, returns false if the edge is not present
	public boolean RemoveOutgoing (Edge newEdge) {
		if (!outgoing.isEmpty()) {
			if (outgoing.contains(newEdge)) {
				outgoing.remove(newEdge);
				newEdge.setSource(null);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	// overloading the toString() method
	public String toString() {
		String data = "Name: " +name + " Type: \n"+ type +" Incoming:\n";
		for (int i = 0; i < incoming.size(); i ++) {
			data = data + incoming.get(i).getName() + "\n";
		}
		data = data + "Outgoing:\n";
		for (int i = 0; i < outgoing.size(); i ++) {
			data = data + outgoing.get(i).getName() + "\n";
		}
		return data;
	}
}
