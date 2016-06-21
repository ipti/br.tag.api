package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Database;
import dao.TAGProject;
import dto.Classroom;
import dto.Credentials;
import dto.DisciplinesByClass;
import dto.Instructor;
import dto.InstructorTeachingData;
import dto.School;
import dto.Student;

public class TAGManager {

	/*
	 * Arrays que seram armazenados todos os estudantes, professores, escolas e
	 * turmas
	 */
	private ArrayList<Student> arrayStudent = new ArrayList<>();
	private ArrayList<Instructor> arrayInstructor = new ArrayList<>();
	private ArrayList<InstructorTeachingData> arrayInstructorTeachingData = new ArrayList<>();
	private ArrayList<Classroom> arrayClassroom = new ArrayList<>();
	private ArrayList<DisciplinesByClass> arrayDisciplinesByClass = new ArrayList<DisciplinesByClass>();
	private ArrayList<School> arraySchool = new ArrayList<>();
	private ArrayList<Credentials> arrayCredentials = new ArrayList<>();

	/*
	 * Criando o banco da classe criada Database para ter conex�o com o banco do
	 * tag
	 */
	private Database database = new Database();
	private Connection connection;
	TAGProject tagProject = new TAGProject();

	public ArrayList<Credentials> getCredentials() throws Exception {
		try {
			connection = database.getConnection();
			arrayCredentials = tagProject.getCredentials(connection);
		} catch (Exception e) {
			throw e;
		}
		return arrayCredentials;
	}

	public ArrayList<Credentials> getCredentials(String username, String password) throws Exception {
		try {
			connection = database.getConnection();
			arrayCredentials = tagProject.getCredentials(connection, username, password);
		} catch (Exception e) {
			throw e;
		}
		return arrayCredentials;
	}

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

	public ArrayList<Student> getStudentsPerClassroom(String classroom_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayStudent = tagProject.getStudentsPerClassroom(connection, classroom_id);
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

	public ArrayList<Student> getStudentsByID(String classroom_id, String id) throws Exception {
		try {
			connection = database.getConnection();
			arrayStudent = tagProject.getStudentsByID(connection, classroom_id, id);
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
	
	public ArrayList<InstructorTeachingData> getInstructorsByClassroom(String instructor_inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayInstructorTeachingData = tagProject.getInstructorsByClassroom(connection, instructor_inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arrayInstructorTeachingData;
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
	
	public ArrayList<Classroom> getClassroomsOfInstructor(String instructor_inep_id) throws Exception {
		try {
			connection = database.getConnection();
			arrayClassroom = tagProject.getClassroomsOfInstructor(connection, instructor_inep_id);
		} catch (Exception e) {
			throw e;
		}
		return arrayClassroom;
	}

	public ArrayList<Classroom> getClassroomsBySchoolInep(String school_inep_fk) throws Exception {
		try {
			connection = database.getConnection();
			arrayClassroom = tagProject.getClassroomsBySchoolInep(connection, school_inep_fk);
		} catch (Exception e) {
			throw e;
		}
		return arrayClassroom;
	}

	public ArrayList<DisciplinesByClass> getDisciplinesByClassID(String id) throws Exception {
		try {
			connection = database.getConnection();
			arrayDisciplinesByClass = tagProject.getDisciplinesByClassID(connection, id);
		} catch (Exception e) {
			throw e;
		}
		return arrayDisciplinesByClass;
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
