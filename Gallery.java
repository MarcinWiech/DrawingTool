
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Gallery extends JPanel {

    //arrayList storing all images
    private ArrayList<GalleryImage> drawings;

    //ScrollPane with the images
    JScrollPane scrollPane;

    //gallery constructor
    public Gallery() {

        this.setPreferredSize(new Dimension(400, 3550));

        //initialise member variables
        drawings = new ArrayList<GalleryImage>();
        scrollPane = new JScrollPane(this);

        //set horizontal scroll to never
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //set vertical scroll to always
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //increase scrolling unit so that it is more comfortable with the mouse
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setVisible(true);
    }


    //deletes selected images
    public void deleteSelectedImages() {

        //iterates over all drawings in reverse order
        for(int i=(drawings.size()-1); i >=0; i--){

            //if drawing is selected remove drawing
            if(drawings.get(i).isSelected() == true){

                this.remove(drawings.get(i));
                drawings.remove(i);
            }
        }

        //refreshes the panel
        revalidate();
        repaint();
    }

    //adds image to drawings (array of images)
    public void addImage(BufferedImage toDrawings) {

        if(drawings.size()<12) {

            //resize the passed buffered image using one of the JLabels sizes
            BufferedImage imageToAdd = resize(toDrawings);

            //setting the image to JLabel
            GalleryImage label = new GalleryImage();
            label.setIcon(new ImageIcon(imageToAdd));

            drawings.add(label);

            //update the gallery panel
            for(JLabel drawing: drawings){
                this.add(drawing);
            }

            //refresh
            revalidate();
        }

        //if number of images is 12, show the dialog windows and do not add the image to the drawings
        else {
            JOptionPane.showMessageDialog(null, "Only up to 12 drawings can be stored");
        }
    }

    //resize the BufferedImage (DrawingPanel screenshot)
    private static BufferedImage resize(BufferedImage image) {

        //scale the buffered image to the given dimensions
        Image tempImage = image.getScaledInstance(390,275,Image.SCALE_SMOOTH);

        BufferedImage resizedBI = new BufferedImage(390, 275, BufferedImage.TYPE_INT_ARGB);

        //get graphics and draw image on it
        Graphics2D g2d = resizedBI.createGraphics();
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();

        return resizedBI;
    }
}