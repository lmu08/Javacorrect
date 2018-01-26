package controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import db.MarkingDbManagementException;
import db.MysqlRequest;
import db.Student;
import db.StudentCsvParser;
import db.StudentGroup;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import tools.AlertTools;

public class MainViewController
implements Initializable {
	private static final String PROJECT_CREATION_ERROR = "Impossible de créer le projet";
	private static final String DELETE_PROJECT_ERROR = "Impossible de supprimer le projet";
	private static final String SAVE_CSV_ERROR = "Impossible d'enregistrer le csv";
	private static final String SEND_OUTPUTFILE_ERROR = "Impossible d'envoyer le fichier de sortie au serveur";
	private static final int SEND_FILE_PORT = 52112;
	private static final int DELETE_PROJECT_PORT = 52113;
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
	private TableColumn<StudentProject, String> studentGroupColumn;
	@FXML
	private TableColumn<StudentProject, String> studentEmailColumn;
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
	private String host;

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
		projectNameButton.setOnMouseClicked(event -> {
			projectNameButton.getItems().clear();
			projectNameButton.getItems().addAll(queryProjectNames());
		});
		projectNameButton.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				if (newValue != null) {
					updateTable();
				}
			}
		});

		studentNameColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentName"));
		studentIdColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentId"));
		studentEmailColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentEmail"));
		markColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, Double>("mark"));
		studentGroupColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, String>("studentGroup"));
		sendDateColumn.setCellValueFactory(new PropertyValueFactory<StudentProject, Date>("sendDate"));

		projectNameField.textProperty().addListener(event -> updateCreateProjectButton());
		deadlineDatePicker.setEditable(false);
		deadlineDatePicker.setOnAction(event -> updateCreateProjectButton());
		argumentsField.textProperty().addListener(event -> updateCreateProjectButton());
		this.host = "localhost";
	}
	/**
	 * Sets current user in a variable
	 * @param windowManager
	 * @param login
	 */
	public void initUser(final WindowManager windowManager, final String login) {
		this.currentUser = login;
		logoutContextMenu.setOnAction(event -> {
			AlertTools.showConfirmation("Logout","Are you sure you want to logout ?")
			.filter(ButtonType.OK::equals).ifPresent(button -> windowManager.showLoginView());

		});
		projectNameButton.setItems(FXCollections.observableList(queryProjectNames()));
	}

	/**
	 * Gets a list with all projects by their name(intituleProjet)
	 * @return List of all projects
	 */
	private List<String> queryProjectNames() {
		final ArrayList<String> al = new ArrayList<>();
		String projectName;
		try {
			final ResultSet rs = MysqlRequest.getProjectNameByTeacher(currentUser);
			while (rs.next()) {
				projectName = rs.getString("intituleProjet");
				al.add(projectName);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			System.out.println(e.getSQLState());
		}
		return al.stream().distinct().collect(Collectors.toList());
	}

	public static final class StudentProject {
		private final SimpleStringProperty studentName;
		private final SimpleIntegerProperty studentId;
		private final SimpleStringProperty studentEmail;
		private final SimpleDoubleProperty mark;
		private final SimpleObjectProperty<Date> sendDate;
		private final SimpleStringProperty studentGroup;
		
		/**
		 * makes an object to store a marking for a student
		 * @param studentName
		 * @param studentId
		 * @param studentEmail
		 * @param studentGroup
		 * @param mark
		 * @param sendDate
		 */
		public StudentProject(final String studentName, final Integer studentId, final String studentEmail, final String studentGroup, final Double mark, final Date sendDate) {
			this.studentName = (studentName == null) ? new SimpleStringProperty() : new SimpleStringProperty(studentName);
			this.studentId = (studentId == null) ? new SimpleIntegerProperty() : new SimpleIntegerProperty(studentId);
			this.studentEmail = (studentEmail == null) ? new SimpleStringProperty() : new SimpleStringProperty(studentEmail);
			this.mark = (mark == null) ? new SimpleDoubleProperty() : new SimpleDoubleProperty(mark);
			this.sendDate = (sendDate == null) ? new SimpleObjectProperty<>() : new SimpleObjectProperty<>(sendDate);
			this.studentGroup = (studentGroup == null) ? new SimpleStringProperty() : new SimpleStringProperty(studentGroup);
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
		
		public String getStudentGroup() {
			return studentGroup.get();
		}
		
		public String getStudentEmail() {
			return studentEmail.get();
		}
	}
	
	/**
	 * updates a table
	 */
	private void updateTable() {
		try {
			studentProjectsTable.setItems(parseStudentProjectList());
			deleteProjectButton.setDisable(false);
		} catch (final MarkingDbManagementException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pulls all marking data related to a project and store it in a List
	 * 
	 * @return List of Marking(EVALUATION table) containing students
	 * @throws MarkingDbManagementException
	 */
	private ObservableList<StudentProject> parseStudentProjectList()
	throws MarkingDbManagementException {
		final String intituleProjet = projectNameButton.getValue();
		final ArrayList<StudentProject> projets = new ArrayList<>();
		try {
			final ResultSet rs = MysqlRequest.getEvaluation(currentUser, intituleProjet);
			while (rs.next()) {
				final Date dateEnvoi = rs.getDate("EVALUATION_date_envoi");
				final String nom = rs.getString("nomEtu") + " " + rs.getString("prenomEtu");
				final String mail = rs.getString("emailEtu");
				final int numEtu = rs.getInt("numEtu");
				final BigDecimal note = rs.getBigDecimal("EVALUATION_note");
				final String classe = rs.getString("intituleClasse");
				projets.add(new StudentProject(nom, numEtu, mail, classe, (note != null ? note.doubleValue() : null), dateEnvoi));
			}
		} catch (final SQLException e) {
			System.out.println(e.getSQLState());
			throw new MarkingDbManagementException("Erreur lors de la récupération des données", e);
		}
		return FXCollections.observableArrayList(projets);
	}
	
	/**
	 * Deletes a project 
	 */
	@FXML
	private void handleDeleteProjectAction() {
		final Optional<ButtonType> result = AlertTools.showConfirmation("Voulez-vous vraiment supprimer ce projet ", "ATTENTION : action irreversible");
		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			String idProjet = null;
			final String intituleProjet = projectNameButton.getValue();
			try {
				final ResultSet rs = MysqlRequest.getProjetByIntitule(intituleProjet);
				rs.next();
				idProjet = rs.getString("idProjet");
				System.out.println(idProjet);
			} catch (final SQLException e) {
				e.printStackTrace();
				AlertTools.showWarning(DELETE_PROJECT_ERROR, "Erreur lors de la récupération du projet en base de données");
			}
			if (deleteProjectFiles(this.host, DELETE_PROJECT_PORT, idProjet)) {
				try {
					System.out.println("Suppression du projet en cours");
					MysqlRequest.deleteProjet(idProjet);
				} catch (final SQLException e) {
					e.printStackTrace();
					AlertTools.showWarning(DELETE_PROJECT_ERROR, "Erreur lors de la suppression du projet");
					
				}
			}
			updateTable();
			AlertTools.showAlert(AlertType.INFORMATION, "Suppression réussie", "Le projet a été supprimé avec succès");
		}
	}

	/**
	 * Sends a request of deleting concerning a project to the server.
	 * This request is sent using a socket contained in a Collable<Boolean> Thread.
	 * 
	 * @param host
	 * @param port
	 * @param idProjet
	 * @return indicates if files has been successfully deleted.
	 */
	public boolean deleteProjectFiles(final String host, final int port, final String idProjet) {
		// ExecutorService make use of callable able. 
		// Callable is needed to get a returned value
		final ExecutorService pool = Executors.newFixedThreadPool(1);
		final Callable<Boolean> task = new DeleteProjectSocket(host, port, idProjet);
		final Future<Boolean> future = pool.submit(task);
		boolean bool = false;
		try {
			bool = future.get().booleanValue();
			if (!bool) {
				AlertTools.showWarning(DELETE_PROJECT_ERROR, "erreur de suppression des fichiers associés sur le serveur");
			}
		} catch (final InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return bool;
	}
	
	/**
	 * Makes a Txt file chooser and fetch filePath and show in on the pressed button
	 */
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
		updateCreateProjectButton();
	}
	
	/**
	 * Makes a CSV file chooser and fetch filePath and show in on the pressed button
	 */
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

	/**
	 * creates a project
	 */
	@FXML
	private void handleCreateProjectAction() {
		
		try {
			//Create the project in db
			final Optional<String> projectId = insertProject();
			
			if (projectId.isPresent()) {
				try {
					//Parse the student list and send it to db
					if (sendOutputFileProjet(this.host, SEND_FILE_PORT, projectId.get())) {
						insertCSV(projectId.get());
						AlertTools.showAlert(AlertType.INFORMATION, "Création réussie", "Le projet a été créé avec succès");
					}

				} catch (final SQLException e) {
					e.printStackTrace();
					System.out.println(e.getSQLState());
					AlertTools.showWarning(SAVE_CSV_ERROR, "Une erreur serveur s'est produite lors de l'enregistrement du csv.");
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			System.out.println(e.getSQLState());
			AlertTools.showWarning(PROJECT_CREATION_ERROR, "Une erreur serveur s'est produite lors de la création du projet.");
		}
	}

	/**
	 * Makes and start a thread sending a file to a specified server
	 * 
	 * @param host : host name of server to send file
	 * @param port : port of socket
	 * @param idProjet : id of project (UUID)
	 * @return indicates if file has been well sent
	 */
	private boolean sendOutputFileProjet(final String host, final int port, final String idProjet) {
		final String expectedOutputPath = (String) expectedOutputButton.getUserData();
		final File fichier = new File(expectedOutputPath);
		if (!fichier.exists()) {
			AlertTools.showWarning(PROJECT_CREATION_ERROR, "Le fichier passé en paramètre n'existe pas");
		}

		// ExecutorService to make Callable thread with return value
		final ExecutorService pool = Executors.newFixedThreadPool(15);
		final Callable<Boolean> task = new SendOutputFileSocket(host, port, expectedOutputPath, idProjet);
		final Future<Boolean> future = pool.submit(task);
		boolean bool = false;
		try {
			bool = future.get().booleanValue();
			if (!bool) {
				AlertTools.showWarning(SEND_OUTPUTFILE_ERROR, "erreur lors de la connexion au serveur ou de l'envoi");
			}
		} catch (final InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return bool;

	}
	
	/**
	 * Inserts project within database
	 * @return projectId if exists
	 * @throws SQLException
	 */
	private Optional<String> insertProject()
	throws SQLException {
		final String projectId = UUID.randomUUID().toString();
		final LocalDate deadline = deadlineDatePicker.getValue();
		if (deadline.compareTo(LocalDate.now().plusDays(3)) < 0) {
			AlertTools.showWarning(PROJECT_CREATION_ERROR, "L'échéance du projet doit être de 3 jours minimum.");
		} else {
			final String projectName = projectNameField.getText();
			if (MysqlRequest.getEvaluationByLoginProjName(currentUser, projectName).isBeforeFirst()) {
				AlertTools.showWarning(PROJECT_CREATION_ERROR, "Vous avez déjà créé un projet du même nom.");
			} else {
				final String arguments = argumentsField.getText();
				MysqlRequest.insertProject(projectId, deadline, projectName, arguments);
				return Optional.of(projectId);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Parses CSV file put by user on the application and add all data to database
	 */
	private void insertCSV(final String projectId)
	throws SQLException {
		final StudentCsvParser sparser = new StudentCsvParser();
		sparser.parse((String) studentListButton.getUserData());
		final ArrayList<Student> students = sparser.getStudents();
		final ArrayList<StudentGroup> studentGroups = sparser.getStudentGroups();
		for (final StudentGroup studentGroup : studentGroups) {
			if (!MysqlRequest.getIdPromotionRequest(studentGroup.getYear(), studentGroup.getStudentGroup()).isBeforeFirst()) {
				if (!MysqlRequest.getidClasseRequest(studentGroup.getStudentGroup()).isBeforeFirst()) {
					MysqlRequest.insertClasse(studentGroup.getStudentGroup());
				}
				final ResultSet rsClasse = MysqlRequest.getidClasseRequest(studentGroup.getStudentGroup());
				rsClasse.next();
				MysqlRequest.insertPromotion(studentGroup.getYear(), rsClasse.getInt("idClasse"));
			}
		}
		for (final Student student : students) {
			System.out.println(student.getStudentGroup().toString());
			System.out.println(student.getStudentGroup().getStudentGroup());
			final ResultSet rspromo = MysqlRequest.getIdPromotionRequest(student.getStudentGroup().getYear(), student.getStudentGroup().getStudentGroup());
			rspromo.next();
			final int idPromotion = rspromo.getInt("idPromotion");
			if (!MysqlRequest.getStudentByNum(student.getNumEtu()).isBeforeFirst()) {
				MysqlRequest.insertStudent(student.getNumEtu(), student.getPrenom(), student.getNom(), student.getEmail(), idPromotion, UUID.randomUUID().toString());
			}
			MysqlRequest.insertEvaluation(projectId, currentUser, student.getNumEtu(), idPromotion);
		}
	}
	
	/**
	 * Allows button to be clicked when data are entered to create a project
	 */
	private void updateCreateProjectButton() {
		createProjectButton.setDisable(projectNameField.getText().isEmpty() || deadlineDatePicker.getValue() == null || (String) studentListButton.getUserData() == null || (String) expectedOutputButton.getUserData() == null || argumentsField.getText().isEmpty());
	}
}
