package db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentCsvParser {
	private static final String SEPARATOR = ",";
	private String line = "";
	private String className = null;
	private int classYear = -1;
	private ArrayList<Student> alStudent;
	
	public StudentCsvParser(){
		alStudent = new ArrayList<Student>();
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
				
				if (className == null) {
					className = students[3];
				}
				if (classYear == -1) {
					classYear = Integer.parseInt(students[4]);
				}
				if (!students[3].equals(className) || !(Integer.parseInt(students[4]) == classYear)) {
					System.err.println("Warning : l'étudiant " + studentFirstName + " " + studentLastName + " n'a pas pu être ajouté" + " car sa classe ou promotion est différente de celle des autres");
					System.out.println(classYear);
					continue;
				}
				alStudent.add(
				new Student(studentLastName, studentFirstName, studentNum, className, classYear)
				);
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Student> getStudents() {
		return this.alStudent;
	}
}
