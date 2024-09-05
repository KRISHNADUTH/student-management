package com.ghss.studentmanagement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;
import com.ghss.studentmanagement.service.StudentManagementService;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;
import com.ghss.studentmanagement.service.impl.StudentServiceImpl;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class StudentmanagementApplicationTests {

	@Autowired
	private MockMvc mvc;


	@Autowired
	StudentManagementService studentManagementService;

	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	public void testGetAllCourses() throws Exception {
		mvc.perform(get("/courses/get-all").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	public void testAddCourse() throws Exception {
		mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isOk());
		mvc.perform(post("/courses/add-course").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getCourseDetails("flutter", 1550).toString())).andExpect(status().isBadRequest())
				.andDo(result -> {
					if (result.getResponse().getStatus() != HttpStatus.BAD_REQUEST.value()) {
						System.out.println("Courses must have unique name condition failed");
					}
				});
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
		mvc.perform(post("/students/add-student").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getStudentDetails("Krishna", LocalDate.of(2023, 12, 11), courseDtos, 010).toString()))
				.andExpect(status().isCreated());
	}

	public JSONObject getStudentDetails(String name, LocalDate enrollmentDate, List<CourseDto> courses,
			double pendingFee) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("enrollmentDate", enrollmentDate);
		map.put("courses", courses);
		map.put("pendingFee", pendingFee);
		return new JSONObject(map);
	}

}
