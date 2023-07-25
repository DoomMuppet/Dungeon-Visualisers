package Visualiser;

import java.util.Map;

import javax.swing.*;

import MissionGenerator.Mission;
import MissionGenerator.Room;

public class DungeonDrawer {

    public void drawFromDungeon(Mission mission, String Mode, Map<String,Room> rooms){
        int w = 640;
        int h = 640;
        
        if(Mode == "Rewrite"){
            w = 960; 
            h = 960;
        }

        int squareSize = 5; //Squares of this size fill up entire screen with worley noise.
        int noiseSpread = 20;
        int numOfPoints = rooms.size();
        JFrame f = new JFrame();
        DrawingCanvas dc = new DrawingCanvas(w,h,squareSize,noiseSpread,numOfPoints, mission, Mode, rooms);
        f.setSize(w,h);
        f.setTitle("Drawing test");
        f.add(dc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
