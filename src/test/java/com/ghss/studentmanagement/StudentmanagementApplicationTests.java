package com.ghss.studentmanagement;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;
import com.ghss.studentmanagement.service.impl.StudentServiceImpl;

@SpringBootTest
class StudentmanagementApplicationTests {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	CourseServiceImpl courseServiceImpl;

	@Test
	void contextLoads() {
	}

	@Sql("/data.sql")
	@Test
	public void getIntitalCourseListTest(){
		List<Course> courses = courseServiceImpl.
	}


}
