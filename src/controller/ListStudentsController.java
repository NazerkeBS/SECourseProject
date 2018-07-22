package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.Student;

@SuppressWarnings("restriction")
public class ListStudentsController implements Initializable {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private Authentication auth;

	@FXML
	private TextField courseCode;

	@FXML
	private TableView<Student> tableView;

	@FXML
	private TableColumn<Student, String> studentCodeColumn;

	@FXML
	private TableColumn<Student, String> nameColumn;

	@FXML
	private GridPane content;

	@FXML
	private TableColumn<Student, String> emailColumn;

	private TeacherController teacherController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
	}

	@FXML
	public void listStudents() {
		this.teacherController = new TeacherController(auth.searchForExistingTeacher(StoreUsername.getTeacherUsername()));
		Set<Student> students = teacherController.listStudents(courseCode.getText());
		if (students == null || students.size() == 0) {
			content.getChildren().clear();
			content.getChildren().add(new Label("Not found students or doesn't exist course"));
		} else {
			tableView.setItems(FXCollections.observableArrayList(students));
			settingColumnContents();
		}
	}

	void settingColumnContents() {
		studentCodeColumn
				.setCellValueFactory(new Callback<CellDataFeatures<Student, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Student, String> data) {
						return ObjectConstant.<String>valueOf(data.getValue().getCode());
					}
				});

		nameColumn.setCellValueFactory(new Callback<CellDataFeatures<Student, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Student, String> data) {
				return ObjectConstant.<String>valueOf(data.getValue().getName());
			}
		});

		emailColumn.setCellValueFactory(new Callback<CellDataFeatures<Student, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Student, String> data) {
				return ObjectConstant.<String>valueOf(data.getValue().getEmail());

			}
		});
	}

}
