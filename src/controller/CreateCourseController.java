package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Authentication;
import model.Teacher;
import model.Teacher_;

public class CreateCourseController implements Initializable {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;

	@FXML
	private TextField courseName;

	@FXML
	private TextField courseCode;

	@FXML
	private DatePicker date;

	@FXML
	private TextField time;

	@FXML
	private TextField credit;

	@FXML
	private Label successfullyCreated;

	private TeacherController teacherController;

	private Authentication auth;

	public CreateCourseController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
		date.setConverter(new DateConverter());
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
	}

	@FXML
	public void createCourse() {

		teacherController = new TeacherController(searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		int credits = Integer.parseInt(credit.getText());
		try {
			teacherController.createCourse(courseCode.getText(), courseName.getText(), credits,
					date.getEditor().getText(), time.getText());
			successfullyCreated.setText("Successfully  created");

		} catch (Exception e) {
		}

	}

	private Teacher searchForExistingTeacher(String teacherCode) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Teacher> cquery = cb.createQuery(Teacher.class);
		Root<Teacher> person = cquery.from(Teacher.class);
		cquery.where(person.get(Teacher_.code).in(teacherCode));
		cquery.select(person);
		List<Teacher> resultList = entityManager.createQuery(cquery).getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	public Authentication getAuth() {
		return this.auth;
	}

}
