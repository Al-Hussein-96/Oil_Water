package oil_water;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Start {

    static Canvas canvas = new Canvas();

    public static void main(String[] args) {
        face f = new face();
        OilData OD=new OilData();
   //     f.setBounds(600, 0, f.getWidth(), f.getHeight());
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(-10, 0, 1600, 600);
   //     frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(45, 102, 102));
        canvas.setPreferredSize(new Dimension(600, 600));
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

        frame.getContentPane().add(canvas);
  //      f.getContentPane().setBounds(600, 0, f.getWidth(), f.getHeight()-100);
        frame.getContentPane().add(f.getContentPane());
     //   frame.getContentPane().add(OD.getContentPane());
        canvas.setBounds(0, 0, 600, 600);

        frame.setVisible(true);

        Oil_Water oilwater = new Oil_Water();
        try {
            oilwater.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
