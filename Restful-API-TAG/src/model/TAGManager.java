package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Database;
import dao.TAGProject;
import dto.Classroom;
import dto.Instructor;
import dto.School;
import dto.Student;

public class TAGManager {

	/*
	 * Arrays que seram armazenados todos os estudantes, professores, escolas e
	 * turmas
	 */
	private ArrayList<Student> arrayStudent = new ArrayList<>();
	private ArrayList<Instructor> arrayInstructor = new ArrayList<>();
	private ArrayList<Classroom> arrayClassroom = new ArrayList<>();
	private ArrayList<School> arraySchool = new ArrayList<>();
	/*
	 * Criando o banco da classe criada Database para ter conex�o com o banco do
	 * tag
	 */
	private Database database = new Database();
	private Connection connection;
	TAGProject tagProject = new TAGProject();

	public ArrayList<Student> getStudents() throws Exception {
		try {
			connection = database.getConnection();
			arrayStudent = tagProject.getStudents(connection);
		} catch (Exception e) {
			throw e;
		}
		return arrayStudent;
	}

	public ArrayList<Student> getStudents(String inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayStudent = tagProject.getStudents(connection, inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arrayStudent;
	}
	
	public ArrayList<Student> getStudentsByName(String name) throws Exception {
		try {
			connection = database.getConnection();
			arrayStudent = tagProject.getStudentsByName(connection, name);
		} catch (Exception e) {
			throw e;
		}
		return arrayStudent;
	}

	public ArrayList<Instructor> getInstructors() throws Exception {
		try {
			connection = database.getConnection();
			arrayInstructor = tagProject.getInstructors(connection);
		} catch (Exception e) {
			throw e;
		}
		return arrayInstructor;
	}

	public ArrayList<Instructor> getInstructors(String inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayInstructor = tagProject.getInstructors(connection, inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arrayInstructor;
	}

	public ArrayList<Classroom> getClassrooms() throws Exception {
		try {
			connection = database.getConnection();
			arrayClassroom = tagProject.getClassrooms(connection);
		} catch (Exception e) {
			throw e;
		}
		return arrayClassroom;
	}

	public ArrayList<Classroom> getClassrooms(String inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayClassroom = tagProject.getClassrooms(connection, inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arrayClassroom;
	}

	public ArrayList<School> getSchools() throws Exception {
		try {
			connection = database.getConnection();
			arraySchool = tagProject.getSchools(connection);
		} catch (Exception e) {
			throw e;
		}
		return arraySchool;
	}

	public ArrayList<School> getSchools(String inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arraySchool = tagProject.getSchools(connection, inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arraySchool;
	}
}
