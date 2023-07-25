package MissionGenerator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

//This is the first version of the mission generator
//this code in particular controls the rule applications
//vertexes store what type they are and what connections they have (but not where those connections go)
//edges store what is connected to what type they are and what vertexes they connect
//rules functions which take a number of vertexes and check they fit the rule's requirements, then apply the rule
//you have to create edges before vertexes and then connect them
public class Mission {
	Random rnd;
	NameStorage nameStorage;
	public Map<String,Vertex> vertexs; //the list of vertexes currently in the graph
	public Map<String,Edge> edges; //the list of edges currently in the graph
	String encounter = "encounter";
	String item = "item";
	String goal = "goal";
	String start = "start";
	String lock = "lock";
	String key = "key";
	

	public Mission(){
		this.edges = new HashMap<String,Edge>();
		this.vertexs = new HashMap<String,Vertex>();
		this.nameStorage = new NameStorage();
		this.rnd = new Random();
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		//System.out.println("Enter Dungeon Iterations:");
		//int iterMax = scan.nextInt();
		System.out.println("Enter Approximate Dungeon size:");
		int size = scan.nextInt();
		System.out.println("Enter amount of optional content:");
		int optional = scan.nextInt();
		long start = System.currentTimeMillis();
		//for(int i =0; i<iterMax; i++){
			Mission mission = new Mission();
		mission.StartGraph();
		mission.generateDungeon(size,optional, "Rewrite"); //Mode Options: Deery, Chebyshev, Main Hub, Rewrite
		//}

		scan.close();
		long end = System.currentTimeMillis();

		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
	}
	
	public void StartGraph() {
		//creating the start graph
		Vertex StartV = createVertex(start, true);
		Vertex GoalV = createVertex(goal, true);
		connect(StartV,GoalV,false);
		applyr1(StartV,GoalV);
		//displaying the current graph for testing purposes

	}
	public void generateDungeon(int size,int optional, String mode) {
		int rulechoice;
		int failchoice =0;
		boolean success = false;
		Vertex StartV = vertexs.get("v0");
		Vertex GoalV = vertexs.get("v1");
		applyr1(StartV,GoalV);
		for (int i = 0; i < size; i++) {
			rulechoice = rnd.nextInt(4) + 2;
			while (rulechoice == failchoice) { //making sure it cannot infinitely fail to match
				rulechoice = rnd.nextInt(4) + 2;
			}
			switch(rulechoice) {
			case 2: success = matchr2();
				break;
			case 3: success = matchr3();
				break;
			case 4: success = matchr4();
				break;
			case 5: success = matchr5();
				break;
			}
			if (!success) { //if it didn't find a match for the chosen rule try again
				i -=1;
				failchoice = rulechoice;
			}
		}
		//creating the optional rewards
		for(int i = 0; i < optional; i++) {
			matchr6();
		}
		int numOpLock = rnd.nextInt(optional);
		for (int i = 0; i < numOpLock; i++) {
			matchr7();
		}
		//System.out.println (this.toString());
		new Dungeon(this, mode);
	}
	private Vertex createVertex (String type,boolean critical) {
		String name = nameStorage.getVertexname();
		Vertex New = new Vertex(name,type, critical);
		vertexs.put (name,New);
		
		return New;
	}
	private Edge createEdge(Boolean key) {
		String name = nameStorage.getEdgename();
		Edge New = new Edge(key,name);
		edges.put(name, New);
		return New;
	}
	
	private void deleteEdge(String name) {
		Edge edge1 = edges.get(name);
		edge1.getSource().RemoveOutgoing(edge1);
		edge1.getTarget().RemoveIncoming(edge1);
		edges.remove(name,edge1);
	}
	private void deleteVertex(String name) {
		Vertex v1 = vertexs.get(name);
		ArrayList<Edge> incoming = v1.getIncoming();
		ArrayList<Edge> outgoing = v1.getOutgoing();
		for (int i = 0; i < incoming.size(); i++) {
			deleteEdge(incoming.get(i).getName());
		}
		for (int i = 0; i < outgoing.size(); i++) {
			deleteEdge(outgoing.get(i).getName());
		}
		vertexs.remove(name);
	}
	//toString method, used for displaying the structure generated
	public String toString() {
		String data;
		data = "Vertexes: \n";
		for(Map.Entry<String, Vertex> entry : vertexs.entrySet()) {
			 data = data + "    " + vertexs.get(entry.getKey()).toString();
		}
		data = data +"\n Edges: \n";
		for(Map.Entry<String, Edge> entry : edges.entrySet()) {
			 data = data + "    " + edges.get(entry.getKey()).toString();
		}
		return data;
	}
	//returns the edge connecting if v1 has a node connecting to v2 that isn't a key edge
	public Edge connected (Vertex v1, Vertex v2) {
		if(v1.getOutgoing().isEmpty() == true) {
			return null;
		}
		ArrayList<Edge> v1out = v1.getOutgoing();
		if(v2.getIncoming().isEmpty() == true) {
			return null;
		}
		ArrayList<Edge> v2in = v2.getIncoming();
		for (int i = 0; i < v1out.size(); i++) {
			if(v2in.contains(v1out.get(i)) && !(v1out.get(i).key)) {
				return v1out.get(i);
			}
		}
		return null;
	}
	//creates an edge connecting two nodes, can be a key
	public void connect(Vertex v1, Vertex v2, Boolean key) {
		Edge e1 = createEdge(key);
		v1.AddOutgoing(e1);
		v2.AddIncoming(e1);
	}

