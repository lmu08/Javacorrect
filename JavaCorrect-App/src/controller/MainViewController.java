package controller;

import java.io.File;
import java.math.BigDecimal;
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
import db.Student;
import db.StudentCsvParser;
import exceptions.DeadlineException;
import exceptions.MarkingDbManagementException;
import exceptions.ProjectDbManagementException;
import exceptions.ProjectNameException;
import exceptions.StudentClassroomException;
import exceptions.StudentDbManagementException;
import db.MysqlRequest;
import javafx.beans.property.SimpleDoubleProperty;
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
import tools.DateTools;

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
	private TableColumn<StudentProject, Double> markColumn;
	@FXML
	private TableColumn<StudentProject, String> classroomColumn;
	@FXML
	private TableColumn<StudentProject, Date> sendDateColumn;
	@FXML
	private TextField projectNameField;
	@FXML
	private DatePicker deadlineDatePicker;
	@FXML
	private TextField argumentsField;
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
		projectNameButton.setOnAction(event -> {
				updateTable();
		});
		
		studentNameColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentName"));
		studentIdColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentId"));
		markColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, Double>("mark"));
		classroomColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("classroom"));
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
		private final SimpleStringProperty studentName;
		private final SimpleIntegerProperty studentId;
		private final SimpleStringProperty classroom;
		private final SimpleDoubleProperty mark;
		private final SimpleObjectProperty<Date> sendDate;

		public StudentProject(final String studentName,final Integer studentId, final String classroom, final Double mark, final Date sendDate) {
			this.studentName = (studentName == null) ? new SimpleStringProperty() : new SimpleStringProperty(studentName);
			this.studentId = (studentId == null) ? new SimpleIntegerProperty() : new SimpleIntegerProperty(studentId);
			this.classroom = (classroom == null) ? new SimpleStringProperty() : new SimpleStringProperty(classroom);
			this.mark = (mark == null) ? new SimpleDoubleProperty() : new SimpleDoubleProperty(mark);
			this.sendDate = (sendDate == null) ? new SimpleObjectProperty<>() : new SimpleObjectProperty<>(sendDate);
		
		}
		
		public String getClassroom() {
			return classroom.get();
		}
		
		public String getStudentName() {
			return studentName.get();
		}

		public int getStudentId() {
			return studentId.get();
		}
		
		public double getMark() {
			return mark.get();
		}
		
		public Date getSendDate() {
			return sendDate.get();
		}
	}

	private void updateTable(){
		try {
		studentProjectsTable.setItems(parseStudentProjectList());
		deleteProjectButton.setDisable(false);
		}
		catch(MarkingDbManagementException e) {
			e.printStackTrace();
		}
	}

	private ObservableList<StudentProject> parseStudentProjectList() throws MarkingDbManagementException {
		//TODO Get selected project and logged user + get students from DB + create the list
		String nom;
		int numEtu;
		BigDecimal note;
		String classe;
		java.sql.Date dateEnvoi;
		String intituleProjet = projectNameButton.getValue();
		ArrayList<StudentProject> al = new ArrayList<StudentProject>();
		java.sql.Connection mysqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());
		try {
			ResultSet rs = MysqlRequest.getEvaluation(mysqlco, this.currentUser, intituleProjet );
			while(rs.next()) {
				dateEnvoi = rs.getDate("EVALUATION_date_envoi");
				nom = rs.getString("nomEtu") + " "+ rs.getString("prenomEtu");
				
				numEtu = rs.getInt("numEtu");
				note = rs.getBigDecimal("EVALUATION_note");
				classe = rs.getString("intituleClasse");
				System.out.println(classe);
				System.out.println(classe);
				al.add(
						new StudentProject(nom, numEtu, classe, note.doubleValue(), dateEnvoi)
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MarkingDbManagementException("Erreur lors de la récupération des données");
		}
		// Sample :
		return FXCollections.observableArrayList(
			al);
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
	private void handleCreateProjectAction() throws ProjectNameException, StudentClassroomException, ProjectDbManagementException, DeadlineException, StudentDbManagementException, MarkingDbManagementException {
		final String projectId = UUID.randomUUID() + "";
		final String projectName = projectNameField.getText();
		final LocalDate deadline = deadlineDatePicker.getValue();
		final String arguments = argumentsField.getText();
		final String expectedOutputPath = (String) expectedOutputButton.getUserData(); //TODO send to the server using a socket (scp temporarily)
		ArrayList<Student> students;
		StudentCsvParser sparser = new StudentCsvParser();
		sparser.parse((String) studentListButton.getUserData());
		students = sparser.getStudents();
		String className = students.get(0).getClasse();
		int classYear = students.get(0).getPromo();
		ResultSet rspromo;
		int idPromotion = -1;
		
		
		if(DateTools.checkMinimumDeadlineDays(deadline,3) <0) {
			throw new DeadlineException("L'échéance du projet doit être dans au moins 3 jours");
		}
		
		try {
			ResultSet rs = MysqlRequest.getEvaluationByLoginProjName(this.currentUser, projectName);
			if(rs.isBeforeFirst()) {
				throw new ProjectNameException("Vous avez déjà crée un projet du même nom ");
			}
		}
		catch(SQLException sqle) {
			System.out.println(sqle.getSQLState());
			throw new ProjectNameException("Erreur lors de la vérification du nom de projet ");
		}
		
		try {
			rspromo = MysqlRequest.getIdPromotionRequest(classYear, className);
			if (!rspromo.isBeforeFirst()) {
				ResultSet rsidClasse = MysqlRequest.getidClasseRequest(className);
				if (!rsidClasse.isBeforeFirst()) {
					MysqlRequest.insertClasse(className);
					rsidClasse = MysqlRequest.getidClasseRequest(className);
				}
				rsidClasse.next();
				final int idClasse = rsidClasse.getInt("idClasse");
				MysqlRequest.insertPromotion(classYear, idClasse);
			}
			rspromo = MysqlRequest.getIdPromotionRequest(classYear, className);
			rspromo.next();
			idPromotion = rspromo.getInt("idPromotion");
		}
		catch(SQLException e) {
			System.out.println(e.getSQLState());
			throw new StudentClassroomException("Erreur sur la gestion des classes et promotions,"
					+ " vérifiez ces colonnes dans votre csv");
		}
		try {
			MysqlRequest.insertProject(projectId, deadline, projectName);
		}
		catch(SQLException e) {
			throw new ProjectDbManagementException("Erreur lors de l'insertion du projet");
		}
		for(Student student : students) {
			ResultSet rstudent;
			try {
				rstudent = MysqlRequest.getStudentByNum(student.getNumEtu());
				if (!rstudent.isBeforeFirst()) {
					MysqlRequest.insertStudent(
							student.getNumEtu(),
							student.getPrenom(),
							student.getNom(),
							idPromotion);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getSQLState());
				throw new StudentDbManagementException("Erreur lors de l'ajout des étudiants en base de données");
			}
			
			try {
				MysqlRequest.insertEvaluation(projectId, this.currentUser, student.getNumEtu(), idPromotion);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getSQLState());
				throw new MarkingDbManagementException("Erreur lors de l'ajout des étudiants en base de données");
				
			}
		}
		
		
			
	}

	private void updateCreateProjectButton() {
		createProjectButton.setDisable(projectNameField.getText().isEmpty() || deadlineDatePicker.getValue() == null || (String) studentListButton.getUserData() == null);
	}
}
