import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener { // make it inharit Jpanel, basically making it a version of Jpanel but you can add more properties to the game
    //to store the info for the walls, ghosts n packman, creat a class to store all these objects
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        //save the x and y position as as the game goes on the posito=ions will keep changing
        int startX;
        int startY;

        char direction = 'U'; // U D L R, in other words, up, down, left or right
        int velocityX = 0;
        int velocityY = 0;

//create the constructor
Block(Image image, int x, int y, int width, int height) {
    this.image = image;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.startX = x;
    this.startY = y;
    }

    void updateDirection(char direction) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall :walls) {
            if (collision(this, wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity();
            }
        }
    }

    void updateVelocity() {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize/4;
        }
        else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = +tileSize/4;
        }
        else if (this.direction == 'L') {
            this.velocityX = -tileSize/4;
            this.velocityY = 0;
        }
        else if (this.direction == 'R') {
            this.velocityX = +tileSize/4;
            this.velocityY = 0;
        }
    }
    // for when pacman colides with te ghosts, it goes back to where it should be, n it
    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}
    
    // we copied these from the App.java file so our Jpanel will have the same size as our window, here we set it to private tho
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    //loading the images for the game
    //create member variable to store the images
    private Image wallImage;
    private Image firstGhostImage;
    private Image secondGhostImage;
    private Image thirdGhostImage;
    private Image forthGhostImage;

    private Image PacmanUpImage;
    private Image PacmanDownImage;
    private Image PacmanLeftImage;
    private Image PacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: 1 = 1st, 2 = 2nd, 3 = 3rd, 4 = 4rth
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XX4XX X XXXX",
        "O       123       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    //create a hashset
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    int score = 0;
    int lives = 3;
    boolean gameOver = false;
    boolean YouWin = false;

    //create a constructor
    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.WHITE);

        //to make our key presses work
        addKeyListener(this);
        //make sure theg pannel is listening to the key presses
        setFocusable(true);

        //load the images
        wallImage = new ImageIcon(getClass().getResource("./assets/wall image.png")).getImage();
        
        firstGhostImage = new ImageIcon(getClass().getResource("./assets/1st ghost.png")).getImage();
        secondGhostImage = new ImageIcon(getClass().getResource("./assets/2nd ghost.png")).getImage();
        thirdGhostImage = new ImageIcon(getClass().getResource("./assets/3rd ghost.png")).getImage();
        forthGhostImage = new ImageIcon(getClass().getResource("./assets/4th ghost.png")).getImage();
        
        PacmanUpImage = new ImageIcon(getClass().getResource("./assets/pacman up.png")).getImage();
        PacmanRightImage = new ImageIcon(getClass().getResource("./assets/pacman right.png")).getImage();
        PacmanLeftImage = new ImageIcon(getClass().getResource("./assets/pacman left.png")).getImage();
        PacmanDownImage = new ImageIcon(getClass().getResource("./assets/pacman down.png")).getImage();

        loadMap();
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameLoop = new Timer(50, this); //every 50 milla second we are gonna repaint, in a second there are 1000 milli seconds, so (1000/50) we will have 20 frames per second
        gameLoop.start(); //start the loop
        //testing to see how if they save or not
        //System.out.println(walls.size());
        //System.out.println(foods.size());
        //System.out.println(ghosts.size());
    }

    //create the objects for the walls, food, ghosts n packman
    public void loadMap(){
        //initialize the hashsets
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        //itirate through the map
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++){
                String row = tileMap[r]; //get the current row
                char tileMapChar = row.charAt(c); //get the current character

                int x = c*tileSize; //to get the x position -the example was 4 x 32-
                int y = r*tileSize; //to get the y position

                if (tileMapChar == 'X') { //we have a wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == '1') {//first ghost
                    Block ghost = new Block(firstGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == '2') {//second ghost
                    Block ghost = new Block(secondGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == '3') {//third ghost
                    Block ghost = new Block(thirdGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == '4') {//forth ghost
                    Block ghost = new Block(forthGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') {//pacman
                    pacman = new Block(PacmanRightImage, x, y, tileSize, tileSize);

                }
                else if (tileMapChar == ' '){//food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    //draw all of them in the game
    //to keep redrawing them as they move they keep changing
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        //created hot pink
        Color hotpink = Color.decode("#ff019f");

        g.setColor(Color.BLACK);
        for (Block food : foods) {
            g.fillOval(food.x, food.y, food.width, food.height);
        }

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        
        
        g.setColor(hotpink); //called hotpink

        //score font
        g.setFont(new Font("Ariel", Font.BOLD, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2); 
        }
        else if (YouWin) { 
            g.drawString("You Won!: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }


    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //so pacman teleports through the open edge
        if (pacman.x < 0) {
        pacman.x = boardWidth;
        }
        else if (pacman.x > boardWidth){
        pacman.x = 0;
        }

        //check wall colisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        for (Block ghost : ghosts) {
            //check to see if ghost has colided in pacman
            if (collision(ghost, pacman)) {
                lives -= 1;
                //make the game stop when we run out of lives
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D'){
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall :walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    //for it to go in a different direction after coliding w the wall
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
            //to make the ghosts teleport through the whole thingys
            if (ghost.x < 0) {
            ghost.x = boardWidth;
            }
            else if (ghost.x > boardWidth){
            ghost.x = 0;
            }
        }

        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove((foodEaten));

        if (foods.isEmpty()) {
            YouWin = true;
            gameLoop.stop();
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void  resetPositions () {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }


    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        //for me to be able to restart the game by pressing any key
        if (gameOver) {
            loadMap();
            resetPositions();
            lives =3;
            score =0;
            gameOver = false;
            gameLoop.start();
        }
        if (YouWin) {
            loadMap();
            resetPositions();
            lives =3;
            score =0;
            gameOver = false;
            YouWin = false;
            gameLoop.start();
        }

        //System.out.println("KeyEvent; " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = PacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = PacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = PacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = PacmanRightImage;
        }
    }
}