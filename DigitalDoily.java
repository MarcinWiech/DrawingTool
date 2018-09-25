
import javax.swing.*;

public class DigitalDoily extends JFrame {

    //constructor
    public DigitalDoily(){
        super("Doily");
        this.init();

    }

    //initialize the DigitalDoily
    public void init(){

        //add Java look and feel
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            System.err.println("Setting look and feel did not work. Default UIManager will be used.");
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Display display = new Display();
        this.setContentPane(display);
        this.setSize(1500,900);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
}
