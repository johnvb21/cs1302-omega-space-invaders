package cs1302.omega;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.InputEvent;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import java.util.LinkedList;
import javafx.geometry.Bounds;
import java.util.Random;
import javafx.scene.layout.StackPane;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A simple version of space invaders.
 */

public class OmegaApp extends Application {
    Pane root = new Pane();
    Rectangle r;
    int laserStatus = 0;
    Rectangle barrier;
    Rectangle barrier1;
    Text scoreDisplay;
    Text controls = new Text(100, 300, "USE A, D to move\nSPACE to shoot");
    int level = 0;
    int count = 0;
    int score = 0;
    int counter = 0;
    int counterRight = 0;
    int levelTimer = 0;
    int barrierCount = 0;
    int barrier1Count = 0;
    int timeCount = 0;
    Stage stage;
    Pane gameOverPane;
    Text gameOver = new Text(250, 400, "GAME OVER: PLAY AGAIN?");

    Button playAgain = new Button("Play Again");

    Image playerIcon =  new Image("file:resources/sprites/space-invaders.png");

    Image iEnemy1 = new Image("file:resources/sprites/space-ship.png");

    LinkedList<Ship> enemySprites;
    Pane over;
    Pane starterRoot;
    Text gameMenu;
    Button playGame;
    Scene starter;
    Scene end;
    Scene scene;

    private Ship player;

    /**
       Cleans the scene and prepares for a rerun.

     */

    public void cleanup() {
        level = 0;
        count = 0;
        score = 0;
        counter = 0;
        counterRight = 0;
        levelTimer = 0;
        barrierCount = 0;
        barrier1Count = 0;
        timeCount = 0;
        stage.setScene(null);
        root.getChildren().clear();

    } // cleanup

    /**
       Calls cleanup and restarts the game.
       @param stage
    */

    public void restart(Stage stage) {
        cleanup();
        startGame(stage);
    } // restart

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */


    public OmegaApp() {


    }


    /**
       Passes the primary stage into the method and starts it.
       @param primaryStage

     */



    public void start(Stage primaryStage) {
        startGame(primaryStage);

    } // start

    /**
       Initializes the end screen.
    */

    public void newEnd() {
        playAgain.setTranslateX(400);
        playAgain.setTranslateY(500);
        playAgain.setPrefWidth(200);
        playAgain.setPrefHeight(100);
        over.setPrefSize(1000, 1000);
        over.getChildren().addAll(gameOverPane, gameOver, playAgain);
        stage.setTitle("SPACE INVADERS");
    } // newEnd

/**
       Initializes and prepares the game.
       @param stage
*/

    public void startGame(Stage stage) {
        // scene graph is built below
        starterRoot = new Pane();
        gameOverPane = new Pane();
        enemySprites = new LinkedList<Ship>();
        over = new Pane();
        end = new Scene(over, 1000, 1000);
        player = new Ship(500, 600, 50, 50, playerIcon);
        barrier = new Rectangle(200, 50, Color.GRAY);
        barrier1 = new Rectangle(200, 50, Color.GRAY);
        scoreDisplay = new Text(100, 200, "Level 1\nScore: 0");
        this.stage = stage;
        // newEnd sets up the end screen
        newEnd();
        root = new Pane();
        root.setPrefSize(1000, 1000);
        scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setMaxHeight(700);
        stage.setMaxWidth(1000);
        stage.setHeight(1000);
        gameOverPane.setStyle("-fx-background-color: black;");
        gameOver.setFont(new Font(35));
        gameOver.setFill(Color.PURPLE);
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(player, controls, barrier1, barrier);
        newLevel();
        root.getChildren().add(scoreDisplay);
        controls.setFont(new Font(20));
        controls.setFill(Color.PURPLE);
        scoreDisplay.setFont(new Font(25));
        scoreDisplay.setFill(Color.PURPLE);
        timer.start();
        barrier.setX(700);
        barrier.setY(450);
        barrier1.setX(200);
        barrier1.setY(450);
        // below defines the key event handler
        scene.setOnKeyPressed(p -> {
            switch (p.getCode()) {
            case SPACE:
                laserStatus = 1;
                if (count > 10 || count == 0) {
                    count = 0;
                    fire(player, player.laserBeam);
                } // if
                break;
            case A:
                player.moveLeft();
                break;
            case D:
                player.moveRight();
                break;
            } // switch
        });
        stage.show();
        playAgain.setOnAction(restartGame);
    } // start

    EventHandler<ActionEvent> restartGame = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                restart(stage);
            }
        };

    AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                update();

            } // handle
        }; // timer

/**
   Assists with checking game conditions.
 */

    public void checkConditions() {
        // checks if barrier is dead

        if (barrierCount == 100) {
            root.getChildren().remove(barrier);
        } // if
        if (barrier1Count == 100) {
            root.getChildren().remove(barrier1);
        } // if
        if (player.dead == true || level == 5) {
            root.getChildren().clear();
            timer.stop();
            stage.setScene(end);
            stage.setWidth(1000);
            stage.setHeight(1000);
        } // if
        // if all enemys are dead, next level starts
        if (enemySprites.size() == 0) {
            level += 1;
            newLevel();
        } // if
 // if lasers are on, the laser position will be updated per frame
        if (laserStatus == 1) {
            player.laserBeam.setY(player.laserBeam.getY() - 55);
            count += 2;
        } // if
        // checks if laser hit player
        for (int h = 0; h < enemySprites.size(); h++) {
            try {
                enemySprites.get(h).laserBeam.setY(enemySprites.get(h).laserBeam.getY() + 20);
                if (enemySprites.get(h).laserBeam.getBoundsInParent().
                    intersects(player.getBoundsInParent())) {
                    player.dead = true;
                } // if
            } catch (Exception e) {
                e.getMessage();
                continue;
            } // catch
        } // for

    } // checkConditions


