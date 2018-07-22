package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Authentication;

public class GiveGradeController implements Initializable {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private Authentication auth;

	@FXML
	private TextField courseCode;

	@FXML
	private TextField studentCode;

	@FXML
	private TextField grade;

	@FXML
	private Label graded;

	private TeacherController teacherController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
	}

	@FXML
	public void giveGrade() {
		teacherController = new TeacherController(auth.searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		int mark = Integer.parseInt(grade.getText());
		String result = teacherController.giveGrade(courseCode.getText(), studentCode.getText(), mark);
		graded.setText(result);
	}

}
