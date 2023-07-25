package MissionGenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import Visualiser.DungeonDrawer;


//this is my new dungeon class
//Gets given a mission and creates a space for it
//the mapping is isomorphic
//Each Vertex is mapped to a room
public class Dungeon {
	public Mission mission;
	public Map<String,Room> rooms;
	ArrayList<Vertex> vertexs;
	ArrayList<Edge> edges;
	String[][] grid;
	int size;
	ArrayList<Node> closed;
	ArrayList<Node> open;
	Room start;
	Room goal;
	Stocking stocker;
	
	Dungeon(Mission mission, String Mode)
	{
		double lvlCR = 5;
		stocker = new Stocking();
		this.size = 50;
		this.mission = mission;
		this.rooms = new HashMap<String,Room>();
		this.vertexs = mission.getVertexs();
		this.edges = mission.getEdges();
		grid = new String[size][size];
		mapDungeon();
		stocker.stock(rooms,lvlCR);
		if(Mode == "Deery"){
			saveDungeon(rooms, grid);
		}


		DungeonDrawer drawer = new DungeonDrawer();
		drawer.drawFromDungeon(mission, Mode, rooms); //Options: Main Hub, Chebyshev
	}
	
	private void mapDungeon() { //this function performs the actual mapping, the steps of which are broken down into separete functions
		createRooms();
		try {
			//posRoom(0,0,start);
			positionRooms("v0");
			posEdges();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int empty =0;
		for(int y =0;y <size; y++) {
			for(int x = 0;x <size;x++) {
				//System.out.print(" " + grid[x][y]);
				if(grid[x][y]== null){
					empty ++;
				}
			}
			//System.out.print("\n");
			
		}
		System.out.println("Unused space in Deery Representation: " + empty + "/" + size*size);
	}
	
	private void createRooms() {
		for(Vertex v : vertexs) { //creating a room for each of the vertexs
			rooms.put(v.getName(), new Room(v));
			if(v.getType() == "start") {
				start = rooms.get(v.getName());
			} else if (v.getType() == "goal") {
				goal = rooms.get(v.getName());
			}
		}
	}
	// positions each of the rooms within the dungeon
	// must ensure the rooms don't clash, this is basically done by having a space much large than the number of rooms
	// although research shows this doesn't matter for players this display needs to work for dms
	// basically it places them all into a grid and then works out the paths between them
	// all rooms occupy a 2 by 2 area of the grid with their position being the top left corner
	
	//I'm now changing this to start from the start of the dungeon and layout rooms from there
	//there is also now a stack of room positions
	//works recursively
	private boolean positionRooms(String baseName) throws Exception {
		int[] currentPos;
		Room current;
		for(Room r: rooms.values()) {
			anyPos(r);
		}
		return true;
	}
	
	private boolean anyPos(Room r) {
		for(int y = 0; y< size-2; y +=10) {
			if (Math.floorMod(y/10, 2) == 0) {
				for (int x = 0; x < size-2; x +=10) {
					if(posRoom(x,y,r)) {
						return true;
					}
				}
			}
			else {
				for (int x = 0; x < size-2; x +=15) {
					if(posRoom(x,y,r)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
		
	private void posEdges() {
		for(Edge e : edges) { //mapping out all of the non-key paths
			if(!e.key) {
				Room source = rooms.get(e.getSource().getName());
				Room target = rooms.get(e.getTarget().getName());
				pathFind(source,target);
			}
		}
	}
	
	
	/*private boolean inGrid(Room room) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (grid[x][y] == room.getName()) {
					return true;
				}
			}
		}
		return false;
	}*/
	
	
	private boolean posRoom(int xpos, int ypos, Room room) {
		if((grid[xpos][ypos] == null) ||grid[xpos][ypos] == room.getName()) {
			room.setXpos(xpos);
			room.setYpos(ypos);
			grid[xpos][ypos] = room.getName(); //fills in the are this takes up with its name
			grid[xpos +1][ypos] = room.getName();
			grid[xpos][ypos+1] = room.getName();
			grid[xpos+1][ypos+1] = room.getName();
			grid[xpos+2][ypos] = room.getName();
			grid[xpos+2][ypos+1] = room.getName();
			grid[xpos+2][ypos+2] = room.getName();
			grid[xpos][ypos+2] = room.getName();
			grid[xpos+1][ypos+2] = room.getName();
			return true;
		}
		return false;
	}

	//uses A* to find and map out the path between source and target
	//Paths cannot go through rooms
	public ArrayList<Node> pathFind(Room source, Room target){
		Node start = new Node(source.getPosition()[0]+1,source.getPosition()[1]+1); //starts from the center of rooms
		Node goal = new Node(target.getPosition()[0]+1,target.getPosition()[1]+1);
		//System.out.println("Start: " + start.x + "," + start.y);
		//System.out.println("End: " + goal.x + "," + goal.y);
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		
		Map<Node,Node> cameFrom = new HashMap<Node,Node>();
		start.g = 0;
		start.f = manhattanDist(start.x,start.y,goal.x,goal.y);
		
		while(!open.isEmpty()) {
			Node current =  lowestOpen(open);
			if ((current == goal) || (grid[current.x][current.y] == target.getName())){
				return markPath(cameFrom,current,start);
			}
			open.remove(current);
			closed.add(current);
			ArrayList<Node> neighbours = getNeighbours2(current,goal,source);
			for (Node neighbour : neighbours) {
				if (closed.contains(neighbour)) {
					continue;
				}
				int tentativeG = current.g + 1;
				if(!open.contains(neighbour)) {
					open.add(neighbour);
				}
				if (tentativeG > neighbour.g) {
					continue;
				}
				cameFrom.put(neighbour, current);
				//System.out.println("Came from: " + cameFrom.get(neighbour).x + "," + cameFrom.get(neighbour).y);
				neighbour.g = tentativeG;
				neighbour.f = neighbour.g + neighbour.h;
				}
			}
		throw new Error("Didn't find a path");
	}
		
	// calculates the manhattan distance between two points
	private int manhattanDist(int xpos1, int ypos1, int xpos2, int ypos2) {
		int dist = Math.abs(xpos1-xpos2) +Math.abs(ypos1 - ypos2);
		return dist;
	}
	//marks out a path once its been worked out
	private ArrayList<Node> markPath(Map<Node,Node> path,Node current,Node start) {
		ArrayList<Node> totalpath = new ArrayList<Node>();
		totalpath.add(current);
		/*
		if(grid[path.get(current).x][path.get(current).y]==null) {
			grid[current.x][current.y] = "End";
		}
		*/
		//System.out.println("Search length: " + path.size());
		while (path.containsKey(current)) {
			current = path.get(current);
			totalpath.add(current);
			//System.out.println(current.x + " " + current.y);
		}
		//System.out.println("Path length: " + totalpath.size());
		
		for (int i = 0; i < totalpath.size(); i++) {
			int x = totalpath.get(i).x;
			int y = totalpath.get(i).y;
			//System.out.println("Step: " + x + "," +y);
			if (grid[x][y] == null) {
				grid[x][y] = "Path";
			}
		}
		return totalpath;
	}

	
	//finds the value in the open set with the lowest SScore
	private Node lowestOpen(ArrayList<Node>open) {
		Node node = new Node(0,0);
		int current= 99999; //instantiating a really big current lowest
		for (Node r : open) {
			if (r.f < current) {
				current = r.f;
				node = r;
			}
		}
		
		return node;
	}

	private ArrayList<Node> getNeighbours2(Node current, Node end, Room start){
		Node north;
		Node east;
		Node south;
		Node west;
		ArrayList<Node> neighbours = new ArrayList<Node>();
		
		if(current.y-1 < 0){
			north = null;
		}else if ((grid[current.x][current.y-1] == null) || (grid[current.x][current.y-1] == start.getName())|| (grid[current.x][current.y-1] == "Path") || (grid[current.x][current.y-1] == grid[end.x][end.y])){
		
		north = new Node (current.x,current.y -1);
		
		north.h = (Math.abs (north.x - end.x)) + (Math.abs(north.y - end.y));
		
		north.g = current.g + 1;
		
		north.f = north.h + north.g;
		
		neighbours.add(north);
		}
		
		if(current.x+1 >= size){
			east = null;
		}else if ((grid[current.x+1][current.y]== null) || (grid[current.x+1][current.y] == start.getName())|| (grid[current.x+1][current.y] == "Path")|| (grid[current.x+1][current.y] == grid[end.x][end.y])) {
		east = new Node (current.x + 1,current.y);
		
		east.h = Math.abs (east.x - end.x) + Math.abs(east.y - end.y);
		
		east.g = current.g + 1;
		
		east.f = east.h + east.g;		
		neighbours.add(east);
		}
		
		if(current.y+1 >= size){
			south = null;
		}else if ((grid[current.x][current.y+1]== null) || (grid[current.x][current.y+1] == start.getName())|| (grid[current.x][current.y+1] == "Path")|| (grid[current.x][current.y+1] == grid[end.x][end.y])) {
		south = new Node ( current.x, current.y +1);
		
		south.h = Math.abs (south.x - end.x) + Math.abs(south.y - end.y);
		
		south.g = current.g + 1;
		
		south.f = south.h + south.g;
		
		neighbours.add(south);
		}
		
		if(current.x-1 < 0){
			west = null;
		}else if ((grid[current.x-1][current.y] == null) || (grid[current.x-1][current.y] == start.getName()) || (grid[current.x-1][current.y] == "Path")|| (grid[current.x-1][current.y] == grid[end.x][end.y])) {
		
		west = new Node (current.x - 1,current.y);
		
		west.h = Math.abs (west.x - end.x) + Math.abs(west.y - end.y);
		
		west.g = current.g + 1;
		
		west.f = west.h + west.g;
		
		neighbours.add(west);
		}
		return neighbours;
	
	}
	//saves the dungeon as an image, based off inherited code
	private void saveDungeon(Map<String,Room> rooms,String[][] dungeon) {
		BufferedImage image = new BufferedImage(size*10, size*10, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
			
				if (dungeon[i][j] == null) {
					g2.setColor(Color.white);
					g2.fillRect(i*10, j*10, 100, 100);
				} else if (dungeon[i][j] == "Path") {
					g2.setColor(Color.black);
					g2.fillRect(i*10, j*10, 10, 10);
				} else if (dungeon[i][j].contains("v")) {
					g2.setColor(Color.blue);
					g2.fillRect(i*10,j*10, 100, 100);
				}
			}

		}

		// write room number in top left hand corner of each room in root in black

		for (String name : rooms.keySet()) {
			g2.setColor(Color.red);
			g2.drawString(name, ((rooms.get(name).xpos)*10),( rooms.get(name).ypos+1)*10);
			
		}
		
		

		String fmt = "Dungeon%02d.png";
		File f = null;
		for (int i = 1;; i++) {
			f = new File(String.format(fmt, i));
			if (!f.exists()) {
				break;
			}
		}
		try {
			ImageIO.write(image, "png", f);

		} catch (IOException e) {
			System.err.println("Could not save image");
		}
	}
}
