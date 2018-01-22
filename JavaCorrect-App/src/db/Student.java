package db;

public class Student {
	private String nom;
	private String prenom;
	private int numEtu;
	private String classe;
	private int promo;
	
	Student(String nom,	String prenom, 	int numEtu , String classe,	int classYear){
		this.nom=nom;
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
