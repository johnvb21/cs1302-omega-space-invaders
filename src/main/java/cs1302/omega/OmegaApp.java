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
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {
    Pane root = new Pane();
    Rectangle r;
    int laserStatus = 0;
    Rectangle barrier = new Rectangle(200, 50, Color.GRAY);
    Rectangle barrier1 = new Rectangle(200, 50, Color.GRAY);
    Text scoreDisplay = new Text(100, 200, "Score: 0");
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
    Pane game_over = new Pane();
    Text gameOver = new Text(250, 400, "GAME OVER: PLAY AGAIN?");
    Button playAgain = new Button("Play Again");
    Image player_icon = new Image("file:resources/sprites/space-invaders.png");
    Image i_enemy1 = new Image("file:resources/sprites/space-ship.png");
    LinkedList<Ship> enemySprites = new LinkedList<Ship>();

    private Ship player = new Ship(500, 600, 50, 50, player_icon);


    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */


    public OmegaApp() {
//        root.setPrefSize(1000, 1000);




    }


    /** {@inheritDoc} */
    @Override


    public void start(Stage primaryStage) {
        startGame(primaryStage);

    } // start

    public void startGame(Stage stage) {
        this.stage = stage;
        root = new Pane();
        root.setPrefSize(1000, 1000);
        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(1000);
        game_over.setStyle("-fx-background-color: black;");
        gameOver.setFont(new Font(35));
        gameOver.setFill(Color.PURPLE);
        root.setStyle("-fx-background-color: black;");
        root.getChildren().add(player);
        newLevel();
        root.getChildren().add(scoreDisplay);
        scoreDisplay.setFont(new Font(25));
        scoreDisplay.setFill(Color.PURPLE);
        timer.start();
        root.getChildren().add(barrier);
        barrier.setX(700);
        barrier.setY(450);
        root.getChildren().add(barrier1);
        barrier1.setX(200);
        barrier1.setY(450);

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
        playAgain.setOnAction(restartGame);
        stage.show();
    } // start
        EventHandler<ActionEvent> restartGame = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                    {
                        stage.close();

                    }
            };
//    playAgain.setOnAction(restartGame);


    AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                update();

            } // handle
        }; // timer






    private void update() {

        if (barrierCount == 100) {
            root.getChildren().remove(barrier);
        } // if
        if (barrier1Count == 100) {
            root.getChildren().remove(barrier1);
        } // if
        if (player.dead == true) {
            root.getChildren().clear();
            timer.stop();
            playAgain.setTranslateX(400);
            playAgain.setTranslateY(500);
            playAgain.setPrefWidth(200);
            playAgain.setPrefHeight(100);
            Pane over = new Pane();
            over.setPrefSize(1000, 1000);
            over.getChildren().addAll(game_over, gameOver, playAgain);
            Scene end = new Scene(over, 1000, 1000);
            stage.setScene(end);
            stage.setWidth(1000);
            stage.setHeight(1000);
        } // if
        if (enemySprites.size() == 0) {
            level += 1;

            newLevel();
            } // if


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
                    scoreDisplay.setText("Score: "+ score);
                } // if

            } // if
        } // for


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

// if lasers are on, the laser position will be updated per frame
        if (laserStatus == 1) {
            player.laserBeam.setY(player.laserBeam.getY() - 55);
            count += 2;
        } // if
    } // update

    public void enemyFire() {
        for (int i = 0; i < enemySprites.size(); i++) {
            Random rand = new Random();
            int upperbound = 200;
            int random = rand.nextInt(upperbound);
            if (random % 2 == 0) {
                fire(enemySprites.get(i), enemySprites.get(i).laserBeam);
            } // if

        } // for
    } // enemy Fire

    public void newLevel() {
//        int x = 800;

        int y = 100;
        for (int u = 0; u < level; u++) {
            int x = 800;
            for (int i = 0; i < 8; i++) {
                Ship s = new Ship(x, y, 50, 50, i_enemy1);
                enemySprites.add(s);
                root.getChildren().add(s);
                x -= 70;
            } // for
            y += 70;
        } // for


    } // newLevel

    public static class Ship extends ImageView {
        boolean dead = false;
        public Rectangle laserBeam = new Rectangle(5, 20, Color.RED);

        public Ship(int x, int y, int w, int h, Image icon) {
            this.setImage(icon);
            this.setPreserveRatio(true);
            setX(x);
            setY(y);
            setFitWidth(w);
            setFitHeight(h);

        } // Ship
        void moveRight() {
            setX(getX() + 20);

        } // moveRight
        void moveLeft() {
            setX(getX() - 20);

        } // moveLeft
        void moveUp() {
            setY(getY() - 12);
        } // moveUp

        void moveDown() {
            setY(getY() + 12);
        } // move down
    } // Ship

    public void fire(Ship s, Rectangle r) {
        // laserStatus indicates if lasers are turned on for player

        try {
            root.getChildren().remove(r);
        } catch (Exception e) {
        } // catch
        r.setX(s.getX() + 23);
        r.setY(s.getY() - 10);
        root.getChildren().add(r);
    } // fire



} // OmegaApp
