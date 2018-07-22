package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	@FXML
	private MenuBar menuBar;

	@FXML
	private Button logoutButton;

	@FXML
	private Pane content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
		menuBar.setFocusTraversable(true);
	}

	@FXML
	public void createCourse() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/CreateCourseForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void modifyCourse() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/ModifyCourseForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void deleteCourse() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/DeleteCourseForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void listStudents() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/ListStudentsForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void giveGrade() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/GiveGradeForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void listCourses() {
		content.getChildren().clear();
		try {
			content.getChildren().add(FXMLLoader.load(getClass().getResource("../view/CreatedCoursesForm.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void logout() {
		Stage stage = (Stage) logoutButton.getScene().getWindow();
		stage.close();

		Stage primaryStage = new Stage();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"));
			Scene scene = new Scene(root, 900, 500);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Neptun System");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
