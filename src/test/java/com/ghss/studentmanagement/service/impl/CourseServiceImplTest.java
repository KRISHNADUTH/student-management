package com.ghss.studentmanagement.service.impl;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import com.ghss.studentmanagement.controller.StudentManagementController;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.service.StudentManagementService;

public class CourseServiceImplTest {
    
	@MockBean
	StudentManagementService studentManagementService;

	@Autowired
	CourseServiceImpl courseServiceImpl;

    @Test
    public void getIntitalCourseListTest() {
        // when(studentManagementService.getAllCourses()).thenReturn()
    }
}
