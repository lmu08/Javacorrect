package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StudentCsvParser {
	private static final String SEPARATOR = ",";
	private String line = "";
	private String className;
	private int classYear = -1;
	private StudentGroup studentGroup;
	private final ArrayList<Student> students;
	private final ArrayList<StudentGroup> studentGroups;

	public StudentCsvParser() {
		students = new ArrayList<>();
		studentGroups = new ArrayList<>();
	}

	/**
	 * Reads a CSV file.
	 * the 1st column contains the student numbers</br>
	 * the 2nd column contains the first name of the student at this line</br>
	 * the 3rd column contains the last name of the student at this line</br>
	 * the 4th column contains the class name of the student at this line</br>
	 * the 5th column contains the class year of the student at this line
	 * </br>
	 * All students with a different class name and class year from the first
	 * student in list are ignored.
	 *
	 * @param csvFile the file to read
	 */
	public void parse(final String csvFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				final String[] students = line.split(SEPARATOR);
				final int studentNum = Integer.parseInt(students[0]);
				final String studentFirstName = students[1];
				final String studentLastName = students[2];
				final String studentemail = students[3];
				final String currentClassName = students[4];
				final int currentClassYear = Integer.parseInt(students[5]);

				if (this.className == null) {
					this.className = currentClassName;
				}
				if (this.classYear == -1) {
					this.classYear = currentClassYear;
				}
				
				if (this.studentGroup == null) {
					this.studentGroup = new StudentGroup(this.className, this.classYear);
				}
				
				if (!this.className.equals(currentClassName) || this.classYear != currentClassYear) {
					this.className = currentClassName;
					this.classYear = currentClassYear;
					this.studentGroup = new StudentGroup(this.className, this.classYear);
					this.studentGroups.add(this.studentGroup);
				}
				this.students.add(new Student(studentLastName, studentFirstName, studentNum, studentemail, this.studentGroup));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Student> getStudents() {
		return students;
	}

	public ArrayList<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
}
