package boggle;

import java.io.FileNotFoundException;
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
import javafx.scene.control.TextArea;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BoggleGUI extends Application {
	// creating fields
	private BoggleBackEnd b;
	private Validator v;
	private Button[][] buttonArray;
	public TextField inputTextField;
	private Set<String> inputWordSet;
	private Label timerLabel;
	private TextArea wordsArea;
	private Stage nextLevelPopup;
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
		startTimer(2);
	}

	private void init(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(this.getClass().getResource("boggle.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		// initializing b field
		b = new BoggleBackEnd();

		// initializing v field
		try {
			v = new Validator();
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
				buttonArray[i][j] = new Button(Character.toString(v.getBoard()[i][j]));
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
			b.highlightWord(newValue);
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
					if (v.getValidWordSet().contains(text)) {
						updateWordsArea(text);
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

		// intitializing inputWordSet
		inputWordSet = new HashSet<String>();

		// initializing wordsArea
		wordsArea = new TextArea();
		wordsArea.setPrefSize(300, 375);
		wordsArea.setFont(new Font(20));
		wordsArea.setStyle("-fx-border-color: black;");
		wordsArea.setEditable(false);

		// creating nextLevelButton
		Button nextLevelButton = new Button("Go to Next Level");

		// setting nextLevelButton properties
		Paint paint = Paint.valueOf("#ffffff");
		nextLevelButton.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
		nextLevelButton.setFont(new Font(15));
		nextLevelButton.setStyle("-fx-border-color: black;");

		// creating nextLevelScene
		Scene nextLevelScene = new Scene(nextLevelButton, 150, 65);

		// initializing nextLevelPopup
		nextLevelPopup = new Stage();
		nextLevelPopup.initModality(Modality.APPLICATION_MODAL);
		nextLevelPopup.initOwner(primaryStage);
		nextLevelPopup.setScene(nextLevelScene);

		// creating vboxes and hboxes and adding them to GUI
		VBox boardAndInput = new VBox();
		boardAndInput.getChildren().addAll(tilePane, inputTextField);

		VBox timerAndWords = new VBox();
		timerAndWords.getChildren().addAll(timerLabel, wordsArea);

		HBox hbox = new HBox();
		hbox.getChildren().addAll(boardAndInput, timerAndWords);

		resetBoardColor();

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
		timerLabel.setText(b.parseSeconds(timeSeconds));
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler() {
			// KeyFrame event handler
			@Override
			public void handle(Event event) {
				timeSeconds--;
				// update timerLabel
				timerLabel.setText(b.parseSeconds(timeSeconds));
				if (timeSeconds <= 0) {
					timeline.stop();
					displayValidWords();
					popup();
				}
			}
		}));
		timeline.playFromStart();
	}

	// method to update the wordsArea
	private void updateWordsArea(String text) {
		// adding text to inputWordSet
		inputWordSet.add(text);

		// resetting wordsArea text
		wordsArea.setText("");

		// iterating through inputWordSet and adding the words to GUI
		for (String word : inputWordSet) {
			wordsArea.setText(wordsArea.getText() + "\n" + word);
		}
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

	private void displayValidWords() {
		Set<String> validWordSet = v.getValidWordSet();
		wordsArea.setText(wordsArea.getText() + "\n" + "--------------------");
		for (String validWord : validWordSet) {
			wordsArea.setText(wordsArea.getText() + "\n" + validWord);
		}
	}

	private void popup() {
		nextLevelPopup.show();
	}
}
