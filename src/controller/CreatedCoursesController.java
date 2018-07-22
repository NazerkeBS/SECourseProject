package controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sun.javafx.binding.ObjectConstant;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import main.Authentication;
import model.Course;

@SuppressWarnings("restriction")
public class CreatedCoursesController implements Initializable {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private Authentication auth;

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

	private TeacherController teacherController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);

		this.teacherController = new TeacherController(
				auth.searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		List<Course> courses = this.teacherController.listCreatedCourses();
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
