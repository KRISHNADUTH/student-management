package com.ghss.studentmanagement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ghss.studentmanagement.constatnts.StudentManagementConstants;
import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Enrollment;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.service.StudentManagementService;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class StudentmanagementApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private StudentManagementService studentManagementService;

	@Autowired
	CourseServiceImpl courseServiceImpl;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setUpCourseDb() {
		studentManagementService.loadData();
	}

	// @AfterEach
	// public void reFresStudentDb() {
	// }

	@Test
	@Order(0)
	public void testGetAllCourses_NoCourseFound() throws Exception {
		// Mock the behavior of studentManagementService to return an empty list for
		// getAllCourses()
		studentManagementService.setCourses(Collections.emptyList());

		// Perform the GET request on the /courses/get-all endpoint
		MvcResult result = mvc.perform(get("/courses/get-all")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound())
				.andReturn();

		// Verify the response contains the appropriate status and message
		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);

		// Expected ResponseDto structure
		String expectedResponse = "{\"statusCode\":\"404\",\"statusMsg\":\"Requested resource not found\"}"; // Update
																												// based
																												// on
																												// actual
		// message in
		// StudentManagementConstants

		// Assert the response
		assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse);

		// Verify that the getAllCourses() method was called on the service
		// verify(studentManagementService,times(1)).getAllCourses();
	}

	@Test
	@Order(1)
	public void testGetAllCourses_ReturnCourses() throws Exception {
		MvcResult result = mvc.perform(get("/courses/get-all"))
				.andExpect(status().isOk()).andReturn(); // Expect 200 OK status

		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);
		String expectedResponse = "[{\"courseName\":\"python\",\"courseFee\":2000.0}," +
				"{\"courseName\":\"java\",\"courseFee\":6000.0}," +
				"{\"courseName\":\"ts\",\"courseFee\":3000.0}," +
				"{\"courseName\":\"js\",\"courseFee\":3500.0}," +
				"{\"courseName\":\"go\",\"courseFee\":4000.0}," +
				"{\"courseName\":\"html\",\"courseFee\":4500.0}," +
				"{\"courseName\":\"rust\",\"courseFee\":3200.0}]";

		assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse);

	}

	@Test
	@Order(2)
	public void testAddCourse_addNewCourse_and_addExistingCourse() throws Exception {

		MvcResult result = mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isOk()).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);

		String expectedResponse = "{\"statusCode\":\"201\",\"statusMsg\":\"Course created successfully\"}";

		assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse);

		// trying to add same course again. - BAD_REQUEST
		MvcResult result1 = mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isBadRequest()).andReturn();

		String responseContent1 = result1.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent1);

		// Will always fail here since LocalDateTime.now() is very sensitive.
		// String expectedResponse1 =
		// "{\"apiPath\":\"uri=/courses/add-course\",\"errorCode\":\"BAD_REQUEST\",\"errorMessage\":\"Course
		// with name = 'flutter' already
		// present\",\"errorTime\":\"2024-09-07T07:54:51.1368653\"}";
		// assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse); -

		// Below case also will be always failed since thrown exception is already
		// handled by Global exception handler.
		// assertThrows(ResourseAlreadyExistsException.class,()->mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
		// .content(getCourseDetails("flutter",
		// 1550).toString())).andExpect(status().isBadRequest()));
	}

	public JSONObject getCourseDetails(String courseName, double courseFee) {
		Map<String, Object> map = new HashMap<>();
		map.put("courseName", courseName);
		map.put("courseFee", courseFee);
		return new JSONObject(map);
	}

	// @Sql("/data.sql")
	@Test
	@Order(3)
	public void testAddStudent_addNewStudent_and_addExistingStudent() throws Exception {
		List<CourseDto> courseDtos = Arrays.asList(new CourseDto("python", 2000), new CourseDto("java", 6000),
				new CourseDto("go", 4000));

		MvcResult result = mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Krishna", "krishna@123", LocalDate.of(2023, 12, 11), courseDtos, 010)
						.toString()))
				.andExpect(status().isCreated())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);
		String expectedResponse = "{\"statusCode\":\"201\",\"statusMsg\":\"Student created successfully\"}";

		assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse);

		// Try to add same student details
		MvcResult result1 = mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Krishna", "krishna@123", LocalDate.of(2023, 12, 11), courseDtos, 010)
						.toString()))
				.andExpect(status().isBadRequest()).andReturn();
		System.out.println(result1.getResponse().getContentAsString());
	}

	public JSONObject getStudentDetails(String name, String userId, LocalDate enrollmentDate, List<CourseDto> courses,
			double pendingFee) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("userId", userId);
		map.put("enrollmentDate", enrollmentDate);
		map.put("courses", courses.stream()
				.map(course -> {
					Map<String, Object> courseMap = new HashMap<>();
					courseMap.put("courseName", course.getCourseName());
					courseMap.put("courseFee", course.getCourseFee());
					return courseMap;
				}).collect(Collectors.toList()));
		map.put("pendingFee", pendingFee);
		return new JSONObject(map);
	}

	@Test
	@Order(4)
	public void testfindNthStudentByEnrollmentDateWithHighestPendingFee1() throws Exception {
		// Try invoking endpoint while course table is not having relevant data
		MvcResult result1 = mvc.perform(get("/student/1/nth-highest-pending-fee?date=2013-07-30"))
				.andExpect(status().isBadRequest()).andReturn();

		String actualRes = result1.getResponse().getContentAsString();
		String expectedRes1 = "No one enrolled on 2013-07-30 pending for fee payment.";
		assertEquals(expectedRes1, actualRes);
		System.out.println(result1.getResponse().getContentAsString());
		// Only one student pending with fee payment
		mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Amal", "Amal@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 20)
						.toString()));
		MvcResult result2 = mvc.perform(get("/student/1/nth-highest-pending-fee?date=2013-07-30"))
				.andExpect(status().isOk()).andReturn();

		String actualRes2 = result2.getResponse().getContentAsString();
		System.out.println("Only one student pen  -- " + actualRes2);

		// 2 are pending with the fee payment
		mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Amal", "Amal@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 20)
						.toString()));
		mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Don", "don@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 20)
						.toString()));

		MvcResult result3 = mvc.perform(get("/student/3/nth-highest-pending-fee?date=2013-07-30"))
				.andExpect(status().isBadRequest()).andReturn();

		String actualRes3 = result3.getResponse().getContentAsString();
		String expectedRes3 = "2 are pending with the fee payment";
		assertEquals(expectedRes3, actualRes3);
	}

	@Test
	@Order(4)
	public void testfindNthStudentByEnrollmentDateWithHighestPendingFee2() throws Exception {
		// Try invoking endpoint after adding correct data to the course DB.
		List<JSONObject> studentstoBeAdded = Arrays.asList(
				getStudentDetails("Krishna", "rew@123", LocalDate.of(2023, 12, 11), Arrays.asList(
						new CourseDto("python", 2000),
						new CourseDto("java", 6000),
						new CourseDto("go", 4000)), 10),

				getStudentDetails("Amal", "xcv@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 20),

				getStudentDetails("Lata", "nvv@123", LocalDate.of(2001, 11, 3), Arrays.asList(
						new CourseDto("ts", 3000),
						new CourseDto("js", 3500),
						new CourseDto("go", 4000)), 30),

				getStudentDetails("Rahul", "sdf@123", LocalDate.of(2001, 11, 3), Arrays.asList(
						new CourseDto("python", 2000),
						new CourseDto("rust", 3200)), 40),

				getStudentDetails("Sneha", "zxc@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("go", 4000),
						new CourseDto("java", 6000)), 50));
		studentManagementService.setStudents(Collections.emptyList());

		System.out.println(mvc.perform(post("/students/add-students").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(studentstoBeAdded.toString()))
				.andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString());

		MvcResult resultTemp = mvc.perform(get("/student/1/nth-highest-pending-fee?date=2013-07-30"))
				.andExpect(status().isOk()).andReturn();
		System.out.println("All students present in DB   ------------>  " + studentManagementService.getStudents());
		String actualResTemp = resultTemp.getResponse().getContentAsString();
		String expectedResTemp = "{\"name\":\"Amal\",\"userId\":\"xcv@123\",\"enrollmentDate\":\"2013-07-30\",\"courses\":[{\"courseName\":\"html\",\"courseFee\":4500.0},{\"courseName\":\"rust\",\"courseFee\":3200.0}],\"pendingFee\":7700.0}";
		assertEquals(expectedResTemp, actualResTemp);
	}

	@Test
	@Order(5)
	public void testfindStudentsWithNoFeesInLastYearAndMultipleCourses_no_enrollments() throws Exception {
		// studentManagementService.setStudents(
		// Arrays.asList(new Student(1L, "krish@123", "Krishna", LocalDate.of(2024, 011,
		// 025), 1000, 100, null)));
		// System.out.println("Students table after addition -
		// "+studentManagementService.getStudents());
		studentManagementService.setStudents(Collections.emptyList());
		MvcResult result1 = mvc.perform(get("/student/lastyear-no-fee-multiple-courses"))
				.andExpect(status().isBadRequest()).andReturn();
		String expectedRes1 = StudentManagementConstants.MESSAGE_NO_ENROLLMENT;
		String actualRes1 = result1.getResponse().getContentAsString();
		assertEquals(expectedRes1, actualRes1);
	}

	@Test
	@Order(6)
	public void testfindStudentsWithNoFeesInLastYearAndMultipleCoursesNo_one_enrolled_in() throws Exception {
		studentManagementService.setStudents(
				Arrays.asList(new Student(1L, "krish@123", "Krishna", LocalDate.of(2024, 011,
						025), 1000, 100, null)));

		MvcResult result1 = mvc.perform(get("/student/lastyear-no-fee-multiple-courses"))
				.andExpect(status().isBadRequest()).andReturn();
		LocalDate oneYearAgo = LocalDate.now().minus(1, ChronoUnit.YEARS);
		String expectedRes1 = String.format(StudentManagementConstants.MESSAGE_NO_PENDING_FEE_PAYMENT_YEAR, oneYearAgo);
		String actualRes1 = result1.getResponse().getContentAsString();
		assertEquals(expectedRes1, actualRes1);
	}

	@Test
	@Order(7)
	public void testfindStudentsWithNoFeesInLastYearAndMultipleCourses_return_student() throws Exception {
		List<JSONObject> studentstoBeAdded = Arrays.asList(
				getStudentDetails("Sam", "samu@123", LocalDate.of(2023, 11, 3), Arrays.asList(
						new CourseDto("python", 0),
						new CourseDto("java", 0),
						new CourseDto("go", 4000)), 0),

				getStudentDetails("Ammu", "amu@123", LocalDate.of(2023, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 0),

				getStudentDetails("Lata", "lta@123", LocalDate.of(2023, 11, 3), Arrays.asList(
						new CourseDto("ts", 0),
						new CourseDto("js", 0),
						new CourseDto("go", 0)), 0),

				getStudentDetails("Rahul", "rul@123", LocalDate.of(2001, 11, 3), Arrays.asList(
						new CourseDto("python", 0),
						new CourseDto("rust", 3200)), 0),

				getStudentDetails("Sneha", "neha@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("go", 0),
						new CourseDto("java", 0)), 0));

		mvc.perform(post("/students/add-students").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(studentstoBeAdded.toString()))
				.andExpect(status().isCreated());
		MvcResult result1 = mvc.perform(get("/student/lastyear-no-fee-multiple-courses"))
				.andExpect(status().isOk()).andReturn();
		String expectedRes1 = "[{\"name\":\"Ammu\",\"userId\":\"amu@123\",\"enrollmentDate\":\"2023-07-30\",\"courses\":[{\"courseName\":\"html\",\"courseFee\":4500.0},{\"courseName\":\"rust\",\"courseFee\":3200.0}],\"pendingFee\":7700.0},{\"name\":\"Lata\",\"userId\":\"lta@123\",\"enrollmentDate\":\"2023-11-03\",\"courses\":[{\"courseName\":\"ts\",\"courseFee\":3000.0},{\"courseName\":\"js\",\"courseFee\":3500.0},{\"courseName\":\"go\",\"courseFee\":4000.0}],\"pendingFee\":10500.0}]";

		String actualRes1 = result1.getResponse().getContentAsString();
		assertEquals(expectedRes1, actualRes1);
	}

	@Test
	@Order(8)
	public void testgetAverageFeeCollectedPerStudentPerBatch() throws Exception {
		studentManagementService.setStudents(Collections.emptyList());
		MvcResult result1 = mvc.perform(get("/student/avg-fees"))
				.andExpect(status().isBadRequest()).andReturn();
		String expectedRes1 = StudentManagementConstants.MESSAGE_NO_ENROLLMENT;
		String actualRes1 = result1.getResponse().getContentAsString();
		assertEquals(expectedRes1, actualRes1);
	}

	@Test
	@Order(9)
	public void testgetAverageFeeCollectedPerStudentPerBatch_return_avgFees() throws Exception {
		List<JSONObject> studentstoBeAdded = Arrays.asList(
				getStudentDetails("Krishna", "lkj@123", LocalDate.of(2023, 12, 11), Arrays.asList(
						new CourseDto("python", 2000),
						new CourseDto("java", 6000),
						new CourseDto("go", 4000)), 10),

				getStudentDetails("Amal", "asx@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("html", 0),
						new CourseDto("rust", 0)), 20),

				getStudentDetails("Lata", "fds@123", LocalDate.of(2001, 11, 3), Arrays.asList(
						new CourseDto("ts", 3000),
						new CourseDto("js", 3500),
						new CourseDto("go", 4000)), 30),

				getStudentDetails("Rahul", "gtd@123", LocalDate.of(2001, 11, 3), Arrays.asList(
						new CourseDto("python", 2000),
						new CourseDto("rust", 3200)), 40),

				getStudentDetails("Sneha", "cvr@123", LocalDate.of(2013, 7, 30), Arrays.asList(
						new CourseDto("go", 4000),
						new CourseDto("java", 6000)), 50));
		studentManagementService.setStudents(Collections.emptyList());
		studentManagementService.loadData();

		mvc.perform(post("/students/add-students").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(studentstoBeAdded.toString()))
				.andExpect(status().isCreated());

		MvcResult result1 = mvc.perform(get("/student/avg-fees"))
				.andExpect(status().isOk()).andReturn();

		String expectedRes1 = "{\"2001-11-03\":6920.0,\"2013-07-30\":2857.1428571428573,\"2023-07-30\":0.0,\"2023-11-03\":2000.0,\"2023-12-11\":12000.0}";
		String actualRes1 = result1.getResponse().getContentAsString();
		assertEquals(expectedRes1, actualRes1);
	}

	@Test
	@Order(10)
	public void findTop5StudentsWithLongestDelinquentPaymentHistory_no_enrollments() throws Exception {
		studentManagementService.setStudents(Collections.emptyList());

		MvcResult res = mvc.perform(get("/student/top-5-longest-delinquent-payment"))
				.andExpect(status().isBadRequest()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		String expectedRes = StudentManagementConstants.MESSAGE_NO_ENROLLMENT;
		assertEquals(expectedRes, actualRes);

		// studentManagementService.setStudents(
		// Arrays.asList(new Student(1L, "krish@123", "Krishna", LocalDate.of(2014, 10,
		// 023), 10000, 0.0, Collections.emptyList())));
		// System.out.println(studentManagementService.getStudents());
	}

	@Test
	@Order(11)
	public void testfindTop5StudentsWithLongestDelinquentPaymentHistory_no_enrollment() throws Exception {
		studentManagementService.setStudents(Collections.emptyList());

		MvcResult res = mvc.perform(get("/student/top-5-longest-delinquent-payment"))
				.andExpect(status().isBadRequest()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		String expectedRes = StudentManagementConstants.MESSAGE_NO_ENROLLMENT;
		assertEquals(expectedRes, actualRes);

		// studentManagementService.setStudents(
		// Arrays.asList(new Student(1L, "krish@123", "Krishna", LocalDate.of(2014, 10,
		// 023), 10000, 0.0, Collections.emptyList())));
		// System.out.println(studentManagementService.getStudents());
	}

	@Test
	@Order(12)
	public void testfindTop5StudentsWithLongestDelinquentPaymentHistory_return_student() throws Exception {
		studentManagementService.setStudents(Collections.emptyList());

		studentManagementService.setStudents(
				Arrays.asList(new Student(1L, "krish@123", "Krishna", LocalDate.of(2014, 10, 023), 10000, 1000,
						Collections.emptyList())));

		MvcResult res = mvc.perform(get("/student/top-5-longest-delinquent-payment"))
				.andExpect(status().isOk()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		String expectedRes = "[{\"name\":\"Krishna\",\"userId\":\"krish@123\",\"enrollmentDate\":\"2014-10-19\",\"courses\":[],\"pendingFee\":1000.0}]";
		assertEquals(expectedRes, actualRes);
	}

	@Test
	@Order(13)
	public void testfindStudentsEnrolledInAllCoursesButNotPaidFees() throws Exception {
		studentManagementService.setStudents(Collections.emptyList());

		studentManagementService.setStudents(
				Arrays.asList(
						new Student(1L, "krish@123", "Krishna", LocalDate.of(2014, 10, 023), 10000, 1000,
								Collections.nCopies(7, new Enrollment(1L, null, new Course(null, "Java", 1000, null), 0, null))),
						new Student(1L, "das@123", "Das", LocalDate.of(2014, 10, 023), 10000, 1000,
								Collections.nCopies(7, new Enrollment(2L, null, new Course(null, "Python", 1000, null), 0, null)))));

		MvcResult res = mvc.perform(get("/student/top-5-longest-delinquent-payment"))
				.andExpect(status().isOk()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		String expectedRes = "[{\"name\":\"Krishna\",\"userId\":\"krish@123\",\"enrollmentDate\":\"2014-10-19\",\"courses\":[{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0},{\"courseName\":\"Java\",\"courseFee\":1000.0}],\"pendingFee\":1000.0},{\"name\":\"Das\",\"userId\":\"das@123\",\"enrollmentDate\":\"2014-10-19\",\"courses\":[{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0},{\"courseName\":\"Python\",\"courseFee\":1000.0}],\"pendingFee\":1000.0}]";

		assertEquals(expectedRes, actualRes);
	}

	@Test
	@Order(14)
	public void addCourse_validation() throws Exception{
		MvcResult res = mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(getCourseDetails("", -123).toString())).andExpect(status().isBadRequest()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		String expectedRes = "{\"courseName\":\"Course name should not be null or empty.\",\"courseFee\":\"Course fee should not be negative\"}";
		assertEquals(expectedRes, actualRes);
	}

	@Test
	@Order(15)
	public void addStudent_validation() throws Exception{
		MvcResult res = mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(getStudentDetails("kr", "", null, Arrays.asList(new CourseDto("solidity", 0)), 0).toString())).andExpect(status().isBadRequest()).andReturn();
		String actualRes = res.getResponse().getContentAsString();
		
		String expectedRes = "{\"name\":\"Name should have characters between 3-30.\",\"userId\":\"User Id is mandatory\"}";

		assertEquals(expectedRes, actualRes);
	}



}

/*
 * [{"courses":[{"courseName":"html","courseFee":0},{"courseName":"rust",
 * "courseFee":0}],"pendingFee":20,"name":"Amal","enrollmentDate":"2013-07-30",
 * "userId":"Amal@123"},
 * {"courses":[{"courseName":"ts","courseFee":3000},{"courseName":"js",
 * "courseFee":3500},{"courseName":"go","courseFee":4000}],"pendingFee":30,
 * "name":"Lata","enrollmentDate":"2001-11-03","userId":"lata@123"},
 * {"courses":[{"courseName":"python","courseFee":2000},{"courseName":"rust",
 * "courseFee":3200}],"pendingFee":40,"name":"Rahul","enrollmentDate":
 * "2001-11-03","userId":"Rahul@123"},
 * {"courses":[{"courseName":"go","courseFee":4000},{"courseName":"java",
 * "courseFee":6000}],"pendingFee":50,"name":"Sneha","enrollmentDate":
 * "2013-07-30","userId":"Sneha@123"}]
 */