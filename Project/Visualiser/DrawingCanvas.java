package Visualiser;

import java.awt.*;
import java.awt.geom.*;
import java.util.Map;

import javax.swing.*;

import MissionGenerator.Mission;
import MissionGenerator.Room;

public class DrawingCanvas extends JComponent {
    private int screenWidth;
    private int screenHeight;
    private int squareSize;
    private RoomGenerator rooms;
    
    /*public DrawingCanvas(int w, int h, int size,int noiseSpread, int numOfPoints){
        screenWidth = w;
        screenHeight = h;
        squareSize = size;
        rooms = new RoomGenerator(screenWidth/squareSize,screenHeight/squareSize,noiseSpread,numOfPoints,size,2);
    }*/

    public DrawingCanvas(int w, int h, int size, int noiseSpread, int numOfPoints, Mission mission, String Mode, Map<String,Room> roomPoints){
        screenWidth = w;
        screenHeight = h;
        squareSize = size;
        rooms = new RoomGenerator(screenWidth/squareSize,screenHeight/squareSize,noiseSpread,numOfPoints,size,0,mission, Mode, roomPoints);
    }

    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        Rectangle2D.Double r = new Rectangle2D.Double(0,0,screenWidth,screenHeight);
        g2d.setColor(Color.BLACK);
        g2d.fill(r);

        rooms.drawRooms(g2d);
        rooms.drawLines(g2d);
        
        
    }


}
