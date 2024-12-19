package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Collection<Student> getStudents() {
        logger.info("Get students");
        return this.studentRepository.findAll();
    }

    public Student getStudent(long id) {
        logger.info("Get student with id {}", id);
        return this.studentRepository.findById(id).orElse(null);
    }

    public Student addStudents(Student student) {
        logger.info("Add student");
        return this.studentRepository.save(student);
    }

    public void removeStudent(Long id) {
        logger.info("Remove student");
        studentRepository.deleteById(id);
    }

    public Student putStudent(Student student) {
        logger.info("Put student");
        return this.studentRepository.save(student);
    }

    public Collection<Student> getStudentBetweenAge(int startAge, int endAge) {
        logger.info("Get student between age");
        return studentRepository.findStudentsByAgeBetween(startAge, endAge);
    }
    public Faculty getStudentFaculty(Long id){
        logger.info("Get student faculty");
        return getStudent(id).getFaculty();
    }

    public Double getAverageStudentAge() {
        logger.info("Get average student age");
        return studentRepository.getAverageAgeStudents();
    }
    public Collection<Student> getLastStudents(Integer limit) {
        logger.info("Get last students");
        return studentRepository.getLastStudent(limit);
    }

    public Collection<String> getNamesStartingWith(String prefix) {
        logger.info("Get names starting with {}", prefix);
        Collection<Student> students = this.getStudents();
        String finalPrefix = prefix.toLowerCase();
        return students
                .stream()
                .parallel()
                .filter(student -> student.getName().toLowerCase().startsWith(finalPrefix))
                .map(student -> student.getName().toUpperCase())
                .sorted().toList();
    }

    public Double getAverageAge() {
        logger.info("Get average age");
        Collection<Student> students = this.getStudents();
        
        return students
                .stream()
                .parallel()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}