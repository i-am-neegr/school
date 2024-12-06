package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Collection<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    public Student getStudent(long id) {
        return this.studentRepository.findById(id).orElse(null);
    }

    public Student addStudents(Student student) {
        return this.studentRepository.save(student);
    }

    public void removeStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student putStudent(Student student) {
        return this.studentRepository.save(student);
    }
}