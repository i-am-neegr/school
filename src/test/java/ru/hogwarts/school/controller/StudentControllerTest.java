package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    public void setUp() {
        testStudent = new Student();
        testStudent.setName("Test Student");
        testStudent.setAge(20);
        studentService.addStudents(testStudent);
    }

    @Test
    public void testGetStudents() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/student", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + testStudent.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(testStudent.getName());
    }

    @Test
    public void testAddStudent() {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setAge(22);

        ResponseEntity<Student> response = restTemplate.postForEntity("/student/add", newStudent, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
    }

    @Test
    public void testPutStudent() {
        testStudent.setName("Updated Student");

        ResponseEntity<Student> response = restTemplate.exchange("/student/put", HttpMethod.PUT, new HttpEntity<>(testStudent), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(testStudent.getName());
    }

    @Test
    public void testDeleteStudent() {
        ResponseEntity<Void> response = restTemplate.exchange("/student/del/" + testStudent.getId(), HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        // Проверяем, что студент был удален
        ResponseEntity<Student> getResponse = restTemplate.getForEntity("/student/" + testStudent.getId(), Student.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void testFindBetweenAge() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/student/find/between/age?min=18&max=25", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/student/get/faculty/" + testStudent.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        // Здесь можно добавить дополнительные проверки, если необходимо
    }
}

