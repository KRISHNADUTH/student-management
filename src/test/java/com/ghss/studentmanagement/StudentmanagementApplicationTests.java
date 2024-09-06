package com.ghss.studentmanagement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;
import com.ghss.studentmanagement.service.StudentManagementService;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;
import com.ghss.studentmanagement.service.impl.StudentServiceImpl;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class StudentmanagementApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StudentManagementService studentManagementService;

	@Autowired
	CourseServiceImpl courseServiceImpl;

	@Test
	void contextLoads() {
	}

	@Test
	@Order(0)
	public void testGetAllCourses_NoCourseFound() throws Exception {
		// Mock the behavior of studentManagementService to return an empty list for
		// getAllCourses()
		when(studentManagementService.getAllCourses()).thenReturn(Collections.emptyList());

		// Perform the GET request on the /courses/get-all endpoint
		MvcResult result = mvc.perform(get("/courses/get-all")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound())
				.andReturn();

		// Verify the response contains the appropriate status and message
		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);

		// Expected ResponseDto structure
		String expectedResponse = "{\"statusCode\":\"404\",\"statusMsg\":\"Requested resource not found\"}"; // Update based on actual
																								// message in
																								// StudentManagementConstants

		// Assert the response
		assertThat(responseContent).isEqualToIgnoringWhitespace(expectedResponse);

		// Verify that the getAllCourses() method was called on the service
		verify(studentManagementService,times(1)).getAllCourses();
	}

	@Test
	@Order(1)
	public void testGetAllCourses_ReturnCourses() throws Exception {
		// Mock the service to return a non-empty list of courses
		List<Course> mockCourses = Arrays.asList(
				new Course(1L, "java", 4000, null),
				new Course(2L, "Python", 3500, null)
		);

		when(studentManagementService.getAllCourses()).thenReturn(mockCourses);

		mvc.perform(get("/courses/get-all").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()); // Expect 200 OK status
	}

	@Test
	@Order(2)
	public void testAddCourse_addNewCourse_and_addExistingCourse() throws Exception {
		// StudentManagementService.courses = Arrays.asList(new Course(1L, "java", 6000, null));
		when(studentManagementService.findByCourseName("flutter")).thenReturn(Optional.empty());
		
		mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isOk());
		// mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
		// 		.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isBadRequest())
		// 		.andDo(result -> {
		// 			if (result.getResponse().getStatus() != HttpStatus.BAD_REQUEST.value()) {
		// 				System.out.println("Courses must have unique name condition failed");
		// 			}
		// 		});
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
	public void testAddStudent() throws Exception {
		List<CourseDto> courseDtos = Arrays.asList(new CourseDto("python", 2000), new CourseDto("java", 6000),
				new CourseDto("go", 4000));

		MvcResult result = mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Krishna", "krishna@123", LocalDate.of(2023, 12, 11), courseDtos, 010)
						.toString()))
				.andReturn();
		// Log the response content
		String responseContent = result.getResponse().getContentAsString();
		System.out.println("Response Content: " + responseContent);

		// Now you can add your assertions here
		// Verify the structure of the response
		// Adjust these to match your actual response keys
		assertThat(responseContent).contains("Student added");
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

}
