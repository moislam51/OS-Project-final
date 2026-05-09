// this file is useless, the project runs from MainGUI.java
import views.MainGUI;

public class OS_Project {

   public static void main(String args[]) {

    java.awt.EventQueue.invokeLater(() -> {
        MainGUI mainScreen = new MainGUI();
        mainScreen.setLocationRelativeTo(null); 
        mainScreen.setVisible(true);
    });
}
}
