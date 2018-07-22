package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Authentication;

public class DeleteCourseController implements Initializable {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private Authentication auth;

	@FXML
	private TextField courseCode;

	@FXML
	private Label display;

	private TeacherController teacherController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
	}

	@FXML
	public void deleteCourse() {
		teacherController = new TeacherController(auth.searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		display.setText(teacherController.deleteCourse(courseCode.getText()));
	}

}
