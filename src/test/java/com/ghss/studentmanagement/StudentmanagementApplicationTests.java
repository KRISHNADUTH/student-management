package com.ghss.studentmanagement;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;
import com.ghss.studentmanagement.service.impl.StudentServiceImpl;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentmanagementApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	public void getAllCourses() throws Exception {
		mvc.perform(get("/courses/get-all").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

}
