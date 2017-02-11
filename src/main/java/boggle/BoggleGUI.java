package boggle;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
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

		// creating listener for inputTextField
		inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			// comparing input to validWordSet
			if (getValidator().getValidWordSet().contains(newValue)) {
				System.out.println("MATCH: " + newValue);
			} else {
				System.out.println("NO MATCH");
			}

		});

		// creating vbox and adding it to GUI
		VBox vbox = new VBox();
		vbox.getChildren().addAll(tilePane, inputTextField);
		root.getChildren().add(vbox);
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
