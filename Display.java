
import java.awt.*;
import javax.swing.*;

public class Display extends JPanel {

    public Display(){

        //setting layout
        this.setLayout(new BorderLayout());

        //adding drawing panel to the centre
        DrawingPanel drawArea = new DrawingPanel();
        this.add(drawArea, BorderLayout.CENTER);

        //initialising gallery
        Gallery gallery = new Gallery();

        //adding gallery as a JScorllPane
        JScrollPane galleryScroll = gallery.scrollPane;
        this.add(galleryScroll,BorderLayout.EAST);

        //adding navigation bar
        NavigationBar topBar = new NavigationBar(drawArea, gallery);
        this.add(topBar, BorderLayout.NORTH);
    }
}