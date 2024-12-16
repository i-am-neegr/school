package ru.hogwarts.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    Faculty faculty;

    @BeforeEach
    public void setUp() {
        faculty = new Faculty();
        faculty.setName("Hogwarts");
    }

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;


    @Test
    public void testGetAllFaculties() {
        when(facultyRepository.findAll()).thenReturn(new ArrayList<>(List.of(faculty)));
        Assertions.assertEquals(facultyService.getFaculties().size(), 1);
    }

    @Test
    public void testGetFacultyById() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        Assertions.assertEquals(facultyService.getFaculty(1L).getName(), "Hogwarts");
    }

    @Test
    public void testAddFaculty() {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        Assertions.assertEquals(facultyService.addFaculty(faculty).getName(), faculty.getName());
    }

    @Test
    public void testUpdateFaculty() {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        Assertions.assertEquals(facultyService.putFaculty(faculty).getName(), faculty.getName());
    }

    @Test
    public void testGetFacultiesByNameOrColor() {
        when(facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(any(), any())).
                thenReturn(List.of(faculty));
        Assertions.assertEquals(facultyService.getFacultiesByNameOrColor("", "").size(), 1);
    }

    @Test
    public void testGetStudentsFromFaculty() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        Assertions.assertNull(facultyService.getStudentsFromFaculty(1L));
    }

}