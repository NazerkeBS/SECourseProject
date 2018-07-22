package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Tuple;

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

@SuppressWarnings("restriction")
public class GradeListController implements Initializable {

	private EntityManagerFactory emFactory;
	private Authentication auth;
	private EntityManager entityManager;

	@FXML
	private TableView<Tuple> tableView;

	@FXML
	private TableColumn<Tuple, String> courseNameColumn;

	@FXML
	private TableColumn<Tuple, Integer> gradeColumn;

	private StudentController studentController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		 entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);
		this.studentController = new StudentController(
		auth.searchForExistingStudent(StoreUsername.getStudentUsername()));
		List<Tuple> grades = studentController.listGrades();
		tableView.setItems(FXCollections.observableArrayList(grades));
		settingColumnContents();

	
	}

 void settingColumnContents() {
		courseNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Tuple, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Tuple, String> data) {
				return ObjectConstant.<String>valueOf(data.getValue().get(0, String.class));
			}
		});

		gradeColumn.setCellValueFactory(new Callback<CellDataFeatures<Tuple, Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Tuple, Integer> data) {
				return ObjectConstant.<Integer>valueOf(data.getValue().get(1, Integer.class));
			}
		});
	}

}
