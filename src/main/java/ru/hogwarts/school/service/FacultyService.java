package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);


    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;

    }

    public Collection<Faculty> getFaculties() {
        logger.info("getFaculties");
        return this.facultyRepository.findAll();
    }

    public Faculty getFaculty(Long id) {
        logger.info("getFaculty");
        return this.facultyRepository.findById((id)).orElse(null);
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("addFaculty");
        return this.facultyRepository.save(faculty);
    }

    public void removeFaculty(Long id) {
        logger.info("removeFaculty");
        this.facultyRepository.deleteById(id);
    }

    public Faculty putFaculty(Faculty faculty) {
        logger.info("putFaculty");
        return this.facultyRepository.save(faculty);
    }

    public Collection<Faculty> getFacultiesByNameOrColor(String name, String color) {
        logger.info("getFacultiesByNameOrColor");
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
    public Set<Student> getStudentsFromFaculty(Long id){
        logger.info("getStudentsFromFaculty");
        return getFaculty(id).getStudents();
    }

    public Integer getStudentsCount(Long id) {
        logger.info("getStudentsCount");
        return facultyRepository.countStudentsInFaculty(id);
    }
}