/**
   Updates the scene every frame within the animation timer.

 */

    private void update() {
        checkConditions();
        for (int i = 0; i < enemySprites.size(); i++) {
            // checks enemy intersection with barrier
            if (enemySprites.get(i).laserBeam.getBoundsInParent().
                intersects(barrier.getBoundsInParent())) {
                if (barrierCount < 100) {
                    enemySprites.get(i).laserBeam.setX(1000);
                } // if
                barrierCount += 1;
                root.getChildren().remove(enemySprites.get(i).laserBeam);
            } // if
            // checks enemy intersection with barrier1
            if (enemySprites.get(i).laserBeam.getBoundsInParent().
                intersects(barrier1.getBoundsInParent())) {
                barrier1Count += 1;
                if (barrier1Count < 100) {
                    enemySprites.get(i).laserBeam.setX(1000);
                } // if
                root.getChildren().remove(enemySprites.get(i).laserBeam);
            } // if
            if (counter < 200) {
                enemySprites.get(i).setX(enemySprites.get(i).getX() - 2);
                counter += 1;
                if (counter == 200) {
                    counterRight = 0;
                } // if
            } // if
            if (counterRight < 200 && counter == 200) {
                enemySprites.get(i).setX(enemySprites.get(i).getX() + 2);
                counterRight += 1;
                if (counterRight == 200) {
                    counter = 0;
                } // if
            } // if
            // timeCount is responsible for timing the rate of enemy fire
            if (timeCount < 600) {
                timeCount += 1;
            } else {
                timeCount = 0;
            } // else
            if (timeCount == 0) {
                enemyFire();
            } // if
            scoreDisplay.setText("Level " + level + "\nScore: " + score);
            if (laserStatus == 1) {
                if (player.laserBeam.getBoundsInParent().
                    intersects(enemySprites.get(i).getBoundsInParent())) {
                    root.getChildren().remove(player.laserBeam);
                    enemySprites.get(i).dead = true;
                    player.laserBeam.setX(1000);
                    laserStatus = 0;
                    score += 2;
                    root.getChildren().remove(enemySprites.get(i));
                    root.getChildren().remove(enemySprites.get(i).laserBeam);
                    enemySprites.remove(i);
                } // if
            } // if
        } // for
    } // update

    /**
       Responsible for generating enemy lasers.
     */

    public void enemyFire() {
        for (int i = 0; i < enemySprites.size(); i++) {
            // a random number is chosen to decide laser fire
            Random rand = new Random();
            int upperbound = 200;
            int random = rand.nextInt(upperbound);
            if (random % 2 == 0) {
                fire(enemySprites.get(i), enemySprites.get(i).laserBeam);
            } // if

        } // for
    } // enemy Fire

    /**
       Loads up new level sprites.
     */

    public void newLevel() {
        // enemy rows spawn specific distance below each other
        int y = 100;
        for (int u = 0; u < level; u++) {
            int x = 800;
            for (int i = 0; i < 8; i++) {
                Ship s = new Ship(x, y, 50, 50, iEnemy1);
                enemySprites.add(s);
                root.getChildren().add(s);
                x -= 70;
            } // for
            y += 70;
        } // for


    } // newLevel

    /**
       Here is the class that represents sprite (Ship) objects.
     */

    public static class Ship extends ImageView {
        boolean dead = false;
        public Rectangle laserBeam = new Rectangle(5, 20, Color.RED);

/**
   Constructs a ship object.
   @param x
   @param y
   @param w
   @param h
   @param icon
*/

        public Ship(int x, int y, int w, int h, Image icon) {
            this.setImage(icon);
            this.setPreserveRatio(true);
            setX(x);
            setY(y);
            setFitWidth(w);
            setFitHeight(h);

        } // Ship


        /**
           Moves the ship right.

         */

        void moveRight() {
            setX(getX() + 20);

        } // moveRight

        /**
           Moves the ship left.
         */

        void moveLeft() {
            setX(getX() - 20);

        } // moveLeft

        /**
           Moves the ship up.
         */

        void moveUp() {
            setY(getY() - 12);
        } // moveUp

/**
   Moves the ship down.
*/

        void moveDown() {
            setY(getY() + 12);
        } // move down
    } // Ship

    /**
       Initializes lasers for ship objects.
       @param s
       @param r
     */

    public void fire(Ship s, Rectangle r) {
        // laserStatus indicates if lasers are turned on for player

        try {
            root.getChildren().remove(r);
        } catch (Exception e) {
            e.getMessage();
        } // catch
        r.setX(s.getX() + 23);
        r.setY(s.getY() - 10);
        root.getChildren().add(r);
    } // fire



} // OmegaApp
