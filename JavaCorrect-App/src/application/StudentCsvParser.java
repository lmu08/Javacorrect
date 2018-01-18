package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCsvParser {
	
	private String line = "";
	private String cvsSplitBy = ",";
	private String csvFile;
	private String className = null;
	private int classYear = -1;
	private MysqlPropertiesParser properties;
	private boolean promotionChecked = false;
	private Connection mysqlco;
	private int idPromotion = -1;
	private String idProjet = null;
	
	public StudentCsvParser(final String csvFile) {
		this.csvFile = csvFile;
		try {
			this.properties = MysqlPropertiesParser.getInstance();
			this.mysqlco = MysqlConnexion.getInstance(this.properties);
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			
			while ((line = br.readLine()) != null) {
				
				/*
				 * Split csv columns into a String array
				 * students[0] represent the student number
				 * students[1] represents the first of the student at this line
				 * students[2] represents the last name of the student at this line
				 * students[3] represents the class name of the student at this line
				 * students[4] represents the class year of the student at this line
				 * all students with a different class name and class year from the first
				 * student in list are ignored. A warning is thrown
				 *
				 */
				final String[] students = line.split(cvsSplitBy);
				final int studentNum = Integer.parseInt(students[0]);
				final String studentFirstName = students[1];
				final String studentLastName = students[2];
				
				if (className == null) {
					this.className = students[3];
				}
				if (classYear == -1) {
					this.classYear = Integer.parseInt(students[4]);
				}
				if (!students[3].equals(this.className) || !(Integer.parseInt(students[4]) == this.classYear)) {
					System.err.println("Warning : l'étudiant " + studentFirstName + " " + studentLastName + " n'a pas pu être ajouté" + " car sa classe ou promotion est différente de celle des autres");
					System.out.println(this.classYear);
					continue;
				}
				try {
					if (!promotionChecked) {
						
						final ResultSet rspromo = MysqlRequest.getIdPromotionRequest(this.mysqlco, this.classYear, this.className);
						// if is not before first, then class name of classe year doesn't exists in database
						
						if (!rspromo.isBeforeFirst()) {
							ResultSet rsidClasse = MysqlRequest.getidClasseRequest(this.mysqlco, this.className);
							if (!rsidClasse.isBeforeFirst()) {
								MysqlRequest.insertClasse(this.mysqlco, this.className);
								rsidClasse = MysqlRequest.getidClasseRequest(this.mysqlco, this.className);
							}
							rsidClasse.next();
							final int idClasse = rsidClasse.getInt("idClasse");
							System.out.println('s');
							MysqlRequest.insertPromotion(this.mysqlco, this.classYear, idClasse);
						}
						rspromo.next();
						this.idPromotion = rspromo.getInt("idPromotion");
						this.promotionChecked = true;
					}
					final ResultSet rstudent = MysqlRequest.getStudentByNum(this.mysqlco, studentNum);
					if (!rstudent.isBeforeFirst()) {
						MysqlRequest.insertStudent(this.mysqlco, studentNum, studentFirstName, studentLastName, this.idPromotion);
					}
					
				}
				
				catch (final SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("SQLState: " + ex.toString());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
			}
			
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
