package controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Authentication;
import model.Teacher;

public class LogInController implements Initializable {

	@FXML
	private TextField user;

	@FXML
	private PasswordField password;

	@FXML
	private Button loginButton;

	@FXML
	private Label cannotLogin;

	private Authentication auth;

	private EntityManager entityManager;

	private EntityManagerFactory emFactory;

	public LogInController(Stage primaryStage) {
		user = new TextField();
		password = new PasswordField();
		loginButton = new Button();

	}

	public LogInController() {
	}

	public void logIn() throws NoSuchAlgorithmException, InvalidKeySpecException {

		this.emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		this.entityManager = emFactory.createEntityManager();
		this.auth = new Authentication(entityManager);

		loginButton.setOnAction(e -> {
			String username;
			String pswd;
			username = user.getText();
			pswd = password.getText();
			try {
				if (canLogIn(auth, username, pswd)) {
					closeButtonAction();
					Parent root;
					try {

						if (auth.searchForExistingTeacher(username) instanceof Teacher) {
							root = FXMLLoader.load(getClass().getResource("../view/MainWindow.fxml"));
							StoreUsername.setTeacherUsername(username);
						} else {

							root = FXMLLoader.load(getClass().getResource("../view/MenuStudentForm.fxml"));
							StoreUsername.setStudentUsername(username);
						}
						Scene scene = new Scene(root, 900, 500);
						scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
						Stage primaryStage = new Stage();
						primaryStage.setScene(scene);
						primaryStage.setResizable(false);
						primaryStage.setTitle("Neptun System");

						primaryStage.show();
					
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else if (!canLogIn(auth, username, pswd)) {
					cannotLogin.setText("Incorrect password or username. Try again");
                    user.clear();
                    password.clear();
				}
			} catch (NoSuchAlgorithmException e1) {
				cannotLogin.setText("Incorrect password or username. Try again");

			} catch (InvalidKeySpecException e1) {
				cannotLogin.setText("Incorrect password or username. Try again");
			}
		});

	}

	private void closeButtonAction() {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();
	}

	public boolean canLogIn(Authentication auth, String code, String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return (auth.authenticate(code, password.toCharArray()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			logIn();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	public Authentication getAuth() {
		return auth;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public EntityManagerFactory getEmFactory() {
		return emFactory;
	}

	public void setAuth(Authentication auth) {
		this.auth = auth;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setEmFactory(EntityManagerFactory emFactory) {
		this.emFactory = emFactory;
	}

}