	//returns all vertexs as an Arraylist
	public ArrayList<Vertex> getVertexs(){
		ArrayList<Vertex> allV = new ArrayList<Vertex>();
		Set<String> keys = vertexs.keySet();
		for(String str : keys) {
			allV.add(vertexs.get(str));
		}
		return allV;
		
	}
	public ArrayList<Edge> getEdges(){
		ArrayList<Edge> allE = new ArrayList<Edge>();
		Set<String> keys = edges.keySet();
		for(String str :keys) {
			allE.add(edges.get(str));
		}
		return allE;
			
	}
	
	//There are two parts to each rule, a checker that takes the vertexs and returns whether or not they're applicable
	//The checker now also holds an invariant that if any rule would increase the number of vertexs to more than 4, it fails
	private boolean checkr1 (Vertex startV, Vertex goalV) {
		if(connected(startV,goalV) != null) {
			if((startV.getType() == start) && (goalV.getType() == goal)) {
				if (startV.isCritical() && goalV.isCritical()) {
					return true;
				}
			}
		}
		return false;
	}
	private void applyr1 (Vertex startV, Vertex goalV) {
		if(checkr1(startV,goalV)) {
			deleteEdge(connected(startV,goalV).getName());	
			Vertex v1 = createVertex(lock, true);
			Vertex v2 = createVertex(key, true);
			connect(startV,v1,false);
			connect(startV,v2,false);
			connect(v2,v1,true); //creating the key connection
			connect(v1,goalV,false); //creating the connection between lock and goal
		}
	}
	private boolean checkr2 (Vertex lockV, Vertex goalV) {
		if(connected(lockV,goalV) != null) {
			if((lockV.getType() == lock) && (goalV.getType() == goal)) {
				if (lockV.isCritical() && goalV.isCritical()) {
						return true;
				}
			}
		}
		return false;
	}
	private void applyr2(Vertex lockV, Vertex goalV) {
		if(checkr2(lockV,goalV)) {
			deleteEdge(connected(lockV,goalV).getName());
			Vertex v1 = createVertex(lock, true);
			Vertex v2 = createVertex(key, true);
			connect(lockV,v1,false);
			connect(lockV,v2,false);
			connect(v2,v1,true); //creating the key connection
			connect(v1,goalV,false); //creating the connection between lock and goal
		}
	}
	private boolean checkr3(Vertex blankV, Vertex lockV, Vertex goalV) {
		if(connected(lockV,goalV) != null) {
			if (connected(blankV,lockV)!= null) {
				if(connected(blankV,goalV) != null) {
					return false;
				} else {
					if (blankV.isCritical() && lockV.isCritical() && goalV.isCritical()) {
							return true;
					}
				}
			}
		}
		return false;
	}
	private void applyr3(Vertex blankV, Vertex lockV, Vertex goalV) {
		if (checkr3(blankV,lockV,goalV)) {
			deleteEdge(connected(lockV,goalV).getName());
			Vertex v1 = createVertex(lock, true);
			Vertex v2 = createVertex(key, true);
			connect(lockV,v1,false); //connecting from the start lock to the new lock 
			connect(blankV,v2,false); //connecting from the blank node to the key
			connect(v2,v1,true); //creating the key connection
			connect(v1,goalV,false); //connecting between lock and goal
		}
	}
	private boolean checkr4(Vertex blankS, Vertex blankP, Vertex blankG) {
		if ((connected(blankS,blankP) != null) && (connected(blankP,blankG) != null)) {
			if(connected(blankS,blankG) == null) {
				if (blankS.isCritical() && blankG.isCritical()) {
						return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	private void applyr4(Vertex blankS, Vertex blankP, Vertex blankG) {
		if(checkr4(blankS,blankP,blankG)) {
			Vertex v1 = createVertex(lock, true);
			Vertex v2 = createVertex(key, true);
			connect(blankS,v1,false);
			connect(blankS,v2,false);
			connect(v2,v1,true);
			connect(v1,blankG,false);
		}
	}
	private boolean checkr5(Vertex blank1, Vertex blank2) {
		if(connected(blank1,blank2)!= null) {
			if (blank1.isCritical() && blank2.isCritical()) {
				return true;
			}
		}
		return false;
	}
	private void applyr5(Vertex blank1, Vertex blank2) {
		if(checkr5(blank1, blank2)) {
			deleteEdge(connected(blank1,blank2).getName());
			Vertex v1 = createVertex(encounter, true);
			connect(blank1,v1,false);
			connect(v1,blank1,false);
		}
	}
	private boolean checkr6 (Vertex blank1) {
		if(blank1.isCritical()) {
				return true;
		}
		return false;
	}
	private void applyr6(Vertex blank1) {
		if (checkr6(blank1)) {
			Vertex v1 = createVertex(encounter,false);
			Vertex vi = createVertex(item,false);
			connect(blank1,v1,false);
			connect(v1,vi,false);
		}
	}
	private boolean checkr7 (Vertex blank1, Vertex blankE, Vertex blank2, Vertex CriticalE) {
		if(CriticalE.isCritical()) {
			if(connected(blank1,blankE) != null) {
				if(connected(blankE,blank2) != null) {
					if(blankE.getType() == encounter) {
							return true;
					}
				}
			}
		}
		return false;
	}
	private void applyr7 (Vertex blank1, Vertex blankE, Vertex blank2, Vertex CriticalE) {
		if(checkr7(blank1, blankE, blank2, CriticalE)) {
			blankE.setType(lock);
			Vertex K1 = createVertex(key,false);
			connect(CriticalE,K1,false);
			connect(K1,blankE,true);
		}
	}
	
	//These functions are used to find a match for a specific rule within the graph and then apply it
	//They're the most computationally expensive part of the code currently
	//functioning at worst at O(VVVV)
	private boolean matchr2() {
		Vertex startV;
		Vertex goalV;
		ArrayList<Vertex> searchspace =  getVertexs();
		for(int i = 0; i <searchspace.size(); i++) {
			startV = searchspace.get(i);
			for(int x = 0; x< vertexs.size(); x++) {
				goalV = searchspace.get(x);
				if(startV != goalV) { //checking their not the same vertex
					if(checkr2(startV,goalV)){
						applyr2(startV,goalV);
						return true;
					}
				}
			}
		}
		return false;
	}	
	private boolean matchr3() {
		Vertex blankV;
		Vertex lockV;
		Vertex goalV;
		ArrayList<Vertex> searchspace =  getVertexs();
		for(int i = 0; i <searchspace.size(); i++) {
			blankV = searchspace.get(i);
			for(int x = 0; x< vertexs.size(); x++) {
				lockV = searchspace.get(x);
				for(int y = 0; y < vertexs.size(); y++) {
					goalV = searchspace.get(x);
					if((blankV != goalV) && (blankV != lockV) && (goalV != blankV)) {
						if(checkr3(blankV,lockV,goalV)) {
							applyr3(blankV,lockV,goalV);
							return true;
						}
					}
				}
			}
		}
		return false;
	}	
	private boolean matchr4() {
		Vertex blankS;
		Vertex blankP;
		Vertex blankG;
		ArrayList<Vertex> searchspace = getVertexs();
		for(int i = 0; i <searchspace.size(); i++) {
			blankS = searchspace.get(i);
			for(int x = 0; x< vertexs.size(); x++) {
				blankP = searchspace.get(x);
				for(int y = 0; y < vertexs.size(); y++) {
					blankG = searchspace.get(x);
					if((blankS != blankP) && (blankS != blankG) && (blankP != blankG)) {
						if(checkr4(blankS,blankP,blankG)) {
							applyr4(blankS,blankP,blankG);
							return true;
						}
					}
				}
			}
		}
		return false;
	}	
	private boolean matchr5() {
		Vertex blank1;
		Vertex blank2;
		ArrayList<Vertex> searchspace = getVertexs();
		for(int i = 0; i <searchspace.size(); i++) {
			blank1 = searchspace.get(i);
			for(int x = 0; x< vertexs.size(); x++) {
				blank2 = searchspace.get(x);
				if(blank1 != blank2) {
					if(checkr5(blank1,blank2)) {
						applyr5(blank1,blank2);
						return true;
					}
				}
			}
		}
		return false;
	}
	private boolean matchr6(){ //this match works slightly differently, as it will almost always match with something early in the graph, so some randomness is added
		int numFails = 0;
		Vertex blank1;
		ArrayList<Vertex> searchspace =  getVertexs();
		int chosen = rnd.nextInt(searchspace.size());
		blank1 = searchspace.get(chosen);
		while (!checkr6(blank1)) {
			chosen = rnd.nextInt(searchspace.size());
			blank1 = searchspace.get(chosen);
			numFails+=1;
			if (numFails >=searchspace.size()) { //if there is no matches, return false
				return false;
			}
		}
		applyr6(blank1);
		return true;
	}
	private boolean matchr7() {
		Vertex blank1;
		Vertex blankE;
		Vertex blank2;
		Vertex CriticalE;
		ArrayList<Vertex> searchspace =  getVertexs();
		for(int i = 0; i <searchspace.size(); i++) {
			blank1 = searchspace.get(i);
			for(int x = 0; x< vertexs.size(); x++) {
				blankE = searchspace.get(x);
				for(int y = 0; y <searchspace.size(); y++) {
					blank2 = searchspace.get(y);
					for(int z = 0; z< vertexs.size(); z++) {
						CriticalE = searchspace.get(z);
						if((blank1 != blank2) && (blank1 != blankE) && (blank1 != CriticalE)) {
							if((blankE != blank2) && (blankE != CriticalE)) {
								if(blank2 != CriticalE) {
									applyr7 (blank1, blankE, blank2, CriticalE); 
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
