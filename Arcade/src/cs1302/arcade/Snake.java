package cs1302.arcade;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Creates a snake game for the user to play where the user can move the snake using
 * UP, DOWN, LEFT, RIGHT keys. The snake has three difficulty levels which are easy, medium
 * and hard. By default the game starts in easy mode, and changing the difficulty while the 
 * snake is moving will restart the game. Changing to higher difficulty of the snake will
 * make the snake move faster. If the snake touches itself or the border of the
 * screen, the snake will start with size one at its original location. As the snake eats the
 * food, the size of the snake increases by one and snakes level also increases by one. Also
 * the game calculates the score based on the size of the snake. Before the game begins 
 * a information window will be provided to the user to inform them about how the game works.
 * 
 * @author Vraj Patel and Vrundh Parikh
 *
 */
public class Snake {
	
	//Enum used for Snake's Direction
	public enum Direction{
		UP,DOWN,LEFT,RIGHT
	}
	
	//Instance Variables
	private Stage stage = new Stage();
	private BorderPane borderPane = new BorderPane();
	private Group snakeBody = new Group();
	private Rectangle food = new Rectangle();
	
	private Timeline timeline = new Timeline();
	private Random random = new Random();
	
	private Direction direction = Direction.RIGHT;
	private ObservableList<Node> snake;
	
	private MenuBar menuBar = new MenuBar();
	private ToolBar toolBar = new ToolBar();
	
