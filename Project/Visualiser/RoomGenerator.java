package Visualiser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
//import java.util.Arrays;
import java.util.Random;

import MissionGenerator.Mission;
import MissionGenerator.Room;

import java.awt.*;


public class RoomGenerator {
    private int width;
    private int height;
    private String mode;
    //private static int numOfPoints;
    private static int noiseSpread;
    private int squareSize;
    private int borderWidth;
    private Mission mission;
    private static int[][] points= {{0,0},{100,100},{100,0}};
    private Map<String,int[]> roomPoints  = new HashMap<String,int[]>();
   
    private Map<String, Room> rooms;

   /* public RoomGenerator(int w, int h, int noiseSpread, int pointNum, int size, int borderWidth){
        this.width = w;
        this.height = h;
        this.squareSize = size;
        //RoomGenerator.numOfPoints = pointNum;
        RoomGenerator.noiseSpread = noiseSpread;
        this.borderWidth = borderWidth;
        RoomGenerator.points = GenerateRandomPoints(w,h,pointNum);
    }*/

    public RoomGenerator(int w, int h, int noiseSpread, int pointNum, int size, int borderWidth, Mission mission, String Mode, Map<String,Room> rooms){
        this.width = w;
        this.height = h;
        this.squareSize = size;
       // RoomGenerator.numOfPoints = pointNum;
        RoomGenerator.noiseSpread = noiseSpread;
        this.borderWidth = borderWidth;
        this.mission = mission;
        this.rooms = rooms;
        this.mode = Mode;
        if(mode == "Rewrite"){
            //while(points.length != rooms.size()){ //Able to allocate all rooms?
            roomPoints = RewritePoints(rooms,w,h);
            points = roomPoints.values().toArray(new int[roomPoints.size()][2]);
            //}
        } else {
            RoomGenerator.points = GetPointsFromRooms(rooms, w, h, Mode);
        
            int i=0;
            for(String roomName : rooms.keySet()){
                if(RoomGenerator.points[i] != null){
                    roomPoints.put(roomName, RoomGenerator.points[i]);
                }
                i+=1;
            }
        }
    }


