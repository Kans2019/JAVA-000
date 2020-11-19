package org.geektime.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IStudentServiceTest {
    @Autowired
    private IStudentService studentService;

    @org.junit.Test
    public void getById() {
        System.out.println(studentService.getById(12700003));
        studentService.getById(12700003);
        studentService.getById(12700003);
        studentService.getById(12700003);
    }
}