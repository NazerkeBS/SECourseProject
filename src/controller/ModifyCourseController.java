package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Authentication;

public class ModifyCourseController implements Initializable {
	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private Authentication auth;

	@FXML
	private TextField courseCode;

	@FXML
	private TextField newCourseName;

	@FXML
	private TextField newCredit;

	@FXML
	private DatePicker newDate;

	@FXML
	private TextField newTime;

	@FXML
	private Label modified;

	private TeacherController teacherController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
		newDate.setConverter(new DateConverter());
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
	}

	@FXML
	public void modifyCourse() {

		int credits = Integer.parseInt(newCredit.getText());
		teacherController = new TeacherController(auth.searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		try {
			String m = teacherController.modifyCourse(courseCode.getText(), newCourseName.getText(), credits,
					newDate.getEditor().getText(), newTime.getText());
			modified.setText(m);

		} catch (Exception e) {
		}

	}

}
