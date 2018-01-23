package db;

public class Student {
	private final String nom;
	private final String prenom;
	private final int numEtu;
	private final Classroom classroom;
	private String email;

	public Student(final String nom, final String prenom, final int numEtu, final String email, final Classroom classroom) {
		this.nom = nom;
		this.prenom = prenom;
		this.numEtu = numEtu;
		this.email = email;
		this.classroom = classroom;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public String getPrenom() {
		return this.prenom;
	}
	
	public int getNumEtu() {
		return this.numEtu;
	}
	
	public Classroom getClassroom() {
		return this.classroom;
	}
	public String getEmail() {
		return this.email;
	}
	
}
