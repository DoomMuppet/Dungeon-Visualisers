package MissionGenerator;

import java.util.ArrayList;

//This is the room class, it stores what the room contains,
//It also stores what doors the room has
//xpos and ypos are the position of the top right corner
public class Room {
	
	String name; //the name of this room
	String type; //the type of room this is
	public String key;
	public String lock;
	ArrayList<String> connections; //the names of rooms this room connects to
	ArrayList<String> outgoingConnections;
	int xpos;
	int ypos;

	
	Room(Vertex vertex) {
		this.name = vertex.getName();
		this.type = vertex.getType();
		this.key = "";
		this.lock = "";
		this.outgoingConnections = new ArrayList<String>();
		this.connections = new ArrayList<String>();
		
		for (int i = 0; i <vertex.getIncoming().size(); i++) { //adding all the incoming edges
			if (!vertex.getIncoming().get(i).key) { // making sure Key connections are not mapped as doorways
				this.connections.add(vertex.getIncoming().get(i).getSource().getName());
			}
			else if(vertex.getIncoming().get(i).key) {
				lock = vertex.getIncoming().get(i).getSource().getName();
				//System.out.println(name + lock);
			}
		}
		for (int i = 0; i < vertex.getOutgoing().size(); i++) { //adding all the outgoing edges
			if (vertex.getOutgoing().get(i).key) {
				key= vertex.getOutgoing().get(i).getTarget().getName();
				//System.out.println(name + key);
			}
			else {
				this.connections.add(vertex.getOutgoing().get(i).getTarget().getName());
				this.outgoingConnections.add(vertex.getOutgoing().get(i).getTarget().getName());
			}
		}
	}
	
	//getters and setters
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public ArrayList<String> getConnections(){
		return connections;
	}
	public ArrayList<String> getOutgoingConnections(){
		return connections;
	}
	public int[] getPosition(){
		int[] pos = new int[2];
		pos[0] = xpos;
		pos[1] = ypos;
		return pos;
	}
	public void setXpos(int newpos) {
		xpos = newpos;
	}
	public void setYpos(int newpos) {
		ypos = newpos;
	}
}
