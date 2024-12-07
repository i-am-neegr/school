package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping()
    public ResponseEntity<Collection<Faculty>> getFaculty() {
        return ResponseEntity.ok(facultyService.getFaculties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/add")
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @PutMapping("/put")
    public ResponseEntity<Faculty> putFaculty(@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.putFaculty(faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Faculty> delFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(value = "name", required = false) String name,
                                                             @RequestParam(value = "color", required = false) String color)
    {
        return ResponseEntity.ok(facultyService.getFacultiesByNameOrColor(name, color));
    }
    @GetMapping("get/students/{id}")
    public ResponseEntity<Set<Student>> getStudents(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getStudentsFromFaculty(id));
    }
}