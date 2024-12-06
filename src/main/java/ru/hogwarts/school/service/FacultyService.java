package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties;
    private Long facultyId;

    public FacultyService() {
        this.faculties = new HashMap<>();
        this.facultyId = 1L;
    }

    public Map<Long, Faculty> getFaculties() {
        return this.faculties;
    }

    public Faculty getFaculty(Long id) {
        return this.faculties.get(id);
    }

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(this.facultyId);
        faculties.put(facultyId, faculty);
        facultyId++;
        return faculty;
    }

    public Faculty removeFaculty(Long id) {
        return faculties.remove(id);
    }

    public Faculty putFaculty(Long id, Faculty faculty) {
        if(faculties.containsKey(id)) {
            faculty.setId(id);
            faculties.put(id, faculty);
            return faculty;
        }
        return null;
    }

    public List<Faculty> getFilterFaculty(String color) {
        Collection<Faculty> faculties = this.faculties.values();
        return faculties.stream().filter(o -> o.getColor().equals(color)).toList();
    }
}