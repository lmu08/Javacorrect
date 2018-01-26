package db;

public class Student {
	private final String nom;
	private final String prenom;
	private final int numEtu;
	private final StudentGroup studentGroup;
	private final String email;
	
	/**
	 * represents a student 
	 * @param nom
	 * @param prenom
	 * @param numEtu
	 * @param email
	 * @param studentGroup represent studentGroup (year and name of the group)
	 */
	public Student(final String nom, final String prenom, final int numEtu, final String email, final StudentGroup studentGroup) {
		this.nom = nom;
		this.prenom = prenom;
		this.numEtu = numEtu;
		this.email = email;
		this.studentGroup = studentGroup;
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

	public StudentGroup getStudentGroup() {
		return this.studentGroup;
	}
	
	public String getEmail() {
		return this.email;
	}

}
