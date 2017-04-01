package boggle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.util.Duration;

public class BoggleGUI extends Application {
	// creating fields
	private Validator validator;
	private Button[][] buttonArray;
	TextField inputTextField;
	private Set<String> inputWordSet;
	private Label timerLabel;
	private Label wordsLabel;
	private Timeline timeline;
	private int timeSeconds;

	public static void main(String[] args) {
		// launching start() method
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// creating GUI
		init(primaryStage);
		primaryStage.show();

		// starting 2 min countdown timer
		startTimer(120);
	}

	private void init(Stage primaryStage) {
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);

		// setting validator field
		try {
			validator = new Validator();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// creating 4*4 tilePane for the board
		TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(4);
		tilePane.setPrefRows(4);

		// creating buttons to go in tilePane
		buttonArray = new Button[4][4];
		for (int i = 0; i < buttonArray.length; i++) {
			for (int j = 0; j < buttonArray.length; j++) {
				// setting button properties
				buttonArray[i][j] = new Button(Character.toString(validator.getBoard()[i][j]));
				buttonArray[i][j].setPrefSize(100, 100);
				buttonArray[i][j].setFont(new Font(30));
				buttonArray[i][j].setAlignment(Pos.CENTER);
				buttonArray[i][j].setStyle("-fx-border-color: black;");

				// creating listener for buttonArray
				buttonArray[i][j].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// getting text by adding clicked button's letter
						// to inputTextField's text
						String text = inputTextField.getText() + ((Button) event.getSource()).getText();

						// setting inputTextField value to new text
						/*
						 * this triggers the listener for the inputTextField,
						 * highlighting the new path
						 */
						inputTextField.setText(text);

					}
				});

				// adding the button to the tilePane
				tilePane.getChildren().add(buttonArray[i][j]);
			}
		}

		// creating inputTextField for user input
		inputTextField = new TextField();
		inputTextField.setPrefSize(400, 75);
		inputTextField.setFont(new Font(20));
		inputTextField.setStyle("-fx-border-color: black;");

		// creating update listener for inputTextField
		inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			highlightWord(newValue);
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
					if (validator.getValidWordSet().contains(text)) {
						updateWordsLabel(text);
					} else {
						System.out.println("NO MATCH");
					}

					// clear text
					inputTextField.setText("");
				}
			}
		});

		// creating max length listener for inputTextField
		inputTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (inputTextField.getText().length() > 16) {
					String text = inputTextField.getText().substring(0, 16);
					inputTextField.setText(text);
				}
			}
		});

		// initializing timerLabel
		timerLabel = new Label();
		timerLabel.setPrefSize(300, 100);
		timerLabel.setFont(new Font(40));
		timerLabel.setStyle("-fx-border-color: black;");
		timerLabel.setAlignment(Pos.CENTER);

		// creating inputWordSet
		inputWordSet = new HashSet<String>();

		// creating and initializing wordsLabel
		wordsLabel = new Label();
		wordsLabel.setPrefSize(300, 375);
		wordsLabel.setFont(new Font(20));
		wordsLabel.setStyle("-fx-border-color: black;");
		wordsLabel.setAlignment(Pos.TOP_CENTER);

		// creating vboxes and hboxes and adding them to GUI
		VBox boardAndInput = new VBox();
		boardAndInput.getChildren().addAll(tilePane, inputTextField);

		VBox timerAndWords = new VBox();
		timerAndWords.getChildren().addAll(timerLabel, wordsLabel);

		HBox hbox = new HBox();
		hbox.getChildren().addAll(boardAndInput, timerAndWords);

		root.getChildren().add(hbox);

	}

	private void startTimer(int timeFieldValue) {
		// setting field values
		timeline = null;
		timeSeconds = timeFieldValue;

		// stop timeline if it is not null
		if (timeline != null) {
			timeline.stop();
		}

		// update timerLabel
		timerLabel.setText(Integer.toString(timeSeconds));
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler() {
			// KeyFrame event handler
			@Override
			public void handle(Event event) {
				timeSeconds--;
				// update timerLabel
				timerLabel.setText(Integer.toString(timeSeconds));
				if (timeSeconds <= 0) {
					timeline.stop();
				}
			}
		}));
		timeline.playFromStart();
	}

	private void highlightWord(String word) {
		// if the input text field is empty
		if (word.equals("")) {
			// resetting board color
			resetBoardColor();
		}
		// checking if the new char is not null or no value
		else if (word != null && word.length() > 0) {
			// creating board variable from Validator board
			char[][] board = validator.getBoard();

			// boardCopy for initial findWordPath recursive start
			char[][] boardCopy = new char[4][4];
			boardCopy = validator.updateToBoard(boardCopy);

			// boolean shouldBreak variable
			boolean shouldBreak = false;

			// hasValidPath variable to help with user input restriction
			boolean hasValidPath = false;

			// nested for loops going through the board
			// and comparing it to first letter of inputPath
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] == word.charAt(0)) {

						// creating initPath variable w/ first index
						List<int[]> initPath = new ArrayList<int[]>();
						initPath.add(new int[] { i, j });

						// recursive findWordPath method for inputPath
						if (findWordPath(word, boardCopy, validator.getNextIndexes(new int[] { i, j }), 1, initPath)) {
							shouldBreak = true;
							hasValidPath = true;
							break;
						}
					}

					if (shouldBreak) {
						break;
					}
				}
			}
			if (!hasValidPath) {
				String inputText = inputTextField.getText();
				if (inputText.length() > 0) {
					inputTextField.setText(inputText.substring(0, (inputText.length() - 1)));
				}
			}
		}

	}

	// method to update the wordsLabel
	private void updateWordsLabel(String text) {
		// adding text to inputWordSet
		inputWordSet.add(text);

		// resetting wordsLabel text
		wordsLabel.setText("");

		// iterating through inputWordSet and adding the words to GUI
		for (String word : inputWordSet) {
			wordsLabel.setText(wordsLabel.getText() + "\n" + word);
		}
	}

	// recursive boolean findWordPath method
	public boolean findWordPath(String inputWord, char[][] board, List<int[]> nextIndexes, int charIndex,
			List<int[]> path) {

		// if the charIndex reaches inputWord.length:
		// highlight the path and then return
		if (charIndex == inputWord.length()) {
			return highlightPath(path);
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
				if (findWordPath(inputWord, board, validator.getNextIndexes(new int[] { x, y }), (charIndex + 1),
						path)) {
					return true;
				} else {
					return false;
				}

			}

		}
		// resetting board to global default
		board = validator.updateToBoard(board);
		return false;
	}

	// highlightPath method to highlight the path
	public boolean highlightPath(List<int[]> path) {
		// resetting board color
		resetBoardColor();

		// going through path
		for (int k = 0; k < path.size(); k++) {
			// creating x and y from path
			int x = path.get(k)[0];
			int y = path.get(k)[1];

			// creating potential highlight background for cell
			Paint paint = Paint.valueOf("#ffff33");
			Background yellowHighlight = new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY));

			// if statement to check if cell is already highlighted
			if (!buttonArray[x][y].getBackground().equals(yellowHighlight)) {
				// highlighting the current square yellow
				buttonArray[x][y].setBackground(yellowHighlight);
			} else {
				System.out.println("HI");
				return false;
			}
		}
		return true;
	}

	// resetBoardColor method to reset the board's color
	public void resetBoardColor() {
		// setting all of the squares back to default white color
		for (int i = 0; i < buttonArray.length; i++) {
			for (int j = 0; j < buttonArray[0].length; j++) {
				Paint paint = Paint.valueOf("#ffffff");
				buttonArray[i][j]
						.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}
	}
}
