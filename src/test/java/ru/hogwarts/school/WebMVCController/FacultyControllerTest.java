package ru.hogwarts.school.WebMVCController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetFaculty() throws Exception {
        when(facultyService.getFaculties()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(facultyService, times(1)).getFaculties();
    }

    @Test
    void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(facultyService, times(1)).getFaculty(1L);
    }

    @Test
    void testAddFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("Green"));

        verify(facultyService, times(1)).addFaculty(any(Faculty.class));
    }

    @Test
    void testPutFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        when(facultyService.putFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hufflepuff"))
                .andExpect(jsonPath("$.color").value("Yellow"));

        verify(facultyService, times(1)).putFaculty(any(Faculty.class));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).removeFaculty(1L);

        mockMvc.perform(delete("/faculty/del/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).removeFaculty(1L);
    }

    @Test
    void testFindFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");
        when(facultyService.getFacultiesByNameOrColor("Ravenclaw", null))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/find").param("name", "Ravenclaw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"));

        verify(facultyService, times(1)).getFacultiesByNameOrColor("Ravenclaw", null);
    }

    @Test
    void testGetStudentsFromFaculty() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(12);
        when(facultyService.getStudentsFromFaculty(1L)).thenReturn(Set.of(student));

        mockMvc.perform(get("/faculty/get/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry"));

        verify(facultyService, times(1)).getStudentsFromFaculty(1L);
    }
}
