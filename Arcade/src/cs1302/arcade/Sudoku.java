package cs1302.arcade;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * This is the class for the sudoku game that will allow the users to play a game of sudoku
 * where the user will be provided with a sudoku board, numbers to enter, three difficulty
 * level, a timer, checking button, and restart. The numbers will allow the user to enter
 * the number they think will solve the puzzle and while making sure that no more than one
 * digit is added into the board. Once the sudoku is solved then the user has the option to
 * check the game and if they successfully solve the game then a congrats message will pop
 * or a message telling them to try again. As the difficulty level increases than more 
 * sudoku boxes will be left blank, thus making the game harder. Before the game begins 
 * a information window will be provided to the user to inform them about how the game works.
 * 
 * @author Vraj Patel and Vrund Parikh
 *
 */
public class Sudoku {
	
	//These are private instane variables that will be used through the game code
	private int gridSize = 9;
	private int seconds = 0;
	private int minutes = 0;
	private int hours = 0;
	private Random randNum = new Random();
	private int levelSpaces = 7;
	private ToolBar toolbar = new ToolBar();
	private MenuBar fileBar = new MenuBar();
	private Timer timer;
	private TimerTask task;
	private String time = "";
	private String number = "";
	private Stage stage = new Stage();
	private BorderPane borderPane = new BorderPane();
	private GridPane gridPane = new GridPane();
	private GridPane numGrid = new GridPane();
	private Text timeText = new Text();
	private TextField[][] textFieldCells= new TextField[gridSize][gridSize];
	/**
	 * @see https://sco.wikipedia.org/wiki/Sudoku#/media/File:Sudoku_Puzzle_by_L2G-20050714_solution_standardized_layout.svg
	 */
	private int[][] sudokuSolution = new int[][] {
							{5,3,4,6,7,8,9,1,2},
							{6,7,2,1,9,5,3,4,8},
							{1,9,8,3,4,2,5,6,7},
							{8,5,9,7,6,1,4,2,3},
							{4,2,6,8,5,3,7,9,1},
							{7,1,3,9,2,4,8,5,6},
							{9,6,1,5,3,7,2,8,4},
							{2,8,7,4,1,9,6,3,5},
							{3,4,5,2,8,6,1,7,9}	
	};
	private Button one = new Button("1");
	private Button two = new Button("2");
	private Button three = new Button("3");
	private Button four = new Button("4");
	private Button five = new Button("5");
	private Button six = new Button("6");
	private Button seven = new Button("7");
	private Button eight = new Button("8");
	private Button nine = new Button("9");
	private Button[] numButtons = new Button[] {
			one, two, three, four, five, six, seven, eight, nine
	};
	private Button easy = new Button("[Easy]");
	private Button medium = new Button("Medium");
	private Button hard = new Button("Hard");
	private Button restart = new Button("Restart");
	private Button check = new Button("Check");
	private HBox menu = new HBox();
	
	/**
	 * This is the starting method that will create a stage, scene containing a border
	 * pane which stores the whole game including the buttons and the board. In this 
	 * method also all the necessary methods will be called in order to allow the board
	 * and game to function properly while providing a good user interaction.
	 */
	public void startSudoku() {
		//Here methods are being called to where grid will be made, and the buttons
		//actions will be set up
		addingToNumGrid();
		addingToBoard();
		difficultyLevel();
		restartAndCheck();
		fileMenuBar();
		information();
		//A random number is created to for a new variation to be created when game begins
		int randRotation = randNum.nextInt(4);
		int randSwaps = randNum.nextInt(3);
		findingVariations(randRotation, randSwaps);		
		creatingGame();
		toolbar();
		VBox topSetting = new VBox();
		topSetting.getChildren().addAll(menu, toolbar);
		//Here all the things are being added to the border pane for a complete game
		borderPane.setRight(numGrid);
		borderPane.setCenter(gridPane);
		borderPane.setTop(topSetting);
		borderPane.setStyle("-fx-background-color:black");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Sudoku");
		//Scene is created and added to the stage so that it can begin
		Scene scene = new Scene(borderPane, 500, 400);  
		stage.setScene(scene);
		stage.setMaxHeight(400);
		stage.setMaxWidth(600);
		stage.showAndWait();
	}
	
