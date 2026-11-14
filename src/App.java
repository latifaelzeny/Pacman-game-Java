import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        
        // define some variables, the size of the window
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;
        
        //createing the window, the "Pac Man" is a string that surves as the title of the window
        JFrame frame = new JFrame ("Pac Man");

        //frame.setVisible(true); //making the window visable, but i commentted it out as we need to set window visable after all the componets are in the window
        frame.setSize(boardWidth, boardHeight); //setting window size
        frame.setLocationRelativeTo(null); //to make the window appear at the center of the screen
        frame.setResizable(false); //so the user cannot expand the width n height of the window

// Load icon from your project's resources folder
        ImageIcon icon = new ImageIcon(App.class.getResource("./assets/icon.png"));
        frame.setIconImage(icon.getImage());
        frame.setSize(10, 10);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminates when the player clicks at the x in the window

        //create an instance of the JPanel
        PacMan pacmanGame = new PacMan();
        //add this pannel into the window
        frame.add(pacmanGame);
        frame.pack(); //to insure we get the full size of the JPanel within thw window
        pacmanGame.requestFocus();
        frame.setVisible(true); //set window to visable after all componenets are added
    }
}