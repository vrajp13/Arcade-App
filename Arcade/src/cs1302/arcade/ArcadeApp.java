package cs1302.arcade;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Creates a stage which has a title and has a list of two games. It also contains the team name
 * and the group members name. It also contains few animation transition on all the Nodes in
 * that stage.
 * 
 * @author Vraj Patel and Vrundh Parikh
 *
 */
public class ArcadeApp extends Application {

	 /**
     * Creates a GUI of the opening stage which contains the title and list of all the available
     * games. Also has the team name and members names.
     * 
     * @param stage  stage of the GUI application
     */
    @Override
    public void start(Stage stage) {
    	BorderPane borderPane = new BorderPane();
    	VBox vbox = new VBox();
    	
    	//Creating all the necessary information about the opening stage
    	Text arcade = new Text("CS1302-Arcade!");
    	Button sudoku = new Button("Sudoku");
    	Button snake = new Button("Snake");
    	
    	VBox infoVbox = new VBox();
    	Text teamName = new Text("Team Name: Ice Breakers");
    	Text firstAuthor = new Text("Vraj Patel");
    	Text secondAuthor = new Text("Vrund Parikh");
    	infoVbox.setSpacing(10);
    	//Adding everything to a VBox
    	infoVbox.getChildren().addAll(teamName, firstAuthor, secondAuthor);
    	
    	//Setting different style to the background and text
    	borderPane.setStyle("-fx-background-color: "
    			+ "linear-gradient(from 0% 0% to 100% 100%, cadetblue 0%, firebrick 100%);");
    	arcade.setStyle("-fx-font: 45 arial;");	
    	arcade.setFill(Color.WHITE);
    	sudoku.setStyle("-fx-font: 20 arial;");
    	snake.setStyle("-fx-font: 20 arial;");
    	
    	teamName.setStyle("-fx-font: 20 arial;");
    	teamName.setFill(Color.WHITE);
    	firstAuthor.setStyle("-fx-font: 20 arial;");
    	firstAuthor.setFill(Color.WHITE);
    	secondAuthor.setStyle("-fx-font: 20 arial;");
    	secondAuthor.setFill(Color.WHITE);
    	
    	//Adding rotation transition to the title
    	RotateTransition rotation = new RotateTransition(Duration.seconds(2), arcade);
    	rotation.setFromAngle(180);
    	rotation.setToAngle(360);
    	rotation.play();
    	
    	//Adding Translate transition to the title
    	TranslateTransition translate = new TranslateTransition(Duration.seconds(2), arcade);
    	translate.setFromX(-150);
    	translate.setToX(0);
    	translate.play();
    	
    	//Adding Translate transition to the Sudoku button
    	TranslateTransition sudokuTranslate = new TranslateTransition(Duration.seconds(2), sudoku);
    	sudokuTranslate.setFromX(-200);
    	sudokuTranslate.setToX(0);
    	sudokuTranslate.play();
    	
    	//Adding Translate transition to the Snake button
    	TranslateTransition snakeTranslate = new TranslateTransition(Duration.seconds(2), snake);
    	snakeTranslate.setFromX(200);
    	snakeTranslate.setToX(0);
    	snakeTranslate.play();
    	
    	//Adding Fade transition to the BorderPane
    	FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), borderPane);
    	fadeIn.setFromValue(0.1);
    	fadeIn.setToValue(1.0);
    	fadeIn.play();
    	
    	//Adding Translate transition to the Team Name
    	TranslateTransition team = new TranslateTransition(Duration.seconds(2), teamName);
    	team.setFromX(200);
    	team.setToX(400);
    	team.play();
    	
    	//Adding Translate transition to the Author
    	TranslateTransition author = new TranslateTransition(Duration.seconds(2), firstAuthor);
    	author.setFromX(200);
    	author.setToX(400);
    	author.play();
    	
    	//Adding Translate transition to the Author
    	TranslateTransition author2 = new TranslateTransition(Duration.seconds(2), secondAuthor);
    	author2.setFromX(200);
    	author2.setToX(400);
    	author2.play();
    	
    	//Adding everything into the VBox
    	Text space = new Text("");
    	vbox.getChildren().addAll(arcade, sudoku, snake, space, infoVbox);
    	
    	//Event Handling the Sudoku and Snake button by creating an object of that class
    	//and calling their start method on that object
    	sudoku.setOnAction(event -> {
    		Sudoku game = new Sudoku();
    		game.startSudoku();
    	});
    	snake.setOnAction(event -> {
    		Snake game = new Snake();
    		game.startSnake();
    	});
    	
    	vbox.setAlignment(Pos.CENTER);
    	vbox.setSpacing(25);
    	borderPane.setCenter(vbox);
    	
    	//Setting the scene to the BorderPane
        Scene scene = new Scene(borderPane, 640, 440);
        stage.setTitle("cs1302-arcade!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
	
    } // start

    /**
     * Launches the GUI Application. Also prints out error message when the X server 
     * connection likely timed out
     * 
     * @param args Array of String arguments
     */
    public static void main(String[] args) {
		try {
		    Application.launch(args);
		} catch (UnsupportedOperationException e) {
		    System.out.println(e);
		    System.err.println("If this is a DISPLAY problem, then your X server connection");
		    System.err.println("has likely timed out. This can generally be fixed by logging");
		    System.err.println("out and logging back in.");
		    System.exit(1);
		} // try
    } // main

} // ArcadeApp