	/**
	 * This private method creates the timer that will be used in order to let the user
	 * knows how long they take to solve the puzzle. This method a timer class and timer
	 * task will be used to increment the seconds, minutes, and hours.
	 * 
	 */
	private void timer() {
		//A new instance of the time and timerTask is created to allow the time clock to
		//function properly
		timer = new Timer();
		task = new TimerTask() {
			//This method will make sure that the timer runs indefinately until the game
			//is checked or restarted
			public void run() {
				//This if/else statement makes sure that after seconds is equal to 59 then
				//minutes will increase and seconds will be set back to zero
				if(seconds < 59) {
					seconds++;
					//This nested if/else statement makes sure that if seconds and mins are
					//less than 10 then a zero will be pressed in front of it or there is no 0
					if(seconds < 10) {
						if(minutes < 10)
							time = "0" + Integer.toString(hours)+ " : 0" + 
							Integer.toString(minutes) + " : " + "0" + Integer.toString(seconds);
						else
							time = "0" + Integer.toString(hours) + " : " +
						    Integer.toString(minutes) + " : " + "0" + Integer.toString(seconds);}
					else 
						time = "0" + Integer.toString(hours)+ " : 0" + 
						Integer.toString(minutes) + " : " + Integer.toString(seconds);
					timeText.setText(time);}
				else {
					seconds = 0;
					minutes++;
					if(minutes < 10)
						time = "0" + Integer.toString(hours)+ " : 0" + 
						Integer.toString(minutes) + " : " +	"0" + Integer.toString(seconds);
					else
						time = "0" + Integer.toString(hours) + " : " + Integer.toString(minutes)
						+ " : " + "0" + Integer.toString(seconds);
					//This if statement makes sure that if mins reach to 59 then it will inc 
					//the hours and set the minutes and seconds back to zero
					if(minutes > 59
							) {
						minutes = 0;
						seconds = 0;
						hours++;
						time = "0" + Integer.toString(hours) + " : 0" + Integer.toString(minutes)
						+ " : " + "0" + Integer.toString(seconds);
					}
					timeText.setText(time);}}};
		//A schedule is set up where timer will repeat every 1 second while increasing the time
		timer.schedule(task, 1000, 1000);
	}
	
	/**
	 * This private method creates a toolbar for the sudoku application, so that the necessary
	 * buttons can be added to it so that user can interact with. The toolbar includes restart,
	 * check, difficulty levels buttons and a timer allowing the user to interact with the app
	 */
	private void toolbar() {
		//These few labels are created for so that it could added to the toolbar to let the
		//user know what buttons are there and other things
		Label timer = new Label("Time: ");
		Label difficulty = new Label("Difficulty: ");
		Label space = new Label(" ");
		Label space1 = new Label(" ");
		Label space2 = new Label(" ");
		Label space3 = new Label(" ");
		//Two separators are added to separate the buttons and the timer
		Separator separate = new Separator();
		separate.setOrientation(Orientation.VERTICAL);
		Separator separateOnce = new Separator();
		separateOnce.setOrientation(Orientation.VERTICAL);
		toolbar.getItems().addAll(restart, space, check, separate, difficulty, space1, easy,
				space2, medium, space3, hard, separateOnce, timer, timeText);
	}
	
