package boggle;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoggleGUI extends Application {
	// creating validator field
	private Validator validator;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// creating 4*4 tilePane for the board
		TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(4);
		tilePane.setPrefRows(4);

		// creating labels to go in tilePane
		Label[] labels = new Label[16];
		for (int j = 0; j < labels.length; j++) {
			labels[j] = new Label("label" + (j + 1));
			labels[j].setPrefSize(100, 100);
			labels[j].setStyle("-fx-border-color: black;");
			tilePane.getChildren().add(labels[j]);
		}

		// creating inputTextField for user input
		TextField inputTextField = new TextField();
		inputTextField.setPrefSize(400, 75);
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

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
