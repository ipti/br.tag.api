package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import dto.*;

public class TAGProject {

	private ArrayList<Credentials> arrayCredentials = new ArrayList<>();
	private ArrayList<CredentialsReturn> arrayCredentialsReturn = new ArrayList<>();
	private ArrayList<Login> arrayLogin = new ArrayList<>();
	private ArrayList<LoginReturn> arrayLoginReturn = new ArrayList<>();

	private ArrayList<UserInfo> arrayUserInfo = new ArrayList<>();
	private ArrayList<UserInfoReturn> arrayUserInfoReturn = new ArrayList<>();

	private ArrayList<Student> arrayStudent = new ArrayList<>();
	private ArrayList<StudentReturn> arrayStudentReturn = new ArrayList<>();

	private ArrayList<SchoolReport> arraySchoolReport = new ArrayList<>();
	private ArrayList<SchoolReportReturn> arraySchoolReportReturn = new ArrayList<>();

	private ArrayList<Instructor> arrayIntructor = new ArrayList<>();
	private ArrayList<InstructorReturn> arrayIntructorReturn = new ArrayList<>();
	private ArrayList<InstructorTeachingData> arrayInstructorTeachingData = new ArrayList<>();
	private ArrayList<InstructorTeachingDataReturn> arrayInstructorTeachingDataReturn = new ArrayList<>();

	private ArrayList<Classroom> arrayClassroom = new ArrayList<>();
	private ArrayList<ClassroomReturn> arrayClassroomReturn = new ArrayList<>();

	private ArrayList<DisciplinesByClass> arrayDisciplinesByClass = new ArrayList<>();
	private ArrayList<DisciplinesByClassReturn> arrayDisciplinesByClassReturn = new ArrayList<>();

	private ArrayList<School> arraySchool = new ArrayList<>();
	private ArrayList<SchoolReturn> arraySchoolReturn = new ArrayList<>();

	private ArrayList<Grade> arrayGrade = new ArrayList<>();
	private ArrayList<GradeReturn> arrayGradeReturn = new ArrayList<>();

	// ------------- METODOS AUXILIARES -----------//
	// Usando MD5 para criptografar
	private static String getPasswordEncrypted(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashText = number.toString(16);
			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}
			return hashText;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	// --------------- LOGIN ------------------ //
	public ArrayList<LoginReturn> getLogin(Connection connection, String username, String password) throws Exception {
		try {
			String passwordEncrypted = getPasswordEncrypted(password);
			String sql = "SELECT * FROM login WHERE username = '" + username + "' AND password = '" + passwordEncrypted
					+ "';";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Login login = new Login();

				login.setId(rs.getString("id"));
				login.setUsername(rs.getString("username"));

				arrayLogin.add(login);
			}

			LoginReturn loginReturn = new LoginReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayLogin.size() > 0) {
				loginReturn.setValid(true);
				loginReturn.setError(error);
				loginReturn.setLogin(arrayLogin);
			} else {
				error.add("Erro ao fazer login!");

				loginReturn.setValid(false);
				loginReturn.setError(error);
				loginReturn.setLogin(null);
			}

			arrayLoginReturn.add(loginReturn);

