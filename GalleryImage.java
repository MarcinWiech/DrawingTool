
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class GalleryImage extends JLabel {

    //if selected
    private boolean selected = false;

    //set this border when image selected
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 4);

    //constructor
    public GalleryImage(){

        //if mouse pressed change border
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selected = !selected;
                if(selected) {
                    setBorder(redBorder);
                }
                else{
                    setBorder(null);
                }
            }
        });
    }

    //check if selected
    public boolean isSelected(){
        return selected;
    }
}