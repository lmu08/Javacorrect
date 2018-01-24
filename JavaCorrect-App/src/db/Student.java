package db;

public class Student {
	private final String nom;
	private final String prenom;
	private final int numEtu;
	private final String classe;
	private final int promo;

	public Student(final String nom, final String prenom, final int numEtu, final String classe, final int classYear) {
		this.nom = nom;
		this.prenom = prenom;
		this.numEtu = numEtu;
		this.classe = classe;
		this.promo = classYear;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public int getNumEtu() {
		return numEtu;
	}
	
	public String getClasse() {
		return classe;
	}
	
	public int getPromo() {
		return promo;
	}
	
}