			return arrayLoginReturn;
		} catch (Exception e) {
			LoginReturn loginReturn = new LoginReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			loginReturn.setValid(false);
			loginReturn.setError(error);
			loginReturn.setLogin(null);

			arrayLoginReturn.add(loginReturn);
			e.printStackTrace();
			return arrayLoginReturn;
		}
	}

	public ArrayList<CredentialsReturn> getCredentials(Connection connection, String username, String password)
			throws Exception {
		try {
			String passwordEncrypted = getPasswordEncrypted(password);
			String sql = "SELECT id, name, instructor_fk, username, password, type FROM users " + "WHERE username = '"
					+ username + "' AND password = '" + passwordEncrypted + "';";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Credentials credentials = new Credentials();

				credentials.setId(rs.getString("id"));
				credentials.setName(rs.getString("name"));
				credentials.setInstructor_fk(rs.getString("instructor_fk"));
				credentials.setUsername(rs.getString("username"));
				credentials.setPassword(null);
				credentials.setType(rs.getString("type"));

				arrayCredentials.add(credentials);
			}

			CredentialsReturn credentialsReturn = new CredentialsReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayCredentials.size() > 0) {
				credentialsReturn.setValid(true);
				credentialsReturn.setError(error);
				credentialsReturn.setCredentials(arrayCredentials);
			} else {
				error.add("Erro ao fazer login!");

				credentialsReturn.setValid(false);
				credentialsReturn.setError(error);
				credentialsReturn.setCredentials(null);
			}

			arrayCredentialsReturn.add(credentialsReturn);
			return arrayCredentialsReturn;
		} catch (Exception e) {
			CredentialsReturn credentialsReturn = new CredentialsReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			credentialsReturn.setValid(false);
			credentialsReturn.setError(error);
			credentialsReturn.setCredentials(null);

			arrayCredentialsReturn.add(credentialsReturn);
			e.printStackTrace();
			return arrayCredentialsReturn;
		}
	}

	// -------------- USER -----------------//
	public ArrayList<UserInfoReturn> getUserInfo(Connection connection, String username) throws Exception {
		try {
			String sql = "SELECT filiation_1, filiation_2, responsable, responsable_cpf, "
					+ "responsable_rg, responsable_job, responsable_telephone FROM student_identification WHERE responsable_cpf = '"
					+ username + "' LIMIT 1;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserInfo userInfo = new UserInfo();

				if (rs.getString("responsable").equals("0")) {
					userInfo.setName(rs.getString("filiation_1"));
				} else if (rs.getString("responsable").equals("1")) {
					userInfo.setName(rs.getString("filiation_2"));
				}
				userInfo.setCpf(rs.getString("responsable_cpf"));
				userInfo.setRg(rs.getString("responsable_rg"));
				if (rs.getString("responsable_telephone") == null) {
					userInfo.setPhone("N�o possui");
				} else {
					userInfo.setPhone(rs.getString("responsable_telephone"));
				}
				userInfo.setJob(rs.getString("responsable_job"));

				arrayUserInfo.add(userInfo);
			}

			UserInfoReturn userInfoReturn = new UserInfoReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayUserInfo.size() > 0) {
				userInfoReturn.setValid(true);
				userInfoReturn.setError(error);
				userInfoReturn.setUser(arrayUserInfo);
			} else {
				error.add("Erro ao buscar informa��es");

				userInfoReturn.setValid(false);
				userInfoReturn.setError(error);
				userInfoReturn.setUser(null);
			}

			arrayUserInfoReturn.add(userInfoReturn);

			return arrayUserInfoReturn;
		} catch (Exception e) {
			UserInfoReturn userInfoReturn = new UserInfoReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			userInfoReturn.setValid(false);
			userInfoReturn.setError(error);
			userInfoReturn.setUser(null);

			arrayUserInfoReturn.add(userInfoReturn);
			e.printStackTrace();
			return arrayUserInfoReturn;
		}
	}

	// --------------- STUDENT ------------------ //
	public ArrayList<StudentReturn> getChildrenPerParent(Connection connection, String responsable_cpf)
			throws Exception {
		try {
			String sql = "SELECT S.*, EC.*, EU.*, EN.* FROM student_identification S "
					+ "JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id WHERE S.responsable_cpf = '" + responsable_cpf
					+ "' ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("Esse respons�vel n�o possui filho!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}
	}

	// REFAZER PARA IGUALAR COM A CLASSE QUE TA SENDO USADA NO APP
	public ArrayList<SchoolReportReturn> getStudentParent(Connection connection, String responsable_cpf)
			throws Exception {
		try {
			String sql = "SELECT CE.year FROM student_identification S JOIN classroom_enrollment CE ON S.id = CE.enrollment WHERE S.responsable_cpf = '"
					+ responsable_cpf + "' GROUP BY CE.year;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				SchoolReport schoolReport = new SchoolReport();
				ArrayList<StudentReport> studentReports = new ArrayList<>();
				String year = rs.getString("CE.year");
				schoolReport.setYear(year);

				String sqlChildren = "SELECT S.id, S.name, CE.classroom_id, C.name "
						+ "FROM student_identification S JOIN classroom_enrollment CE ON CE.enrollment = S.id "
						+ "JOIN classroom C ON C.id = CE.classroom_id WHERE responsable_cpf = '" + responsable_cpf
						+ "' AND CE.year = '" + year + "';";
				PreparedStatement psChildren = connection.prepareStatement(sqlChildren);
				ResultSet rsChildren = psChildren.executeQuery();

				while (rsChildren.next()) {
					StudentReport studentReport = new StudentReport();

					studentReport.setId(rsChildren.getString("S.id"));
					studentReport.setName(rsChildren.getString("S.name"));
					studentReport.setClassroom_id(rsChildren.getString("CE.classroom_id"));
					studentReport.setClassroom_name(rsChildren.getString("C.name"));

					int current_year = Calendar.getInstance().get(Calendar.YEAR);
					if (Integer.valueOf(year) < current_year) {
						studentReport.setSituation("Conclu�do");
					} else {
						studentReport.setSituation("Cursando");
					}

					studentReports.add(studentReport);
				}

				schoolReport.setStudents(studentReports);

				arraySchoolReport.add(schoolReport);
			}

			SchoolReportReturn schoolReportReturn = new SchoolReportReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arraySchoolReport.size() > 0) {
				schoolReportReturn.setValid(true);
				schoolReportReturn.setError(error);
				schoolReportReturn.setSchoolReports(arraySchoolReport);
			} else {
				error.add("Esse respons�vel n�o possui filho!");

				schoolReportReturn.setValid(false);
				schoolReportReturn.setError(error);
				schoolReportReturn.setSchoolReports(null);
			}

			arraySchoolReportReturn.add(schoolReportReturn);
			return arraySchoolReportReturn;
		} catch (Exception e) {
			SchoolReportReturn schoolReportReturn = new SchoolReportReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			schoolReportReturn.setValid(true);
			schoolReportReturn.setError(error);
			schoolReportReturn.setSchoolReports(null);

			arraySchoolReportReturn.add(schoolReportReturn);
			e.printStackTrace();

			return arraySchoolReportReturn;
		}
	}

	public ArrayList<StudentReturn> getStudentsPerClassroom(Connection connection, String classroom_id)
			throws Exception {
		try {
			String sql = "SELECT S.*, EC.*, EU.*, EN.* FROM student_identification S "
					+ "JOIN student_enrollment SE ON S.id = SE.student_fk "
					+ "JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id WHERE SE.classroom_fk= '" + classroom_id
					+ "' ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("Esse turma n�o possui alunos!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}
	}

	public ArrayList<StudentReturn> getStudentsByName(Connection connection, String name) throws Exception {
		try {
			String sql = "SELECT S.*, EC.*, EU.*, EN.* "
					+ "FROM student_identification S JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id" + "WHERE S.name LIKE '%" + name
					+ "%' ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("N�o existe aluno com esse nome!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}
	}

	public ArrayList<StudentReturn> getStudents(Connection connection, String inep_id) throws Exception {
		try {
			String sql = "SELECT S.*, EC.*, EU.*, EN.* "
					+ "FROM student_identification S JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id WHERE S.inep_id = '" + inep_id
					+ "' ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("N�o existe aluno com esse inep_id!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}
	}

	public ArrayList<StudentReturn> getStudentsByID(Connection connection, String classroom_id, String id)
			throws Exception {
		try {
			String sql = "SELECT S.*, EC.*, EU.*, EN.* "
					+ "FROM student_identification S JOIN student_enrollment SE ON S.id = SE.student_fk "
					+ "JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id WHERE S.id= '" + id
					+ "' AND SE.classroom_fk = '" + classroom_id + "' ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("N�o existe aluno com esse id na turma com esse classroom_id!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}

	}

	public ArrayList<StudentReturn> getStudents(Connection connection) throws Exception {
		try {
			String sql = "SELECT DISTINCT S.*, EC.*, EU.*, EN.* FROM student_identification S JOIN student_enrollment SE ON S.id = SE.student_fk "
					+ "JOIN edcenso_nation EN ON S.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON S.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON S.edcenso_city_fk = EC.id ORDER BY S.id;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();

				student.setRegister_type(rs.getString("S.register_type"));
				student.setSchool_inep_id_fk(rs.getString("S.school_inep_id_fk"));
				if (rs.getString("S.inep_id") == null || rs.getString("S.inep_id").equals("")) {
					student.setInep_id("N�o foi informado");
				} else {
					student.setInep_id(rs.getString("S.inep_id"));
				}
				student.setId(rs.getString("S.id"));
				student.setName(rs.getString("S.name"));
				student.setBirthday(rs.getString("S.birthday"));
				if (rs.getString("S.sex").equals("1")) {
					student.setSex("M");
				} else {
					student.setSex("F");
				}
				if (rs.getString("S.color_race").equals("0")) {
					student.setColor_race("N�o declarada");
				} else if (rs.getString("S.color_race").equals("1")) {
					student.setColor_race("Branca");
				} else if (rs.getString("S.color_race").equals("2")) {
					student.setColor_race("Preta");
				} else if (rs.getString("S.color_race").equals("3")) {
					student.setColor_race("Parda");
				} else if (rs.getString("S.color_race").equals("4")) {
					student.setColor_race("Amarela");
				} else if (rs.getString("S.color_race").equals("5")) {
					student.setColor_race("Ind�gena");
				}
				if (rs.getString("S.filiation") == null || rs.getString("S.filiation").equals("0")) {
					student.setFiliation("N�o declarado/Ignorado");
				} else {
					student.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("S.filiation_1") == null || rs.getString("S.filiation_1").equals("0")) {
					student.setFiliation_1("N�o declarado/Ignorado");
				} else {
					student.setFiliation_1(rs.getString("S.filiation_1"));
				}
				if (rs.getString("S.filiation_2") == null || rs.getString("S.filiation_2").equals("0")) {
					student.setFiliation_2("N�o declarado/Ignorado");
				} else {
					student.setFiliation_2(rs.getString("S.filiation_2"));
				}
				if (rs.getString("S.nationality").equals("1")) {
					student.setNationality("Brasileira");
				} else if (rs.getString("S.nationality").equals("2")) {
					student.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("S.nationality").equals("3")) {
					student.setNationality("Estrangeira");
				}
				if (rs.getString("S.edcenso_nation_fk").equals(rs.getString("EN.id"))) {
					student.setEdcenso_nation_fk(rs.getString("EN.name"));
				}
				if (rs.getString("S.edcenso_uf_fk").equals(rs.getString("EU.id"))) {
					student.setEdcenso_uf_fk(rs.getString("EU.name"));
				}
				if (rs.getString("S.edcenso_city_fk").equals(rs.getString("EC.id"))) {
					student.setEdcenso_city_fk(rs.getString("EC.name"));
				}
				if (rs.getString("S.deficiency") == null || rs.getString("S.deficiency").equals("0")) {
					student.setDeficiency("N�o");
				} else {
					student.setDeficiency("Sim");
				}
				if (rs.getString("S.deficiency_type_blindness") == null
						|| rs.getString("S.deficiency_type_blindness").equals("0")) {
					student.setDeficiency_type_blindness("N�o");
				} else {
					student.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("S.deficiency_type_low_vision") == null
						|| rs.getString("S.deficiency_type_low_vision").equals("0")) {
					student.setDeficiency_type_low_vision("N�o");
				} else {
					student.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("S.deficiency_type_deafness") == null
						|| rs.getString("S.deficiency_type_deafness").equals("0")) {
					student.setDeficiency_type_deafness("N�o");
				} else {
					student.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("S.deficiency_type_disability_hearing") == null
						|| rs.getString("S.deficiency_type_disability_hearing").equals("0")) {
					student.setDeficiency_type_disability_hearing("N�o");
				} else {
					student.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("S.deficiency_type_deafblindness") == null
						|| rs.getString("S.deficiency_type_deafblindness").equals("0")) {
					student.setDeficiency_type_deafblindness("N�o");
				} else {
					student.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("S.deficiency_type_phisical_disability") == null
						|| rs.getString("S.deficiency_type_phisical_disability").equals("0")) {
					student.setDeficiency_type_phisical_disability("N�o");
				} else {
					student.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_intelectual_disability") == null
						|| rs.getString("S.deficiency_type_intelectual_disability").equals("0")) {
					student.setDeficiency_type_intelectual_disability("N�o");
				} else {
					student.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("S.deficiency_type_multiple_disabilities") == null
						|| rs.getString("S.deficiency_type_multiple_disabilities").equals("0")) {
					student.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					student.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("S.deficiency_type_autism") == null
						|| rs.getString("S.deficiency_type_autism").equals("0")) {
					student.setDeficiency_type_autism("N�o");
				} else {
					student.setDeficiency_type_autism("Sim");
				}
				if (rs.getString("S.deficiency_type_aspenger_syndrome") == null
						|| rs.getString("S.deficiency_type_aspenger_syndrome").equals("0")) {
					student.setDeficiency_type_aspenger_syndrome("N�o");
				} else {
					student.setDeficiency_type_aspenger_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_rett_syndrome") == null
						|| rs.getString("S.deficiency_type_rett_syndrome").equals("0")) {
					student.setDeficiency_type_rett_syndrome("N�o");
				} else {
					student.setDeficiency_type_rett_syndrome("Sim");
				}
				if (rs.getString("S.deficiency_type_childhood_disintegrative_disorder") == null
						|| rs.getString("S.deficiency_type_childhood_disintegrative_disorder").equals("0")) {
					student.setDeficiency_type_childhood_disintegrative_disorder("N�o");
				} else {
					student.setDeficiency_type_childhood_disintegrative_disorder("Sim");
				}
				if (rs.getString("S.deficiency_type_gifted") == null
						|| rs.getString("S.deficiency_type_gifted").equals("0")) {
					student.setDeficiency_type_gifted("N�o");
				} else {
					student.setDeficiency_type_gifted("Sim");
				}
				if (rs.getString("S.resource_aid_lector") == null || rs.getString("S.resource_aid_lector").equals("")
						|| rs.getString("S.resource_aid_lector").equals("0")) {
					student.setResource_aid_lector("N�o");
				} else {
					student.setResource_aid_lector("Sim");
				}
				if (rs.getString("S.resource_aid_transcription") == null
						|| rs.getString("S.resource_aid_transcription").equals("")
						|| rs.getString("S.resource_aid_transcription").equals("0")) {
					student.setResource_aid_transcription("N�o");
				} else {
					student.setResource_aid_transcription("Sim");
				}
				if (rs.getString("S.resource_interpreter_guide") == null
						|| rs.getString("S.resource_interpreter_guide").equals("")
						|| rs.getString("S.resource_interpreter_guide").equals("0")) {
					student.setResource_interpreter_guide("N�o");
				} else {
					student.setResource_interpreter_guide("Sim");
				}
				if (rs.getString("S.resource_interpreter_libras") == null
						|| rs.getString("S.resource_interpreter_libras").equals("")
						|| rs.getString("S.resource_interpreter_libras").equals("0")) {
					student.setResource_interpreter_libras("N�o");
				} else {
					student.setResource_interpreter_libras("Sim");
				}
				if (rs.getString("S.resource_lip_reading") == null || rs.getString("S.resource_lip_reading").equals("")
						|| rs.getString("S.resource_lip_reading").equals("0")) {
					student.setResource_lip_reading("N�o");
				} else {
					student.setResource_lip_reading("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_16") == null
						|| rs.getString("S.resource_zoomed_test_16").equals("")
						|| rs.getString("S.resource_zoomed_test_16").equals("0")) {
					student.setResource_zoomed_test_16("N�o");
				} else {
					student.setResource_zoomed_test_16("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_20") == null
						|| rs.getString("S.resource_zoomed_test_20").equals("")
						|| rs.getString("S.resource_zoomed_test_20").equals("0")) {
					student.setResource_zoomed_test_20("N�o");
				} else {
					student.setResource_zoomed_test_20("Sim");
				}
				if (rs.getString("S.resource_zoomed_test_24") == null
						|| rs.getString("S.resource_zoomed_test_24").equals("")
						|| rs.getString("S.resource_zoomed_test_24").equals("0")) {
					student.setResource_zoomed_test_24("N�o");
				} else {
					student.setResource_zoomed_test_24("Sim");
				}
				if (rs.getString("S.resource_braille_test") == null
						|| rs.getString("S.resource_braille_test").equals("")
						|| rs.getString("S.resource_braille_test").equals("0")) {
					student.setResource_braille_test("N�o");
				} else {
					student.setResource_braille_test("Sim");
				}
				if (rs.getString("S.resource_none") == null || rs.getString("S.resource_none").equals("")
						|| rs.getString("S.resource_none").equals("0")) {
					student.setResource_none("N�o");
				} else {
					student.setResource_none("Sim");
				}
				student.setSend_year(rs.getString("send_year"));
				if (rs.getString("S.last_change") == null) {
					student.setLast_change("Nunca houve modifica��es");
				} else {
					student.setLast_change(rs.getString("S.last_change"));
				}
				if (rs.getString("S.responsable") == null) {
					student.setResponsable("N�o foi selecionado");
				} else if (rs.getString("S.responsable").equals("0")) {
					student.setResponsable("Pai");
				} else if (rs.getString("S.responsable").equals("1")) {
					student.setResponsable("M�e");
				} else if (rs.getString("S.responsable").equals("2")) {
					student.setResponsable("Outro");
				}
				if (rs.getString("S.responsable_name") == null || rs.getString("S.responsable_name").equals("")) {
					student.setResponsable_name("N�o foi informado");
				} else {
					student.setResponsable_name(rs.getString("S.responsable_name"));
				}
				if (rs.getString("S.responsable_rg") == null || rs.getString("S.responsable_rg").equals("")) {
					student.setResponsable_rg("N�o foi informado");
				} else {
					student.setResponsable_rg(rs.getString("S.responsable_rg"));
				}
				if (rs.getString("S.responsable_cpf") == null || rs.getString("S.responsable_cpf").equals("")) {
					student.setResponsable_cpf("N�o foi informado");
				} else {
					student.setResponsable_cpf(rs.getString("S.responsable_cpf"));
				}
				if (rs.getString("S.responsable_scholarity") == null) {
					student.setResponsable_scholarity("N�o foi informado");
				} else if (rs.getString("S.responsable_scholarity").equals("0")) {
					student.setResponsable_scholarity("N�o sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("1")) {
					student.setResponsable_scholarity("Sabe ler e escrever");
				} else if (rs.getString("S.responsable_scholarity").equals("2")) {
					student.setResponsable_scholarity("Ens. Fund. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("3")) {
					student.setResponsable_scholarity("Ens. Fund. Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("4")) {
					student.setResponsable_scholarity("Ens. M�dio Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("5")) {
					student.setResponsable_scholarity("Ens. M�dio Completo");
				} else if (rs.getString("S.responsable_scholarity").equals("6")) {
					student.setResponsable_scholarity("Ens. Sup. Incompleto");
				} else if (rs.getString("S.responsable_scholarity").equals("7")) {
					student.setResponsable_scholarity("Ens. Sup. Completo");
				}
				if (rs.getString("S.responsable_job") == null || rs.getString("S.responsable_job").equals("")) {
					student.setResponsable_job("N�o foi informado");
				} else {
					student.setResponsable_job(rs.getString("S.responsable_job"));
				}
				if (rs.getString("S.bf_participator") == null || rs.getString("S.bf_participator").equals("")) {
					student.setBf_participator("N�o foi informado");
				} else if (rs.getString("S.bf_participator").equals("0")) {
					student.setBf_participator("N�o");
				} else {
					student.setBf_participator("Sim");
				}
				if (rs.getString("S.food_restrictions") == null || rs.getString("S.food_restrictions").equals("")) {
					student.setFood_restrictions("N�o foi informado");
				} else {
					student.setFood_restrictions(rs.getString("S.food_restrictions"));
				}
				if (rs.getString("S.responsable_telephone") == null
						|| rs.getString("S.responsable_telephone").equals("")) {
					student.setResponsable_telephone("N�o foi informado");
				} else {
					student.setResponsable_telephone(rs.getString("S.responsable_telephone"));
				}
				if (rs.getString("S.fkid") == null || rs.getString("S.fkid").equals("")) {
					student.setFkid("N�o foi informado");
				} else {
					student.setFkid(rs.getString("S.fkid"));
				}

				arrayStudent.add(student);
			}

			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayStudent.size() > 0) {
				studentReturn.setValid(true);
				studentReturn.setError(error);
				studentReturn.setStudent(arrayStudent);
			} else {
				error.add("N�o existe alunos cadastrados!");

				studentReturn.setValid(false);
				studentReturn.setError(error);
				studentReturn.setStudent(null);
			}

			arrayStudentReturn.add(studentReturn);
			return arrayStudentReturn;
		} catch (Exception e) {
			StudentReturn studentReturn = new StudentReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			studentReturn.setValid(true);
			studentReturn.setError(error);
			studentReturn.setStudent(null);

			arrayStudentReturn.add(studentReturn);
			e.printStackTrace();
			return arrayStudentReturn;
		}
	}

	// --------------- INSTRUCTOR ------------------ //
	public ArrayList<InstructorReturn> getInstructors(Connection connection) throws Exception {
		try {
			String sql = "SELECT I.*, EC.*, EU.*, EN.* FROM instructor_identification I "
					+ "JOIN edcenso_nation EN ON I.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON I.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON I.edcenso_city_fk = EC.id ORDER BY I.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Instructor instructor = new Instructor();

				instructor.setRegister_type(rs.getString("I.register_type"));
				instructor.setSchool_inep_id_fk(rs.getString("I.school_inep_id_fk"));
				if (rs.getString("I.inep_id") == null || rs.getString("I.inep_id").equals("")) {
					instructor.setInep_id("N�o foi informado");
				} else {
					instructor.setInep_id(rs.getString("I.inep_id"));
				}
				instructor.setName(rs.getString("I.name"));
				instructor.setEmail(rs.getString("I.email"));
				if (rs.getString("I.nis") == null || rs.getString("I.nis").equals("")) {
					instructor.setNis("N�o foi informado");
				} else {
					instructor.setNis(rs.getString("I.nis"));
				}
				instructor.setBirthday_date(rs.getString("I.birthday_date"));
				if (rs.getString("I.sex").equals("1")) {
					instructor.setSex("M");
				} else {
					instructor.setSex("F");
				}
				if (rs.getString("I.color_race").equals("0")) {
					instructor.setColor_race("N�o declarada");
				} else if (rs.getString("I.color_race").equals("1")) {
					instructor.setColor_race("Branca");
				} else if (rs.getString("I.color_race").equals("2")) {
					instructor.setColor_race("Preta");
				} else if (rs.getString("I.color_race").equals("3")) {
					instructor.setColor_race("Parda");
				} else if (rs.getString("I.color_race").equals("4")) {
					instructor.setColor_race("Amarela");
				} else if (rs.getString("I.color_race").equals("5")) {
					instructor.setColor_race("Ind�gena");
				}
				if (rs.getString("I.filiation") == null || rs.getString("I.filiation").equals("0")) {
					instructor.setFiliation("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("I.filiation_1") == null || rs.getString("I.filiation_1").equals("0")) {
					instructor.setFiliation_1("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_1(rs.getString("I.filiation_1"));
				}
				if (rs.getString("I.filiation_2") == null || rs.getString("I.filiation_2").equals("0")) {
					instructor.setFiliation_2("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_2(rs.getString("I.filiation_2"));
				}
				if (rs.getString("I.nationality").equals("1")) {
					instructor.setNationality("Brasileira");
				} else if (rs.getString("I.nationality").equals("2")) {
					instructor.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("I.nationality").equals("3")) {
					instructor.setNationality("Estrangeira");
				}
				if (rs.getString("I.edcenso_nation_fk").equals(rs.getString("I.edcenso_nation.id"))) {
					instructor.setEdcenso_nation_fk(rs.getString("I.edcenso_nation.name"));
				}
				if (rs.getString("I.edcenso_uf_fk").equals(rs.getString("I.edcenso_uf.id"))) {
					instructor.setEdcenso_uf_fk(rs.getString("I.edcenso_uf.name"));
				}
				if (rs.getString("I.edcenso_city_fk").equals(rs.getString("I.edcenso_city.id"))) {
					instructor.setEdcenso_city_fk(rs.getString("I.edcenso_city.name"));
				}
				if (rs.getString("I.deficiency").equals("0")) {
					instructor.setDeficiency("N�o");
				} else {
					instructor.setDeficiency("Sim");
				}
				if (rs.getString("I.deficiency_type_blindness") == null
						|| rs.getString("I.deficiency_type_blindness").equals("0")) {
					instructor.setDeficiency_type_blindness("N�o");
				} else {
					instructor.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("I.deficiency_type_low_vision") == null
						|| rs.getString("I.deficiency_type_low_vision").equals("0")) {
					instructor.setDeficiency_type_low_vision("N�o");
				} else {
					instructor.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("I.deficiency_type_deafness") == null
						|| rs.getString("I.deficiency_type_deafness").equals("0")) {
					instructor.setDeficiency_type_deafness("N�o");
				} else {
					instructor.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("I.deficiency_type_disability_hearing") == null
						|| rs.getString("I.deficiency_type_disability_hearing").equals("0")) {
					instructor.setDeficiency_type_disability_hearing("N�o");
				} else {
					instructor.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("I.deficiency_type_deafblindness") == null
						|| rs.getString("I.deficiency_type_deafblindness").equals("0")) {
					instructor.setDeficiency_type_deafblindness("N�o");
				} else {
					instructor.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("I.deficiency_type_phisical_disability") == null
						|| rs.getString("I.deficiency_type_phisical_disability").equals("0")) {
					instructor.setDeficiency_type_phisical_disability("N�o");
				} else {
					instructor.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_intelectual_disability") == null
						|| rs.getString("I.deficiency_type_intelectual_disability").equals("0")) {
					instructor.setDeficiency_type_intelectual_disability("N�o");
				} else {
					instructor.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_multiple_disabilities") == null
						|| rs.getString("I.deficiency_type_multiple_disabilities").equals("0")) {
					instructor.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					instructor.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("I.fkid") == null || rs.getString("I.fkid").equals("")) {
					instructor.setFkid("N�o foi informado");
				} else {
					instructor.setFkid(rs.getString("I.fkid"));
				}
				arrayIntructor.add(instructor);
			}

			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayIntructor.size() > 0) {
				instructorReturn.setValid(true);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(arrayIntructor);
			} else {
				error.add("N�o existe professor cadastrado!");

				instructorReturn.setValid(false);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(null);
			}

			arrayIntructorReturn.add(instructorReturn);

			return arrayIntructorReturn;
		} catch (Exception e) {
			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			instructorReturn.setValid(false);
			instructorReturn.setError(error);
			instructorReturn.setInstructor(null);

			arrayIntructorReturn.add(instructorReturn);
			e.printStackTrace();
			return arrayIntructorReturn;
		}
	}

	public ArrayList<InstructorReturn> getInstructor(Connection connection, String inep_id) throws Exception {
		try {
			String sql = "SELECT I.*, EC.*, EU.*, EN.* FROM instructor_identification I "
					+ "JOIN edcenso_nation EN ON I.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON I.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON I.edcenso_city_fk = EC.id WHERE I.inep_id = '" + inep_id
					+ "' ORDER BY I.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Instructor instructor = new Instructor();

				instructor.setRegister_type(rs.getString("I.register_type"));
				instructor.setSchool_inep_id_fk(rs.getString("I.school_inep_id_fk"));
				if (rs.getString("I.inep_id") == null || rs.getString("I.inep_id").equals("")) {
					instructor.setInep_id("N�o foi informado");
				} else {
					instructor.setInep_id(rs.getString("I.inep_id"));
				}
				instructor.setName(rs.getString("I.name"));
				instructor.setEmail(rs.getString("I.email"));
				if (rs.getString("I.nis") == null || rs.getString("I.nis").equals("")) {
					instructor.setNis("N�o foi informado");
				} else {
					instructor.setNis(rs.getString("I.nis"));
				}
				instructor.setBirthday_date(rs.getString("I.birthday_date"));
				if (rs.getString("I.sex").equals("1")) {
					instructor.setSex("M");
				} else {
					instructor.setSex("F");
				}
				if (rs.getString("I.color_race").equals("0")) {
					instructor.setColor_race("N�o declarada");
				} else if (rs.getString("I.color_race").equals("1")) {
					instructor.setColor_race("Branca");
				} else if (rs.getString("I.color_race").equals("2")) {
					instructor.setColor_race("Preta");
				} else if (rs.getString("I.color_race").equals("3")) {
					instructor.setColor_race("Parda");
				} else if (rs.getString("I.color_race").equals("4")) {
					instructor.setColor_race("Amarela");
				} else if (rs.getString("I.color_race").equals("5")) {
					instructor.setColor_race("Ind�gena");
				}
				if (rs.getString("I.filiation") == null || rs.getString("I.filiation").equals("0")) {
					instructor.setFiliation("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("I.filiation_1") == null || rs.getString("I.filiation_1").equals("0")) {
					instructor.setFiliation_1("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_1(rs.getString("I.filiation_1"));
				}
				if (rs.getString("I.filiation_2") == null || rs.getString("I.filiation_2").equals("0")) {
					instructor.setFiliation_2("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_2(rs.getString("I.filiation_2"));
				}
				if (rs.getString("I.nationality").equals("1")) {
					instructor.setNationality("Brasileira");
				} else if (rs.getString("I.nationality").equals("2")) {
					instructor.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("I.nationality").equals("3")) {
					instructor.setNationality("Estrangeira");
				}
				if (rs.getString("I.edcenso_nation_fk").equals(rs.getString("I.edcenso_nation.id"))) {
					instructor.setEdcenso_nation_fk(rs.getString("I.edcenso_nation.name"));
				}
				if (rs.getString("I.edcenso_uf_fk").equals(rs.getString("I.edcenso_uf.id"))) {
					instructor.setEdcenso_uf_fk(rs.getString("I.edcenso_uf.name"));
				}
				if (rs.getString("I.edcenso_city_fk").equals(rs.getString("I.edcenso_city.id"))) {
					instructor.setEdcenso_city_fk(rs.getString("I.edcenso_city.name"));
				}
				if (rs.getString("I.deficiency").equals("0")) {
					instructor.setDeficiency("N�o");
				} else {
					instructor.setDeficiency("Sim");
				}
				if (rs.getString("I.deficiency_type_blindness") == null
						|| rs.getString("I.deficiency_type_blindness").equals("0")) {
					instructor.setDeficiency_type_blindness("N�o");
				} else {
					instructor.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("I.deficiency_type_low_vision") == null
						|| rs.getString("I.deficiency_type_low_vision").equals("0")) {
					instructor.setDeficiency_type_low_vision("N�o");
				} else {
					instructor.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("I.deficiency_type_deafness") == null
						|| rs.getString("I.deficiency_type_deafness").equals("0")) {
					instructor.setDeficiency_type_deafness("N�o");
				} else {
					instructor.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("I.deficiency_type_disability_hearing") == null
						|| rs.getString("I.deficiency_type_disability_hearing").equals("0")) {
					instructor.setDeficiency_type_disability_hearing("N�o");
				} else {
					instructor.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("I.deficiency_type_deafblindness") == null
						|| rs.getString("I.deficiency_type_deafblindness").equals("0")) {
					instructor.setDeficiency_type_deafblindness("N�o");
				} else {
					instructor.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("I.deficiency_type_phisical_disability") == null
						|| rs.getString("I.deficiency_type_phisical_disability").equals("0")) {
					instructor.setDeficiency_type_phisical_disability("N�o");
				} else {
					instructor.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_intelectual_disability") == null
						|| rs.getString("I.deficiency_type_intelectual_disability").equals("0")) {
					instructor.setDeficiency_type_intelectual_disability("N�o");
				} else {
					instructor.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_multiple_disabilities") == null
						|| rs.getString("I.deficiency_type_multiple_disabilities").equals("0")) {
					instructor.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					instructor.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("I.fkid") == null || rs.getString("I.fkid").equals("")) {
					instructor.setFkid("N�o foi informado");
				} else {
					instructor.setFkid(rs.getString("I.fkid"));
				}
				arrayIntructor.add(instructor);
			}

			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayIntructor.size() > 0) {
				instructorReturn.setValid(true);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(arrayIntructor);
			} else {
				error.add("N�o existe professor cadastrado!");

				instructorReturn.setValid(false);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(null);
			}

			arrayIntructorReturn.add(instructorReturn);

			return arrayIntructorReturn;
		} catch (Exception e) {
			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			instructorReturn.setValid(false);
			instructorReturn.setError(error);
			instructorReturn.setInstructor(null);

			arrayIntructorReturn.add(instructorReturn);
			e.printStackTrace();
			return arrayIntructorReturn;
		}
	}

	public ArrayList<InstructorReturn> getInstructorByID(Connection connection, String id) throws Exception {
		try {
			String sql = "SELECT I.*, EC.*, EU.*, EN.* FROM instructor_identification I "
					+ "JOIN edcenso_nation EN ON I.edcenso_nation_fk = EN.id "
					+ "JOIN edcenso_uf EU ON I.edcenso_uf_fk = EU.id "
					+ "JOIN edcenso_city EC ON I.edcenso_city_fk = EC.id WHERE I.id = '" + id + "' ORDER BY I.id;";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Instructor instructor = new Instructor();

				instructor.setRegister_type(rs.getString("I.register_type"));
				instructor.setSchool_inep_id_fk(rs.getString("I.school_inep_id_fk"));
				if (rs.getString("I.inep_id") == null || rs.getString("I.inep_id").equals("")) {
					instructor.setInep_id("N�o foi informado");
				} else {
					instructor.setInep_id(rs.getString("I.inep_id"));
				}
				instructor.setName(rs.getString("I.name"));
				instructor.setEmail(rs.getString("I.email"));
				if (rs.getString("I.nis") == null || rs.getString("I.nis").equals("")) {
					instructor.setNis("N�o foi informado");
				} else {
					instructor.setNis(rs.getString("I.nis"));
				}
				instructor.setBirthday_date(rs.getString("I.birthday_date"));
				if (rs.getString("I.sex").equals("1")) {
					instructor.setSex("M");
				} else {
					instructor.setSex("F");
				}
				if (rs.getString("I.color_race").equals("0")) {
					instructor.setColor_race("N�o declarada");
				} else if (rs.getString("I.color_race").equals("1")) {
					instructor.setColor_race("Branca");
				} else if (rs.getString("I.color_race").equals("2")) {
					instructor.setColor_race("Preta");
				} else if (rs.getString("I.color_race").equals("3")) {
					instructor.setColor_race("Parda");
				} else if (rs.getString("I.color_race").equals("4")) {
					instructor.setColor_race("Amarela");
				} else if (rs.getString("I.color_race").equals("5")) {
					instructor.setColor_race("Ind�gena");
				}
				if (rs.getString("I.filiation") == null || rs.getString("I.filiation").equals("0")) {
					instructor.setFiliation("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation("Pai e/ou M�e");
				}
				if (rs.getString("I.filiation_1") == null || rs.getString("I.filiation_1").equals("0")) {
					instructor.setFiliation_1("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_1(rs.getString("I.filiation_1"));
				}
				if (rs.getString("I.filiation_2") == null || rs.getString("I.filiation_2").equals("0")) {
					instructor.setFiliation_2("N�o declarado/Ignorado");
				} else {
					instructor.setFiliation_2(rs.getString("I.filiation_2"));
				}
				if (rs.getString("I.nationality").equals("1")) {
					instructor.setNationality("Brasileira");
				} else if (rs.getString("I.nationality").equals("2")) {
					instructor.setNationality("Brasileira: Nascido no exterior ou Naturalizado");
				} else if (rs.getString("I.nationality").equals("3")) {
					instructor.setNationality("Estrangeira");
				}
				if (rs.getString("I.edcenso_nation_fk").equals(rs.getString("I.edcenso_nation.id"))) {
					instructor.setEdcenso_nation_fk(rs.getString("I.edcenso_nation.name"));
				}
				if (rs.getString("I.edcenso_uf_fk").equals(rs.getString("I.edcenso_uf.id"))) {
					instructor.setEdcenso_uf_fk(rs.getString("I.edcenso_uf.name"));
				}
				if (rs.getString("I.edcenso_city_fk").equals(rs.getString("I.edcenso_city.id"))) {
					instructor.setEdcenso_city_fk(rs.getString("I.edcenso_city.name"));
				}
				if (rs.getString("I.deficiency").equals("0")) {
					instructor.setDeficiency("N�o");
				} else {
					instructor.setDeficiency("Sim");
				}
				if (rs.getString("I.deficiency_type_blindness") == null
						|| rs.getString("I.deficiency_type_blindness").equals("0")) {
					instructor.setDeficiency_type_blindness("N�o");
				} else {
					instructor.setDeficiency_type_blindness("Sim");
				}
				if (rs.getString("I.deficiency_type_low_vision") == null
						|| rs.getString("I.deficiency_type_low_vision").equals("0")) {
					instructor.setDeficiency_type_low_vision("N�o");
				} else {
					instructor.setDeficiency_type_low_vision("Sim");
				}
				if (rs.getString("I.deficiency_type_deafness") == null
						|| rs.getString("I.deficiency_type_deafness").equals("0")) {
					instructor.setDeficiency_type_deafness("N�o");
				} else {
					instructor.setDeficiency_type_deafness("Sim");
				}
				if (rs.getString("I.deficiency_type_disability_hearing") == null
						|| rs.getString("I.deficiency_type_disability_hearing").equals("0")) {
					instructor.setDeficiency_type_disability_hearing("N�o");
				} else {
					instructor.setDeficiency_type_disability_hearing("Sim");
				}
				if (rs.getString("I.deficiency_type_deafblindness") == null
						|| rs.getString("I.deficiency_type_deafblindness").equals("0")) {
					instructor.setDeficiency_type_deafblindness("N�o");
				} else {
					instructor.setDeficiency_type_deafblindness("Sim");
				}
				if (rs.getString("I.deficiency_type_phisical_disability") == null
						|| rs.getString("I.deficiency_type_phisical_disability").equals("0")) {
					instructor.setDeficiency_type_phisical_disability("N�o");
				} else {
					instructor.setDeficiency_type_phisical_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_intelectual_disability") == null
						|| rs.getString("I.deficiency_type_intelectual_disability").equals("0")) {
					instructor.setDeficiency_type_intelectual_disability("N�o");
				} else {
					instructor.setDeficiency_type_intelectual_disability("Sim");
				}
				if (rs.getString("I.deficiency_type_multiple_disabilities") == null
						|| rs.getString("I.deficiency_type_multiple_disabilities").equals("0")) {
					instructor.setDeficiency_type_multiple_disabilities("N�o");
				} else {
					instructor.setDeficiency_type_multiple_disabilities("Sim");
				}
				if (rs.getString("I.fkid") == null || rs.getString("I.fkid").equals("")) {
					instructor.setFkid("N�o foi informado");
				} else {
					instructor.setFkid(rs.getString("I.fkid"));
				}
				arrayIntructor.add(instructor);
			}

			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayIntructor.size() > 0) {
				instructorReturn.setValid(true);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(arrayIntructor);
			} else {
				error.add("N�o existe professor cadastrado!");

				instructorReturn.setValid(false);
				instructorReturn.setError(error);
				instructorReturn.setInstructor(null);
			}

			arrayIntructorReturn.add(instructorReturn);

			return arrayIntructorReturn;
		} catch (Exception e) {
			InstructorReturn instructorReturn = new InstructorReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			instructorReturn.setValid(false);
			instructorReturn.setError(error);
			instructorReturn.setInstructor(null);

			arrayIntructorReturn.add(instructorReturn);
			e.printStackTrace();
			return arrayIntructorReturn;
		}
	}

	// --------------- CLASSROOM ------------------ //
	public ArrayList<ClassroomReturn> getClassrooms(Connection connection) throws Exception {
		try {
			String sql = "SELECT * FROM classroom";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Classroom classroom = new Classroom();

				classroom.setRegister_type(rs.getString("register_type"));
				classroom.setSchool_inep_fk(rs.getString("school_inep_fk"));
				if (rs.getString("inep_id") == null || rs.getString("inep_id").equals("")) {
					classroom.setInep_id("N�o foi informado");
				} else {
					classroom.setInep_id(rs.getString("inep_id"));
				}
				classroom.setId(rs.getString("id"));
				classroom.setName(rs.getString("name"));
				if (rs.getString("pedagogical_mediation_type") == null
						|| rs.getString("pedagogical_mediation_type").equals("")) {
					classroom.setPedagogical_mediation_type("N�o foi informado");
				} else {
					classroom.setPedagogical_mediation_type(rs.getString("pedagogical_mediation_type"));
				}
				classroom.setInitial_hour(rs.getString("initial_hour"));
				classroom.setInitial_minute(rs.getString("initial_minute"));
				classroom.setFinal_hour(rs.getString("final_hour"));
				classroom.setFinal_minute(rs.getString("final_minute"));
				if (rs.getString("week_days_sunday").equals("1")) {
					classroom.setWeek_days_sunday("Sim");
				} else {
					classroom.setWeek_days_sunday("N�o");
				}
				if (rs.getString("week_days_monday").equals("1")) {
					classroom.setWeek_days_monday("Sim");
				} else {
					classroom.setWeek_days_monday("N�o");
				}
				if (rs.getString("week_days_tuesday").equals("1")) {
					classroom.setWeek_days_tuesday("Sim");
				} else {
					classroom.setWeek_days_tuesday("N�o");
				}
				if (rs.getString("week_days_wednesday").equals("1")) {
					classroom.setWeek_days_wednesday("Sim");
				} else {
					classroom.setWeek_days_wednesday("N�o");
				}
				if (rs.getString("week_days_thursday").equals("1")) {
					classroom.setWeek_days_thursday("Sim");
				} else {
					classroom.setWeek_days_thursday("N�o");
				}
				if (rs.getString("week_days_friday").equals("1")) {
					classroom.setWeek_days_friday("Sim");
				} else {
					classroom.setWeek_days_friday("N�o");
				}
				if (rs.getString("week_days_saturday").equals("1")) {
					classroom.setWeek_days_saturday("Sim");
				} else {
					classroom.setWeek_days_saturday("N�o");
				}
				if (rs.getString("assistance_type").equals("0")) {
					classroom.setAssistance_type("N�o se Aplica");
				} else if (rs.getString("assistance_type").equals("1")) {
					classroom.setAssistance_type("Classe Hospitalar");
				} else if (rs.getString("assistance_type").equals("2")) {
					classroom.setAssistance_type("Unidae de Interna��o Socioeducativa");
				} else if (rs.getString("assistance_type").equals("3")) {
					classroom.setAssistance_type("Unidade Prisional");
				} else if (rs.getString("assistance_type").equals("4")) {
					classroom.setAssistance_type("Atividade Complementar");
				} else if (rs.getString("assistance_type").equals("5")) {
					classroom.setAssistance_type("Atendimento Educacional Especializado (AEE)");
				}
				if (rs.getString("mais_educacao_participator") == null
						|| rs.getString("mais_educacao_participator").equals("")) {
					classroom.setMais_educacao_participator("N�o foi informado");
				} else if (rs.getString("mais_educacao_participator").equals("0")) {
					classroom.setMais_educacao_participator("N�o");
				} else if (rs.getString("mais_educacao_participator").equals("1")) {
					classroom.setMais_educacao_participator("Sim");
				}
				if (rs.getString("complementary_activity_type_1") == null
						|| rs.getString("complementary_activity_type_1").equals("")) {
					classroom.setComplementary_activity_type_1("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_1(rs.getString("complementary_activity_type_1"));
				}
				if (rs.getString("complementary_activity_type_2") == null
						|| rs.getString("complementary_activity_type_2").equals("")) {
					classroom.setComplementary_activity_type_2("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_2(rs.getString("complementary_activity_type_2"));
				}
				if (rs.getString("complementary_activity_type_3") == null
						|| rs.getString("complementary_activity_type_3").equals("")) {
					classroom.setComplementary_activity_type_3("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_3(rs.getString("complementary_activity_type_3"));
				}
				if (rs.getString("complementary_activity_type_4") == null
						|| rs.getString("complementary_activity_type_4").equals("")) {
					classroom.setComplementary_activity_type_4("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_4(rs.getString("complementary_activity_type_4"));
				}
				if (rs.getString("complementary_activity_type_5") == null
						|| rs.getString("complementary_activity_type_5").equals("")) {
					classroom.setComplementary_activity_type_5("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_5(rs.getString("complementary_activity_type_5"));
				}
				if (rs.getString("complementary_activity_type_6") == null
						|| rs.getString("complementary_activity_type_6").equals("")) {
					classroom.setComplementary_activity_type_6("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_6(rs.getString("complementary_activity_type_6"));
				}
				if (rs.getString("aee_braille_system_education") == null
						|| rs.getString("aee_braille_system_education").equals("")) {
					classroom.setAee_braille_system_education("N�o foi informado");
				} else {
					classroom.setAee_braille_system_education(rs.getString("aee_braille_system_education"));
				}
				if (rs.getString("aee_optical_and_non_optical_resources") == null
						|| rs.getString("aee_optical_and_non_optical_resources").equals("")) {
					classroom.setAee_optical_and_non_optical_resources("N�o foi informado");
				} else {
					classroom.setAee_optical_and_non_optical_resources(
							rs.getString("aee_optical_and_non_optical_resources"));
				}
				if (rs.getString("aee_mental_processes_development_strategies") == null
						|| rs.getString("aee_mental_processes_development_strategies").equals("")) {
					classroom.setAee_mental_processes_development_strategies("N�o foi informado");
				} else {
					classroom.setAee_mental_processes_development_strategies(
							rs.getString("aee_mental_processes_development_strategies"));
				}
				if (rs.getString("aee_mobility_and_orientation_techniques") == null
						|| rs.getString("aee_mobility_and_orientation_techniques").equals("")) {
					classroom.setAee_mobility_and_orientation_techniques("N�o foi informado");
				} else {
					classroom.setAee_mobility_and_orientation_techniques(
							rs.getString("aee_mobility_and_orientation_techniques"));
				}
				if (rs.getString("aee_libras") == null || rs.getString("aee_libras").equals("")) {
					classroom.setAee_libras("N�o foi informado");
				} else {
					classroom.setAee_libras(rs.getString("aee_libras"));
				}
				if (rs.getString("aee_caa_use_education") == null || rs.getString("aee_caa_use_education").equals("")) {
					classroom.setAee_caa_use_education("N�o foi informado");
				} else {
					classroom.setAee_caa_use_education(rs.getString("aee_caa_use_education"));
				}
				if (rs.getString("aee_curriculum_enrichment_strategy") == null
						|| rs.getString("aee_curriculum_enrichment_strategy").equals("")) {
					classroom.setAee_curriculum_enrichment_strategy("N�o foi informado");
				} else {
					classroom.setAee_curriculum_enrichment_strategy(rs.getString("aee_curriculum_enrichment_strategy"));
				}
				if (rs.getString("aee_soroban_use_education") == null
						|| rs.getString("aee_soroban_use_education").equals("")) {
					classroom.setAee_soroban_use_education("N�o foi informado");
				} else {
					classroom.setAee_soroban_use_education(rs.getString("aee_soroban_use_education"));
				}
				if (rs.getString("aee_usability_and_functionality_of_computer_accessible_education") == null || rs
						.getString("aee_usability_and_functionality_of_computer_accessible_education").equals("")) {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education("N�o foi informado");
				} else {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education(
							rs.getString("aee_usability_and_functionality_of_computer_accessible_education"));
				}
				if (rs.getString("aee_teaching_of_Portuguese_language_written_modality") == null
						|| rs.getString("aee_teaching_of_Portuguese_language_written_modality").equals("")) {
					classroom.setAee_teaching_of_Portuguese_language_written_modality("N�o foi informado");
				} else {
					classroom.setAee_teaching_of_Portuguese_language_written_modality(
							rs.getString("aee_teaching_of_Portuguese_language_written_modality"));
				}
				if (rs.getString("aee_strategy_for_school_environment_autonomy") == null
						|| rs.getString("aee_strategy_for_school_environment_autonomy").equals("")) {
					classroom.setAee_strategy_for_school_environment_autonomy("N�o foi informado");
				} else {
					classroom.setAee_strategy_for_school_environment_autonomy(
							rs.getString("aee_strategy_for_school_environment_autonomy"));
				}
				if (rs.getString("modality") == null) {
					classroom.setModality("N�o foi informado");
				} else if (rs.getString("modality").equals("1")) {
					classroom.setModality("Ensino Regular");
				} else if (rs.getString("modality").equals("2")) {
					classroom.setModality("Educa��o Especial - Modalidade Substitutiva");
				} else if (rs.getString("modality").equals("3")) {
					classroom.setModality("Educa��o de Jovens e Adultos (EJA)");
				}
				if (rs.getString("edcenso_stage_vs_modality_fk") == null
						|| rs.getString("edcenso_stage_vs_modality_fk").equals("")) {
					classroom.setEdcenso_stage_vs_modality_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_stage_vs_modality_fk(rs.getString("edcenso_stage_vs_modality_fk"));
				}
				if (rs.getString("edcenso_professional_education_course_fk") == null
						|| rs.getString("edcenso_professional_education_course_fk").equals("")) {
					classroom.setEdcenso_professional_education_course_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_professional_education_course_fk(
							rs.getString("edcenso_professional_education_course_fk"));
				}
				if (rs.getString("discipline_chemistry") == null || rs.getString("discipline_chemistry").equals("")
						|| rs.getString("discipline_chemistry").equals("0")) {
					classroom.setDiscipline_chemistry("N�o");
				} else {
					classroom.setDiscipline_chemistry("Sim");
				}
				if (rs.getString("discipline_physics") == null || rs.getString("discipline_physics").equals("")
						|| rs.getString("discipline_physics").equals("0")) {
					classroom.setDiscipline_physics("N�o");
				} else {
					classroom.setDiscipline_physics("Sim");
				}
				if (rs.getString("discipline_mathematics") == null || rs.getString("discipline_mathematics").equals("")
						|| rs.getString("discipline_mathematics").equals("0")) {
					classroom.setDiscipline_mathematics("N�o");
				} else {
					classroom.setDiscipline_mathematics("Sim");
				}
				if (rs.getString("discipline_biology") == null || rs.getString("discipline_biology").equals("")
						|| rs.getString("discipline_biology").equals("0")) {
					classroom.setDiscipline_biology("N�o");
				} else {
					classroom.setDiscipline_biology("Sim");
				}
				if (rs.getString("discipline_science") == null || rs.getString("discipline_science").equals("")
						|| rs.getString("discipline_science").equals("0")) {
					classroom.setDiscipline_science("N�o");
				} else {
					classroom.setDiscipline_science("Sim");
				}
				if (rs.getString("discipline_language_portuguese_literature") == null
						|| rs.getString("discipline_language_portuguese_literature").equals("")
						|| rs.getString("discipline_language_portuguese_literature").equals("0")) {
					classroom.setDiscipline_language_portuguese_literature("N�o");
				} else {
					classroom.setDiscipline_language_portuguese_literature("Sim");
				}
				if (rs.getString("discipline_foreign_language_english") == null
						|| rs.getString("discipline_foreign_language_english").equals("")
						|| rs.getString("discipline_foreign_language_english").equals("0")) {
					classroom.setDiscipline_foreign_language_english("N�o");
				} else {
					classroom.setDiscipline_foreign_language_english("Sim");
				}
				if (rs.getString("discipline_foreign_language_spanish") == null
						|| rs.getString("discipline_foreign_language_spanish").equals("")
						|| rs.getString("discipline_foreign_language_spanish").equals("0")) {
					classroom.setDiscipline_foreign_language_spanish("N�o");
				} else {
					classroom.setDiscipline_foreign_language_spanish("Sim");
				}
				if (rs.getString("discipline_foreign_language_franch") == null
						|| rs.getString("discipline_foreign_language_franch").equals("")
						|| rs.getString("discipline_foreign_language_franch").equals("0")) {
					classroom.setDiscipline_foreign_language_franch("N�o");
				} else {
					classroom.setDiscipline_foreign_language_franch("Sim");
				}
				if (rs.getString("discipline_foreign_language_other") == null
						|| rs.getString("discipline_foreign_language_other").equals("")
						|| rs.getString("discipline_foreign_language_other").equals("0")) {
					classroom.setDiscipline_foreign_language_other("N�o");
				} else {
					classroom.setDiscipline_foreign_language_other("Sim");
				}
				if (rs.getString("discipline_arts") == null || rs.getString("discipline_arts").equals("")
						|| rs.getString("discipline_arts").equals("0")) {
					classroom.setDiscipline_arts("N�o");
				} else {
					classroom.setDiscipline_arts("Sim");
				}
				if (rs.getString("discipline_physical_education") == null
						|| rs.getString("discipline_physical_education").equals("")
						|| rs.getString("discipline_physical_education").equals("0")) {
					classroom.setDiscipline_physical_education("N�o");
				} else {
					classroom.setDiscipline_physical_education("Sim");
				}
				if (rs.getString("discipline_history") == null || rs.getString("discipline_history").equals("")
						|| rs.getString("discipline_history").equals("0")) {
					classroom.setDiscipline_history("N�o");
				} else {
					classroom.setDiscipline_history("Sim");
				}
				if (rs.getString("discipline_geography") == null || rs.getString("discipline_geography").equals("")
						|| rs.getString("discipline_geography").equals("0")) {
					classroom.setDiscipline_geography("N�o");
				} else {
					classroom.setDiscipline_geography("Sim");
				}
				if (rs.getString("discipline_philosophy") == null || rs.getString("discipline_philosophy").equals("")
						|| rs.getString("discipline_philosophy").equals("0")) {
					classroom.setDiscipline_philosophy("N�o");
				} else {
					classroom.setDiscipline_philosophy("Sim");
				}
				if (rs.getString("discipline_social_study") == null
						|| rs.getString("discipline_social_study").equals("")
						|| rs.getString("discipline_social_study").equals("0")) {
					classroom.setDiscipline_social_study("N�o");
				} else {
					classroom.setDiscipline_social_study("Sim");
				}
				if (rs.getString("discipline_sociology") == null || rs.getString("discipline_sociology").equals("")
						|| rs.getString("discipline_sociology").equals("0")) {
					classroom.setDiscipline_sociology("N�o");
				} else {
					classroom.setDiscipline_sociology("Sim");
				}
				if (rs.getString("discipline_informatics") == null || rs.getString("discipline_informatics").equals("")
						|| rs.getString("discipline_informatics").equals("0")) {
					classroom.setDiscipline_informatics("N�o");
				} else {
					classroom.setDiscipline_informatics("Sim");
				}
				if (rs.getString("discipline_professional_disciplines") == null
						|| rs.getString("discipline_professional_disciplines").equals("")
						|| rs.getString("discipline_professional_disciplines").equals("0")) {
					classroom.setDiscipline_professional_disciplines("N�o");
				} else {
					classroom.setDiscipline_professional_disciplines("Sim");
				}
				if (rs.getString("discipline_special_education_and_inclusive_practices") == null
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("")
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("0")) {
					classroom.setDiscipline_special_education_and_inclusive_practices("N�o");
				} else {
					classroom.setDiscipline_special_education_and_inclusive_practices("Sim");
				}
				if (rs.getString("discipline_sociocultural_diversity") == null
						|| rs.getString("discipline_sociocultural_diversity").equals("")
						|| rs.getString("discipline_sociocultural_diversity").equals("0")) {
					classroom.setDiscipline_sociocultural_diversity("N�o");
				} else {
					classroom.setDiscipline_sociocultural_diversity("Sim");
				}
				if (rs.getString("discipline_libras") == null || rs.getString("discipline_libras").equals("")
						|| rs.getString("discipline_libras").equals("0")) {
					classroom.setDiscipline_libras("N�o");
				} else {
					classroom.setDiscipline_libras("Sim");
				}
				if (rs.getString("discipline_pedagogical") == null || rs.getString("discipline_pedagogical").equals("")
						|| rs.getString("discipline_pedagogical").equals("0")) {
					classroom.setDiscipline_pedagogical("N�o");
				} else {
					classroom.setDiscipline_pedagogical("Sim");
				}
				if (rs.getString("discipline_religious") == null || rs.getString("discipline_religious").equals("")
						|| rs.getString("discipline_religious").equals("0")) {
					classroom.setDiscipline_religious("N�o");
				} else {
					classroom.setDiscipline_religious("Sim");
				}
				if (rs.getString("discipline_native_language") == null
						|| rs.getString("discipline_native_language").equals("")
						|| rs.getString("discipline_native_language").equals("0")) {
					classroom.setDiscipline_native_language("N�o");
				} else {
					classroom.setDiscipline_native_language("Sim");
				}
				if (rs.getString("discipline_others") == null || rs.getString("discipline_others").equals("")
						|| rs.getString("discipline_others").equals("0")) {
					classroom.setDiscipline_others("N�o");
				} else {
					classroom.setDiscipline_others("Sim");
				}
				classroom.setSchool_year(rs.getString("school_year"));
				if (rs.getString("turn") == null || rs.getString("turn").equals("")) {
					classroom.setTurn("N�o foi informado");
				} else if (rs.getString("turn").equals("T")) {
					classroom.setTurn("Tarde");
				} else if (rs.getString("turn").equals("M")) {
					classroom.setTurn("Manh�");
				} else {
					classroom.setTurn("Noturno");
				}
				classroom.setCreate_date(rs.getString("create_date"));
				if (rs.getString("fkid") == null || rs.getString("fkid").equals("")
						|| rs.getString("fkid").equals("0")) {
					classroom.setFkid("N�o foi informado");
				} else {
					classroom.setFkid(rs.getString("fkid"));
				}
				if (rs.getString("calendar_fk") != null) {
					classroom.setCalendar_fk(rs.getString("calendar_fk"));
				} else {
					classroom.setCalendar_fk("N�o foi informado");
				}
				arrayClassroom.add(classroom);
			}

			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayClassroom.size() > 0) {
				classroomReturn.setValid(true);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(arrayClassroom);
			} else {
				error.add("Essa escola n�o possui turma!");

				classroomReturn.setValid(false);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(null);
			}

			arrayClassroomReturn.add(classroomReturn);

			return arrayClassroomReturn;
		} catch (Exception e) {
			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			classroomReturn.setValid(false);
			classroomReturn.setError(error);
			classroomReturn.setClassroom(null);

			arrayClassroomReturn.add(classroomReturn);
			e.printStackTrace();
			return arrayClassroomReturn;
		}
	}

	public ArrayList<ClassroomReturn> getClassroomsOfInstructor(Connection connection, String instructor_fk,
			String year) throws Exception {
		try {
			String sql = "SELECT I.* FROM instructor_teaching_data I JOIN classroom C ON I.classroom_id_fk = C.id "
					+ "WHERE I.instructor_fk = '" + instructor_fk + "' AND C.school_year = '" + year
					+ "' ORDER BY C.id;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Classroom classroom = new Classroom();

				classroom.setRegister_type(rs.getString("register_type"));
				classroom.setSchool_inep_fk(rs.getString("school_inep_fk"));
				if (rs.getString("inep_id") == null || rs.getString("inep_id").equals("")) {
					classroom.setInep_id("N�o foi informado");
				} else {
					classroom.setInep_id(rs.getString("inep_id"));
				}
				classroom.setId(rs.getString("id"));
				classroom.setName(rs.getString("name"));
				if (rs.getString("pedagogical_mediation_type") == null
						|| rs.getString("pedagogical_mediation_type").equals("")) {
					classroom.setPedagogical_mediation_type("N�o foi informado");
				} else {
					classroom.setPedagogical_mediation_type(rs.getString("pedagogical_mediation_type"));
				}
				classroom.setInitial_hour(rs.getString("initial_hour"));
				classroom.setInitial_minute(rs.getString("initial_minute"));
				classroom.setFinal_hour(rs.getString("final_hour"));
				classroom.setFinal_minute(rs.getString("final_minute"));
				if (rs.getString("week_days_sunday").equals("1")) {
					classroom.setWeek_days_sunday("Sim");
				} else {
					classroom.setWeek_days_sunday("N�o");
				}
				if (rs.getString("week_days_monday").equals("1")) {
					classroom.setWeek_days_monday("Sim");
				} else {
					classroom.setWeek_days_monday("N�o");
				}
				if (rs.getString("week_days_tuesday").equals("1")) {
					classroom.setWeek_days_tuesday("Sim");
				} else {
					classroom.setWeek_days_tuesday("N�o");
				}
				if (rs.getString("week_days_wednesday").equals("1")) {
					classroom.setWeek_days_wednesday("Sim");
				} else {
					classroom.setWeek_days_wednesday("N�o");
				}
				if (rs.getString("week_days_thursday").equals("1")) {
					classroom.setWeek_days_thursday("Sim");
				} else {
					classroom.setWeek_days_thursday("N�o");
				}
				if (rs.getString("week_days_friday").equals("1")) {
					classroom.setWeek_days_friday("Sim");
				} else {
					classroom.setWeek_days_friday("N�o");
				}
				if (rs.getString("week_days_saturday").equals("1")) {
					classroom.setWeek_days_saturday("Sim");
				} else {
					classroom.setWeek_days_saturday("N�o");
				}
				if (rs.getString("assistance_type").equals("0")) {
					classroom.setAssistance_type("N�o se Aplica");
				} else if (rs.getString("assistance_type").equals("1")) {
					classroom.setAssistance_type("Classe Hospitalar");
				} else if (rs.getString("assistance_type").equals("2")) {
					classroom.setAssistance_type("Unidae de Interna��o Socioeducativa");
				} else if (rs.getString("assistance_type").equals("3")) {
					classroom.setAssistance_type("Unidade Prisional");
				} else if (rs.getString("assistance_type").equals("4")) {
					classroom.setAssistance_type("Atividade Complementar");
				} else if (rs.getString("assistance_type").equals("5")) {
					classroom.setAssistance_type("Atendimento Educacional Especializado (AEE)");
				}
				if (rs.getString("mais_educacao_participator") == null
						|| rs.getString("mais_educacao_participator").equals("")) {
					classroom.setMais_educacao_participator("N�o foi informado");
				} else if (rs.getString("mais_educacao_participator").equals("0")) {
					classroom.setMais_educacao_participator("N�o");
				} else if (rs.getString("mais_educacao_participator").equals("1")) {
					classroom.setMais_educacao_participator("Sim");
				}
				if (rs.getString("complementary_activity_type_1") == null
						|| rs.getString("complementary_activity_type_1").equals("")) {
					classroom.setComplementary_activity_type_1("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_1(rs.getString("complementary_activity_type_1"));
				}
				if (rs.getString("complementary_activity_type_2") == null
						|| rs.getString("complementary_activity_type_2").equals("")) {
					classroom.setComplementary_activity_type_2("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_2(rs.getString("complementary_activity_type_2"));
				}
				if (rs.getString("complementary_activity_type_3") == null
						|| rs.getString("complementary_activity_type_3").equals("")) {
					classroom.setComplementary_activity_type_3("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_3(rs.getString("complementary_activity_type_3"));
				}
				if (rs.getString("complementary_activity_type_4") == null
						|| rs.getString("complementary_activity_type_4").equals("")) {
					classroom.setComplementary_activity_type_4("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_4(rs.getString("complementary_activity_type_4"));
				}
				if (rs.getString("complementary_activity_type_5") == null
						|| rs.getString("complementary_activity_type_5").equals("")) {
					classroom.setComplementary_activity_type_5("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_5(rs.getString("complementary_activity_type_5"));
				}
				if (rs.getString("complementary_activity_type_6") == null
						|| rs.getString("complementary_activity_type_6").equals("")) {
					classroom.setComplementary_activity_type_6("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_6(rs.getString("complementary_activity_type_6"));
				}
				if (rs.getString("aee_braille_system_education") == null
						|| rs.getString("aee_braille_system_education").equals("")) {
					classroom.setAee_braille_system_education("N�o foi informado");
				} else {
					classroom.setAee_braille_system_education(rs.getString("aee_braille_system_education"));
				}
				if (rs.getString("aee_optical_and_non_optical_resources") == null
						|| rs.getString("aee_optical_and_non_optical_resources").equals("")) {
					classroom.setAee_optical_and_non_optical_resources("N�o foi informado");
				} else {
					classroom.setAee_optical_and_non_optical_resources(
							rs.getString("aee_optical_and_non_optical_resources"));
				}
				if (rs.getString("aee_mental_processes_development_strategies") == null
						|| rs.getString("aee_mental_processes_development_strategies").equals("")) {
					classroom.setAee_mental_processes_development_strategies("N�o foi informado");
				} else {
					classroom.setAee_mental_processes_development_strategies(
							rs.getString("aee_mental_processes_development_strategies"));
				}
				if (rs.getString("aee_mobility_and_orientation_techniques") == null
						|| rs.getString("aee_mobility_and_orientation_techniques").equals("")) {
					classroom.setAee_mobility_and_orientation_techniques("N�o foi informado");
				} else {
					classroom.setAee_mobility_and_orientation_techniques(
							rs.getString("aee_mobility_and_orientation_techniques"));
				}
				if (rs.getString("aee_libras") == null || rs.getString("aee_libras").equals("")) {
					classroom.setAee_libras("N�o foi informado");
				} else {
					classroom.setAee_libras(rs.getString("aee_libras"));
				}
				if (rs.getString("aee_caa_use_education") == null || rs.getString("aee_caa_use_education").equals("")) {
					classroom.setAee_caa_use_education("N�o foi informado");
				} else {
					classroom.setAee_caa_use_education(rs.getString("aee_caa_use_education"));
				}
				if (rs.getString("aee_curriculum_enrichment_strategy") == null
						|| rs.getString("aee_curriculum_enrichment_strategy").equals("")) {
					classroom.setAee_curriculum_enrichment_strategy("N�o foi informado");
				} else {
					classroom.setAee_curriculum_enrichment_strategy(rs.getString("aee_curriculum_enrichment_strategy"));
				}
				if (rs.getString("aee_soroban_use_education") == null
						|| rs.getString("aee_soroban_use_education").equals("")) {
					classroom.setAee_soroban_use_education("N�o foi informado");
				} else {
					classroom.setAee_soroban_use_education(rs.getString("aee_soroban_use_education"));
				}
				if (rs.getString("aee_usability_and_functionality_of_computer_accessible_education") == null || rs
						.getString("aee_usability_and_functionality_of_computer_accessible_education").equals("")) {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education("N�o foi informado");
				} else {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education(
							rs.getString("aee_usability_and_functionality_of_computer_accessible_education"));
				}
				if (rs.getString("aee_teaching_of_Portuguese_language_written_modality") == null
						|| rs.getString("aee_teaching_of_Portuguese_language_written_modality").equals("")) {
					classroom.setAee_teaching_of_Portuguese_language_written_modality("N�o foi informado");
				} else {
					classroom.setAee_teaching_of_Portuguese_language_written_modality(
							rs.getString("aee_teaching_of_Portuguese_language_written_modality"));
				}
				if (rs.getString("aee_strategy_for_school_environment_autonomy") == null
						|| rs.getString("aee_strategy_for_school_environment_autonomy").equals("")) {
					classroom.setAee_strategy_for_school_environment_autonomy("N�o foi informado");
				} else {
					classroom.setAee_strategy_for_school_environment_autonomy(
							rs.getString("aee_strategy_for_school_environment_autonomy"));
				}
				if (rs.getString("modality") == null) {
					classroom.setModality("N�o foi informado");
				} else if (rs.getString("modality").equals("1")) {
					classroom.setModality("Ensino Regular");
				} else if (rs.getString("modality").equals("2")) {
					classroom.setModality("Educa��o Especial - Modalidade Substitutiva");
				} else if (rs.getString("modality").equals("3")) {
					classroom.setModality("Educa��o de Jovens e Adultos (EJA)");
				}
				if (rs.getString("edcenso_stage_vs_modality_fk") == null
						|| rs.getString("edcenso_stage_vs_modality_fk").equals("")) {
					classroom.setEdcenso_stage_vs_modality_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_stage_vs_modality_fk(rs.getString("edcenso_stage_vs_modality_fk"));
				}
				if (rs.getString("edcenso_professional_education_course_fk") == null
						|| rs.getString("edcenso_professional_education_course_fk").equals("")) {
					classroom.setEdcenso_professional_education_course_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_professional_education_course_fk(
							rs.getString("edcenso_professional_education_course_fk"));
				}
				if (rs.getString("discipline_chemistry") == null || rs.getString("discipline_chemistry").equals("")
						|| rs.getString("discipline_chemistry").equals("0")) {
					classroom.setDiscipline_chemistry("N�o");
				} else {
					classroom.setDiscipline_chemistry("Sim");
				}
				if (rs.getString("discipline_physics") == null || rs.getString("discipline_physics").equals("")
						|| rs.getString("discipline_physics").equals("0")) {
					classroom.setDiscipline_physics("N�o");
				} else {
					classroom.setDiscipline_physics("Sim");
				}
				if (rs.getString("discipline_mathematics") == null || rs.getString("discipline_mathematics").equals("")
						|| rs.getString("discipline_mathematics").equals("0")) {
					classroom.setDiscipline_mathematics("N�o");
				} else {
					classroom.setDiscipline_mathematics("Sim");
				}
				if (rs.getString("discipline_biology") == null || rs.getString("discipline_biology").equals("")
						|| rs.getString("discipline_biology").equals("0")) {
					classroom.setDiscipline_biology("N�o");
				} else {
					classroom.setDiscipline_biology("Sim");
				}
				if (rs.getString("discipline_science") == null || rs.getString("discipline_science").equals("")
						|| rs.getString("discipline_science").equals("0")) {
					classroom.setDiscipline_science("N�o");
				} else {
					classroom.setDiscipline_science("Sim");
				}
				if (rs.getString("discipline_language_portuguese_literature") == null
						|| rs.getString("discipline_language_portuguese_literature").equals("")
						|| rs.getString("discipline_language_portuguese_literature").equals("0")) {
					classroom.setDiscipline_language_portuguese_literature("N�o");
				} else {
					classroom.setDiscipline_language_portuguese_literature("Sim");
				}
				if (rs.getString("discipline_foreign_language_english") == null
						|| rs.getString("discipline_foreign_language_english").equals("")
						|| rs.getString("discipline_foreign_language_english").equals("0")) {
					classroom.setDiscipline_foreign_language_english("N�o");
				} else {
					classroom.setDiscipline_foreign_language_english("Sim");
				}
				if (rs.getString("discipline_foreign_language_spanish") == null
						|| rs.getString("discipline_foreign_language_spanish").equals("")
						|| rs.getString("discipline_foreign_language_spanish").equals("0")) {
					classroom.setDiscipline_foreign_language_spanish("N�o");
				} else {
					classroom.setDiscipline_foreign_language_spanish("Sim");
				}
				if (rs.getString("discipline_foreign_language_franch") == null
						|| rs.getString("discipline_foreign_language_franch").equals("")
						|| rs.getString("discipline_foreign_language_franch").equals("0")) {
					classroom.setDiscipline_foreign_language_franch("N�o");
				} else {
					classroom.setDiscipline_foreign_language_franch("Sim");
				}
				if (rs.getString("discipline_foreign_language_other") == null
						|| rs.getString("discipline_foreign_language_other").equals("")
						|| rs.getString("discipline_foreign_language_other").equals("0")) {
					classroom.setDiscipline_foreign_language_other("N�o");
				} else {
					classroom.setDiscipline_foreign_language_other("Sim");
				}
				if (rs.getString("discipline_arts") == null || rs.getString("discipline_arts").equals("")
						|| rs.getString("discipline_arts").equals("0")) {
					classroom.setDiscipline_arts("N�o");
				} else {
					classroom.setDiscipline_arts("Sim");
				}
				if (rs.getString("discipline_physical_education") == null
						|| rs.getString("discipline_physical_education").equals("")
						|| rs.getString("discipline_physical_education").equals("0")) {
					classroom.setDiscipline_physical_education("N�o");
				} else {
					classroom.setDiscipline_physical_education("Sim");
				}
				if (rs.getString("discipline_history") == null || rs.getString("discipline_history").equals("")
						|| rs.getString("discipline_history").equals("0")) {
					classroom.setDiscipline_history("N�o");
				} else {
					classroom.setDiscipline_history("Sim");
				}
				if (rs.getString("discipline_geography") == null || rs.getString("discipline_geography").equals("")
						|| rs.getString("discipline_geography").equals("0")) {
					classroom.setDiscipline_geography("N�o");
				} else {
					classroom.setDiscipline_geography("Sim");
				}
				if (rs.getString("discipline_philosophy") == null || rs.getString("discipline_philosophy").equals("")
						|| rs.getString("discipline_philosophy").equals("0")) {
					classroom.setDiscipline_philosophy("N�o");
				} else {
					classroom.setDiscipline_philosophy("Sim");
				}
				if (rs.getString("discipline_social_study") == null
						|| rs.getString("discipline_social_study").equals("")
						|| rs.getString("discipline_social_study").equals("0")) {
					classroom.setDiscipline_social_study("N�o");
				} else {
					classroom.setDiscipline_social_study("Sim");
				}
				if (rs.getString("discipline_sociology") == null || rs.getString("discipline_sociology").equals("")
						|| rs.getString("discipline_sociology").equals("0")) {
					classroom.setDiscipline_sociology("N�o");
				} else {
					classroom.setDiscipline_sociology("Sim");
				}
				if (rs.getString("discipline_informatics") == null || rs.getString("discipline_informatics").equals("")
						|| rs.getString("discipline_informatics").equals("0")) {
					classroom.setDiscipline_informatics("N�o");
				} else {
					classroom.setDiscipline_informatics("Sim");
				}
				if (rs.getString("discipline_professional_disciplines") == null
						|| rs.getString("discipline_professional_disciplines").equals("")
						|| rs.getString("discipline_professional_disciplines").equals("0")) {
					classroom.setDiscipline_professional_disciplines("N�o");
				} else {
					classroom.setDiscipline_professional_disciplines("Sim");
				}
				if (rs.getString("discipline_special_education_and_inclusive_practices") == null
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("")
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("0")) {
					classroom.setDiscipline_special_education_and_inclusive_practices("N�o");
				} else {
					classroom.setDiscipline_special_education_and_inclusive_practices("Sim");
				}
				if (rs.getString("discipline_sociocultural_diversity") == null
						|| rs.getString("discipline_sociocultural_diversity").equals("")
						|| rs.getString("discipline_sociocultural_diversity").equals("0")) {
					classroom.setDiscipline_sociocultural_diversity("N�o");
				} else {
					classroom.setDiscipline_sociocultural_diversity("Sim");
				}
				if (rs.getString("discipline_libras") == null || rs.getString("discipline_libras").equals("")
						|| rs.getString("discipline_libras").equals("0")) {
					classroom.setDiscipline_libras("N�o");
				} else {
					classroom.setDiscipline_libras("Sim");
				}
				if (rs.getString("discipline_pedagogical") == null || rs.getString("discipline_pedagogical").equals("")
						|| rs.getString("discipline_pedagogical").equals("0")) {
					classroom.setDiscipline_pedagogical("N�o");
				} else {
					classroom.setDiscipline_pedagogical("Sim");
				}
				if (rs.getString("discipline_religious") == null || rs.getString("discipline_religious").equals("")
						|| rs.getString("discipline_religious").equals("0")) {
					classroom.setDiscipline_religious("N�o");
				} else {
					classroom.setDiscipline_religious("Sim");
				}
				if (rs.getString("discipline_native_language") == null
						|| rs.getString("discipline_native_language").equals("")
						|| rs.getString("discipline_native_language").equals("0")) {
					classroom.setDiscipline_native_language("N�o");
				} else {
					classroom.setDiscipline_native_language("Sim");
				}
				if (rs.getString("discipline_others") == null || rs.getString("discipline_others").equals("")
						|| rs.getString("discipline_others").equals("0")) {
					classroom.setDiscipline_others("N�o");
				} else {
					classroom.setDiscipline_others("Sim");
				}
				classroom.setSchool_year(rs.getString("school_year"));
				if (rs.getString("turn") == null || rs.getString("turn").equals("")) {
					classroom.setTurn("N�o foi informado");
				} else if (rs.getString("turn").equals("T")) {
					classroom.setTurn("Tarde");
				} else if (rs.getString("turn").equals("M")) {
					classroom.setTurn("Manh�");
				} else {
					classroom.setTurn("Noturno");
				}
				classroom.setCreate_date(rs.getString("create_date"));
				if (rs.getString("fkid") == null || rs.getString("fkid").equals("")
						|| rs.getString("fkid").equals("0")) {
					classroom.setFkid("N�o foi informado");
				} else {
					classroom.setFkid(rs.getString("fkid"));
				}
				if (rs.getString("calendar_fk") != null) {
					classroom.setCalendar_fk(rs.getString("calendar_fk"));
				} else {
					classroom.setCalendar_fk("N�o foi informado");
				}
				arrayClassroom.add(classroom);
			}

			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayClassroom.size() > 0) {
				classroomReturn.setValid(true);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(arrayClassroom);
			} else {
				error.add("Essa escola n�o possui turma!");

				classroomReturn.setValid(false);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(null);
			}

			arrayClassroomReturn.add(classroomReturn);

			return arrayClassroomReturn;
		} catch (Exception e) {
			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			classroomReturn.setValid(false);
			classroomReturn.setError(error);
			classroomReturn.setClassroom(null);

			arrayClassroomReturn.add(classroomReturn);
			e.printStackTrace();
			return arrayClassroomReturn;
		}
	}

	public ArrayList<ClassroomReturn> getClassroomsBySchoolInep(Connection connection, String school_inep_fk)
			throws Exception {
		try {
			String sql = "SELECT * FROM classroom WHERE school_inep_fk = '" + school_inep_fk + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Classroom classroom = new Classroom();

				classroom.setRegister_type(rs.getString("register_type"));
				classroom.setSchool_inep_fk(rs.getString("school_inep_fk"));
				if (rs.getString("inep_id") == null || rs.getString("inep_id").equals("")) {
					classroom.setInep_id("N�o foi informado");
				} else {
					classroom.setInep_id(rs.getString("inep_id"));
				}
				classroom.setId(rs.getString("id"));
				classroom.setName(rs.getString("name"));
				if (rs.getString("pedagogical_mediation_type") == null
						|| rs.getString("pedagogical_mediation_type").equals("")) {
					classroom.setPedagogical_mediation_type("N�o foi informado");
				} else {
					classroom.setPedagogical_mediation_type(rs.getString("pedagogical_mediation_type"));
				}
				classroom.setInitial_hour(rs.getString("initial_hour"));
				classroom.setInitial_minute(rs.getString("initial_minute"));
				classroom.setFinal_hour(rs.getString("final_hour"));
				classroom.setFinal_minute(rs.getString("final_minute"));
				if (rs.getString("week_days_sunday").equals("1")) {
					classroom.setWeek_days_sunday("Sim");
				} else {
					classroom.setWeek_days_sunday("N�o");
				}
				if (rs.getString("week_days_monday").equals("1")) {
					classroom.setWeek_days_monday("Sim");
				} else {
					classroom.setWeek_days_monday("N�o");
				}
				if (rs.getString("week_days_tuesday").equals("1")) {
					classroom.setWeek_days_tuesday("Sim");
				} else {
					classroom.setWeek_days_tuesday("N�o");
				}
				if (rs.getString("week_days_wednesday").equals("1")) {
					classroom.setWeek_days_wednesday("Sim");
				} else {
					classroom.setWeek_days_wednesday("N�o");
				}
				if (rs.getString("week_days_thursday").equals("1")) {
					classroom.setWeek_days_thursday("Sim");
				} else {
					classroom.setWeek_days_thursday("N�o");
				}
				if (rs.getString("week_days_friday").equals("1")) {
					classroom.setWeek_days_friday("Sim");
				} else {
					classroom.setWeek_days_friday("N�o");
				}
				if (rs.getString("week_days_saturday").equals("1")) {
					classroom.setWeek_days_saturday("Sim");
				} else {
					classroom.setWeek_days_saturday("N�o");
				}
				if (rs.getString("assistance_type").equals("0")) {
					classroom.setAssistance_type("N�o se Aplica");
				} else if (rs.getString("assistance_type").equals("1")) {
					classroom.setAssistance_type("Classe Hospitalar");
				} else if (rs.getString("assistance_type").equals("2")) {
					classroom.setAssistance_type("Unidae de Interna��o Socioeducativa");
				} else if (rs.getString("assistance_type").equals("3")) {
					classroom.setAssistance_type("Unidade Prisional");
				} else if (rs.getString("assistance_type").equals("4")) {
					classroom.setAssistance_type("Atividade Complementar");
				} else if (rs.getString("assistance_type").equals("5")) {
					classroom.setAssistance_type("Atendimento Educacional Especializado (AEE)");
				}
				if (rs.getString("mais_educacao_participator") == null
						|| rs.getString("mais_educacao_participator").equals("")) {
					classroom.setMais_educacao_participator("N�o foi informado");
				} else if (rs.getString("mais_educacao_participator").equals("0")) {
					classroom.setMais_educacao_participator("N�o");
				} else if (rs.getString("mais_educacao_participator").equals("1")) {
					classroom.setMais_educacao_participator("Sim");
				}
				if (rs.getString("complementary_activity_type_1") == null
						|| rs.getString("complementary_activity_type_1").equals("")) {
					classroom.setComplementary_activity_type_1("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_1(rs.getString("complementary_activity_type_1"));
				}
				if (rs.getString("complementary_activity_type_2") == null
						|| rs.getString("complementary_activity_type_2").equals("")) {
					classroom.setComplementary_activity_type_2("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_2(rs.getString("complementary_activity_type_2"));
				}
				if (rs.getString("complementary_activity_type_3") == null
						|| rs.getString("complementary_activity_type_3").equals("")) {
					classroom.setComplementary_activity_type_3("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_3(rs.getString("complementary_activity_type_3"));
				}
				if (rs.getString("complementary_activity_type_4") == null
						|| rs.getString("complementary_activity_type_4").equals("")) {
					classroom.setComplementary_activity_type_4("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_4(rs.getString("complementary_activity_type_4"));
				}
				if (rs.getString("complementary_activity_type_5") == null
						|| rs.getString("complementary_activity_type_5").equals("")) {
					classroom.setComplementary_activity_type_5("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_5(rs.getString("complementary_activity_type_5"));
				}
				if (rs.getString("complementary_activity_type_6") == null
						|| rs.getString("complementary_activity_type_6").equals("")) {
					classroom.setComplementary_activity_type_6("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_6(rs.getString("complementary_activity_type_6"));
				}
				if (rs.getString("aee_braille_system_education") == null
						|| rs.getString("aee_braille_system_education").equals("")) {
					classroom.setAee_braille_system_education("N�o foi informado");
				} else {
					classroom.setAee_braille_system_education(rs.getString("aee_braille_system_education"));
				}
				if (rs.getString("aee_optical_and_non_optical_resources") == null
						|| rs.getString("aee_optical_and_non_optical_resources").equals("")) {
					classroom.setAee_optical_and_non_optical_resources("N�o foi informado");
				} else {
					classroom.setAee_optical_and_non_optical_resources(
							rs.getString("aee_optical_and_non_optical_resources"));
				}
				if (rs.getString("aee_mental_processes_development_strategies") == null
						|| rs.getString("aee_mental_processes_development_strategies").equals("")) {
					classroom.setAee_mental_processes_development_strategies("N�o foi informado");
				} else {
					classroom.setAee_mental_processes_development_strategies(
							rs.getString("aee_mental_processes_development_strategies"));
				}
				if (rs.getString("aee_mobility_and_orientation_techniques") == null
						|| rs.getString("aee_mobility_and_orientation_techniques").equals("")) {
					classroom.setAee_mobility_and_orientation_techniques("N�o foi informado");
				} else {
					classroom.setAee_mobility_and_orientation_techniques(
							rs.getString("aee_mobility_and_orientation_techniques"));
				}
				if (rs.getString("aee_libras") == null || rs.getString("aee_libras").equals("")) {
					classroom.setAee_libras("N�o foi informado");
				} else {
					classroom.setAee_libras(rs.getString("aee_libras"));
				}
				if (rs.getString("aee_caa_use_education") == null || rs.getString("aee_caa_use_education").equals("")) {
					classroom.setAee_caa_use_education("N�o foi informado");
				} else {
					classroom.setAee_caa_use_education(rs.getString("aee_caa_use_education"));
				}
				if (rs.getString("aee_curriculum_enrichment_strategy") == null
						|| rs.getString("aee_curriculum_enrichment_strategy").equals("")) {
					classroom.setAee_curriculum_enrichment_strategy("N�o foi informado");
				} else {
					classroom.setAee_curriculum_enrichment_strategy(rs.getString("aee_curriculum_enrichment_strategy"));
				}
				if (rs.getString("aee_soroban_use_education") == null
						|| rs.getString("aee_soroban_use_education").equals("")) {
					classroom.setAee_soroban_use_education("N�o foi informado");
				} else {
					classroom.setAee_soroban_use_education(rs.getString("aee_soroban_use_education"));
				}
				if (rs.getString("aee_usability_and_functionality_of_computer_accessible_education") == null || rs
						.getString("aee_usability_and_functionality_of_computer_accessible_education").equals("")) {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education("N�o foi informado");
				} else {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education(
							rs.getString("aee_usability_and_functionality_of_computer_accessible_education"));
				}
				if (rs.getString("aee_teaching_of_Portuguese_language_written_modality") == null
						|| rs.getString("aee_teaching_of_Portuguese_language_written_modality").equals("")) {
					classroom.setAee_teaching_of_Portuguese_language_written_modality("N�o foi informado");
				} else {
					classroom.setAee_teaching_of_Portuguese_language_written_modality(
							rs.getString("aee_teaching_of_Portuguese_language_written_modality"));
				}
				if (rs.getString("aee_strategy_for_school_environment_autonomy") == null
						|| rs.getString("aee_strategy_for_school_environment_autonomy").equals("")) {
					classroom.setAee_strategy_for_school_environment_autonomy("N�o foi informado");
				} else {
					classroom.setAee_strategy_for_school_environment_autonomy(
							rs.getString("aee_strategy_for_school_environment_autonomy"));
				}
				if (rs.getString("modality") == null) {
					classroom.setModality("N�o foi informado");
				} else if (rs.getString("modality").equals("1")) {
					classroom.setModality("Ensino Regular");
				} else if (rs.getString("modality").equals("2")) {
					classroom.setModality("Educa��o Especial - Modalidade Substitutiva");
				} else if (rs.getString("modality").equals("3")) {
					classroom.setModality("Educa��o de Jovens e Adultos (EJA)");
				}
				if (rs.getString("edcenso_stage_vs_modality_fk") == null
						|| rs.getString("edcenso_stage_vs_modality_fk").equals("")) {
					classroom.setEdcenso_stage_vs_modality_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_stage_vs_modality_fk(rs.getString("edcenso_stage_vs_modality_fk"));
				}
				if (rs.getString("edcenso_professional_education_course_fk") == null
						|| rs.getString("edcenso_professional_education_course_fk").equals("")) {
					classroom.setEdcenso_professional_education_course_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_professional_education_course_fk(
							rs.getString("edcenso_professional_education_course_fk"));
				}
				if (rs.getString("discipline_chemistry") == null || rs.getString("discipline_chemistry").equals("")
						|| rs.getString("discipline_chemistry").equals("0")) {
					classroom.setDiscipline_chemistry("N�o");
				} else {
					classroom.setDiscipline_chemistry("Sim");
				}
				if (rs.getString("discipline_physics") == null || rs.getString("discipline_physics").equals("")
						|| rs.getString("discipline_physics").equals("0")) {
					classroom.setDiscipline_physics("N�o");
				} else {
					classroom.setDiscipline_physics("Sim");
				}
				if (rs.getString("discipline_mathematics") == null || rs.getString("discipline_mathematics").equals("")
						|| rs.getString("discipline_mathematics").equals("0")) {
					classroom.setDiscipline_mathematics("N�o");
				} else {
					classroom.setDiscipline_mathematics("Sim");
				}
				if (rs.getString("discipline_biology") == null || rs.getString("discipline_biology").equals("")
						|| rs.getString("discipline_biology").equals("0")) {
					classroom.setDiscipline_biology("N�o");
				} else {
					classroom.setDiscipline_biology("Sim");
				}
				if (rs.getString("discipline_science") == null || rs.getString("discipline_science").equals("")
						|| rs.getString("discipline_science").equals("0")) {
					classroom.setDiscipline_science("N�o");
				} else {
					classroom.setDiscipline_science("Sim");
				}
				if (rs.getString("discipline_language_portuguese_literature") == null
						|| rs.getString("discipline_language_portuguese_literature").equals("")
						|| rs.getString("discipline_language_portuguese_literature").equals("0")) {
					classroom.setDiscipline_language_portuguese_literature("N�o");
				} else {
					classroom.setDiscipline_language_portuguese_literature("Sim");
				}
				if (rs.getString("discipline_foreign_language_english") == null
						|| rs.getString("discipline_foreign_language_english").equals("")
						|| rs.getString("discipline_foreign_language_english").equals("0")) {
					classroom.setDiscipline_foreign_language_english("N�o");
				} else {
					classroom.setDiscipline_foreign_language_english("Sim");
				}
				if (rs.getString("discipline_foreign_language_spanish") == null
						|| rs.getString("discipline_foreign_language_spanish").equals("")
						|| rs.getString("discipline_foreign_language_spanish").equals("0")) {
					classroom.setDiscipline_foreign_language_spanish("N�o");
				} else {
					classroom.setDiscipline_foreign_language_spanish("Sim");
				}
				if (rs.getString("discipline_foreign_language_franch") == null
						|| rs.getString("discipline_foreign_language_franch").equals("")
						|| rs.getString("discipline_foreign_language_franch").equals("0")) {
					classroom.setDiscipline_foreign_language_franch("N�o");
				} else {
					classroom.setDiscipline_foreign_language_franch("Sim");
				}
				if (rs.getString("discipline_foreign_language_other") == null
						|| rs.getString("discipline_foreign_language_other").equals("")
						|| rs.getString("discipline_foreign_language_other").equals("0")) {
					classroom.setDiscipline_foreign_language_other("N�o");
				} else {
					classroom.setDiscipline_foreign_language_other("Sim");
				}
				if (rs.getString("discipline_arts") == null || rs.getString("discipline_arts").equals("")
						|| rs.getString("discipline_arts").equals("0")) {
					classroom.setDiscipline_arts("N�o");
				} else {
					classroom.setDiscipline_arts("Sim");
				}
				if (rs.getString("discipline_physical_education") == null
						|| rs.getString("discipline_physical_education").equals("")
						|| rs.getString("discipline_physical_education").equals("0")) {
					classroom.setDiscipline_physical_education("N�o");
				} else {
					classroom.setDiscipline_physical_education("Sim");
				}
				if (rs.getString("discipline_history") == null || rs.getString("discipline_history").equals("")
						|| rs.getString("discipline_history").equals("0")) {
					classroom.setDiscipline_history("N�o");
				} else {
					classroom.setDiscipline_history("Sim");
				}
				if (rs.getString("discipline_geography") == null || rs.getString("discipline_geography").equals("")
						|| rs.getString("discipline_geography").equals("0")) {
					classroom.setDiscipline_geography("N�o");
				} else {
					classroom.setDiscipline_geography("Sim");
				}
				if (rs.getString("discipline_philosophy") == null || rs.getString("discipline_philosophy").equals("")
						|| rs.getString("discipline_philosophy").equals("0")) {
					classroom.setDiscipline_philosophy("N�o");
				} else {
					classroom.setDiscipline_philosophy("Sim");
				}
				if (rs.getString("discipline_social_study") == null
						|| rs.getString("discipline_social_study").equals("")
						|| rs.getString("discipline_social_study").equals("0")) {
					classroom.setDiscipline_social_study("N�o");
				} else {
					classroom.setDiscipline_social_study("Sim");
				}
				if (rs.getString("discipline_sociology") == null || rs.getString("discipline_sociology").equals("")
						|| rs.getString("discipline_sociology").equals("0")) {
					classroom.setDiscipline_sociology("N�o");
				} else {
					classroom.setDiscipline_sociology("Sim");
				}
				if (rs.getString("discipline_informatics") == null || rs.getString("discipline_informatics").equals("")
						|| rs.getString("discipline_informatics").equals("0")) {
					classroom.setDiscipline_informatics("N�o");
				} else {
					classroom.setDiscipline_informatics("Sim");
				}
				if (rs.getString("discipline_professional_disciplines") == null
						|| rs.getString("discipline_professional_disciplines").equals("")
						|| rs.getString("discipline_professional_disciplines").equals("0")) {
					classroom.setDiscipline_professional_disciplines("N�o");
				} else {
					classroom.setDiscipline_professional_disciplines("Sim");
				}
				if (rs.getString("discipline_special_education_and_inclusive_practices") == null
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("")
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("0")) {
					classroom.setDiscipline_special_education_and_inclusive_practices("N�o");
				} else {
					classroom.setDiscipline_special_education_and_inclusive_practices("Sim");
				}
				if (rs.getString("discipline_sociocultural_diversity") == null
						|| rs.getString("discipline_sociocultural_diversity").equals("")
						|| rs.getString("discipline_sociocultural_diversity").equals("0")) {
					classroom.setDiscipline_sociocultural_diversity("N�o");
				} else {
					classroom.setDiscipline_sociocultural_diversity("Sim");
				}
				if (rs.getString("discipline_libras") == null || rs.getString("discipline_libras").equals("")
						|| rs.getString("discipline_libras").equals("0")) {
					classroom.setDiscipline_libras("N�o");
				} else {
					classroom.setDiscipline_libras("Sim");
				}
				if (rs.getString("discipline_pedagogical") == null || rs.getString("discipline_pedagogical").equals("")
						|| rs.getString("discipline_pedagogical").equals("0")) {
					classroom.setDiscipline_pedagogical("N�o");
				} else {
					classroom.setDiscipline_pedagogical("Sim");
				}
				if (rs.getString("discipline_religious") == null || rs.getString("discipline_religious").equals("")
						|| rs.getString("discipline_religious").equals("0")) {
					classroom.setDiscipline_religious("N�o");
				} else {
					classroom.setDiscipline_religious("Sim");
				}
				if (rs.getString("discipline_native_language") == null
						|| rs.getString("discipline_native_language").equals("")
						|| rs.getString("discipline_native_language").equals("0")) {
					classroom.setDiscipline_native_language("N�o");
				} else {
					classroom.setDiscipline_native_language("Sim");
				}
				if (rs.getString("discipline_others") == null || rs.getString("discipline_others").equals("")
						|| rs.getString("discipline_others").equals("0")) {
					classroom.setDiscipline_others("N�o");
				} else {
					classroom.setDiscipline_others("Sim");
				}
				classroom.setSchool_year(rs.getString("school_year"));
				if (rs.getString("turn") == null || rs.getString("turn").equals("")) {
					classroom.setTurn("N�o foi informado");
				} else if (rs.getString("turn").equals("T")) {
					classroom.setTurn("Tarde");
				} else if (rs.getString("turn").equals("M")) {
					classroom.setTurn("Manh�");
				} else {
					classroom.setTurn("Noturno");
				}
				classroom.setCreate_date(rs.getString("create_date"));
				if (rs.getString("fkid") == null || rs.getString("fkid").equals("")
						|| rs.getString("fkid").equals("0")) {
					classroom.setFkid("N�o foi informado");
				} else {
					classroom.setFkid(rs.getString("fkid"));
				}
				if (rs.getString("calendar_fk") != null) {
					classroom.setCalendar_fk(rs.getString("calendar_fk"));
				} else {
					classroom.setCalendar_fk("N�o foi informado");
				}
				arrayClassroom.add(classroom);
			}

			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayClassroom.size() > 0) {
				classroomReturn.setValid(true);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(arrayClassroom);
			} else {
				error.add("Essa escola n�o possui turma!");

				classroomReturn.setValid(false);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(null);
			}

			arrayClassroomReturn.add(classroomReturn);

			return arrayClassroomReturn;
		} catch (Exception e) {
			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			classroomReturn.setValid(false);
			classroomReturn.setError(error);
			classroomReturn.setClassroom(null);

			arrayClassroomReturn.add(classroomReturn);
			e.printStackTrace();
			return arrayClassroomReturn;
		}
	}

	public ArrayList<ClassroomReturn> getClassrooms(Connection connection, String inep_id) throws Exception {
		try {
			String sql = "SELECT * FROM classroom WHERE INEP_ID = '" + inep_id + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Classroom classroom = new Classroom();

				classroom.setRegister_type(rs.getString("register_type"));
				classroom.setSchool_inep_fk(rs.getString("school_inep_fk"));
				if (rs.getString("inep_id") == null || rs.getString("inep_id").equals("")) {
					classroom.setInep_id("N�o foi informado");
				} else {
					classroom.setInep_id(rs.getString("inep_id"));
				}
				classroom.setId(rs.getString("id"));
				classroom.setName(rs.getString("name"));
				if (rs.getString("pedagogical_mediation_type") == null
						|| rs.getString("pedagogical_mediation_type").equals("")) {
					classroom.setPedagogical_mediation_type("N�o foi informado");
				} else {
					classroom.setPedagogical_mediation_type(rs.getString("pedagogical_mediation_type"));
				}
				classroom.setInitial_hour(rs.getString("initial_hour"));
				classroom.setInitial_minute(rs.getString("initial_minute"));
				classroom.setFinal_hour(rs.getString("final_hour"));
				classroom.setFinal_minute(rs.getString("final_minute"));
				if (rs.getString("week_days_sunday").equals("1")) {
					classroom.setWeek_days_sunday("Sim");
				} else {
					classroom.setWeek_days_sunday("N�o");
				}
				if (rs.getString("week_days_monday").equals("1")) {
					classroom.setWeek_days_monday("Sim");
				} else {
					classroom.setWeek_days_monday("N�o");
				}
				if (rs.getString("week_days_tuesday").equals("1")) {
					classroom.setWeek_days_tuesday("Sim");
				} else {
					classroom.setWeek_days_tuesday("N�o");
				}
				if (rs.getString("week_days_wednesday").equals("1")) {
					classroom.setWeek_days_wednesday("Sim");
				} else {
					classroom.setWeek_days_wednesday("N�o");
				}
				if (rs.getString("week_days_thursday").equals("1")) {
					classroom.setWeek_days_thursday("Sim");
				} else {
					classroom.setWeek_days_thursday("N�o");
				}
				if (rs.getString("week_days_friday").equals("1")) {
					classroom.setWeek_days_friday("Sim");
				} else {
					classroom.setWeek_days_friday("N�o");
				}
				if (rs.getString("week_days_saturday").equals("1")) {
					classroom.setWeek_days_saturday("Sim");
				} else {
					classroom.setWeek_days_saturday("N�o");
				}
				if (rs.getString("assistance_type").equals("0")) {
					classroom.setAssistance_type("N�o se Aplica");
				} else if (rs.getString("assistance_type").equals("1")) {
					classroom.setAssistance_type("Classe Hospitalar");
				} else if (rs.getString("assistance_type").equals("2")) {
					classroom.setAssistance_type("Unidae de Interna��o Socioeducativa");
				} else if (rs.getString("assistance_type").equals("3")) {
					classroom.setAssistance_type("Unidade Prisional");
				} else if (rs.getString("assistance_type").equals("4")) {
					classroom.setAssistance_type("Atividade Complementar");
				} else if (rs.getString("assistance_type").equals("5")) {
					classroom.setAssistance_type("Atendimento Educacional Especializado (AEE)");
				}
				if (rs.getString("mais_educacao_participator") == null
						|| rs.getString("mais_educacao_participator").equals("")) {
					classroom.setMais_educacao_participator("N�o foi informado");
				} else if (rs.getString("mais_educacao_participator").equals("0")) {
					classroom.setMais_educacao_participator("N�o");
				} else if (rs.getString("mais_educacao_participator").equals("1")) {
					classroom.setMais_educacao_participator("Sim");
				}
				if (rs.getString("complementary_activity_type_1") == null
						|| rs.getString("complementary_activity_type_1").equals("")) {
					classroom.setComplementary_activity_type_1("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_1(rs.getString("complementary_activity_type_1"));
				}
				if (rs.getString("complementary_activity_type_2") == null
						|| rs.getString("complementary_activity_type_2").equals("")) {
					classroom.setComplementary_activity_type_2("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_2(rs.getString("complementary_activity_type_2"));
				}
				if (rs.getString("complementary_activity_type_3") == null
						|| rs.getString("complementary_activity_type_3").equals("")) {
					classroom.setComplementary_activity_type_3("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_3(rs.getString("complementary_activity_type_3"));
				}
				if (rs.getString("complementary_activity_type_4") == null
						|| rs.getString("complementary_activity_type_4").equals("")) {
					classroom.setComplementary_activity_type_4("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_4(rs.getString("complementary_activity_type_4"));
				}
				if (rs.getString("complementary_activity_type_5") == null
						|| rs.getString("complementary_activity_type_5").equals("")) {
					classroom.setComplementary_activity_type_5("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_5(rs.getString("complementary_activity_type_5"));
				}
				if (rs.getString("complementary_activity_type_6") == null
						|| rs.getString("complementary_activity_type_6").equals("")) {
					classroom.setComplementary_activity_type_6("N�o foi escolhido");
				} else {
					classroom.setComplementary_activity_type_6(rs.getString("complementary_activity_type_6"));
				}
				if (rs.getString("aee_braille_system_education") == null
						|| rs.getString("aee_braille_system_education").equals("")) {
					classroom.setAee_braille_system_education("N�o foi informado");
				} else {
					classroom.setAee_braille_system_education(rs.getString("aee_braille_system_education"));
				}
				if (rs.getString("aee_optical_and_non_optical_resources") == null
						|| rs.getString("aee_optical_and_non_optical_resources").equals("")) {
					classroom.setAee_optical_and_non_optical_resources("N�o foi informado");
				} else {
					classroom.setAee_optical_and_non_optical_resources(
							rs.getString("aee_optical_and_non_optical_resources"));
				}
				if (rs.getString("aee_mental_processes_development_strategies") == null
						|| rs.getString("aee_mental_processes_development_strategies").equals("")) {
					classroom.setAee_mental_processes_development_strategies("N�o foi informado");
				} else {
					classroom.setAee_mental_processes_development_strategies(
							rs.getString("aee_mental_processes_development_strategies"));
				}
				if (rs.getString("aee_mobility_and_orientation_techniques") == null
						|| rs.getString("aee_mobility_and_orientation_techniques").equals("")) {
					classroom.setAee_mobility_and_orientation_techniques("N�o foi informado");
				} else {
					classroom.setAee_mobility_and_orientation_techniques(
							rs.getString("aee_mobility_and_orientation_techniques"));
				}
				if (rs.getString("aee_libras") == null || rs.getString("aee_libras").equals("")) {
					classroom.setAee_libras("N�o foi informado");
				} else {
					classroom.setAee_libras(rs.getString("aee_libras"));
				}
				if (rs.getString("aee_caa_use_education") == null || rs.getString("aee_caa_use_education").equals("")) {
					classroom.setAee_caa_use_education("N�o foi informado");
				} else {
					classroom.setAee_caa_use_education(rs.getString("aee_caa_use_education"));
				}
				if (rs.getString("aee_curriculum_enrichment_strategy") == null
						|| rs.getString("aee_curriculum_enrichment_strategy").equals("")) {
					classroom.setAee_curriculum_enrichment_strategy("N�o foi informado");
				} else {
					classroom.setAee_curriculum_enrichment_strategy(rs.getString("aee_curriculum_enrichment_strategy"));
				}
				if (rs.getString("aee_soroban_use_education") == null
						|| rs.getString("aee_soroban_use_education").equals("")) {
					classroom.setAee_soroban_use_education("N�o foi informado");
				} else {
					classroom.setAee_soroban_use_education(rs.getString("aee_soroban_use_education"));
				}
				if (rs.getString("aee_usability_and_functionality_of_computer_accessible_education") == null || rs
						.getString("aee_usability_and_functionality_of_computer_accessible_education").equals("")) {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education("N�o foi informado");
				} else {
					classroom.setAee_usability_and_functionality_of_computer_accessible_education(
							rs.getString("aee_usability_and_functionality_of_computer_accessible_education"));
				}
				if (rs.getString("aee_teaching_of_Portuguese_language_written_modality") == null
						|| rs.getString("aee_teaching_of_Portuguese_language_written_modality").equals("")) {
					classroom.setAee_teaching_of_Portuguese_language_written_modality("N�o foi informado");
				} else {
					classroom.setAee_teaching_of_Portuguese_language_written_modality(
							rs.getString("aee_teaching_of_Portuguese_language_written_modality"));
				}
				if (rs.getString("aee_strategy_for_school_environment_autonomy") == null
						|| rs.getString("aee_strategy_for_school_environment_autonomy").equals("")) {
					classroom.setAee_strategy_for_school_environment_autonomy("N�o foi informado");
				} else {
					classroom.setAee_strategy_for_school_environment_autonomy(
							rs.getString("aee_strategy_for_school_environment_autonomy"));
				}
				if (rs.getString("modality") == null) {
					classroom.setModality("N�o foi informado");
				} else if (rs.getString("modality").equals("1")) {
					classroom.setModality("Ensino Regular");
				} else if (rs.getString("modality").equals("2")) {
					classroom.setModality("Educa��o Especial - Modalidade Substitutiva");
				} else if (rs.getString("modality").equals("3")) {
					classroom.setModality("Educa��o de Jovens e Adultos (EJA)");
				}
				if (rs.getString("edcenso_stage_vs_modality_fk") == null
						|| rs.getString("edcenso_stage_vs_modality_fk").equals("")) {
					classroom.setEdcenso_stage_vs_modality_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_stage_vs_modality_fk(rs.getString("edcenso_stage_vs_modality_fk"));
				}
				if (rs.getString("edcenso_professional_education_course_fk") == null
						|| rs.getString("edcenso_professional_education_course_fk").equals("")) {
					classroom.setEdcenso_professional_education_course_fk("N�o foi informado");
				} else {
					classroom.setEdcenso_professional_education_course_fk(
							rs.getString("edcenso_professional_education_course_fk"));
				}
				if (rs.getString("discipline_chemistry") == null || rs.getString("discipline_chemistry").equals("")
						|| rs.getString("discipline_chemistry").equals("0")) {
					classroom.setDiscipline_chemistry("N�o");
				} else {
					classroom.setDiscipline_chemistry("Sim");
				}
				if (rs.getString("discipline_physics") == null || rs.getString("discipline_physics").equals("")
						|| rs.getString("discipline_physics").equals("0")) {
					classroom.setDiscipline_physics("N�o");
				} else {
					classroom.setDiscipline_physics("Sim");
				}
				if (rs.getString("discipline_mathematics") == null || rs.getString("discipline_mathematics").equals("")
						|| rs.getString("discipline_mathematics").equals("0")) {
					classroom.setDiscipline_mathematics("N�o");
				} else {
					classroom.setDiscipline_mathematics("Sim");
				}
				if (rs.getString("discipline_biology") == null || rs.getString("discipline_biology").equals("")
						|| rs.getString("discipline_biology").equals("0")) {
					classroom.setDiscipline_biology("N�o");
				} else {
					classroom.setDiscipline_biology("Sim");
				}
				if (rs.getString("discipline_science") == null || rs.getString("discipline_science").equals("")
						|| rs.getString("discipline_science").equals("0")) {
					classroom.setDiscipline_science("N�o");
				} else {
					classroom.setDiscipline_science("Sim");
				}
				if (rs.getString("discipline_language_portuguese_literature") == null
						|| rs.getString("discipline_language_portuguese_literature").equals("")
						|| rs.getString("discipline_language_portuguese_literature").equals("0")) {
					classroom.setDiscipline_language_portuguese_literature("N�o");
				} else {
					classroom.setDiscipline_language_portuguese_literature("Sim");
				}
				if (rs.getString("discipline_foreign_language_english") == null
						|| rs.getString("discipline_foreign_language_english").equals("")
						|| rs.getString("discipline_foreign_language_english").equals("0")) {
					classroom.setDiscipline_foreign_language_english("N�o");
				} else {
					classroom.setDiscipline_foreign_language_english("Sim");
				}
				if (rs.getString("discipline_foreign_language_spanish") == null
						|| rs.getString("discipline_foreign_language_spanish").equals("")
						|| rs.getString("discipline_foreign_language_spanish").equals("0")) {
					classroom.setDiscipline_foreign_language_spanish("N�o");
				} else {
					classroom.setDiscipline_foreign_language_spanish("Sim");
				}
				if (rs.getString("discipline_foreign_language_franch") == null
						|| rs.getString("discipline_foreign_language_franch").equals("")
						|| rs.getString("discipline_foreign_language_franch").equals("0")) {
					classroom.setDiscipline_foreign_language_franch("N�o");
				} else {
					classroom.setDiscipline_foreign_language_franch("Sim");
				}
				if (rs.getString("discipline_foreign_language_other") == null
						|| rs.getString("discipline_foreign_language_other").equals("")
						|| rs.getString("discipline_foreign_language_other").equals("0")) {
					classroom.setDiscipline_foreign_language_other("N�o");
				} else {
					classroom.setDiscipline_foreign_language_other("Sim");
				}
				if (rs.getString("discipline_arts") == null || rs.getString("discipline_arts").equals("")
						|| rs.getString("discipline_arts").equals("0")) {
					classroom.setDiscipline_arts("N�o");
				} else {
					classroom.setDiscipline_arts("Sim");
				}
				if (rs.getString("discipline_physical_education") == null
						|| rs.getString("discipline_physical_education").equals("")
						|| rs.getString("discipline_physical_education").equals("0")) {
					classroom.setDiscipline_physical_education("N�o");
				} else {
					classroom.setDiscipline_physical_education("Sim");
				}
				if (rs.getString("discipline_history") == null || rs.getString("discipline_history").equals("")
						|| rs.getString("discipline_history").equals("0")) {
					classroom.setDiscipline_history("N�o");
				} else {
					classroom.setDiscipline_history("Sim");
				}
				if (rs.getString("discipline_geography") == null || rs.getString("discipline_geography").equals("")
						|| rs.getString("discipline_geography").equals("0")) {
					classroom.setDiscipline_geography("N�o");
				} else {
					classroom.setDiscipline_geography("Sim");
				}
				if (rs.getString("discipline_philosophy") == null || rs.getString("discipline_philosophy").equals("")
						|| rs.getString("discipline_philosophy").equals("0")) {
					classroom.setDiscipline_philosophy("N�o");
				} else {
					classroom.setDiscipline_philosophy("Sim");
				}
				if (rs.getString("discipline_social_study") == null
						|| rs.getString("discipline_social_study").equals("")
						|| rs.getString("discipline_social_study").equals("0")) {
					classroom.setDiscipline_social_study("N�o");
				} else {
					classroom.setDiscipline_social_study("Sim");
				}
				if (rs.getString("discipline_sociology") == null || rs.getString("discipline_sociology").equals("")
						|| rs.getString("discipline_sociology").equals("0")) {
					classroom.setDiscipline_sociology("N�o");
				} else {
					classroom.setDiscipline_sociology("Sim");
				}
				if (rs.getString("discipline_informatics") == null || rs.getString("discipline_informatics").equals("")
						|| rs.getString("discipline_informatics").equals("0")) {
					classroom.setDiscipline_informatics("N�o");
				} else {
					classroom.setDiscipline_informatics("Sim");
				}
				if (rs.getString("discipline_professional_disciplines") == null
						|| rs.getString("discipline_professional_disciplines").equals("")
						|| rs.getString("discipline_professional_disciplines").equals("0")) {
					classroom.setDiscipline_professional_disciplines("N�o");
				} else {
					classroom.setDiscipline_professional_disciplines("Sim");
				}
				if (rs.getString("discipline_special_education_and_inclusive_practices") == null
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("")
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("0")) {
					classroom.setDiscipline_special_education_and_inclusive_practices("N�o");
				} else {
					classroom.setDiscipline_special_education_and_inclusive_practices("Sim");
				}
				if (rs.getString("discipline_sociocultural_diversity") == null
						|| rs.getString("discipline_sociocultural_diversity").equals("")
						|| rs.getString("discipline_sociocultural_diversity").equals("0")) {
					classroom.setDiscipline_sociocultural_diversity("N�o");
				} else {
					classroom.setDiscipline_sociocultural_diversity("Sim");
				}
				if (rs.getString("discipline_libras") == null || rs.getString("discipline_libras").equals("")
						|| rs.getString("discipline_libras").equals("0")) {
					classroom.setDiscipline_libras("N�o");
				} else {
					classroom.setDiscipline_libras("Sim");
				}
				if (rs.getString("discipline_pedagogical") == null || rs.getString("discipline_pedagogical").equals("")
						|| rs.getString("discipline_pedagogical").equals("0")) {
					classroom.setDiscipline_pedagogical("N�o");
				} else {
					classroom.setDiscipline_pedagogical("Sim");
				}
				if (rs.getString("discipline_religious") == null || rs.getString("discipline_religious").equals("")
						|| rs.getString("discipline_religious").equals("0")) {
					classroom.setDiscipline_religious("N�o");
				} else {
					classroom.setDiscipline_religious("Sim");
				}
				if (rs.getString("discipline_native_language") == null
						|| rs.getString("discipline_native_language").equals("")
						|| rs.getString("discipline_native_language").equals("0")) {
					classroom.setDiscipline_native_language("N�o");
				} else {
					classroom.setDiscipline_native_language("Sim");
				}
				if (rs.getString("discipline_others") == null || rs.getString("discipline_others").equals("")
						|| rs.getString("discipline_others").equals("0")) {
					classroom.setDiscipline_others("N�o");
				} else {
					classroom.setDiscipline_others("Sim");
				}
				classroom.setSchool_year(rs.getString("school_year"));
				if (rs.getString("turn") == null || rs.getString("turn").equals("")) {
					classroom.setTurn("N�o foi informado");
				} else if (rs.getString("turn").equals("T")) {
					classroom.setTurn("Tarde");
				} else if (rs.getString("turn").equals("M")) {
					classroom.setTurn("Manh�");
				} else {
					classroom.setTurn("Noturno");
				}
				classroom.setCreate_date(rs.getString("create_date"));
				if (rs.getString("fkid") == null || rs.getString("fkid").equals("")
						|| rs.getString("fkid").equals("0")) {
					classroom.setFkid("N�o foi informado");
				} else {
					classroom.setFkid(rs.getString("fkid"));
				}
				if (rs.getString("calendar_fk") != null) {
					classroom.setCalendar_fk(rs.getString("calendar_fk"));
				} else {
					classroom.setCalendar_fk("N�o foi informado");
				}
				arrayClassroom.add(classroom);
			}

			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayClassroom.size() > 0) {
				classroomReturn.setValid(true);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(arrayClassroom);
			} else {
				error.add("Essa escola n�o possui turma!");

				classroomReturn.setValid(false);
				classroomReturn.setError(error);
				classroomReturn.setClassroom(null);
			}

			arrayClassroomReturn.add(classroomReturn);

			return arrayClassroomReturn;
		} catch (Exception e) {
			ClassroomReturn classroomReturn = new ClassroomReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			classroomReturn.setValid(false);
			classroomReturn.setError(error);
			classroomReturn.setClassroom(null);

			arrayClassroomReturn.add(classroomReturn);
			e.printStackTrace();
			return arrayClassroomReturn;
		}
	}

	// --------------- DISCIPLINE ------------------ //
	public ArrayList<DisciplinesByClassReturn> getDisciplinesByClassID(Connection connection, String id)
			throws Exception {
		try {
			String sql = "SELECT id, discipline_chemistry, discipline_physics, discipline_mathematics, "
					+ "discipline_biology, discipline_science, discipline_language_portuguese_literature, "
					+ "discipline_foreign_language_english, discipline_foreign_language_spanish, discipline_foreign_language_franch, "
					+ "discipline_foreign_language_other, discipline_arts, discipline_physical_education, discipline_history, "
					+ "discipline_geography, discipline_philosophy, discipline_social_study, discipline_sociology, "
					+ "discipline_informatics, discipline_professional_disciplines, discipline_special_education_and_inclusive_practices, "
					+ "discipline_sociocultural_diversity, discipline_libras, discipline_pedagogical, discipline_religious, discipline_native_language, "
					+ "discipline_others FROM classroom WHERE id = '" + id + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				DisciplinesByClass disciplinesByClass = new DisciplinesByClass();

				disciplinesByClass.setId(id);
				if (rs.getString("discipline_chemistry") == null || rs.getString("discipline_chemistry").equals("")
						|| rs.getString("discipline_chemistry").equals("0")) {
					disciplinesByClass.setDiscipline_chemistry("");
				} else {
					disciplinesByClass.setDiscipline_chemistry("QU�MICA");
				}
				if (rs.getString("discipline_physics") == null || rs.getString("discipline_physics").equals("")
						|| rs.getString("discipline_physics").equals("0")) {
					disciplinesByClass.setDiscipline_physics("");
				} else {
					disciplinesByClass.setDiscipline_physics("F�SICA");
				}
				if (rs.getString("discipline_mathematics") == null || rs.getString("discipline_mathematics").equals("")
						|| rs.getString("discipline_mathematics").equals("0")) {
					disciplinesByClass.setDiscipline_mathematics("");
				} else {
					disciplinesByClass.setDiscipline_mathematics("MATEM�TICA");
				}
				if (rs.getString("discipline_biology") == null || rs.getString("discipline_biology").equals("")
						|| rs.getString("discipline_biology").equals("0")) {
					disciplinesByClass.setDiscipline_biology("");
				} else {
					disciplinesByClass.setDiscipline_biology("BIOLOGIA");
				}
				if (rs.getString("discipline_science") == null || rs.getString("discipline_science").equals("")
						|| rs.getString("discipline_science").equals("0")) {
					disciplinesByClass.setDiscipline_science("");
				} else {
					disciplinesByClass.setDiscipline_science("CI�NCIA");
				}
				if (rs.getString("discipline_language_portuguese_literature") == null
						|| rs.getString("discipline_language_portuguese_literature").equals("")
						|| rs.getString("discipline_language_portuguese_literature").equals("0")) {
					disciplinesByClass.setDiscipline_language_portuguese_literature("");
				} else {
					disciplinesByClass.setDiscipline_language_portuguese_literature("L�NGUA/LITERATURA PORTUGUESA");
				}
				if (rs.getString("discipline_foreign_language_english") == null
						|| rs.getString("discipline_foreign_language_english").equals("")
						|| rs.getString("discipline_foreign_language_english").equals("0")) {
					disciplinesByClass.setDiscipline_foreign_language_english("");
				} else {
					disciplinesByClass
							.setDiscipline_foreign_language_english("L�NGUA/LITERATURA ESTRANGUEIRA - INGL�S");
				}
				if (rs.getString("discipline_foreign_language_spanish") == null
						|| rs.getString("discipline_foreign_language_spanish").equals("")
						|| rs.getString("discipline_foreign_language_spanish").equals("0")) {
					disciplinesByClass.setDiscipline_foreign_language_spanish("");
				} else {
					disciplinesByClass
							.setDiscipline_foreign_language_spanish("L�NGUA/LITERATURA ESTRANGUEIRA - ESPANHOL");
				}
				if (rs.getString("discipline_foreign_language_franch") == null
						|| rs.getString("discipline_foreign_language_franch").equals("")
						|| rs.getString("discipline_foreign_language_franch").equals("0")) {
					disciplinesByClass.setDiscipline_foreign_language_franch("");
				} else {
					disciplinesByClass
							.setDiscipline_foreign_language_franch("L�NGUA/LITERATURA ESTRANGUEIRA - FRANC�S");
				}
				if (rs.getString("discipline_foreign_language_other") == null
						|| rs.getString("discipline_foreign_language_other").equals("")
						|| rs.getString("discipline_foreign_language_other").equals("0")) {
					disciplinesByClass.setDiscipline_foreign_language_other("");
				} else {
					disciplinesByClass.setDiscipline_foreign_language_other("L�NGUA/LITERATURA ESTRANGUEIRA - OUTRA");
				}
				if (rs.getString("discipline_arts") == null || rs.getString("discipline_arts").equals("")
						|| rs.getString("discipline_arts").equals("0")) {
					disciplinesByClass.setDiscipline_arts("");
				} else {
					disciplinesByClass.setDiscipline_arts("ARTES");
				}
				if (rs.getString("discipline_physical_education") == null
						|| rs.getString("discipline_physical_education").equals("")
						|| rs.getString("discipline_physical_education").equals("0")) {
					disciplinesByClass.setDiscipline_physical_education("");
				} else {
					disciplinesByClass.setDiscipline_physical_education("EDUCA��O F�SICA");
				}
				if (rs.getString("discipline_history") == null || rs.getString("discipline_history").equals("")
						|| rs.getString("discipline_history").equals("0")) {
					disciplinesByClass.setDiscipline_history("");
				} else {
					disciplinesByClass.setDiscipline_history("HIST�RIA");
				}
				if (rs.getString("discipline_geography") == null || rs.getString("discipline_geography").equals("")
						|| rs.getString("discipline_geography").equals("0")) {
					disciplinesByClass.setDiscipline_geography("");
				} else {
					disciplinesByClass.setDiscipline_geography("GEOGRAFIA");
				}
				if (rs.getString("discipline_philosophy") == null || rs.getString("discipline_philosophy").equals("")
						|| rs.getString("discipline_philosophy").equals("0")) {
					disciplinesByClass.setDiscipline_philosophy("");
				} else {
					disciplinesByClass.setDiscipline_philosophy("FILOSIFIA");
				}
				if (rs.getString("discipline_social_study") == null
						|| rs.getString("discipline_social_study").equals("")
						|| rs.getString("discipline_social_study").equals("0")) {
					disciplinesByClass.setDiscipline_social_study("");
				} else {
					disciplinesByClass.setDiscipline_social_study("ESTUDOS SOCIAIS");
				}
				if (rs.getString("discipline_sociology") == null || rs.getString("discipline_sociology").equals("")
						|| rs.getString("discipline_sociology").equals("0")) {
					disciplinesByClass.setDiscipline_sociology("");
				} else {
					disciplinesByClass.setDiscipline_sociology("SOCIOLOGIA");
				}
				if (rs.getString("discipline_informatics") == null || rs.getString("discipline_informatics").equals("")
						|| rs.getString("discipline_informatics").equals("0")) {
					disciplinesByClass.setDiscipline_informatics("");
				} else {
					disciplinesByClass.setDiscipline_informatics("INFORM�TICA");
				}
				if (rs.getString("discipline_professional_disciplines") == null
						|| rs.getString("discipline_professional_disciplines").equals("")
						|| rs.getString("discipline_professional_disciplines").equals("0")) {
					disciplinesByClass.setDiscipline_professional_disciplines("");
				} else {
					disciplinesByClass.setDiscipline_professional_disciplines("DISCIPLINAS PROFISSIONALIZANTES");
				}
				if (rs.getString("discipline_special_education_and_inclusive_practices") == null
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("")
						|| rs.getString("discipline_special_education_and_inclusive_practices").equals("0")) {
					disciplinesByClass.setDiscipline_special_education_and_inclusive_practices("");
				} else {
					disciplinesByClass.setDiscipline_special_education_and_inclusive_practices(
							"DISCIPLINAS VOLTADAS AO ATENDIMENTO EDUCACIONAIS");
				}
				if (rs.getString("discipline_sociocultural_diversity") == null
						|| rs.getString("discipline_sociocultural_diversity").equals("")
						|| rs.getString("discipline_sociocultural_diversity").equals("0")) {
					disciplinesByClass.setDiscipline_sociocultural_diversity("");
				} else {
					disciplinesByClass
							.setDiscipline_sociocultural_diversity("DISCIPLINAS VOLTADAS � DIVERSIDADE SOCIOCULTURAL");
				}
				if (rs.getString("discipline_libras") == null || rs.getString("discipline_libras").equals("")
						|| rs.getString("discipline_libras").equals("0")) {
					disciplinesByClass.setDiscipline_libras("");
				} else {
					disciplinesByClass.setDiscipline_libras("LIBRAS");
				}
				if (rs.getString("discipline_pedagogical") == null || rs.getString("discipline_pedagogical").equals("")
						|| rs.getString("discipline_pedagogical").equals("0")) {
					disciplinesByClass.setDiscipline_pedagogical("");
				} else {
					disciplinesByClass.setDiscipline_pedagogical("DISCIPLINAS PEDAG�GICAS");
				}
				if (rs.getString("discipline_religious") == null || rs.getString("discipline_religious").equals("")
						|| rs.getString("discipline_religious").equals("0")) {
					disciplinesByClass.setDiscipline_religious("");
				} else {
					disciplinesByClass.setDiscipline_religious("ENSINO RELIGIOSO");
				}
				if (rs.getString("discipline_native_language") == null
						|| rs.getString("discipline_native_language").equals("")
						|| rs.getString("discipline_native_language").equals("0")) {
					disciplinesByClass.setDiscipline_native_language("");
				} else {
					disciplinesByClass.setDiscipline_native_language("L�NGUA IND�GENA");
				}
				if (rs.getString("discipline_others") == null || rs.getString("discipline_others").equals("")
						|| rs.getString("discipline_others").equals("0")) {
					disciplinesByClass.setDiscipline_others("");
				} else {
					disciplinesByClass.setDiscipline_others("OUTRAS");
				}

				arrayDisciplinesByClass.add(disciplinesByClass);
			}

			DisciplinesByClassReturn disciplinesByClassReturn = new DisciplinesByClassReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayDisciplinesByClass.size() > 0) {
				disciplinesByClassReturn.setValid(true);
				disciplinesByClassReturn.setError(error);
				disciplinesByClassReturn.setDisciplines(arrayDisciplinesByClass);
			} else {
				error.add("Essa turma n�o possui disciplina!");

				disciplinesByClassReturn.setValid(false);
				disciplinesByClassReturn.setError(error);
				disciplinesByClassReturn.setDisciplines(null);
			}

			arrayDisciplinesByClassReturn.add(disciplinesByClassReturn);

			return arrayDisciplinesByClassReturn;
		} catch (Exception e) {
			DisciplinesByClassReturn disciplinesByClassReturn = new DisciplinesByClassReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			disciplinesByClassReturn.setValid(false);
			disciplinesByClassReturn.setError(error);
			disciplinesByClassReturn.setDisciplines(null);

			arrayDisciplinesByClassReturn.add(disciplinesByClassReturn);
			e.printStackTrace();
			return arrayDisciplinesByClassReturn;
		}
	}

	public ArrayList<InstructorTeachingDataReturn> getInstructorTeachingData(Connection connection, String id)
			throws Exception {
		try {
			String sql = "SELECT classroom_id_fk, discipline_1_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_1_fk = E.id) discipline_1_name, "
					+ "discipline_2_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_2_fk = E.id) discipline_2_name, "
					+ "discipline_3_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_3_fk = E.id) discipline_3_name, "
					+ "discipline_4_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_4_fk = E.id) discipline_4_name, "
					+ "discipline_5_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_5_fk = E.id) discipline_5_name, "
					+ "discipline_6_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_6_fk = E.id) discipline_6_name, "
					+ "discipline_7_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_7_fk = E.id) discipline_7_name, "
					+ "discipline_8_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_8_fk = E.id) discipline_8_name, "
					+ "discipline_9_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_9_fk = E.id) discipline_9_name, "
					+ "discipline_10_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_10_fk = E.id) discipline_10_name, "
					+ "discipline_11_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_11_fk = E.id) discipline_11_name, "
					+ "discipline_12_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_12_fk = E.id) discipline_12_name, "
					+ "discipline_13_fk, (SELECT name FROM edcenso_discipline E WHERE T.discipline_13_fk = E.id) discipline_13_name FROM instructor_teaching_data T WHERE T.instructor_fk = '"
					+ id + "';";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				if (rs.getString("discipline_1_fk") == null && rs.getString("discipline_2_fk") == null
						&& rs.getString("discipline_3_fk") == null && rs.getString("discipline_4_fk") == null
						&& rs.getString("discipline_5_fk") == null && rs.getString("discipline_6_fk") == null
						&& rs.getString("discipline_7_fk") == null && rs.getString("discipline_8_fk") == null
						&& rs.getString("discipline_9_fk") == null && rs.getString("discipline_10_fk") == null
						&& rs.getString("discipline_11_fk") == null && rs.getString("discipline_12_fk") == null
						&& rs.getString("discipline_13_fk") == null) {
					continue;
				} else {
					InstructorTeachingData instructorTeachingData = new InstructorTeachingData();

					instructorTeachingData.setClassroom_id_fk(rs.getString("classroom_id_fk"));
					instructorTeachingData.setDiscipline_1_fk(rs.getString("discipline_1_fk"));
					instructorTeachingData.setDiscipline_1_name(rs.getString("discipline_1_name"));
					instructorTeachingData.setDiscipline_2_fk(rs.getString("discipline_2_fk"));
					instructorTeachingData.setDiscipline_2_name(rs.getString("discipline_2_name"));
					instructorTeachingData.setDiscipline_3_fk(rs.getString("discipline_3_fk"));
					instructorTeachingData.setDiscipline_3_name(rs.getString("discipline_3_name"));
					instructorTeachingData.setDiscipline_4_fk(rs.getString("discipline_4_fk"));
					instructorTeachingData.setDiscipline_4_name(rs.getString("discipline_4_name"));
					instructorTeachingData.setDiscipline_5_fk(rs.getString("discipline_5_fk"));
					instructorTeachingData.setDiscipline_5_name(rs.getString("discipline_5_name"));
					instructorTeachingData.setDiscipline_6_fk(rs.getString("discipline_6_fk"));
					instructorTeachingData.setDiscipline_6_name(rs.getString("discipline_6_name"));
					instructorTeachingData.setDiscipline_7_fk(rs.getString("discipline_7_fk"));
					instructorTeachingData.setDiscipline_7_name(rs.getString("discipline_7_name"));
					instructorTeachingData.setDiscipline_8_fk(rs.getString("discipline_8_fk"));
					instructorTeachingData.setDiscipline_8_name(rs.getString("discipline_8_name"));
					instructorTeachingData.setDiscipline_9_fk(rs.getString("discipline_9_fk"));
					instructorTeachingData.setDiscipline_9_name(rs.getString("discipline_9_name"));
					instructorTeachingData.setDiscipline_10_fk(rs.getString("discipline_10_fk"));
					instructorTeachingData.setDiscipline_10_name(rs.getString("discipline_10_name"));
					instructorTeachingData.setDiscipline_11_fk(rs.getString("discipline_11_fk"));
					instructorTeachingData.setDiscipline_11_name(rs.getString("discipline_11_name"));
					instructorTeachingData.setDiscipline_12_fk(rs.getString("discipline_12_fk"));
					instructorTeachingData.setDiscipline_12_name(rs.getString("discipline_12_name"));
					instructorTeachingData.setDiscipline_13_fk(rs.getString("discipline_13_fk"));
					instructorTeachingData.setDiscipline_13_name(rs.getString("discipline_13_name"));

					arrayInstructorTeachingData.add(instructorTeachingData);
				}
			}

			InstructorTeachingDataReturn instructorTeachingDataReturn = new InstructorTeachingDataReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayInstructorTeachingData.size() > 0) {
				instructorTeachingDataReturn.setValid(true);
				instructorTeachingDataReturn.setError(error);
				instructorTeachingDataReturn.setDiscipline(arrayInstructorTeachingData);
			} else {
				error.add("Esse professor n�o possui disciplina");

				instructorTeachingDataReturn.setValid(false);
				instructorTeachingDataReturn.setError(error);
				instructorTeachingDataReturn.setDiscipline(null);
			}

			arrayInstructorTeachingDataReturn.add(instructorTeachingDataReturn);

			return arrayInstructorTeachingDataReturn;
		} catch (Exception e) {
			InstructorTeachingDataReturn instructorTeachingDataReturn = new InstructorTeachingDataReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			instructorTeachingDataReturn.setValid(false);
			instructorTeachingDataReturn.setError(error);
			instructorTeachingDataReturn.setDiscipline(null);

			arrayInstructorTeachingDataReturn.add(instructorTeachingDataReturn);
			e.printStackTrace();
			return arrayInstructorTeachingDataReturn;
		}
	}

	// --------------- SCHOOL ------------------ //
	public ArrayList<SchoolReturn> getSchools(Connection connection) throws Exception {
		try {
			String sql = "SELECT * FROM school_identification;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				School school = new School();

				school.setRegister_type(rs.getString("register_type"));
				school.setInep_id(rs.getString("inep_id"));
				school.setManager_cpf(rs.getString("manager_cpf"));
				school.setManager_name(rs.getString("manager_name"));
				if (rs.getString("manager_role").equals("1")) {
					school.setManager_role("Diretor");
				} else if (rs.getString("manager_role").equals("2")) {
					school.setManager_role("Outro Cargo");
				} else {
					school.setManager_role("N�o foi informado");
				}
				if (rs.getString("manager_email").equals("")) {
					school.setManager_email("N�o foi informado");
				} else {
					school.setManager_email(rs.getString("manager_email"));
				}
				if (rs.getString("situation").equals("1")) {
					school.setSituation("Em Atividade");
				} else if (rs.getString("situation").equals("2")) {
					school.setSituation("Paralisada");
				} else if (rs.getString("situation").equals("3")) {
					school.setSituation("Extinta");
				} else {
					school.setSituation("N�o foi informado");
				}
				if (rs.getString("initial_date") == null || rs.getString("initial_date").equals("")) {
					school.setInitial_date("N�o foi informado");
				} else {
					school.setInitial_date(rs.getString("initial_date"));
				}
				if (rs.getString("final_date") == null || rs.getString("final_date").equals("")) {
					school.setFinal_date("N�o foi informado");
				} else {
					school.setFinal_date(rs.getString("final_date"));
				}
				school.setName(rs.getString("name"));
				if (rs.getString("latitude") == null || rs.getString("latitude").equals("")) {
					school.setLatitude("N�o foi informado");
				} else {
					school.setLatitude(rs.getString("latitude"));
				}
				if (rs.getString("longitude") == null || rs.getString("longitude").equals("")) {
					school.setLongitude("N�o foi informado");
				} else {
					school.setLongitude(rs.getString("longitude"));
				}
				school.setCep(rs.getString("cep"));
				school.setAddress(rs.getString("address"));
				if (rs.getString("address_number") == null || rs.getString("address_number").equals("")) {
					school.setAddress_number("N�o foi informado");
				} else {
					school.setAddress_number(rs.getString("address_number"));
				}
				if (rs.getString("address_complement") == null || rs.getString("address_complement").equals("")) {
					school.setAddress_complement("N�o foi informado");
				} else {
					school.setAddress_complement(rs.getString("address_complement"));
				}
				if (rs.getString("address_neighborhood") == null || rs.getString("address_neighborhood").equals("")) {
					school.setAddress_neighborhood("N�o foi informado");
				} else {
					school.setAddress_neighborhood(rs.getString("address_neighborhood"));
				}
				school.setEdcenso_uf_fk(rs.getString("edcenso_uf_fk"));
				school.setEdcenso_city_fk(rs.getString("edcenso_city_fk"));
				school.setEdcenso_district_fk(rs.getString("edcenso_district_fk"));
				if (rs.getString("ddd") == null || rs.getString("ddd").equals("")) {
					school.setDdd("N�o foi informado");
				} else {
					school.setDdd(rs.getString("ddd"));
				}
				if (rs.getString("phone_number") == null || rs.getString("phone_number").equals("")) {
					school.setPhone_number("N�o foi informado");
				} else {
					school.setPhone_number(rs.getString("phone_number"));
				}
				if (rs.getString("public_phone_number") == null || rs.getString("public_phone_number").equals("")) {
					school.setPublic_phone_number("N�o foi informado");
				} else {
					school.setPublic_phone_number(rs.getString("public_phone_number"));
				}
				if (rs.getString("other_phone_number") == null || rs.getString("other_phone_number").equals("")) {
					school.setOther_phone_number("N�o foi informado");
				} else {
					school.setOther_phone_number(rs.getString("other_phone_number"));
				}
				if (rs.getString("fax_number") == null || rs.getString("fax_number").equals("")) {
					school.setFax_number("N�o foi informado");
				} else {
					school.setFax_number(rs.getString("fax_number"));
				}
				if (rs.getString("email") == null || rs.getString("email").equals("")) {
					school.setEmail("N�o foi informado");
				} else {
					school.setEmail(rs.getString("email"));
				}
				if (rs.getString("edcenso_regional_education_organ_fk") == null
						|| rs.getString("edcenso_regional_education_organ_fk").equals("")) {
					school.setEdcenso_regional_education_organ_fk("N�o foi informado");
				} else {
					school.setEdcenso_regional_education_organ_fk(rs.getString("edcenso_regional_education_organ_fk"));
				}
				if (rs.getString("administrative_dependence").equals("1")) {
					school.setAdministrative_dependence("Federal");
				} else if (rs.getString("administrative_dependence").equals("2")) {
					school.setAdministrative_dependence("Estadual");
				} else if (rs.getString("administrative_dependence").equals("3")) {
					school.setAdministrative_dependence("Municipal");
				} else {
					school.setAdministrative_dependence("Privada");
				}
				if (rs.getString("location").equals("1")) {
					school.setLocation("Urbano");
				} else if (rs.getString("location").equals("2")) {
					school.setLocation("Rural");
				}
				if (rs.getString("private_school_category") == null
						|| rs.getString("private_school_category").equals("")) {
					school.setPrivate_school_category("N�o foi informado");
				} else if (rs.getString("private_school_category").equals("1")) {
					school.setPrivate_school_category("Particular");
				} else if (rs.getString("private_school_category").equals("2")) {
					school.setPrivate_school_category("Comunit�ria");
				} else if (rs.getString("private_school_category").equals("3")) {
					school.setPrivate_school_category("Confessional");
				} else if (rs.getString("private_school_category").equals("4")) {
					school.setPrivate_school_category("Filantr�pica");
				}
				if (rs.getString("public_contract") == null || rs.getString("public_contract").equals("")) {
					school.setPublic_contract("N�o foi informado");
				} else if (rs.getString("public_contract").equals("1")) {
					school.setPublic_contract("Estadual");
				} else if (rs.getString("public_contract").equals("2")) {
					school.setPublic_contract("Municipal");
				} else if (rs.getString("public_contract").equals("3")) {
					school.setPublic_contract("Estadual e Municipal");
				}
				if (rs.getString("private_school_business_or_individual") == null
						|| rs.getString("private_school_business_or_individual").equals("")) {
					school.setPrivate_school_business_or_individual("N�o foi informado");
				} else if (rs.getString("private_school_business_or_individual").equals("0")) {
					school.setPrivate_school_business_or_individual("N�o");
				} else if (rs.getString("private_school_business_or_individual").equals("1")) {
					school.setPrivate_school_business_or_individual("Sim");
				}
				if (rs.getString("private_school_syndicate_or_association") == null
						|| rs.getString("private_school_syndicate_or_association").equals("")) {
					school.setPrivate_school_syndicate_or_association("N�o foi informado");
				} else if (rs.getString("private_school_syndicate_or_association").equals("0")) {
					school.setPrivate_school_syndicate_or_association("N�o");
				} else if (rs.getString("private_school_syndicate_or_association").equals("1")) {
					school.setPrivate_school_syndicate_or_association("Sim");
				}
				if (rs.getString("private_school_ong_or_oscip") == null
						|| rs.getString("private_school_ong_or_oscip").equals("")) {
					school.setPrivate_school_ong_or_oscip("N�o foi informado");
				} else if (rs.getString("private_school_ong_or_oscip").equals("0")) {
					school.setPrivate_school_ong_or_oscip("N�o");
				} else if (rs.getString("private_school_ong_or_oscip").equals("1")) {
					school.setPrivate_school_ong_or_oscip("Sim");
				}
				if (rs.getString("private_school_non_profit_institutions") == null
						|| rs.getString("private_school_non_profit_institutions").equals("")) {
					school.setPrivate_school_non_profit_institutions("N�o foi informado");
				} else if (rs.getString("private_school_non_profit_institutions").equals("0")) {
					school.setPrivate_school_non_profit_institutions("N�o");
				} else if (rs.getString("private_school_non_profit_institutions").equals("1")) {
					school.setPrivate_school_non_profit_institutions("Sim");
				}
				if (rs.getString("private_school_s_system") == null
						|| rs.getString("private_school_s_system").equals("")) {
					school.setPrivate_school_s_system("N�o foi informado");
				} else if (rs.getString("private_school_s_system").equals("0")) {
					school.setPrivate_school_s_system("N�o");
				} else if (rs.getString("private_school_s_system").equals("1")) {
					school.setPrivate_school_s_system("Sim");
				}
				if (rs.getString("private_school_maintainer_cnpj") == null
						|| rs.getString("private_school_maintainer_cnpj").equals("")) {
					school.setPrivate_school_maintainer_cnpj("N�o informado");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("0")) {
					school.setPrivate_school_maintainer_cnpj("N�o");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("1")) {
					school.setPrivate_school_maintainer_cnpj("Sim");
				}
				if (rs.getString("private_school_cnpj") == null || rs.getString("private_school_cnpj").equals("")) {
					school.setPrivate_school_cnpj("N�o informado");
				} else if (rs.getString("private_school_cnpj").equals("0")) {
					school.setPrivate_school_cnpj("N�o");
				} else if (rs.getString("private_school_cnpj").equals("1")) {
					school.setPrivate_school_cnpj("Sim");
				}
				if (rs.getString("regulation") == null || rs.getString("regulation").equals("")) {
					school.setRegulation("N�o foi informado");
				} else if (rs.getString("regulation").equals("1")) {
					school.setRegulation("N�o");
				} else if (rs.getString("regulation").equals("2")) {
					school.setRegulation("Sim");
				} else if (rs.getString("regulation").equals("3")) {
					school.setRegulation("Em tramita��o");
				}
				if (rs.getString("offer_or_linked_unity") == null || rs.getString("offer_or_linked_unity").equals("-1")
						|| rs.getString("offer_or_linked_unity").equals("")) {
					school.setOffer_or_linked_unity("N�o foi informado");
				} else if (rs.getString("offer_or_linked_unity").equals("1")) {
					school.setOffer_or_linked_unity("Unidade vinculada a escola de Educa��o B�sica");
				} else if (rs.getString("offer_or_linked_unity").equals("2")) {
					school.setOffer_or_linked_unity("Unidade ofertante de Ensino Superior");
				} else if (rs.getString("offer_or_linked_unity").equals("0")) {
					school.setOffer_or_linked_unity("N�o");
				}
				if (rs.getString("inep_head_school") == null || rs.getString("inep_head_school").equals("")) {
					school.setInep_head_school("N�o foi informado");
				} else {
					school.setInep_head_school(rs.getString("inep_head_school"));
				}
				if (rs.getString("ies_code") == null || rs.getString("ies_code").equals("")) {
					school.setIes_code("N�o foi informado");
				} else {
					school.setIes_code(rs.getString("ies_code"));
				}
				if (rs.getString("logo_file_name").equals("") || rs.getString("logo_file_name") == null) {
					school.setLogo_file_name("N�o foi informado");
				} else {
					school.setLogo_file_name(rs.getString("logo_file_name"));
				}
				if (rs.getString("logo_file_type").equals("") || rs.getString("logo_file_type") == null) {
					school.setLogo_file_type("N�o foi informado");
				} else {
					school.setLogo_file_type(rs.getString("logo_file_type"));
				}
				// school.setLogo_file_content(rs.getString("logo_file_content"));
				if (rs.getString("act_of_acknowledgement") == null
						|| rs.getString("act_of_acknowledgement").equals("")) {
					school.setAct_of_acknowledgement("N�o foi informado");
				} else {
					school.setAct_of_acknowledgement(rs.getString("act_of_acknowledgement"));
				}

				arraySchool.add(school);
			}

			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arraySchool.size() > 0) {
				schoolReturn.setValid(true);
				schoolReturn.setError(error);
				schoolReturn.setSchool(arraySchool);
			} else {
				error.add("N�o h� escola cadastrada!");

				schoolReturn.setValid(false);
				schoolReturn.setError(error);
				schoolReturn.setSchool(null);
			}

			arraySchoolReturn.add(schoolReturn);

			return arraySchoolReturn;
		} catch (Exception e) {
			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			schoolReturn.setValid(false);
			schoolReturn.setError(error);
			schoolReturn.setSchool(null);

			arraySchoolReturn.add(schoolReturn);
			e.printStackTrace();
			return arraySchoolReturn;
		}
	}

	public ArrayList<SchoolReturn> getSchools(Connection connection, String inep_id) throws Exception {
		try {
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM school_identification WHERE INEP_ID = '" + inep_id + "'");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				School school = new School();

				school.setRegister_type(rs.getString("register_type"));
				school.setInep_id(rs.getString("inep_id"));
				school.setManager_cpf(rs.getString("manager_cpf"));
				school.setManager_name(rs.getString("manager_name"));
				if (rs.getString("manager_role").equals("1")) {
					school.setManager_role("Diretor");
				} else if (rs.getString("manager_role").equals("2")) {
					school.setManager_role("Outro Cargo");
				} else {
					school.setManager_role("N�o foi informado");
				}
				if (rs.getString("manager_email").equals("")) {
					school.setManager_email("N�o foi informado");
				} else {
					school.setManager_email(rs.getString("manager_email"));
				}
				if (rs.getString("situation").equals("1")) {
					school.setSituation("Em Atividade");
				} else if (rs.getString("situation").equals("2")) {
					school.setSituation("Paralisada");
				} else if (rs.getString("situation").equals("3")) {
					school.setSituation("Extinta");
				} else {
					school.setSituation("N�o foi informado");
				}
				if (rs.getString("initial_date") == null || rs.getString("initial_date").equals("")) {
					school.setInitial_date("N�o foi informado");
				} else {
					school.setInitial_date(rs.getString("initial_date"));
				}
				if (rs.getString("final_date") == null || rs.getString("final_date").equals("")) {
					school.setFinal_date("N�o foi informado");
				} else {
					school.setFinal_date(rs.getString("final_date"));
				}
				school.setName(rs.getString("name"));
				if (rs.getString("latitude") == null || rs.getString("latitude").equals("")) {
					school.setLatitude("N�o foi informado");
				} else {
					school.setLatitude(rs.getString("latitude"));
				}
				if (rs.getString("longitude") == null || rs.getString("longitude").equals("")) {
					school.setLongitude("N�o foi informado");
				} else {
					school.setLongitude(rs.getString("longitude"));
				}
				school.setCep(rs.getString("cep"));
				school.setAddress(rs.getString("address"));
				if (rs.getString("address_number") == null || rs.getString("address_number").equals("")) {
					school.setAddress_number("N�o foi informado");
				} else {
					school.setAddress_number(rs.getString("address_number"));
				}
				if (rs.getString("address_complement") == null || rs.getString("address_complement").equals("")) {
					school.setAddress_complement("N�o foi informado");
				} else {
					school.setAddress_complement(rs.getString("address_complement"));
				}
				if (rs.getString("address_neighborhood") == null || rs.getString("address_neighborhood").equals("")) {
					school.setAddress_neighborhood("N�o foi informado");
				} else {
					school.setAddress_neighborhood(rs.getString("address_neighborhood"));
				}
				school.setEdcenso_uf_fk(rs.getString("edcenso_uf_fk"));
				school.setEdcenso_city_fk(rs.getString("edcenso_city_fk"));
				school.setEdcenso_district_fk(rs.getString("edcenso_district_fk"));
				if (rs.getString("ddd") == null || rs.getString("ddd").equals("")) {
					school.setDdd("N�o foi informado");
				} else {
					school.setDdd(rs.getString("ddd"));
				}
				if (rs.getString("phone_number") == null || rs.getString("phone_number").equals("")) {
					school.setPhone_number("N�o foi informado");
				} else {
					school.setPhone_number(rs.getString("phone_number"));
				}
				if (rs.getString("public_phone_number") == null || rs.getString("public_phone_number").equals("")) {
					school.setPublic_phone_number("N�o foi informado");
				} else {
					school.setPublic_phone_number(rs.getString("public_phone_number"));
				}
				if (rs.getString("other_phone_number") == null || rs.getString("other_phone_number").equals("")) {
					school.setOther_phone_number("N�o foi informado");
				} else {
					school.setOther_phone_number(rs.getString("other_phone_number"));
				}
				if (rs.getString("fax_number") == null || rs.getString("fax_number").equals("")) {
					school.setFax_number("N�o foi informado");
				} else {
					school.setFax_number(rs.getString("fax_number"));
				}
				if (rs.getString("email") == null || rs.getString("email").equals("")) {
					school.setEmail("N�o foi informado");
				} else {
					school.setEmail(rs.getString("email"));
				}
				if (rs.getString("edcenso_regional_education_organ_fk") == null
						|| rs.getString("edcenso_regional_education_organ_fk").equals("")) {
					school.setEdcenso_regional_education_organ_fk("N�o foi informado");
				} else {
					school.setEdcenso_regional_education_organ_fk(rs.getString("edcenso_regional_education_organ_fk"));
				}
				if (rs.getString("administrative_dependence").equals("1")) {
					school.setAdministrative_dependence("Federal");
				} else if (rs.getString("administrative_dependence").equals("2")) {
					school.setAdministrative_dependence("Estadual");
				} else if (rs.getString("administrative_dependence").equals("3")) {
					school.setAdministrative_dependence("Municipal");
				} else {
					school.setAdministrative_dependence("Privada");
				}
				if (rs.getString("location").equals("1")) {
					school.setLocation("Urbano");
				} else if (rs.getString("location").equals("2")) {
					school.setLocation("Rural");
				}
				if (rs.getString("private_school_category") == null
						|| rs.getString("private_school_category").equals("")) {
					school.setPrivate_school_category("N�o foi informado");
				} else if (rs.getString("private_school_category").equals("1")) {
					school.setPrivate_school_category("Particular");
				} else if (rs.getString("private_school_category").equals("2")) {
					school.setPrivate_school_category("Comunit�ria");
				} else if (rs.getString("private_school_category").equals("3")) {
					school.setPrivate_school_category("Confessional");
				} else if (rs.getString("private_school_category").equals("4")) {
					school.setPrivate_school_category("Filantr�pica");
				}
				if (rs.getString("public_contract") == null || rs.getString("public_contract").equals("")) {
					school.setPublic_contract("N�o foi informado");
				} else if (rs.getString("public_contract").equals("1")) {
					school.setPublic_contract("Estadual");
				} else if (rs.getString("public_contract").equals("2")) {
					school.setPublic_contract("Municipal");
				} else if (rs.getString("public_contract").equals("3")) {
					school.setPublic_contract("Estadual e Municipal");
				}
				if (rs.getString("private_school_business_or_individual") == null
						|| rs.getString("private_school_business_or_individual").equals("")) {
					school.setPrivate_school_business_or_individual("N�o foi informado");
				} else if (rs.getString("private_school_business_or_individual").equals("0")) {
					school.setPrivate_school_business_or_individual("N�o");
				} else if (rs.getString("private_school_business_or_individual").equals("1")) {
					school.setPrivate_school_business_or_individual("Sim");
				}
				if (rs.getString("private_school_syndicate_or_association") == null
						|| rs.getString("private_school_syndicate_or_association").equals("")) {
					school.setPrivate_school_syndicate_or_association("N�o foi informado");
				} else if (rs.getString("private_school_syndicate_or_association").equals("0")) {
					school.setPrivate_school_syndicate_or_association("N�o");
				} else if (rs.getString("private_school_syndicate_or_association").equals("1")) {
					school.setPrivate_school_syndicate_or_association("Sim");
				}
				if (rs.getString("private_school_ong_or_oscip") == null
						|| rs.getString("private_school_ong_or_oscip").equals("")) {
					school.setPrivate_school_ong_or_oscip("N�o foi informado");
				} else if (rs.getString("private_school_ong_or_oscip").equals("0")) {
					school.setPrivate_school_ong_or_oscip("N�o");
				} else if (rs.getString("private_school_ong_or_oscip").equals("1")) {
					school.setPrivate_school_ong_or_oscip("Sim");
				}
				if (rs.getString("private_school_non_profit_institutions") == null
						|| rs.getString("private_school_non_profit_institutions").equals("")) {
					school.setPrivate_school_non_profit_institutions("N�o foi informado");
				} else if (rs.getString("private_school_non_profit_institutions").equals("0")) {
					school.setPrivate_school_non_profit_institutions("N�o");
				} else if (rs.getString("private_school_non_profit_institutions").equals("1")) {
					school.setPrivate_school_non_profit_institutions("Sim");
				}
				if (rs.getString("private_school_s_system") == null
						|| rs.getString("private_school_s_system").equals("")) {
					school.setPrivate_school_s_system("N�o foi informado");
				} else if (rs.getString("private_school_s_system").equals("0")) {
					school.setPrivate_school_s_system("N�o");
				} else if (rs.getString("private_school_s_system").equals("1")) {
					school.setPrivate_school_s_system("Sim");
				}
				if (rs.getString("private_school_maintainer_cnpj") == null
						|| rs.getString("private_school_maintainer_cnpj").equals("")) {
					school.setPrivate_school_maintainer_cnpj("N�o informado");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("0")) {
					school.setPrivate_school_maintainer_cnpj("N�o");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("1")) {
					school.setPrivate_school_maintainer_cnpj("Sim");
				}
				if (rs.getString("private_school_cnpj") == null || rs.getString("private_school_cnpj").equals("")) {
					school.setPrivate_school_cnpj("N�o informado");
				} else if (rs.getString("private_school_cnpj").equals("0")) {
					school.setPrivate_school_cnpj("N�o");
				} else if (rs.getString("private_school_cnpj").equals("1")) {
					school.setPrivate_school_cnpj("Sim");
				}
				if (rs.getString("regulation") == null || rs.getString("regulation").equals("")) {
					school.setRegulation("N�o foi informado");
				} else if (rs.getString("regulation").equals("1")) {
					school.setRegulation("N�o");
				} else if (rs.getString("regulation").equals("2")) {
					school.setRegulation("Sim");
				} else if (rs.getString("regulation").equals("3")) {
					school.setRegulation("Em tramita��o");
				}
				if (rs.getString("offer_or_linked_unity") == null || rs.getString("offer_or_linked_unity").equals("-1")
						|| rs.getString("offer_or_linked_unity").equals("")) {
					school.setOffer_or_linked_unity("N�o foi informado");
				} else if (rs.getString("offer_or_linked_unity").equals("1")) {
					school.setOffer_or_linked_unity("Unidade vinculada a escola de Educa��o B�sica");
				} else if (rs.getString("offer_or_linked_unity").equals("2")) {
					school.setOffer_or_linked_unity("Unidade ofertante de Ensino Superior");
				} else if (rs.getString("offer_or_linked_unity").equals("0")) {
					school.setOffer_or_linked_unity("N�o");
				}
				if (rs.getString("inep_head_school") == null || rs.getString("inep_head_school").equals("")) {
					school.setInep_head_school("N�o foi informado");
				} else {
					school.setInep_head_school(rs.getString("inep_head_school"));
				}
				if (rs.getString("ies_code") == null || rs.getString("ies_code").equals("")) {
					school.setIes_code("N�o foi informado");
				} else {
					school.setIes_code(rs.getString("ies_code"));
				}
				if (rs.getString("logo_file_name").equals("") || rs.getString("logo_file_name") == null) {
					school.setLogo_file_name("N�o foi informado");
				} else {
					school.setLogo_file_name(rs.getString("logo_file_name"));
				}
				if (rs.getString("logo_file_type").equals("") || rs.getString("logo_file_type") == null) {
					school.setLogo_file_type("N�o foi informado");
				} else {
					school.setLogo_file_type(rs.getString("logo_file_type"));
				}
				// school.setLogo_file_content(rs.getString("logo_file_content"));
				if (rs.getString("act_of_acknowledgement") == null
						|| rs.getString("act_of_acknowledgement").equals("")) {
					school.setAct_of_acknowledgement("N�o foi informado");
				} else {
					school.setAct_of_acknowledgement(rs.getString("act_of_acknowledgement"));
				}

				arraySchool.add(school);
			}

			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arraySchool.size() > 0) {
				schoolReturn.setValid(true);
				schoolReturn.setError(error);
				schoolReturn.setSchool(arraySchool);
			} else {
				error.add("N�o h� escola cadastrada!");

				schoolReturn.setValid(false);
				schoolReturn.setError(error);
				schoolReturn.setSchool(null);
			}

			arraySchoolReturn.add(schoolReturn);

			return arraySchoolReturn;
		} catch (Exception e) {
			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			schoolReturn.setValid(false);
			schoolReturn.setError(error);
			schoolReturn.setSchool(null);

			arraySchoolReturn.add(schoolReturn);
			e.printStackTrace();
			return arraySchoolReturn;
		}
	}

	public ArrayList<SchoolReturn> getSchoolsByUserFK(Connection connection, String user_fk) throws Exception {
		try {
			String sql = "SELECT S.* FROM school_identification S JOIN users_school U ON S.INEP_ID = U.SCHOOL_FK WHERE U.user_fk = '"
					+ user_fk + "';";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				School school = new School();

				school.setRegister_type(rs.getString("register_type"));
				school.setInep_id(rs.getString("inep_id"));
				school.setManager_cpf(rs.getString("manager_cpf"));
				school.setManager_name(rs.getString("manager_name"));
				if (rs.getString("manager_role").equals("1")) {
					school.setManager_role("Diretor");
				} else if (rs.getString("manager_role").equals("2")) {
					school.setManager_role("Outro Cargo");
				} else {
					school.setManager_role("N�o foi informado");
				}
				if (rs.getString("manager_email").equals("")) {
					school.setManager_email("N�o foi informado");
				} else {
					school.setManager_email(rs.getString("manager_email"));
				}
				if (rs.getString("situation").equals("1")) {
					school.setSituation("Em Atividade");
				} else if (rs.getString("situation").equals("2")) {
					school.setSituation("Paralisada");
				} else if (rs.getString("situation").equals("3")) {
					school.setSituation("Extinta");
				} else {
					school.setSituation("N�o foi informado");
				}
				if (rs.getString("initial_date") == null || rs.getString("initial_date").equals("")) {
					school.setInitial_date("N�o foi informado");
				} else {
					school.setInitial_date(rs.getString("initial_date"));
				}
				if (rs.getString("final_date") == null || rs.getString("final_date").equals("")) {
					school.setFinal_date("N�o foi informado");
				} else {
					school.setFinal_date(rs.getString("final_date"));
				}
				school.setName(rs.getString("name"));
				if (rs.getString("latitude") == null || rs.getString("latitude").equals("")) {
					school.setLatitude("N�o foi informado");
				} else {
					school.setLatitude(rs.getString("latitude"));
				}
				if (rs.getString("longitude") == null || rs.getString("longitude").equals("")) {
					school.setLongitude("N�o foi informado");
				} else {
					school.setLongitude(rs.getString("longitude"));
				}
				school.setCep(rs.getString("cep"));
				school.setAddress(rs.getString("address"));
				if (rs.getString("address_number") == null || rs.getString("address_number").equals("")) {
					school.setAddress_number("N�o foi informado");
				} else {
					school.setAddress_number(rs.getString("address_number"));
				}
				if (rs.getString("address_complement") == null || rs.getString("address_complement").equals("")) {
					school.setAddress_complement("N�o foi informado");
				} else {
					school.setAddress_complement(rs.getString("address_complement"));
				}
				if (rs.getString("address_neighborhood") == null || rs.getString("address_neighborhood").equals("")) {
					school.setAddress_neighborhood("N�o foi informado");
				} else {
					school.setAddress_neighborhood(rs.getString("address_neighborhood"));
				}
				school.setEdcenso_uf_fk(rs.getString("edcenso_uf_fk"));
				school.setEdcenso_city_fk(rs.getString("edcenso_city_fk"));
				school.setEdcenso_district_fk(rs.getString("edcenso_district_fk"));
				if (rs.getString("ddd") == null || rs.getString("ddd").equals("")) {
					school.setDdd("N�o foi informado");
				} else {
					school.setDdd(rs.getString("ddd"));
				}
				if (rs.getString("phone_number") == null || rs.getString("phone_number").equals("")) {
					school.setPhone_number("N�o foi informado");
				} else {
					school.setPhone_number(rs.getString("phone_number"));
				}
				if (rs.getString("public_phone_number") == null || rs.getString("public_phone_number").equals("")) {
					school.setPublic_phone_number("N�o foi informado");
				} else {
					school.setPublic_phone_number(rs.getString("public_phone_number"));
				}
				if (rs.getString("other_phone_number") == null || rs.getString("other_phone_number").equals("")) {
					school.setOther_phone_number("N�o foi informado");
				} else {
					school.setOther_phone_number(rs.getString("other_phone_number"));
				}
				if (rs.getString("fax_number") == null || rs.getString("fax_number").equals("")) {
					school.setFax_number("N�o foi informado");
				} else {
					school.setFax_number(rs.getString("fax_number"));
				}
				if (rs.getString("email") == null || rs.getString("email").equals("")) {
					school.setEmail("N�o foi informado");
				} else {
					school.setEmail(rs.getString("email"));
				}
				if (rs.getString("edcenso_regional_education_organ_fk") == null
						|| rs.getString("edcenso_regional_education_organ_fk").equals("")) {
					school.setEdcenso_regional_education_organ_fk("N�o foi informado");
				} else {
					school.setEdcenso_regional_education_organ_fk(rs.getString("edcenso_regional_education_organ_fk"));
				}
				if (rs.getString("administrative_dependence").equals("1")) {
					school.setAdministrative_dependence("Federal");
				} else if (rs.getString("administrative_dependence").equals("2")) {
					school.setAdministrative_dependence("Estadual");
				} else if (rs.getString("administrative_dependence").equals("3")) {
					school.setAdministrative_dependence("Municipal");
				} else {
					school.setAdministrative_dependence("Privada");
				}
				if (rs.getString("location").equals("1")) {
					school.setLocation("Urbano");
				} else if (rs.getString("location").equals("2")) {
					school.setLocation("Rural");
				}
				if (rs.getString("private_school_category") == null
						|| rs.getString("private_school_category").equals("")) {
					school.setPrivate_school_category("N�o foi informado");
				} else if (rs.getString("private_school_category").equals("1")) {
					school.setPrivate_school_category("Particular");
				} else if (rs.getString("private_school_category").equals("2")) {
					school.setPrivate_school_category("Comunit�ria");
				} else if (rs.getString("private_school_category").equals("3")) {
					school.setPrivate_school_category("Confessional");
				} else if (rs.getString("private_school_category").equals("4")) {
					school.setPrivate_school_category("Filantr�pica");
				}
				if (rs.getString("public_contract") == null || rs.getString("public_contract").equals("")) {
					school.setPublic_contract("N�o foi informado");
				} else if (rs.getString("public_contract").equals("1")) {
					school.setPublic_contract("Estadual");
				} else if (rs.getString("public_contract").equals("2")) {
					school.setPublic_contract("Municipal");
				} else if (rs.getString("public_contract").equals("3")) {
					school.setPublic_contract("Estadual e Municipal");
				}
				if (rs.getString("private_school_business_or_individual") == null
						|| rs.getString("private_school_business_or_individual").equals("")) {
					school.setPrivate_school_business_or_individual("N�o foi informado");
				} else if (rs.getString("private_school_business_or_individual").equals("0")) {
					school.setPrivate_school_business_or_individual("N�o");
				} else if (rs.getString("private_school_business_or_individual").equals("1")) {
					school.setPrivate_school_business_or_individual("Sim");
				}
				if (rs.getString("private_school_syndicate_or_association") == null
						|| rs.getString("private_school_syndicate_or_association").equals("")) {
					school.setPrivate_school_syndicate_or_association("N�o foi informado");
				} else if (rs.getString("private_school_syndicate_or_association").equals("0")) {
					school.setPrivate_school_syndicate_or_association("N�o");
				} else if (rs.getString("private_school_syndicate_or_association").equals("1")) {
					school.setPrivate_school_syndicate_or_association("Sim");
				}
				if (rs.getString("private_school_ong_or_oscip") == null
						|| rs.getString("private_school_ong_or_oscip").equals("")) {
					school.setPrivate_school_ong_or_oscip("N�o foi informado");
				} else if (rs.getString("private_school_ong_or_oscip").equals("0")) {
					school.setPrivate_school_ong_or_oscip("N�o");
				} else if (rs.getString("private_school_ong_or_oscip").equals("1")) {
					school.setPrivate_school_ong_or_oscip("Sim");
				}
				if (rs.getString("private_school_non_profit_institutions") == null
						|| rs.getString("private_school_non_profit_institutions").equals("")) {
					school.setPrivate_school_non_profit_institutions("N�o foi informado");
				} else if (rs.getString("private_school_non_profit_institutions").equals("0")) {
					school.setPrivate_school_non_profit_institutions("N�o");
				} else if (rs.getString("private_school_non_profit_institutions").equals("1")) {
					school.setPrivate_school_non_profit_institutions("Sim");
				}
				if (rs.getString("private_school_s_system") == null
						|| rs.getString("private_school_s_system").equals("")) {
					school.setPrivate_school_s_system("N�o foi informado");
				} else if (rs.getString("private_school_s_system").equals("0")) {
					school.setPrivate_school_s_system("N�o");
				} else if (rs.getString("private_school_s_system").equals("1")) {
					school.setPrivate_school_s_system("Sim");
				}
				if (rs.getString("private_school_maintainer_cnpj") == null
						|| rs.getString("private_school_maintainer_cnpj").equals("")) {
					school.setPrivate_school_maintainer_cnpj("N�o informado");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("0")) {
					school.setPrivate_school_maintainer_cnpj("N�o");
				} else if (rs.getString("private_school_maintainer_cnpj").equals("1")) {
					school.setPrivate_school_maintainer_cnpj("Sim");
				}
				if (rs.getString("private_school_cnpj") == null || rs.getString("private_school_cnpj").equals("")) {
					school.setPrivate_school_cnpj("N�o informado");
				} else if (rs.getString("private_school_cnpj").equals("0")) {
					school.setPrivate_school_cnpj("N�o");
				} else if (rs.getString("private_school_cnpj").equals("1")) {
					school.setPrivate_school_cnpj("Sim");
				}
				if (rs.getString("regulation") == null || rs.getString("regulation").equals("")) {
					school.setRegulation("N�o foi informado");
				} else if (rs.getString("regulation").equals("1")) {
					school.setRegulation("N�o");
				} else if (rs.getString("regulation").equals("2")) {
					school.setRegulation("Sim");
				} else if (rs.getString("regulation").equals("3")) {
					school.setRegulation("Em tramita��o");
				}
				if (rs.getString("offer_or_linked_unity") == null || rs.getString("offer_or_linked_unity").equals("-1")
						|| rs.getString("offer_or_linked_unity").equals("")) {
					school.setOffer_or_linked_unity("N�o foi informado");
				} else if (rs.getString("offer_or_linked_unity").equals("1")) {
					school.setOffer_or_linked_unity("Unidade vinculada a escola de Educa��o B�sica");
				} else if (rs.getString("offer_or_linked_unity").equals("2")) {
					school.setOffer_or_linked_unity("Unidade ofertante de Ensino Superior");
				} else if (rs.getString("offer_or_linked_unity").equals("0")) {
					school.setOffer_or_linked_unity("N�o");
				}
				if (rs.getString("inep_head_school") == null || rs.getString("inep_head_school").equals("")) {
					school.setInep_head_school("N�o foi informado");
				} else {
					school.setInep_head_school(rs.getString("inep_head_school"));
				}
				if (rs.getString("ies_code") == null || rs.getString("ies_code").equals("")) {
					school.setIes_code("N�o foi informado");
				} else {
					school.setIes_code(rs.getString("ies_code"));
				}
				if (rs.getString("logo_file_name").equals("") || rs.getString("logo_file_name") == null) {
					school.setLogo_file_name("N�o foi informado");
				} else {
					school.setLogo_file_name(rs.getString("logo_file_name"));
				}
				if (rs.getString("logo_file_type").equals("") || rs.getString("logo_file_type") == null) {
					school.setLogo_file_type("N�o foi informado");
				} else {
					school.setLogo_file_type(rs.getString("logo_file_type"));
				}
				// school.setLogo_file_content(rs.getString("logo_file_content"));
				if (rs.getString("act_of_acknowledgement") == null
						|| rs.getString("act_of_acknowledgement").equals("")) {
					school.setAct_of_acknowledgement("N�o foi informado");
				} else {
					school.setAct_of_acknowledgement(rs.getString("act_of_acknowledgement"));
				}

				arraySchool.add(school);
			}

			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arraySchool.size() > 0) {
				schoolReturn.setValid(true);
				schoolReturn.setError(error);
				schoolReturn.setSchool(arraySchool);
			} else {
				error.add("N�o h� escola cadastrada!");

				schoolReturn.setValid(false);
				schoolReturn.setError(error);
				schoolReturn.setSchool(null);
			}

			arraySchoolReturn.add(schoolReturn);

			return arraySchoolReturn;
		} catch (Exception e) {
			SchoolReturn schoolReturn = new SchoolReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			schoolReturn.setValid(false);
			schoolReturn.setError(error);
			schoolReturn.setSchool(null);

			arraySchoolReturn.add(schoolReturn);
			e.printStackTrace();
			return arraySchoolReturn;
		}
	}

	// --------------- GRADES ------------------ //
	public ArrayList<GradeReturn> getGrades(Connection connection) throws Exception {
		try {
			String sql = "SELECT * FROM grade G JOIN edcenso_discipline E ON G.discipline_fk = E.id ORDER BY G.id;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Grade grade = new Grade();

				grade.setId(rs.getString("G.id"));
				grade.setGrade1(rs.getString("G.grade1"));
				grade.setGrade2(rs.getString("G.grade2"));
				grade.setGrade3(rs.getString("G.grade3"));
				grade.setGrade4(rs.getString("G.grade4"));
				grade.setRecovery_grade1(rs.getString("G.recovery_grade1"));
				grade.setRecovery_grade2(rs.getString("G.recovery_grade2"));
				grade.setRecovery_grade3(rs.getString("G.recovery_grade3"));
				grade.setRecovery_grade4(rs.getString("G.recovery_grade4"));
				grade.setRecovery_final_grade(rs.getString("G.recovery_final_grade"));
				grade.setDiscipline_fk(rs.getString("E.id"));
				grade.setDiscipline_name(rs.getString("E.name"));
				grade.setEnrollment_fk(rs.getString("G.enrollment_fk"));
				grade.setFkid(rs.getString("G.fkid"));

				arrayGrade.add(grade);
			}

			GradeReturn gradeReturn = new GradeReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayGrade.size() > 0) {
				gradeReturn.setValid(true);
				gradeReturn.setError(error);
				gradeReturn.setGrade(arrayGrade);
			} else {
				error.add("N�o h� grade cadastrada!");

				gradeReturn.setValid(false);
				gradeReturn.setError(error);
				gradeReturn.setGrade(null);
			}

			arrayGradeReturn.add(gradeReturn);

			return arrayGradeReturn;
		} catch (Exception e) {
			GradeReturn gradeReturn = new GradeReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			gradeReturn.setValid(false);
			gradeReturn.setError(error);
			gradeReturn.setGrade(null);

			arrayGradeReturn.add(gradeReturn);
			e.printStackTrace();
			return arrayGradeReturn;
		}
	}

	public ArrayList<GradeReturn> getGrades(Connection connection, String enrollment_fk) throws Exception {
		try {
			String sql = "SELECT * FROM grade G JOIN edcenso_discipline E ON G.discipline_fk = E.id AND G.enrollment_fk = '"
					+ enrollment_fk + "';";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Grade grade = new Grade();

				grade.setId(rs.getString("G.id"));
				grade.setGrade1(rs.getString("G.grade1"));
				grade.setGrade2(rs.getString("G.grade2"));
				grade.setGrade3(rs.getString("G.grade3"));
				grade.setGrade4(rs.getString("G.grade4"));
				grade.setRecovery_grade1(rs.getString("G.recovery_grade1"));
				grade.setRecovery_grade2(rs.getString("G.recovery_grade2"));
				grade.setRecovery_grade3(rs.getString("G.recovery_grade3"));
				grade.setRecovery_grade4(rs.getString("G.recovery_grade4"));
				grade.setRecovery_final_grade(rs.getString("G.recovery_final_grade"));
				grade.setDiscipline_fk(rs.getString("E.id"));
				grade.setDiscipline_name(rs.getString("E.name"));
				grade.setEnrollment_fk(rs.getString("G.enrollment_fk"));
				grade.setFkid(rs.getString("G.fkid"));

				arrayGrade.add(grade);
			}

			GradeReturn gradeReturn = new GradeReturn();
			ArrayList<String> error = new ArrayList<>();
			if (arrayGrade.size() > 0) {
				gradeReturn.setValid(true);
				gradeReturn.setError(error);
				gradeReturn.setGrade(arrayGrade);
			} else {
				error.add("N�o h� grade cadastrada!");

				gradeReturn.setValid(false);
				gradeReturn.setError(error);
				gradeReturn.setGrade(null);
			}

			arrayGradeReturn.add(gradeReturn);

			return arrayGradeReturn;
		} catch (Exception e) {
			GradeReturn gradeReturn = new GradeReturn();
			ArrayList<String> error = new ArrayList<>();
			error.add(e.getMessage());

			gradeReturn.setValid(false);
			gradeReturn.setError(error);
			gradeReturn.setGrade(null);

			arrayGradeReturn.add(gradeReturn);
			e.printStackTrace();
			return arrayGradeReturn;
		}
	}

}