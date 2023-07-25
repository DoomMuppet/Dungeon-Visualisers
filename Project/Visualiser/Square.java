package Visualiser;

import java.awt.*;
import java.awt.geom.*;
public class Square {
    private double x;
    private double y;
    private double size;
    private Color colour;

    public Square(double x, double y, double size, Color colour){
        this.x = x;
        this.y = y;
        this.size = size;
        this.colour = colour;
    }

    public void drawSquare(Graphics2D g2d){
        Rectangle2D.Double r = new Rectangle2D.Double(x,y,size,size);
        g2d.setColor(colour);
        g2d.fill(r);
    }
}
