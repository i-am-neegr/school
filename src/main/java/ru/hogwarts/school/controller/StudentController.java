package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> getStudent() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudents(student));
    }

    @PutMapping("/put")
    public ResponseEntity<Student> putStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.putStudent(student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Student> delStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("find/between/age")
    public ResponseEntity<Collection<Student>> findBetweenAge(@RequestParam("min") Integer min,
                                                              @RequestParam("max") Integer max) {
        return ResponseEntity.ok(studentService.getStudentBetweenAge(min, max));
    }
    @GetMapping("get/faculty/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentFaculty(id));
    }

    @GetMapping("get/average/age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageStudentAge());
    }
    @GetMapping("get/last/student/{limit}")
    public ResponseEntity<Collection<Student>> getLastStudent(@PathVariable Integer limit) {
        return ResponseEntity.ok(studentService.getLastStudents(limit));
    }

    @GetMapping("get/names-startingwith{prefix}")
    public ResponseEntity<Collection<String>> getNamesStartingWith(@PathVariable String prefix) {
        return ResponseEntity.ok(studentService.getNamesStartingWith(prefix));
    }

    @GetMapping("get/average-age")
    public ResponseEntity<Double> getAverage() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("print-parallel")
    public void printParallel() {
        studentService.printParallel();
    }

    @GetMapping("print-synchronised")
    public void printSynchronised() {
        studentService.printSynchronized();
    }
}