
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class NavigationBar extends JPanel {

    //declaring gallery and drawingPanel that NavigationBar will interact with
    private Gallery gallery;
    private DrawingPanel drawingPanel;

    //declare draw mode to true by default
    boolean drawMode = true;

    //constructor
    public NavigationBar(DrawingPanel drawingPanel, Gallery gallery) {

        this.gallery = gallery;
        this.drawingPanel = drawingPanel;
        this.setLayout(new FlowLayout());

        //adding buttons to the navigation Bar

        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> drawingPanel.undo());


        JButton redoBtn = new JButton("Redo");
        redoBtn.addActionListener(e -> drawingPanel.redo());


        JButton erasePenBtn = new JButton("Erase Mode");
        //erasePenBtn.addActionListener(e -> drawingPanel.erase());
        erasePenBtn.addActionListener(e -> {
            if(drawMode){
                drawMode=!drawMode;
                ((JButton) e.getSource()).setText("Draw Mode");
            }
            else{
                drawMode=!drawMode;
                ((JButton) e.getSource()).setText("Erase Mode");
            }
            drawingPanel.erase();
        });


        JLabel penSizeLabel = new JLabel("Pen size");

        //adding slider to choose pen size
        JSlider sliderPenSize = new JSlider(JSlider.HORIZONTAL, 1, 20, 1);

        //setting PenSize according to the slider value
        sliderPenSize.addChangeListener(e -> drawingPanel.setPenSize(sliderPenSize.getValue()));
        sliderPenSize.setMajorTickSpacing(5);
        sliderPenSize.setMinorTickSpacing(1);
        sliderPenSize.setPaintTicks(true);
        sliderPenSize.setPaintLabels(true);

        //adding choose colours from dialog window
        JButton choosecolours = new JButton("Colours");
        choosecolours.addActionListener(e -> {

            if (!drawingPanel.eraseMode()) {
                JColorChooser jc = new JColorChooser();
                Color cl = jc.showDialog(drawingPanel, "Select Color", drawingPanel.getMouseColor());
                drawingPanel.setMouseColor(cl);
            }
        });

        JLabel linesNumLabel = new JLabel("Number of Sectors");

        //adding slider to choose pen size
        JSlider sliderLinesNum = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);

        //setting number of sectors according to the slider value
        sliderLinesNum.addChangeListener(e -> drawingPanel.drawSectorLines(sliderLinesNum.getValue()));
        sliderLinesNum.setMajorTickSpacing(20);
        sliderLinesNum.setMinorTickSpacing(10);
        sliderLinesNum.setPaintTicks(true);
        sliderLinesNum.setPaintLabels(true);

        //adding a screenshot of DrawingPanel to the gallery
        JButton addToGalleryBtn = new JButton("Add image to the Gallery");

        addToGalleryBtn.addActionListener((ActionEvent e) -> {

            BufferedImage temp = drawingPanel.drawingPanelScreenShot();
            gallery.addImage(temp);
            gallery.repaint();
        });

        JButton deleteSelectedImages = new JButton("Delete selected images");
        deleteSelectedImages.addActionListener(e -> gallery.deleteSelectedImages());

        JButton reflect = new JButton("Reflect");
        reflect.addActionListener(e -> drawingPanel.setReflectBoolean(!drawingPanel.getReflectBoolean()));

        JButton showLines = new JButton("Lines Visibility");
        showLines.addActionListener(e -> drawingPanel.setLinesVisibleBoolean(!drawingPanel.getLinesVisibleBoolean()));

        JButton newImage = new JButton("New image");
        newImage.addActionListener(e -> drawingPanel.newImage());

        //adding all the buttons to the NavigationBar
        this.add(undoBtn);
        this.add(redoBtn);
        this.add(erasePenBtn);
        this.add(penSizeLabel);
        this.add(sliderPenSize);
        this.add(choosecolours);
        this.add(linesNumLabel);
        this.add(sliderLinesNum);
        this.add(reflect);
        this.add(showLines);
        this.add(addToGalleryBtn);
        this.add(deleteSelectedImages);
        this.add(newImage);
    }
}