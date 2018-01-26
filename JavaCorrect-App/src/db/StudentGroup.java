package db;

public class StudentGroup {
	
	private final int year;
	private final String studentGroup;
	
	/**
	 * represents a group of student
	 * @param studentGroup : group name 
	 * @param year
	 */
	public StudentGroup(final String studentGroup, final int year) {
		this.studentGroup = studentGroup;
		this.year = year;
	}
	
	public int getYear() {
		return this.year;
	}

	public String getStudentGroup() {
		return this.studentGroup;
	}
}
