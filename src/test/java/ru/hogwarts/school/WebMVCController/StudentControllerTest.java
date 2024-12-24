package ru.hogwarts.school.WebMVCController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Student 2");
        student2.setAge(21);

        when(studentService.getStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("Student 1"))
                .andExpect(jsonPath("$[1].name").value("Student 2"));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Student 1");
        student.setAge(20);

        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Student 1"));
    }

    @Test
    public void testGetStudentByIdNotFound() throws Exception {
        when(studentService.getStudent(1L)).thenReturn(null);

        mockMvc.perform(get("/student/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("New Student");
        student.setAge(22);

        when(studentService.addStudents(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Student"));
    }

    @Test
    public void testPutStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Updated Student");
        student.setAge(23);

        when(studentService.putStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Student"));
    }

    @Test
    public void testPutStudentNotFound() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Nonexistent Student");
        student.setAge(30);

        when(studentService.putStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(put("/student/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelStudent() throws Exception {
        doNothing().when(studentService).removeStudent(1L);

        mockMvc.perform(delete("/student/del/{id}", 1L))
                .andExpect(status().isOk());

        verify(studentService, times(1)).removeStudent(1L);
    }

    @Test
    public void testFindStudentsBetweenAge() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Student 2");
        student2.setAge(22);

        when(studentService.getStudentBetweenAge(18, 25)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student/find/between/age")
                        .param("min", "18")
                        .param("max", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Student 1"))
                .andExpect(jsonPath("$[1].name").value("Student 2"));
    }

    @Test
    public void testGetFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Faculty 1");

        when(studentService.getStudentFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/get/faculty/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Faculty 1"));
    }
}
