package ru.hogwarts.school.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;

    private Faculty testFaculty;

    @BeforeEach
    public void setUp() {
        testFaculty = new Faculty();
        testFaculty.setName("Test Faculty");
        testFaculty.setColor("Blue");
        facultyService.addFaculty(testFaculty);
    }

    @Test
    public void testGetFaculties() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetFacultyById() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + testFaculty.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(testFaculty.getName());
    }

    @Test
    public void testAddFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("New Faculty");
        newFaculty.setColor("Green");

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty/add", newFaculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(newFaculty.getName());
    }

    @Test
    public void testPutFaculty() {
        testFaculty.setColor("Red");
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(testFaculty);

        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty/put", HttpMethod.PUT, requestEntity, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    public void testDelFaculty() {
        Long id = testFaculty.getId();

        ResponseEntity<Void> response = restTemplate.exchange("/faculty/del/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        // Проверка, что факультет был удален
        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("/faculty/" + id, Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void testFindFaculties() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty/find?name=Test Faculty", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetStudentsFromFaculty() {
        ResponseEntity<Set> response = restTemplate.getForEntity("/faculty/get/students/" + testFaculty.getId(), Set.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
