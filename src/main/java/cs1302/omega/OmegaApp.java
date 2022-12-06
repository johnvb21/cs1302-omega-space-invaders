package cs1302.omega;

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

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {
    private Pane root = new Pane();
    Rectangle r;
    int laserStatus = 0;
    Image game_end = new Image("file:resources/sprites/661.jpg", 1000, 1000, false, false);
    ImageView game_over = new ImageView(game_end);


    Image player_icon = new Image("file:resources/sprites/space-invaders.png");
    Image i_enemy1 = new Image("file:resources/sprites/space-ship.png");
    LinkedList<Ship> enemySprites = new LinkedList<Ship>();

    private Ship player = new Ship(500, 600, 50, 50, player_icon);


    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */


    public OmegaApp() {
        root.setPrefSize(1000, 1000);
        root.getChildren().add(player);



    }

    int count = 0;
    /** {@inheritDoc} */
    @Override


    public void start(Stage stage) {
        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(1000);
        root.setStyle("-fx-background-color: black;");
        newLevel();
        timer.start();
        scene.setOnKeyPressed(p -> {
            switch (p.getCode()) {
            case SPACE:
                if (count > 60 || count == 0) {
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

    } // start
    int counter = 0;
    AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                update();

            } // handle
        }; // timer
    int timeCount = 0;
    private void update() {
        if (player.dead == true) {
            timer.stop();
            root.getChildren().clear();
            root.getChildren().add(game_over);
        } // if
        for (int i = 0; i < enemySprites.size(); i++) {
            if (counter < 400) {
                enemySprites.get(i).setX(enemySprites.get(i).getX() - 5);
                counter += 1;
            } else if (counter >= 400) {
                enemySprites.get(i).setX(enemySprites.get(i).getX() + 5);
                counter += 1;
                if (counter > 800) {
                    counter = 0;
                } // if

            } // else if
            if (timeCount < 400) {
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
                    enemySprites.get(i).dead = true;
                    root.getChildren().remove(enemySprites.get(i));
                    root.getChildren().remove(enemySprites.get(i).laserBeam);
                    enemySprites.remove(i);

                } // if
            } // if
        } // for


        for (int h = 0; h < enemySprites.size(); h++) {
            try {
                enemySprites.get(h).laserBeam.setY(enemySprites.get(h).laserBeam.getY() + 30);
                if (enemySprites.get(h).laserBeam.getBoundsInParent().
                intersects(player.getBoundsInParent())) {
                    player.dead = true;
                } // if
            } catch (Exception e) {
                e.getMessage();
                continue;
            } // catch
        } // for


        if (laserStatus == 1) {
            player.laserBeam.setY(player.laserBeam.getY() - 30);
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
        int x = 800;

        for (int i = 0; i < 8; i++) {
            Ship s = new Ship(x, 100, 50, 50, i_enemy1);
            enemySprites.add(s);
            root.getChildren().add(s);
            x -= 70;
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
            setX(getX() + 12);

        } // moveRight
        void moveLeft() {
            setX(getX() - 12);

        } // moveLeft
        void moveUp() {
            setY(getY() - 12);
        } // moveUp

        void moveDown() {
            setY(getY() + 12);
        } // move down
    } // Ship

    public void fire(Ship s, Rectangle r) {
        laserStatus = 1;
        try {
            root.getChildren().remove(r);
        } catch (Exception e) {
        } // catch
        r.setX(s.getX() + 23);
        r.setY(s.getY() - 10);
        root.getChildren().add(r);
    } // fire



} // OmegaApp
