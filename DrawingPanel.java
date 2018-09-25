
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {

    //member variables
    private Graphics2D g2;

    //number of sector lines needed to repaint when window changes size
    private int linesCounter, mouseX, mouseY, mouseOldX, mouseOldY;

    //stores lines
    private ArrayList<Shape> lines = new ArrayList<Shape>();

    //stores current points;
    private Queue<ShapeToDraw> currentDrawing;

    //stores drawings performed used in Undo
    private Stack<Queue<ShapeToDraw>> drawingsPerformed;

    //stores drawings redone used to store Undo images
    private Stack<Queue<ShapeToDraw>> drawingsRedone;

    //private Color backgroundColor = new Color(0,0,0);
    private Color mouseColor, previousColor;


    private Point mousePoint;

    //initial options
    private int size = 1;
    private boolean erase = false;
    private boolean reflect = false;
    private boolean linesVisible = true;


    //constructor
    public DrawingPanel() {

        super();

        //setting layout
        this.setLayout(new FlowLayout());

        //initialising member variables
        drawingsPerformed = new Stack<Queue<ShapeToDraw>>();
        drawingsRedone = new Stack<Queue<ShapeToDraw>>();
        currentDrawing = new LinkedList<>();
        mouseColor = new Color(244,244,244);
        this.linesCounter = 0;

        //adding MouseAdaper
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                //gets coordinates where mouse was pressed
                mouseOldX = e.getX();
                mouseOldY = e.getY();
                mouseX = e.getX();
                mouseY = e.getY();

                //adds a circle at the beginning of a line
                if(g2 != null) {

                    //creates a point
                    mousePoint = new Point(mouseX,mouseY);
                    //adds the point to the current drawn points
                    currentDrawing.add(new ShapeToDraw(mousePoint, mousePoint, size, mouseColor));

                    //if reflecting adds also reflected point
                    if (reflect) {
                        currentDrawing.add(new ShapeToDraw(new Point(getSize().width - mouseX, mouseY), new Point(getSize().width - mouseOldX, mouseOldY), size, mouseColor));
                    }
                }
                //adds a copy of current drawing to the drawings performed stack
                //drawingsPerformed.push(new LinkedList<ShapeToDraw>(currentDrawing));
            }

            public void mouseReleased(MouseEvent e) {

                //when mouse released add the copy of current drawing to the stack
                drawingsPerformed.push(new LinkedList<ShapeToDraw>(currentDrawing));

                //clear current drawing
                currentDrawing.clear();
            }
        });

        //adding mouse motion adapter
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {

                //declaring mouse's point
                mouseX = e.getX();
                mouseY = e.getY();
                mousePoint = new Point(mouseX, mouseY);

                if(g2 != null) {

                    //adding a line to the currentDrawing
                    currentDrawing.add(new ShapeToDraw(mousePoint, new Point(mouseOldX,mouseOldY), size, mouseColor));

                    //if reflected adds also reflected point
                    if(reflect){
                        currentDrawing.add(new ShapeToDraw(new Point(getSize().width-mouseX,mouseY),new Point(getSize().width-mouseOldX,mouseOldY),size,mouseColor));
                    }

                    //sets old point equal to the current point
                    mouseOldX = mouseX;
                    mouseOldY = mouseY;
                }
            }
        });


        addComponentListener(new ComponentAdapter() {

            //if component is resized adjust lines
            @Override
            public void componentResized(ComponentEvent e) {
                drawSectorLines(linesCounter);
            }
        });

    }

    //painting
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        //initialise graphics
        g2 = (Graphics2D) g;

        //setting background and mouse color
        setBackground(Color.BLACK);
        g2.setColor(mouseColor);

        //drawing points;
        draw(linesCounter,g2);


        //drawing sector lines
        drawSectorLines(linesCounter);
    }


    //drawing sector lines
    public void drawSectorLines(int numberOfLines) {

        //deletes previous lines
        lines.clear();

        //if number of lines is different than 0
        if(numberOfLines != 0) {

            Line2D line;

            //this allows to maintain circle in the middle
            if(this.getSize().height>this.getSize().width) {

                //if the panel is higher than wider then the sectors will be of the size of half the width
                line = new Line2D.Double(this.getSize().width/2, this.getSize().height / 2, this.getSize().width, this.getSize().height / 2);

            }
            else{

                //if the panel is wider than higher then the sectors will be of the size of half the height
                line = new Line2D.Double(this.getSize().width/2, this.getSize().height / 2, this.getSize().width/2, this.getSize().height);

            }

            //calculating the angle that the line must be rotated by
            double rotateBy = (double) 360 / numberOfLines;

            double n = 0;

            for (int i = 0; i < numberOfLines; i++) {

                //rotate the line
                AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(n), line.getX1(), line.getY1());
                //add the rotated line
                lines.add(at.createTransformedShape(line));
                n += rotateBy;
            }
        }

        //setting the lineColour
        Color lineColour = new Color(200,200,200);

        //paint the lines
        if(linesVisible && numberOfLines != 0) {

            //set the stroke
            g2.setStroke(new BasicStroke(1));

            //set the colour
            g2.setColor(lineColour);

            //draw lines
            for (Shape shape : lines) {
                g2.draw(shape);
            }
        }

        repaint();
        this.linesCounter = numberOfLines;
    }

    private void draw(int linesCounter, Graphics2D g2){

        //if no sector lines
        double rotateBy = 0;

        if(linesCounter != 0 ) {

            //calculates by how many degrees the drawing should be rotated
            rotateBy = (double) 360 / linesCounter;
        }

        if(linesCounter == 0){
            //if no lines then we need to print point anyway
            linesCounter = 1;
        }

        //firstly prints all the shapes from previous drawings
        for(Queue<ShapeToDraw> drawing: drawingsPerformed) {

            for(int i=0; i<linesCounter; i++) {

                //rotates by the rotateby angle
                g2.rotate(Math.toRadians(rotateBy), this.getSize().width / 2, this.getSize().height / 2);

                //prints all the shapes from one of the queues of shapes
                for (ShapeToDraw line : drawing) {
                    line.drawShape(g2);
                }
            }
        }

        //on top of the previously drawn shapes draw currentDrawing
        for(ShapeToDraw lineDrawingNow : currentDrawing){

            for(int i=0; i<linesCounter; i++) {

                //rotate accordingly
                g2.rotate(Math.toRadians(rotateBy), this.getSize().width / 2, this.getSize().height / 2);

                //draw the shape
                lineDrawingNow.drawShape(g2);
            }
        }

        repaint();
    }

    //undo method, pushes recent drawing to the drawings redone stack
    public void undo(){
        if(!drawingsPerformed.isEmpty()) {
            drawingsRedone.push(drawingsPerformed.pop());
        }
    }

    //redo pushes the drawing from redone stack to drawings performed
    public void redo(){
        if(!drawingsRedone.isEmpty()){
            drawingsPerformed.push(drawingsRedone.pop());
        }
    }

    //erase method
    public void erase(){
        erase = !erase;

        if(erase) {
            previousColor = mouseColor;
            this.mouseColor = Color.BLACK;
        }
        else{
            if(mouseColor.equals(Color.BLACK))
            this.mouseColor = previousColor;
        }
    }

    //creating new image by clearing all memory
    public void newImage(){
        this.drawingsPerformed.clear();
        this.drawingsRedone.clear();
        this.currentDrawing.clear();
    }

    //take the screenshot of drawing panel
    public BufferedImage drawingPanelScreenShot() {
        BufferedImage screenshot = new BufferedImage((int)getSize().getWidth(), (int)getSize().getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        paint(screenshot.getGraphics());
        return screenshot;
    }

    //GETTERS AND SETTERS

    //check if in erase mode
    public boolean eraseMode(){
        return erase;
    }

    //check if reflecting
    public boolean getReflectBoolean(){
        return this.reflect;
    }

    //set reflect boolean
    public void setReflectBoolean(boolean reflect){
        this.reflect = reflect;
        //repaint();
    }

    //check if lines visible
    public boolean getLinesVisibleBoolean(){
        return this.linesVisible;
    }

    //setting linesVisible
    public void setLinesVisibleBoolean(boolean linesVisible){
        this.linesVisible = linesVisible;
        repaint();
    }

    //getting PenSize
    public int getPenSize(){
        return size;
    }

    //setting PenSize
    public void setPenSize(int size){
        this.size = size;
        repaint();
    }

    //setting mouse color
    public void setMouseColor(Color mouseColor){
        this.mouseColor = mouseColor;
    }

    //getting mouse color
    public Color getMouseColor(){
        return this.mouseColor;
    }

}