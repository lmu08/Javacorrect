package controller;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController
implements Initializable {
	@FXML
	private MenuItem logoutContextMenu;
	@FXML
	private ComboBox<String> projectNameButton;
	@FXML
	private Button deleteProjectButton;
	@FXML
	private TableView<StudentProject> studentProjectsTable;
	@FXML
	private TableColumn<StudentProject, String> studentNameColumn;
	@FXML
	private TableColumn<StudentProject, String> studentIdColumn;
	@FXML
	private TableColumn<StudentProject, Integer> markColumn;
	@FXML
	private TableColumn<StudentProject, Date> sendDateColumn;
	@FXML
	private TextField projectNameField;
	@FXML
	private DatePicker deadlineDatePicker;
	@FXML
	private Button expectedOutputButton;
	@FXML
	private Button studentListButton;
	@FXML
	private Button createProjectButton;
	private String currentUser;
	
	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
		projectNameButton.setItems(FXCollections.observableList(queryProjectNames()));
		projectNameButton.setOnAction(event -> updateTable());
		
		studentNameColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("name"));
		studentIdColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentId"));
		markColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, Integer>("mark"));
		sendDateColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, Date>("sendDate"));
		
		projectNameField.textProperty().addListener(event -> updateCreateProjectButton());
		deadlineDatePicker.setEditable(false);
		deadlineDatePicker.setOnAction(event -> updateCreateProjectButton());
	}

	public void initUser(final WindowManager windowManager, final String login) {
		this.currentUser = login;
		logoutContextMenu.setOnAction(event -> {
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setContentText("Are you sure you want to logout ?");
			alert.showAndWait().filter(ButtonType.OK::equals).ifPresent(button -> windowManager.showLoginView());
			
		});
		projectNameButton.setItems(FXCollections.observableList(queryProjectNames()));
	}
	
	private List<String> queryProjectNames() {
		//TODO get project names from database. IDs can be retrieved too, and added to MenuItem.userData if needed.
		ArrayList<String> al = new ArrayList<String>();
		java.sql.Connection mysqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());
		String projectName;
		try {
			ResultSet rs = MysqlRequest.getProjectNameByTeacher(mysqlco, this.currentUser);
			while(rs.next()) {
				projectName = rs.getString("intituleProjet");
				al.add(projectName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return al;
	}
	
	public static final class StudentProject {
		private final SimpleStringProperty name;
		private final SimpleStringProperty studentId;
		private final SimpleIntegerProperty mark;
		private final SimpleObjectProperty<Date> sendDate;

		public StudentProject(final String name, final String studentId, final Integer mark, final Date sendDate) {
			this.name = (name == null) ? new SimpleStringProperty() : new SimpleStringProperty(name);
			this.studentId = (studentId == null) ? new SimpleStringProperty() : new SimpleStringProperty(studentId);
			this.mark = (mark == null) ? new SimpleIntegerProperty() : new SimpleIntegerProperty(mark);
			this.sendDate = (sendDate == null) ? new SimpleObjectProperty<>() : new SimpleObjectProperty<>(sendDate);
		}
		
		public String getName() {
			return name.get();
		}

		public String getStudentId() {
			return studentId.get();
		}
		
		public Integer getMark() {
			return mark.get();
		}
		
		public Date getSendDate() {
			return sendDate.get();
		}
	}

	private void updateTable() {
		studentProjectsTable.setItems(parseStudentProjectList());
		deleteProjectButton.setDisable(false);
	}

	private ObservableList<StudentProject> parseStudentProjectList() {
		//TODO Get selected project and logged user + get students from DB + create the list
		
		// Sample :
		return FXCollections.observableArrayList(
			new StudentProject("Jean DUPONT", "985656", null, null),
			new StudentProject("Paul SMITH", "765654", 16, new Date()));
	}

	@FXML
	private void handleDeleteProjectAction() {
		//TODO Send delete request to DB + display response
	}

	@FXML
	private void handleSelectOutputAction() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text (.txt)", "*.txt"));
		final File file = fileChooser.showOpenDialog(null);
		Optional.ofNullable(file).map(File::getPath).filter(path -> !path.isEmpty()).ifPresent(path -> {
			expectedOutputButton.setUserData(path);
			expectedOutputButton.setText(path);
		});
	}

	@FXML
	private void handleSelectListAction() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Comma Separated Values (.csv)", "*.csv"));
		final File file = fileChooser.showOpenDialog(null);
		Optional.ofNullable(file).map(File::getPath).filter(path -> !path.isEmpty()).ifPresent(path -> {
			studentListButton.setUserData(path);
			studentListButton.setText(path);
		});
		updateCreateProjectButton();
	}
	
	@FXML
	private void handleCreateProjectAction() {
		final String projectName = projectNameField.getText();
		final LocalDate deadline = deadlineDatePicker.getValue();
		final String expectedOutputPath = (String) expectedOutputButton.getUserData();
		final String studentListPath = (String) studentListButton.getUserData();
		//		new StudentCsvParser("/home/flo/Documents/JavaCorrect/Javacorrect/studentTest.csv");
		final String projectId = UUID.randomUUID() + "";
		//TODO Parse files + send data to DB + display response
	}

	private void updateCreateProjectButton() {
		createProjectButton.setDisable(projectNameField.getText().isEmpty() || deadlineDatePicker.getValue() == null || (String) studentListButton.getUserData() == null);
	}
}
