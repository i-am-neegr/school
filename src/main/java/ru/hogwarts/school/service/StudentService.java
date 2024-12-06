package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students;
    private Long studentId;

    public StudentService() {
        this.students = new HashMap<>();
        this.studentId = 1L;
    }

    public Map<Long, Student> getStudents() {
        return this.students;
    }

    public Student getStudent(long id) {
        return this.students.get(id);
    }

    public Student addStudents(Student student) {
        student.setId(this.studentId);
        students.put(studentId, student);
        studentId++;
        return student;
    }

    public Student removeStudent(Long id) {
        return students.remove(id);
    }

    public Student putStudent(Long id, Student student) {
        if(students.containsKey(id)) {
            student.setId(id);
            students.put(id, student);
            return student;
        }
        return null;
    }

    public List<Student> getFilterStudents(int age) {
        Collection<Student> students = this.students.values();
        return students.stream().filter(o -> o.getAge() == age).toList();
    }
}