    public void drawRooms(Graphics2D g2d){
        int numRoomSquares = 0;
        int numSquaresTotal = 0;
        int numConnectionSquares = 0;
        int[][] noiseMap;
        int noiseLevel;
        //int colourPerNoise = (int) 255/noiseSpread;
        Color colour = Color.WHITE;
        noiseMap = WorleyNoise(width,height, points, noiseSpread, this.mode);
        if(this.mode == "Main Hub"){
            for(int i = borderWidth; i<height-borderWidth*6; i++){
                for(int j= borderWidth; j<width-borderWidth*3; j++){
                    noiseLevel = noiseMap[i][j];
                    if ((noiseLevel > 13)){
                        //colour = new Color(colourPerNoise*noiseLevel, colourPerNoise*noiseLevel, colourPerNoise*noiseLevel);
                        Square square = new Square(squareSize * j, squareSize*i, squareSize, colour);
                        square.drawSquare(g2d);
                        numRoomSquares++;
                    } else if ((noiseLevel < 12) && (noiseLevel > 8)){
                       // colour = Color.WHITE;
                        Square square = new Square(squareSize * j, squareSize*i, squareSize, colour);
                        square.drawSquare(g2d);
                        numConnectionSquares++;
                    }
                    numSquaresTotal++;
                }
            }
            System.out.println("Unused space in Main Hub Representation: " + numRoomSquares + "/" + numSquaresTotal);

            //ADDING DOORS FOR MAIN HUB <3
            int[] startPoint, endPoint;
            Color darkRed = new Color(120,0,0);
            Color darkBlue = new Color(0,0,120);
            Color darkGreen = new Color(0,120,0);
            Color[] keyColours = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.DARK_GRAY, darkRed, darkBlue, darkGreen};
            int keyIndex = 0;
            HashSet<String> openedRooms = new HashSet<String>(); //Rooms fully explored
            Queue<String> availableRooms = new LinkedList<>(); //Rooms to be explored
            Map<String, Integer> roomConnectionNums = new HashMap<>(); //How many connections each room contains
            int connectionNum = 0;
            

            double[][] doorAngles = {{-1,6.5},{6.5,-1},{-1,-8.5},{-8.5,-1}}; //4 cardinal directions for doors. NSEW
            Boolean roomOpened = false;

            
            for(Map.Entry<String, Room> entry : rooms.entrySet()){
                roomConnectionNums.put(entry.getKey(), 0);
            }
            //Mark final room
            startPoint = roomPoints.get("v1");
            Square finalRoom = new Square(squareSize * (startPoint[1]-2.5), squareSize*(startPoint[0]-2.5), squareSize*6, Color.BLACK);
            finalRoom.drawSquare(g2d);

            String firstRoom = "v0";
            availableRooms.add(firstRoom);
            
            //Open first room
            startPoint = roomPoints.get(firstRoom);
            Square openDoor = new Square(squareSize * (startPoint[1]-1), squareSize*(startPoint[0]+6.5), squareSize*3, Color.WHITE);
            connectionNum = roomConnectionNums.get(firstRoom);
            connectionNum++;
            if(connectionNum > 3){ connectionNum = 0;}    
            roomConnectionNums.put(firstRoom, connectionNum); 
            openDoor.drawSquare(g2d);
            openedRooms.add(firstRoom);

            
            while(!availableRooms.isEmpty()){ //If still rooms to explore
                String currentRoom = availableRooms.remove(); //Loop through rooms
                System.out.println("Room:" + currentRoom);
                colour = keyColours[keyIndex]; //Current colour
                startPoint = roomPoints.get(currentRoom); //Actual location of room
                roomOpened = false; //Checking if a room was opened by this exploration
                 
                //For other rooms we are connected to
                for(String connection : rooms.get(currentRoom).getOutgoingConnections()){   
                    if(!openedRooms.contains(connection)){ //If isnt already opened
                        System.out.println("Opening: " + connection);
                        availableRooms.add(connection);
                        endPoint = roomPoints.get(connection); //Get connected room location
                        connectionNum = roomConnectionNums.get(connection); //How many doors does room have?

                        Square squareLock = new Square(squareSize * (endPoint[1] + doorAngles[connectionNum][0]), squareSize*(endPoint[0] + doorAngles[connectionNum][1]) , squareSize*3, colour);
                        squareLock.drawSquare(g2d);

                        roomOpened = true; //Room was opened by this exploration!
                        openedRooms.add(currentRoom); //Current room has been opened! Dont explore again

                        connectionNum++;
                        if(connectionNum > 3){ connectionNum = 0;}    
                        roomConnectionNums.put(currentRoom, connectionNum); //Make sure process knows a door was added in this room
                    }
                }

                //Only draw a key if there is a lock it corresponds with
                if(roomOpened == true){
                    keyIndex++; //Only loop through key index if a key is needed
                    if (keyIndex > 8){
                        keyIndex = 0;
                    }
                    Square squareKey = new Square(squareSize * (startPoint[1]-0.5), squareSize*(startPoint[0]-0.5), squareSize*2, colour);
                    squareKey.drawSquare(g2d);
                }
            }
        } else if ((this.mode == "Chebyshev") || (this.mode == "Rewrite")){
            for(int i = borderWidth; i<height-borderWidth*6; i++){
                for(int j= borderWidth; j<width-borderWidth*3; j++){
                    noiseLevel = noiseMap[i][j];
                    if (noiseLevel > 2){
                        //colour = new Color(colourPerNoise*noiseLevel, colourPerNoise*noiseLevel, colourPerNoise*noiseLevel);
                        Square square = new Square(squareSize * j, squareSize*i, squareSize, colour);
                        square.drawSquare(g2d);
                        numRoomSquares++;
                    }
                    numSquaresTotal++;
                }
            }
            System.out.println("Unused space in " +mode+" Representation: " + numRoomSquares + "/" + numSquaresTotal);
        }
    
        
        System.out.println("Connection to Room ratio: " + numConnectionSquares + ":" + numRoomSquares);
    } 

    public void drawLines(Graphics2D g2d){
       if((this.mode == "Chebyshev")||(mode == "Rewrite")){
            int[] startPoint, endPoint;
            for(String start: roomPoints.keySet()){
                    startPoint = roomPoints.get(start);
                    for(String connection : rooms.get(start).getOutgoingConnections()){
                        endPoint = roomPoints.get(connection);
                        g2d.setColor(Color.BLUE);
                        g2d.drawLine(startPoint[1]*this.squareSize,startPoint[0]*this.squareSize,endPoint[1]*this.squareSize,endPoint[0]*this.squareSize);
                    }
            }
        } 

    }

    public static int[][] GetPointsFromRooms(Map<String,Room> rooms, int screenWidth, int screenHeight, String mode){
        int[][] roomPositions = new int [rooms.size()][2];
        int entryNum = 0;
        double maxValue = 0;
        int xDistancingVal; //random default val
        int yDistancingVal; //random default val
        if(mode != "Rewrite"){
            for(Map.Entry<String,Room> entry : rooms.entrySet()){
                roomPositions[entryNum] = entry.getValue().getPosition();
                if(roomPositions[entryNum][0] > maxValue){
                    maxValue = roomPositions[entryNum][0];
                }
                if(roomPositions[entryNum][1] >maxValue){
                    maxValue = roomPositions[entryNum][1];
                }
    
                entryNum +=1;
            }
        }

        //Spacing between rooms roughly
        xDistancingVal = (int) Math.floor(screenWidth/((maxValue/10)+2));
        yDistancingVal = (int) Math.floor(screenHeight/((maxValue/10)+2));

        //Mode Select
        if(mode == "Chebyshev"){
            //Adding a random element
            Random rand = new Random();
            int xRandOffset, yRandOffset;
            for(int[] pnt : roomPositions){
                //The offset shouldnt be more than half the spacing, so it never moves past another room
                xRandOffset = (rand.nextInt(xDistancingVal))- (int) 0.5*xDistancingVal;
                yRandOffset = (rand.nextInt(yDistancingVal))- (int) 0.5*yDistancingVal;
                pnt[0] = (pnt[0]/10+1) * (int)xDistancingVal + xRandOffset;
                pnt[1] = (pnt[1]/10+1) * (int)yDistancingVal + yRandOffset;
    
            }
        } else if (mode == "Main Hub"){
            //No random in Main Hub mode
            int equalDistanceVal = (xDistancingVal > yDistancingVal) ? yDistancingVal : xDistancingVal;
            for(int[] pnt : roomPositions){
                pnt[0] = (pnt[0]/10+1) * (int)equalDistanceVal;
                pnt[1] = (pnt[1]/10+1) * (int)equalDistanceVal;
            }
        } 

       



        //System.out.println(Arrays.deepToString(roomPoints));
        return roomPositions;
    }

    public static Map<String, int[]> RewritePoints(Map<String,Room> rooms, int screenWidth, int screenHeight){
        int[] startPoint = {screenWidth/2,screenHeight/2};
        HashSet<String> openedRooms = new HashSet<String>(); //Rooms fully explored
        Queue<String> availableRooms = new LinkedList<>(); //Rooms to be explored

        int connectionNum = 0;
        
        //double[][] roomAngles = {{1,1},{-1,-1},{-1,1},{1,-1},{0,1},{0,-1},{1,0},{-1,0}}; //8 directions for rooms. NSEW ALTERNATING
        double[][] roomAngles = {{1,1},{1,0},{0,1},{-1,1},{1,-1},{0,-1},{-1,0},{-1,-1}}; //NW to SE. Increases to spread out large connected rooms
        HashMap<String, int[]> positionNames = new HashMap<String,int[]>();

        String firstRoom = "v0";
        positionNames.put(firstRoom, startPoint);
        openedRooms.add(firstRoom);
        

        Random rand = new Random();
        int xRandOffset, yRandOffset;
        String currentRoom;
        int highestPoint = screenHeight;
        int leftestPoint = screenWidth;
        ArrayList<String> outgoingConnections = new ArrayList<String>();
        Boolean pointAccepted;
        availableRooms.add(firstRoom);  
        while(!availableRooms.isEmpty()){ //If still rooms to explore
            currentRoom = availableRooms.remove(); //Loop through rooms
            System.out.println("Room:" + currentRoom);
            startPoint = positionNames.get(currentRoom);
            connectionNum = 0;
            //For other rooms we are connected to
            outgoingConnections = rooms.get(currentRoom).getOutgoingConnections();
            for(String connection : outgoingConnections){   
                if((!openedRooms.contains(connection)) && (startPoint != null)){ //If isnt already opened
                    System.out.println("Opening: " + connection);
                    availableRooms.add(connection);
                    
                     //How many doors does room have?
                    int[] endPoint = new int[2];
                    pointAccepted = false;
                    endPoint[0] = startPoint[0] + ((int) roomAngles[connectionNum][0] *  15);
                    endPoint[1] = startPoint[1] + ((int) roomAngles[connectionNum][1] *  15);
                    while (!pointAccepted){ //If point already been used
                        for(Map.Entry<String,int[]> entry : positionNames.entrySet()){
                            if((entry.getValue()[0] == endPoint[0]) && (entry.getValue()[1] == endPoint[1])){
                                connectionNum++; //Move
                                endPoint[0] = startPoint[0] + ((int) roomAngles[connectionNum][0] *  15);
                                endPoint[1] = startPoint[1] + ((int) roomAngles[connectionNum][1] *  15);
                                pointAccepted = false;
                                System.out.println("Rewritten to " + endPoint[0] + ", " + endPoint[1]); 
                                break;
                            } else {
                                pointAccepted = true;
                            }
                        }          
                    }


                    System.out.println(endPoint[0] + ", " + endPoint[1]);    
                    positionNames.put(connection, endPoint);
                    
                    if(leftestPoint > endPoint[0]){
                        leftestPoint = endPoint[0];
                    }
                    
                    if(highestPoint > endPoint[1]){
                        highestPoint = endPoint[1];
                    }
                    connectionNum++;
                    openedRooms.add(connection); //Current room has been opened! Dont explore again                    
                    

                }
            }
        }

        //Move everything to the top left corner
        for(String roomName : positionNames.keySet()){
            xRandOffset = (rand.nextInt(8))- 4;
            yRandOffset = (rand.nextInt(8))- 4;
            int[] pnt = positionNames.get(roomName);
            pnt[0] -= leftestPoint - 40 + xRandOffset; //Noise spread is 20, +5 for border
            pnt[1] -= highestPoint - 30 + yRandOffset; 
            positionNames.put(roomName, pnt);
        }
        return positionNames;
    }

    public static int[][] GenerateRandomPoints(int width, int height, int points){
        int randX,randY;
        Random rand = new Random();
        int[][] listOfPoints = new int[points][2];

        for(int r = 0; r < points; r++)
        {
            randY = rand.nextInt( height); 
            randX = rand.nextInt( width);
            

            //Dosent check if its already been done.
            listOfPoints[r][0] = randY;
            listOfPoints[r][1] = randX;
    
        }
        return listOfPoints;
    }

    public static int[][] WorleyNoise(int width, int height, int[][] points, int pointNoise, String mode){ 
        int[][] noiseMap = new int[height][width];
        int dist; 
        int secondClosest =0;
        if((mode == "Chebyshev")||(mode == "Rewrite")){
            for(int j = 0; j<height; j++){
                for(int i = 0; i<width; i++){
    
                    for(int pnt[] : points){
                        if(pnt == null){ continue;}
                        dist = (int) Math.round(pointNoise - squareRoomDistance(i,j,pnt[0],pnt[1]));
                        if(noiseMap[i][j] < dist){
                            
                            secondClosest = noiseMap[i][j];
                            noiseMap[i][j] = dist;
                        } 
                        
                    }
                   noiseMap[i][j] = noiseMap[i][j] - secondClosest; //Gives f1-f2 version of noise
                }
            }
        } else if (mode == "Main Hub"){

            
            for(int pnt[] : points){
                int dir = 0;
                int T=0;
                int B = height-1;
                int L = 0;
                int R = width-1;
                while(T<=B && L <= R){ //Implemented as a spiral going inwards
                    if(dir ==0){ // Left to Right
                        for(int i = L; i<=R; i++){
                            
                            dist = (int) Math.round(pointNoise - squareRoomDistance(T,i,pnt[0],pnt[1]));
                            if(noiseMap[T][i] < dist){
                                
                               // secondClosest = noiseMap[T][i];
                                noiseMap[T][i] = dist;
                            } 
                            
                           // noiseMap[T][i] = noiseMap[T][i] - secondClosest; 
                        }
                        T++;
                        dir = 1;
                    } else if (dir == 1){ // Top to Bottom
                        for(int i = T; i<=B; i++){
                        
                            dist = (int) Math.round(pointNoise - squareRoomDistance(i,R,pnt[0],pnt[1]));
                            if(noiseMap[i][R] < dist){
                                
                               // secondClosest = noiseMap[i][R];
                                noiseMap[i][R] = dist;
                            } 
                            
                            //noiseMap[i][R] = noiseMap[i][R] - secondClosest; 
                        }
                        R--;
                        dir = 2;
                    } else if (dir == 2){ // Right to Left
                        for(int i = R; i>=L; i--){
                            
                            dist = (int) Math.round(pointNoise - squareRoomDistance(B,i,pnt[0],pnt[1]));
                            if(noiseMap[B][i] < dist){
                                
                                //secondClosest = noiseMap[B][i];
                                noiseMap[B][i] = dist;
                            } 
                        
                            //noiseMap[B][i] = noiseMap[B][i] - secondClosest; 
                        }
                        B--;
                        dir = 3;
                    } else if (dir == 3){ //Bottom to Top
                        for(int i = B; i>=T; i--){
                            
                            dist = (int) Math.round(pointNoise - squareRoomDistance(i,L,pnt[0],pnt[1]));
                            if(noiseMap[i][L] < dist){
                                
                                //secondClosest = noiseMap[i][L];
                                noiseMap[i][L] = dist;
                            } 
                            
                            //noiseMap[i][L] = noiseMap[i][L] - secondClosest; 
                        }
                        L++;
                        dir = 0;
                    }
                }
            }

            
        }
        return noiseMap;


    }

    static int squareRoomDistance(int x, int y, int x2, int y2){ //Use for chebyshev noise
        return Math.max(Math.abs(x2-x),Math.abs(y2-y)); 
    }

    static double circleRoomDistance(int x, int y,int x2, int y2){ //Use for worley noise
        return Math.sqrt(Math.pow((x2-x),2) + Math.pow((y2-y),2));
    }

}
