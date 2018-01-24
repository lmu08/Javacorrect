package db;

public class Classroom {

	private int year;
	private String classroom;
	
	public Classroom(final String classroom, final int year){
		this.classroom = classroom;
		this.year = year;
	}
	public int getYear() {
		return this.year;
	}
	
	public String getClassroom() {
		return this.classroom;
	}
}
