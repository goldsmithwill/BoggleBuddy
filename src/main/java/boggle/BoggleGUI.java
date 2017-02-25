package boggle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BoggleGUI extends Application {
	// creating validator and labelArray fields
	private Validator validator;
	private Label[][] labelArray;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
		primaryStage.show();
	}

	private void init(Stage primaryStage) {
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		
		// setting validator field
		try {
			setValidator(new Validator());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// creating 4*4 tilePane for the board
		TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(4);
		tilePane.setPrefRows(4);

		// creating labels to go in tilePane
		setLabelArray(new Label[4][4]);
		for (int i = 0; i < getLabelArray().length; i++) {
			for (int j = 0; j < getLabelArray().length; j++) {
				// setting label properties
				getLabelArray()[i][j] = new Label(Character.toString(getValidator().getBoard()[i][j]));
				getLabelArray()[i][j].setPrefSize(100, 100);
				getLabelArray()[i][j].setFont(new Font(30));
				getLabelArray()[i][j].setAlignment(Pos.CENTER);
				getLabelArray()[i][j].setStyle("-fx-border-color: black;");

				// adding the label to the tilePane
				tilePane.getChildren().add(getLabelArray()[i][j]);
			}
		}

		// creating inputTextField for user input
		TextField inputTextField = new TextField();
		inputTextField.setPrefSize(400, 75);
		inputTextField.setFont(new Font(20));
		inputTextField.setStyle("-fx-border-color: black;");

		// creating update listener for inputTextField
		inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			// if the input text field is empty
			if (newValue.equals("")) {
				// resetting board color
				resetBoardColor();
			}
			// checking if the new char is not null or no value
			else if (newValue != null && newValue.length() > 0) {
				// creating board variable from Validator board
				char[][] board = getValidator().getBoard();

				// boardCopy for initial findWordPath recursive start
				char[][] boardCopy = new char[4][4];
				boardCopy = getValidator().updateToBoard(boardCopy);

				// boolean shouldBreak variable
				boolean shouldBreak = false;

				// nested for loops going through the board
				// and comparing it to first letter of newValue
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						if (board[i][j] == newValue.charAt(0)) {

							// creating initPath variable w/ first index
							List<int[]> initPath = new ArrayList<int[]>();
							initPath.add(new int[] { i, j });

							// recursive findWordPath method for newValue
							if (findWordPath(newValue, boardCopy, getValidator().getNextIndexes(new int[] { i, j }), 1,
									initPath)) {
								shouldBreak = true;
								break;
							}
						}
						if (shouldBreak) {
							break;
						}
					}
				}

			}
		});

		// creating enter listener for inputTextField
		inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				// if enter key is pressed
				if (keyEvent.getCode() == KeyCode.ENTER) {
					String text = inputTextField.getText();

					// resetting board color
					resetBoardColor();

					// comparing validWordSet w/ textField value
					if (getValidator().getValidWordSet().contains(text)) {
						System.out.println("MATCH: " + text);
					} else {
						System.out.println("NO MATCH");
					}

					// clear text
					inputTextField.setText("");
				}
			}
		});

		//creating timerLabel
		Label timerLabel = new Label();	
		timerLabel.setPrefSize(300, 100);
		timerLabel.setFont(new Font(20));
		timerLabel.setStyle("-fx-border-color: black;");
		
		// creating vboxes and hboxes and adding them to GUI
		VBox boardAndInput = new VBox();
		boardAndInput.getChildren().addAll(tilePane, inputTextField);

		VBox timerAndWords = new VBox();
		timerAndWords.getChildren().addAll(timerLabel);

		HBox hbox = new HBox();
		hbox.getChildren().addAll(boardAndInput, timerAndWords);

		root.getChildren().add(hbox);
		
		new Timer().schedule(new TimerTask() {
			  @Override
			  public void run() {
				  startTimer(timerLabel);
			  }
			}, 1*1000);
			
		
	}

	private void startTimer(Label timerLabel) {
		// seconds variable for length in seconds
		int seconds = 120;

		// start count down
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + (seconds * 1000);
		long temp = startTime + 1000;

		while (startTime < endTime) {
			if (startTime == temp) {
				seconds--;
				
				temp += 1000;
			}

			startTime = System.currentTimeMillis();
		}
	}

	// recursive boolean findWordPath method
	public boolean findWordPath(String inputWord, char[][] board, List<int[]> nextIndexes, int charIndex,
			List<int[]> path) {

		// if the charIndex reaches inputWord.length:
		// highlight the path and then return
		if (charIndex == inputWord.length()) {
			highlightPath(path);
			return true;
		}

		// creating currentChar from charIndex
		char currentChar = inputWord.charAt(charIndex);

		// going through nextIndexes list
		for (int i = 0; i < nextIndexes.size(); i++) {
			// creating x and y from current index in nextIndexes
			int x = nextIndexes.get(i)[0];
			int y = nextIndexes.get(i)[1];

			// if the currentIndex is currentChar
			if (board[x][y] == currentChar) {
				// making board index w/ currentChar blank
				board[x][y] = ' ';

				// adding index to path
				path.add(new int[] { x, y });

				// recursion
				if (findWordPath(inputWord, board, getValidator().getNextIndexes(new int[] { x, y }), (charIndex + 1),
						path)) {
					return true;
				} else {
					return false;
				}

			}

		}
		// resetting board to global default
		board = getValidator().updateToBoard(board);
		return false;

	}

	// highlightPath method to highlight the path
	public void highlightPath(List<int[]> path) {
		// resetting board color
		resetBoardColor();

		// going through path
		for (int k = 0; k < path.size(); k++) {
			// creating x and y from path
			int x = path.get(k)[0];
			int y = path.get(k)[1];

			// highlighting the current square yellow
			Paint paint = Paint.valueOf("#ffff33");
			getLabelArray()[x][y]
					.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
		}

	}

	// resetBoardColor method to reset the board's color
	public void resetBoardColor() {
		// setting all of the squares back to default white color
		for (int i = 0; i < getLabelArray().length; i++) {
			for (int j = 0; j < getLabelArray()[0].length; j++) {
				Paint paint = Paint.valueOf("#ffffff");
				getLabelArray()[i][j]
						.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}
	}

	// getters and setters
	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public Label[][] getLabelArray() {
		return labelArray;
	}

	public void setLabelArray(Label[][] labels) {
		this.labelArray = labels;
	}
}