	/**
	 * This private method adds the numbers from the numButtons array which contains the
	 * number buttons, and it adds it to the numGrid which will be used to allow the user
	 * add a a number to the sudoku board so that when the board is not empty it could be
	 * checked to make sure that the puzzle is solved. While the numButtons are added
	 * also a set on action will be added to them so that the yser can interact with it
	 * easily. 
	 */
	private void addingToNumGrid() {
		//A prefSize and padding is set so that numGrid pane looks proper and easy to
		//interact with
		numGrid.setPrefSize(50, 50);
		numGrid.setPadding(new Insets(80,80,0,0));
		numGrid.setHgap(5);
		numGrid.setVgap(5);
		int count = 0;
		//This loop will add buttons from array to the numGrid while setting a style 
		//and setting an action on each of those buttons
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				numButtons[count].setStyle("-fx-background-color: gold");
				numHandler(count);
				numGrid.add(numButtons[count], col, row);
				count++;
			}
		}
	}
	
	/**
	 * This private method will add the textField to the gridPane from a textFieldCells
	 * 2D array. The textField will allow the user to enter the numbers so that when it 
	 * is full then the 2D array will be used to pull the numbers and make sure that it
	 * is indeed sudoku. While the textFields are being added, also vertical and horizontal
	 * lines will be added to separate each 3 X 3 grid in the 9 X 9 grid.
	 */
	private void addingToBoard() {
		//These variables are used to initialize a new textField in the 2D array
		int row = -1;
		int col = 0;
		//This nested loops will add textFields to the gridPane while adding a line
		//between each 3 X 3 grid to make the users interact with the board easier
		for(int i = 0; i <= gridSize+1; i++) {
			//At these rows in the gridPane a horizontal line will be added
			if(i==3 || i==7) {
				Line line = new Line(0,0,10,0);
				line.setStrokeWidth(2);
				gridPane.add(line, 0, i);
				continue;
			}
			//Else the row variable will increase
			else
				row++;
			for(int j = 0; j <= gridSize+1; j++) {
				//At these cols in the gridPane a vertical line will be added
				if(j==3 || j==7) {
					Line line = new Line();
					line.setStrokeWidth(1);
					gridPane.add(line, j, i);
				}
				//Else each textField in the 2D array will be initialized and its editable
				//will be taken away. Also a set on mouse click will be added to each textField
				//so a number could be added to it
				else {
					textFieldCells[row][col] = new TextField();
					textFieldCells[row][col].setPrefSize(30, 40);
					textFieldCells[row][col].setEditable(false);
					textFieldCells[row][col].setAlignment(Pos.CENTER);
					int finalRow = row;
					int finalCol = col;
					textFieldCells[row][col].setOnMouseClicked(event -> {
						textFieldCells[finalRow][finalCol].setText(number);
					});
					gridPane.add(textFieldCells[row][col], j, i);
					col++;
				}
			}
			col = 0;
		}
	}
	
	/**
	 * This private method will set restart and check button an action that each will
	 * follow in order to accomplish its respective function. So the restart set
	 * on action will set each textFieldCells to blank so new variations could be
	 * added to it and also the timer will start again. The check set on action
	 * will print a message to the user depending on whether the sudoku is solved
	 * or not. 
	 */
	private void restartAndCheck() {
		//A background color is added to the restart and check buttons
		check.setStyle("-fx-background-color:lightCoral");
		restart.setStyle("-fx-background-color:yellowgreen");
		//A set on action is added to the restart method where the textField
		//will be empty so that a new variation of the board could be added 
		restart.setOnAction(event -> {
			//This loop will empty out the textField
			for(int i = 0; i < gridSize; i++) {
				for(int j = 0; j < gridSize; j++) {
					textFieldCells[i][j].setText("");
					textFieldCells[i][j].setStyle(null);
					textFieldCells[i][j].setDisable(false);
					textFieldCells[i][j].setEditable(true);
				}
			}
			//A new variation will be added by using the respective method
			int randRotation = randNum.nextInt(4);
			int randSwaps = randNum.nextInt(3);
			findingVariations(randRotation, randSwaps);		
			creatingGame();
			timer.cancel();
			//The timer will also start again
			timer();
			seconds = 0;
			minutes = 0;
		});
		//A set on action is added to the check method where isSudoku method will
		//be used to check whether it is sudoku and a proper message will pop which
		//will be related to whether it is sudoku or not
		check.setOnAction(event -> {
			if(isSudoku() == true) {
				creatingAlert("Congrats, you solved the sudoku");
				timer.cancel();
			}
			else
				creatingAlert("Sorry, the sudoku is wrong try again");
				
		});
	}
	
	/**
	 * This private method is using a lambda expression to create a handler for the numbers
	 * button and by creating a lambda expression a button action can be added easily without
	 * writing a lot of code. 
	 * 
	 * @param count the index value in the numButtons array
	 */
	private void numHandler(int count) {
		//A lambda expression of the EventHandler interface is used to create an event handler
		//that will be used for the set on action of the buttons
		EventHandler<ActionEvent> handler = (event -> {
			number = numButtons[count].getText();
			System.out.println(number);
		});
		numButtons[count].setOnAction(handler);
	}
	
	/**
	 * This private method is used to set on action for the difficulty levels where the user 
	 * will know when they have changed the difficulty level and also when the difficulty
	 * level changes than the number of blank textField shown to the user will change so
	 * that the difficulty can increase for the sudoku game
	 */
	private void difficultyLevel() {
		//A background color is added to the difficulty level buttons 
		easy.setStyle("-fx-background-color:lime");
		medium.setStyle("-fx-background-color:lime");
		hard.setStyle("-fx-background-color:lime");
		//The setOnAction is added to each difficulty level where it is common the only thing
		//that is different is the number blank textField shown to the user
		easy.setOnAction(event -> {
			levelSpaces = 7;
			easy.setText("[Easy]");
			medium.setText("Medium");
			hard.setText("Hard");
		});
		medium.setOnAction(event -> {
			levelSpaces = 4;	
			easy.setText("Easy");
			medium.setText("[Medium]");
			hard.setText("Hard");
		});
		hard.setOnAction(event -> {
			levelSpaces = 3;
			easy.setText("Easy");
			medium.setText("Medium");
			hard.setText("[Hard]");
		});
	}
	
	/**
     * This private method creates the message that will have a message for check button
     * so that when the user wins than a congrats method will pop or when its not sudoku
     * then a sorry message will pop up
     */
	private void creatingAlert(String expression) {
		//By using the alert object, a pop window will open with a message 
		//for letting the user know what is up with the sudoku
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText(expression);
		alert.showAndWait();	
	}
	
	/**
	 * This private method creates instructions for the game so that before the game begins
	 * all the information will be given to the user to notify of them how the game works
	 * and how each button will work
	 */
	private void information() {
		//By using the alert object, a pop window will open with the information regarding
		//the instructions for how to play the game and how they could interact with the app
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sudoku Game Information");
		alert.setHeaderText(null);
		alert.setContentText("1). The timer starts immediately as the game begins." + "\n"
		+ "2). The game begins with an easy level and if the user wishes to change the level"
		+ " then they just have to select the preferred level and click restart" + "\n" 
		+ "3). In order to check if the user has solved the sudoku then simply just click on"
		+ " check button." + "\n" + "4). In order to add the preferred number in the board then "
				+ "simply just click on the number to the right and go to the board where the"
				+ " number is supposed to be added and the number will show up." + "\n" + 
				"5). To exit there are two options either go to file and click on exit or "
				+ "just press on the cross button to the top right, like the traditional way");
		alert.showAndWait();
		//Also when the cancel the information message timer will begin with application
		timer();
	}

	/**
	 * This private method just simply randomly rotates, and swaps rows and cols of the 
	 * sudoku solution in order to create a new variation for the game
	 * @param randRotation random integer for the rotation loop
	 * @param randSwaps random integer for the swap loop
	 */
	private void findingVariations(int randRotation, int randSwaps) {
		//If rotation number and swaps is the original then it will be rotated once
		if(randRotation == 3 && randSwaps == 2) {
			rotate();
		}
		else {
			//This loop will randomly rotate the solution for a variation
			for(int repeat = 0; repeat <= randRotation; repeat++) {
				rotate();
			}
			//This loop will randomly swap the rows and cols the solution for a variation
			for(int repeat = 0; repeat <= randSwaps; repeat++) {
				swapRows();
				swapCols();
				swapInnerRows();
				swapInnerCols();
			}
		}
	}
	
	/**
	 * This private method will create the game board by keeping the number of textFields
	 * blank according to the level they are in. Also the board will randomly empty out 
	 * the textFields in each of the 3 X 3 grid for a random sudoku game
	 */
	private void creatingGame() {
		//This nested loop will produced a game board with each 3 X 3 grid having a certain
		//number of blank textFields which is related to the level they are in
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				//These nested if/else if statements will use a private method in order
				//to create a sudoku board game with certain textFields blank
				if(col == 0) {
					//This if/else if statments will correlate to each row of the board
					if(row == 0)
						producingGame(levelSpaces, 0, 0);
					else if(row == 1)
						producingGame(levelSpaces, 3, 0);
					else
						producingGame(levelSpaces, 6, 0);
				}
				else if(col == 1) {
					if(row == 0)
						producingGame(levelSpaces, 0, 3);
					else if(row == 1)
						producingGame(levelSpaces, 3, 3);
					else
						producingGame(levelSpaces, 6, 3);
				}
				else{
					if(row == 0)
						producingGame(levelSpaces, 0, 6);
					else if(row == 1)
						producingGame(levelSpaces, 3, 6);
					else
						producingGame(levelSpaces, 6, 6);
				}
			}
		}
	}
	
	/**
	 * This private method will swap inner rows of 0-2, 3-5 and 6-8 row sections and this
	 * will make sure that a new variation of the sudoku solution is created because 
	 * the integrity of the game will still be intact because solution will still be 
	 * correct. By swaping inner rows of the respective row sections a new variation is
	 * created
	 */
	private void swapInnerRows() {
		//A temp int 2D array is created so that it can store the new variation to the 
		//solution to the sudoku game
		int[][] tempSolution = new int[gridSize][gridSize];
		//This nested loop will swap each inner row within its respective row sections
		//for a new variation
		for(int row = 0; row <= gridSize -3; row+=3) {
			int tempSolutionRow = 0;
			for(int innerRow = row; innerRow < row+3; innerRow++) {
				//This if/else statment will make sure that inner rows are swapped with
				//each other in the correct manner
				if(innerRow == row)
					tempSolutionRow = row + 1;
				else if(innerRow == row+1)
					tempSolutionRow = row + 2;
				else
					tempSolutionRow = row;
				//This loop will add all the numbers from sudokuSolution to the tempSolution
				//array with a new swapped rows
				for(int col = 0; col < gridSize; col++) {
					tempSolution[innerRow][col] = sudokuSolution[tempSolutionRow][col]; 
				}
			}
		}
		sudokuSolution = tempSolution;
	}
	
	/**
	 * This private method will swap inner cols of 0-2, 3-5 and 6-8 col sections and this
	 * will make sure that a new variation of the sudoku solution is created because 
	 * the integrity of the game will still be intact because solution will still be 
	 * correct. By swaping inner cols of the respective row sections a new variation is
	 * created
	 */
	private void swapInnerCols() {
		//A temp int 2D array is created so that it can store the new variation to the 
		//solution to the sudoku game
		int[][] tempSolution = new int[gridSize][gridSize];
		//This nested loop will swap each inner col within its respective col sections
		//for a new variation
		for(int col = 0; col <= gridSize -3; col+=3) {
			int tempSolutionCol = 0;
			for(int innerCol = col; innerCol < col+3; innerCol++) {
				//This if/else statment will make sure that inner cols are swapped with
				//each other in the correct manner
				if(innerCol == col)
					tempSolutionCol = col + 1;
				else if(innerCol == col+1)
					tempSolutionCol = col + 2;
				else
					tempSolutionCol = col;
				//This loop will add all the numbers from sudokuSolution to the tempSolution
				//array with a new swapped cols
				for(int row = 0; row < gridSize; row++) {
					tempSolution[row][innerCol] = sudokuSolution[row][tempSolutionCol]; 
				}
			}
		}
		sudokuSolution = tempSolution;
	}
	
	/**
	 * This private method will swap each row section of 0-2, 3-5, and 6-8, with each other
	 * while still making sure that the solution is still a valid sudoku game according
	 * to the game rules. By swapping the row sections with each other it is creating a 
	 * new variation to the solution for the game
	 */
	private void swapRows() {
		//A temp int 2D array is created to store the swapped row solution
		int[][] tempSolution = new int[gridSize][gridSize];
		//This nested loop makes sure that each row section is swapped with each other
		//so that a new variation can be created. Ex row 0-2 will be swapped with 6-8 and
		//etc
		for(int row = 0; row < gridSize; row++) {
			int tempSolutionRow = 0;
			for(int col = 0; col < gridSize; col++) {
				//This if/else if statements assigns the correct value to the row to be
				//swapped with so that a new variation can be created
				if(row == 0)
					tempSolutionRow = 3;
				else if(row == 1)
					tempSolutionRow = 4;
				else if(row == 2)
					tempSolutionRow = 5;
				else if(row == 3)
					tempSolutionRow = 6;
				else if(row == 4)
					tempSolutionRow = 7;
				else if(row == 5)
					tempSolutionRow = 8;
				else if(row == 6)
					tempSolutionRow = 0;
				else if(row == 7)
					tempSolutionRow = 1;
				else if(row == 8)
					tempSolutionRow = 2;
				tempSolution[row][col] = sudokuSolution[tempSolutionRow][col];
			}
		}
		sudokuSolution = tempSolution;
	}
	
	/**
	 * This private method will swap each col section of 0-2, 3-5, and 6-8, with each other
	 * while still making sure that the solution is still a valid sudoku game according
	 * to the game rules. By swapping the col sections with each other it is creating a 
	 * new variation to the solution for the game
	 */
	private void swapCols() {
		//A temp int 2D array is created to store the swapped col solution
		int[][] tempSolution = new int[gridSize][gridSize];
		//This nested loop makes sure that each col section is swapped with each other
		//so that a new variation can be created. Ex col 0-2 will be swapped with 6-8 and
		//etc
		for(int col = 0; col < gridSize; col++) {
			int tempSolutionCol = 0;
			for(int row = 0; row < gridSize; row++) {
				//This if/else if statements assigns the correct value to the col to be
				//swapped with so that a new variation can be created
				if(col == 0)
					tempSolutionCol = 3;
				else if(col == 1)
					tempSolutionCol = 4;
				else if(col == 2)
					tempSolutionCol = 5;
				else if(col == 3)
					tempSolutionCol = 6;
				else if(col == 4)
					tempSolutionCol = 7;
				else if(col == 5)
					tempSolutionCol = 8;
				else if(col == 6)
					tempSolutionCol = 0;
				else if(col == 7)
					tempSolutionCol = 1;
				else if(col == 8)
					tempSolutionCol = 2;
				tempSolution[row][col] = sudokuSolution[row][tempSolutionCol];
			}
		}
		sudokuSolution = tempSolution;
	}
	
	/**
	 * This private method rotates the whole sudokuSolution and it rotates to the right
	 * while making sure that a new variation is created. With each rotation there is a
	 * new variation until it rotates 4 times which will be back to original solution
	 */
	private void rotate() {
		//A temp int 2D array is created to store the rotated sudokuSolution
		int[][] tempSolution = new int[gridSize][gridSize];
		int tempSolutionRow = -1;
		//This nested loop will add the once rotated to the right solution to the temp
		//array so that a new variation is created
		for(int col = gridSize - 1; col >= 0; col--) {
			tempSolutionRow++;
			for(int row = tempSolution.length -1; row>= 0; row--) {
				tempSolution[row][col] = sudokuSolution[tempSolutionRow][row];
			}
		}
		sudokuSolution = tempSolution;
	}	
	
	/**
	 * This private method produces the game by adding random numbers from the solution
	 * to the random row and col of each 3 X 3 grid with only adding numbers that is 
	 * specified by the difficulty level, and keeping the rest of the textFields blank.
	 * Also whereever the number from the new variation of solution is added to the
	 * textField it will be disabled so that the user can't change it again.
	 * @param randomInt the number of times the loop will iterate
	 * @param countRow specifying how a random value for row will be created
	 * @param countCol specifying how a random value for col will be created
	 */
	private void producingGame(int randomInt, int countRow, int countCol) {
		//This loop will iterate the number of randomInt times and also the loop will make
		//sure that a number from the solution is added at a random location in the 3 x 3 
		//grid
		for(int i = 0; i < randomInt; i++) {
			int randRow = randNum.nextInt(3) + countRow;
			int randCol = randNum.nextInt(3) + countCol;
			//This if/else statement makes sure that a number is added to the random location
			//in the 3 x 3 grid where a number is already not there. If it is not empty
			//the loop will iterate one extra time.
			if(i != 0) {
				//This if/else statement makes sure that where the number is added it is 
				//disabled and it can't be changed by the user.
				if((textFieldCells[randRow][randCol].getText()).equals("")) {
					String solution = Integer.toString(sudokuSolution[randRow][randCol]);
					textFieldCells[randRow][randCol].setText(solution);
					textFieldCells[randRow][randCol].setDisable(true);
					textFieldCells[randRow][randCol].setStyle("-fx-background-color:ivory");
					System.out.println(textFieldCells[randRow][randCol].getText());
				}
				else {
					i = i-1;
				}
			}
			else {
				//Same thing happens here the textField cell containing the number is disabled
				String solution = Integer.toString(sudokuSolution[randRow][randCol]);
				textFieldCells[randRow][randCol].setText(solution);
				textFieldCells[randRow][randCol].setDisable(true);
				textFieldCells[randRow][randCol].setStyle("-fx-background-color:ivory");
				System.out.println(textFieldCells[randRow][randCol].getText());
			}
		}
	}
	
	/**
	 * This private method will check if the numbers entered by the user into the board
	 * is indeed a sudoku or not. First this method will check if the board is empty or
	 * not and if it is empty then it will return false or true. Next it will check 
	 * each col and row to make sure that it is indeed a sudoku and if all these conditions
	 * are true then it will return true or else it is false
	 * @return true or false according to whether the conditions are met or not for sudoku
	 */
	public boolean isSudoku(){
		boolean answer = false;
		String[][] temporarySquare = new String[gridSize][gridSize];
		//This loop adds all the elements from the textFieldCells to the new temp string array
		for(int row = 0; row <temporarySquare.length; row++){
			for(int col = 0; col < temporarySquare.length; col++){
				temporarySquare[row][col] = textFieldCells[row][col].getText();
		}}
		//This if/else statement will check whether the game board is empty or not
		//and if it is empty then it will return false or it will return true
		if(isSudokuEmpty(temporarySquare) == false)
			return false;
		else{
			//This if/else if statement checks the col and row of the textFieldCells and
			//returns true or false depending on what those methods return
			if(checkingEachColumnOfSudoku(temporarySquare) == false)
				return false;
			else
				if(checkingEachRowOfSudoku(temporarySquare) == false)
					return false;
				else
					answer = true;
			}
		return answer;
		
	}
	
	/**
	 * This private method makes sure that the game board is not empty and it is
	 * empty then it will return false and if it is not empty then it will return 
	 * true. 
	 * @param temporarySquare the string array containing the user response
	 * @return true or false depending on whether the board is empty or not
	 */
	private boolean isSudokuEmpty(String[][] temporarySquare){
		boolean checking = true;
		//This nested loop will go through each col and row to check whether it
		//is empty or not
		for(int row = 0; row < gridSize; row++){
			for(int col = 0; col < gridSize; col++){
				if(temporarySquare[row][col].equals("")){
					checking = false;}
		}}
		return checking;
	}
	
	/**
	 * This private method checks each col of the board to make sure that each number
	 * is not repeated twice in that col. And this is also correlating to the game rules
	 * where the each number in the col of 3 x 3 grid and the col can't have repeated
	 * numbers
	 * @param square temporary board containing user responses
	 * @return true or false depending on if there are repeated numbers or not
	 */
	private boolean checkingEachColumnOfSudoku(String[][] square){
		String[][] columnSquare = square;
		String columnCharacter = "";
		boolean checking = false;
		//This nested loop will pick a number in each column and go through the whole
		//column to make sure that the specific number is not found, and it is found
		//then it will return false immediately
		for(int row = 0; row < columnSquare.length; row++){
			for(int col = 0; col < columnSquare.length; col++){
				columnCharacter = columnSquare[row][col];
				if(col != columnSquare.length - 1){
					//This loop is going through the whole column to check the number if it is
					//repeated or not
					for(int nextCharc = col+1; nextCharc < columnSquare.length; nextCharc++){
						if(!columnCharacter.equals(columnSquare[row][nextCharc])){
							checking = true;}
						else{
							return false;}
				}}
		}}
		return checking;
	}
	
	/**
	 * This private method checks each row of the board to make sure that each number
	 * is not repeated twice in that row. And this is also correlating to the game rules
	 * where the each number in the row of 3 x 3 grid and the row can't have repeated
	 * numbers
	 * @param square temporary board containing user responses
	 * @return true or false depending on if there are repeated numbers or not
	 */
	private boolean checkingEachRowOfSudoku(String[][] square){
		String[][] rowSquare = square;
		String rowCharacter = "";
		boolean checking = false;
		//This nested loop will pick a number in each row and go through the whole
		//row to make sure that the specific number is not found, and it is found
		//then it will return false immediately
		for(int row = 0; row < rowSquare.length; row++){
			for(int col = 0; col < rowSquare.length; col++){
				rowCharacter = rowSquare[col][row];
				if(col != rowSquare.length - 1){
					//This loop is going through the whole row to check the number if it is
					//repeated or not
					for(int nextCharc = col+1; nextCharc < rowSquare.length; nextCharc++){
						if(!rowCharacter.equals(rowSquare[nextCharc][row])){
							checking = true;}
						else{
							return false;}
				}}
		}}
		return checking;
	}
	

	/**
     * Creates a file menubar that will have one menu item which will terminate the application
     */
    private void fileMenuBar(){
    	//Creates a menuBar with a menu called file and inside that a menuItem 
    	//called exit which will terminate the application
		Menu file = new Menu("File");
		MenuItem exiting = new MenuItem("Exit");
		//When exiting menuItem is pressed then the application terminate
		exiting.setOnAction(event -> {
			System.out.println("Exiting....");
			stage.close();
		    });
		//Adds the menuItem to the menu and menuBar
		file.getItems().add(exiting);
		fileBar.getMenus().add(file);
		menu.getChildren().add(fileBar);
    }


}
