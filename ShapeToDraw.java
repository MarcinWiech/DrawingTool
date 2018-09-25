
import java.awt.*;
import java.awt.geom.Line2D;

public class ShapeToDraw {

    //member variables
    private Point startPoint, endPoint;
    private int size;
    private Color color;

    //constructor
    public ShapeToDraw(Point startPoint, Point endPoint, int size, Color color){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.size = size;
        this.color = color;
    }

    //drawing shape on given Graphics2D
    public void drawShape(Graphics2D g2){

        //set the color to the one of the shape
        g2.setColor(this.color);

        //set the size
        g2.setStroke(new BasicStroke(this.size));

        //if the start and end point differs draw a line
        if(!this.startPoint.equals(this.endPoint)){
            g2.draw(new Line2D.Double(this.startPoint.getX(), this.startPoint.getY(), this.endPoint.getX(),this.endPoint.getY()));
        }
        //else draw an oval
        else{
            g2.fillOval((int) (this.startPoint.getX()-(this.size+1)/2),(int) (this.startPoint.getY()-(this.size+1)/2),this.getSize()+1,this.getSize()+1);
        }

    }

    //get the size
    public int getSize(){
        return size;
    }
}
