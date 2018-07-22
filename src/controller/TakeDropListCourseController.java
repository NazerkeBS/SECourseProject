package controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sun.javafx.binding.ObjectConstant;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import main.Authentication;
import model.Course;

@SuppressWarnings("restriction")
public class TakeDropListCourseController implements Initializable {

	private EntityManagerFactory emFactory;
	private Authentication auth;
	private EntityManager entityManager;

	@FXML
	private TextField courseCode;

	@FXML
	private GridPane content;

	private StudentController studentController;

	@FXML
	private TableView<Course> tableView;

	@FXML
	private TableColumn<Course, String> courseCodeColumn;

	@FXML
	private TableColumn<Course, String> courseNameColumn;

	@FXML
	private TableColumn<Course, Integer> creditColumn;

	@FXML
	private TableColumn<Course, Date> startTimeColumn;

	@FXML
	private Label display;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
		this.auth = new Authentication(entityManager);
		content.setVisible(false);

	}

	@FXML
	public void takeCourse() {
		this.studentController = new StudentController(
				auth.searchForExistingStudent(StoreUsername.getStudentUsername()));
		String r = this.studentController.takeCourse(courseCode.getText());
		display.setText(r);
		courseCode.clear();
	}

	@FXML
	public void dropCourse() {
		this.studentController = new StudentController(
				auth.searchForExistingStudent(StoreUsername.getStudentUsername()));
		try {
			display.setText(studentController.dropCourse(courseCode.getText()));
			courseCode.clear();
		} catch (Exception e) {
		}
	}

	@FXML
	public void registeredCourses() {
		display.setVisible(false);

		content.setVisible(true);
		this.studentController = new StudentController(
				auth.searchForExistingStudent(StoreUsername.getStudentUsername()));
		Set<Course> courses = this.studentController.registeredSubjects();
		if (courses.size() == 0) {
			display.setText("You haven't registered to any courses");
		} else {
			tableView.setItems(FXCollections.observableArrayList(courses));
			settingColumnContents();
		}
	}

	@FXML
	public void listAllSubjects() {
		content.setVisible(true);
		this.studentController = new StudentController(
				auth.searchForExistingStudent(StoreUsername.getStudentUsername()));
		List<Course> courses = this.studentController.listAllSubjects();
		tableView.setItems(FXCollections.observableArrayList(courses));
		settingColumnContents();
	}

	void settingColumnContents() {
		courseCodeColumn.setCellValueFactory(new Callback<CellDataFeatures<Course, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Course, String> data) {
				return ObjectConstant.<String>valueOf(data.getValue().getCourseCode());
			}
		});

		courseNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Course, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Course, String> data) {
				return ObjectConstant.<String>valueOf(data.getValue().getCourseName());
			}
		});

		creditColumn.setCellValueFactory(new Callback<CellDataFeatures<Course, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Course, Integer> data) {
				return ObjectConstant.<Integer>valueOf(data.getValue().getCredit());
			}
		});

		startTimeColumn.setCellValueFactory(new Callback<CellDataFeatures<Course, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(CellDataFeatures<Course, Date> data) {
				return ObjectConstant.<Date>valueOf(data.getValue().getStartTime());
			}
		});
	}

}