	private Text levelNumText = new Text("1");
	private Text scoreNumText = new Text("0");
	private int levelCounter = 1;
	private int scoreCounter = 0;
	private Text difficultyText = new Text("Easy");
	
	
	/**
	 * Starting method that will create a stage and scene containing a BorderPane which
	 * contains the MenuBar, ToolBar, Snake's Body and the food. It uses all the other
	 * methods to create a snake game and make it work properly.
	 */
	public void startSnake() {
		//Creating MenuBar and ToolBar at top of BorderPane by calling other methods
		VBox topPane = new VBox();
		createMenuBar();
		createToolBar();
    	topPane.getChildren().addAll(menuBar, toolBar);
    	borderPane.setTop(topPane);
    	
		//Creating a list of snake rectangles and creating a food
		snake = snakeBody.getChildren();
		food = createFood();
		
		//Adding the head to the snake
		Rectangle head = new Rectangle(20,20);
		head.setFill(Color.GREEN);
		head.setTranslateX(0);
		head.setTranslateY(80);
		snake.add(head);
		
		//Creating a TimeLine
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), 
				event -> createSnake(food));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(keyFrame);
		//Get the direction of Snake
		snakeDirection();
		
		//Background and Stage Modifications
		borderPane.setStyle("-fx-background-color: #000000;");
		borderPane.getChildren().addAll(food, snakeBody);
		Scene scene = new Scene(borderPane, 900, 700);  
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Snake");
		stage.setScene(scene);
		stage.show();
		
		//Game information to the user
		infoDialog();
		//Request input focus to receive key events
		snakeBody.requestFocus();
	}
		
	/**
	 * Gets the last rectangle of the snake which is the tail and moves it to the front
	 * as the head of the snake. It checks if the snakes goes out of the bounds, detects 
	 * collision with itself and checks if the snake ate the food by calling other methods.
	 * 
	 * @param food Rectangle which is snake's food
	 */
	private void createSnake(Rectangle food) {
		Node snakeTail;
		boolean snakeSize = snake.size() > 1;
		
		//Determining the tail of the Snake
		if(snakeSize) {
			snakeTail = snake.remove(snake.size() - 1);
		}
		else {
			snakeTail = snake.get(0);
		}
		
		//Storing the location of the tail before moving the tail to the front as the head.
		double xCoordinate = snakeTail.getTranslateX();
		double yCoordinate = snakeTail.getTranslateY();
		
		//Move the tail in front of the head
		moveSnake(snakeTail);
		
		//Adding the tail to the head of the snake
		if(snakeSize) {
			snake.add(0, snakeTail);	
		}
		//Checks out of bounds, collision and if the snake ate the food
		snakeOutOfBounds(snakeTail);
		detectCollision(snakeTail);
		checkSnakeEatFood(snakeTail, xCoordinate, yCoordinate);	
	}
	
	/**
	 * Moves the tail from the snake in front of the snake based on the direction
	 * of the snake
	 * 
	 * @param snakeTail tail of the Snake
	 */
	private void moveSnake(Node snakeTail) {
		//Moving the tail of the snake to its correct location by using the 
		//location of the current head and the direction of the snake
		if(direction == Direction.UP) {
			snakeTail.setTranslateX(snake.get(0).getTranslateX());
			snakeTail.setTranslateY(snake.get(0).getTranslateY() - 20);
		}
		else if(direction == Direction.DOWN) {
			snakeTail.setTranslateX(snake.get(0).getTranslateX());
			snakeTail.setTranslateY(snake.get(0).getTranslateY() + 20);
		}
		else if(direction == Direction.LEFT) {
			snakeTail.setTranslateX(snake.get(0).getTranslateX() - 20);
			snakeTail.setTranslateY(snake.get(0).getTranslateY());
		}
		else {
			snakeTail.setTranslateX(snake.get(0).getTranslateX() + 20);
			snakeTail.setTranslateY(snake.get(0).getTranslateY());
		}
	}
	
	/**
	 * Determining the direction of the snake based on the keyboard events.
	 */
	private void snakeDirection() {
		snakeBody.setOnKeyPressed(event -> {
			timeline.play();
			if (event.getCode() == KeyCode.UP)  {
				direction = Direction.UP;
			}
			else if (event.getCode() == KeyCode.DOWN) {
				direction = Direction.DOWN;
			}
			else if (event.getCode() == KeyCode.LEFT) {
				direction = Direction.LEFT;
			}
			else if (event.getCode() == KeyCode.RIGHT) {
				direction = Direction.RIGHT;
			} 
		});	
	}
	
	/**
	 * Gives the game information to the user before the game starts.
	 */
	private void infoDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Snake Information");
		alert.setHeaderText(null);
		alert.setContentText("Use UP, DOWN, LEFT, RIGHT keybord buttons to move the snake in that"
				+ " direction. Snake restarts itself at the original location, if the snake touches"
				+ " any of the border and also if it touchs itself. Changing the difficulty of"
				+ " the snake, while a game is running makes the snake restart at its original"
				+ " location. After any restart, the user has to press movement keyboard buttons"
				+ " to make the snake move again.");
		alert.setHeight(500);
		alert.setWidth(0);
		System.out.println(alert.getHeight());
		System.out.println(alert.getWidth());
		
		alert.showAndWait();
	}
	
	/**
	 * Creates a Rectangle food at random location for the snake to eat
	 * 
	 * @return Rectangle food
	 */
	private Rectangle createFood() {
		//Creating the food rectangle
		Rectangle food = new Rectangle(20,20);
		food.setFill(Color.RED);
		
		//Setting random location to the food rectangle which is inside the scene.
		food.setTranslateX(Math.floor(random.nextInt(44)) * 20);
		food.setTranslateY(Math.floor(random.nextInt(31)) * 20 + 80);
		System.out.println(food.getTranslateX());
		System.out.println(food.getTranslateY());
		
		return food;
	}
	
	/**
	 * Checks if the snake touches any edges of the screen. If it does touch than it 
	 * shows a Game Over message and restarts the game.
	 * 
	 * @param snakeTail head of the snake
	 */
	private void snakeOutOfBounds(Node snakeTail) {
		//Checks if the head touches the bounds
		if(snakeTail.getTranslateX() < 0 || snakeTail.getTranslateX() >= 900 ||
				snakeTail.getTranslateY() < 80 || snakeTail.getTranslateY() >= 700) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game Over!");
			alert.setHeaderText("Game Over!");
			alert.setContentText("Snake Out of bounds. Close to Restart");
			alert.show();
			restartGame();
		}
	}
	
	/**
	 * Checks if the snake touches itself. If it does than it shows a Game Over message and
	 * restarts the game.
	 * 
	 * @param snakeTail head of the snake
	 */
	private void detectCollision(Node snakeTail) {
		try {
			//Goes through each rectangles in the snake and checks if it equals to 
			//the head of the snake
			for(Node rect : snake) {
				if(snakeTail.getTranslateX() == rect.getTranslateX() && snakeTail.getTranslateY() ==
						rect.getTranslateY() && rect != snakeTail) {
					restartGame();
				}
			}
		} catch (Exception e) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game Over!");
			alert.setHeaderText("Game Over!");
			alert.setContentText("Snake touched itself. Close to Restart");
			alert.show();
		}
	}
	
	/**
	 * Adds food to a random location after the snake eats the current food and also
	 * increases the size of snake by adding a rectangle at the end of snake. Also
	 * updates the level of snake and score of the snake.
	 * 
	 * @param snakeTail  head of the snake
	 * @param xCoordinate  x coordinate for the tail
	 * @param yCoordinate  y coordinate for the tail
	 */
	private void checkSnakeEatFood(Node snakeTail, Double xCoordinate, Double yCoordinate) {
		if(snakeTail.getTranslateX() == food.getTranslateX() && snakeTail.getTranslateY() 
				== food.getTranslateY()) {
			
			food.setTranslateX(Math.floor(random.nextInt(44)) * 20);
			food.setTranslateY(Math.floor(random.nextInt(31)) * 20 + 80);
			
			//Increasing the level of snake by one every time it eats the food
			levelCounter++;
			levelNumText.setText("" + levelCounter);
			
			//Increasing the score by adding the size of the snake to the previous score
			scoreCounter = scoreCounter + snake.size();
			scoreNumText.setText("" + scoreCounter);
			
			//Creating and adding a rectangle to end of the snake
			Rectangle rect = new Rectangle(20,20);
			rect.setFill(Color.GREEN);
			rect.setTranslateX(xCoordinate);
			rect.setTranslateY(yCoordinate);
			
			snake.add(rect);
		}
	}
	
	/**
	 * Restarts the game by stopping the TimeLine and clearing the snake list. Also resetting
	 * the level, score and the direction of snake. And making the game start with snake of size 1 
	 * and changing the location of the food to a random location.
	 */
	private void restartGame() {
		timeline.stop();
		snake.clear();
		
		food.setTranslateX(Math.floor(random.nextInt(44)) * 20);
		food.setTranslateY(Math.floor(random.nextInt(31)) * 20 + 80);
		
		//Level and Score tracking
		levelNumText.setText("1");
		scoreNumText.setText("0");
		levelCounter = 1;
		scoreCounter = 0;
		
		direction = Direction.RIGHT;
		Rectangle head = new Rectangle(20,20);
		head.setFill(Color.GREEN);
		head.setTranslateY(80);
		snake.add(head);
	}
	
	/**
	 * Creating the MenuBar for the game which has options for the game. It contains
	 * file, difficulty and restart options.
	 */
	private void createMenuBar() {
		//File Menu which contains a Exit menu item
    	final Menu fileMenu = new Menu("File");
    	MenuItem exitMenu = new MenuItem("Exit");
    	fileMenu.getItems().add(exitMenu);
    	exitMenu.setOnAction(event -> {
    		stage.close();
    	});
    	
    	//Difficulty Menu which contains 3 difficulties for the game
    	final Menu difficultyMenu = new Menu("Difficulty");
    	MenuItem easy = new MenuItem("Easy");
    	MenuItem medium = new MenuItem("Medium");
    	MenuItem hard = new MenuItem("Hard");
    	difficultyMenu.getItems().addAll(easy, medium, hard);
    	//Calling another method to handle the event for each difficulty
    	eventHandlingDifficulty(easy,medium,hard);
    	
    	//Restart Menu which contains an option to restart the game
    	final Menu restartMenu = new Menu("Restart");
    	MenuItem restartGame = new MenuItem("Restart Game");
    	restartMenu.getItems().add(restartGame);
    	restartGame.setOnAction(event -> {
    		restartGame();
    	});
    	
    	//Adding everything to the MenuBar
    	menuBar.setPadding(new Insets(5, 0, 0, 0));
    	menuBar.getMenus().addAll(fileMenu, difficultyMenu, restartMenu);
    	menuBar.setPrefHeight(40);
	}
	
	/**
	 * Creating a ToolBar for the game which displays the current level of the snake,
	 * the score and the current difficulty of the game.
	 */
	private void createToolBar() {
		Label level = new Label("Level :");
		Label score = new Label("Score :");
		Label space = new Label("  ");
		Label secondSpace = new Label("  ");
		Label difficulty = new Label("Difficulty:");
		
		toolBar.setPrefHeight(40);
		//Adding everything to the ToolBar
		toolBar.getItems().addAll(level, levelNumText, space, score, scoreNumText, secondSpace,
				difficulty, difficultyText);
		
	}
	
	/**
	 * Event handling for the difficulty MenuItem and changing the speed of the snake 
	 * based on the current difficulty of the game.
	 * 
	 * @param easy easy difficulty of the snake
	 * @param medium medium difficulty of the snake
	 * @param hard hard difficulty of the snake
	 */
	private void eventHandlingDifficulty(MenuItem easy, MenuItem medium, MenuItem hard) {
		//Event Handling for Easy
		easy.setOnAction(event -> {
    		difficultyText.setText("Easy");
    		restartGame();
    		timeline.setRate(timeline.getCycleDuration().toSeconds() / 0.3);
    	});
		//Event Handling for Medium
    	medium.setOnAction(event -> {
    		difficultyText.setText("Medium");
    		restartGame();
    		timeline.setRate(timeline.getCycleDuration().toSeconds() / 0.2);
    	});
    	//Event Handling for Hard
    	hard.setOnAction(event -> {
    		difficultyText.setText("Hard");
    		restartGame();
    		timeline.setRate(timeline.getCycleDuration().toSeconds() / 0.1);
    	});
	}
}