package ru.hogwarts.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.setName("Hogwarts");
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts2");
        student.setFaculty(faculty);
    }

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;


    @Test
    public void testGetAllStudent() {
        when(studentRepository.findAll()).thenReturn(new ArrayList<>(List.of(student)));
        Assertions.assertEquals(studentService.getStudents().size(), 1);
    }

    @Test
    public void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Assertions.assertEquals(studentService.getStudent(1L).getName(), "Hogwarts");
    }

    @Test
    public void testAddStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Assertions.assertEquals(studentService.addStudents(student).getName(), student.getName());
    }

    @Test
    public void testUpdateStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Assertions.assertEquals(studentService.putStudent(student).getName(), student.getName());
    }

    @Test
    public void testGetStudentBetweenAge() {
        when(studentRepository.findStudentsByAgeBetween(anyInt(), anyInt())).thenReturn(List.of(student));
        Assertions.assertEquals(studentService.getStudentBetweenAge(1, 10).size(), 1);
    }

    @Test
    public void testGetStudentFaculty() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Assertions.assertEquals(studentService.getStudentFaculty(1L).getName(), "Hogwarts2");
    }
}