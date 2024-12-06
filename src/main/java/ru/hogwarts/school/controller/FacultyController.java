package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping()
    public ResponseEntity<Map<Long, Faculty>> getStudent() {
        return ResponseEntity.ok(facultyService.getFaculties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getStudent(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/add")
    public ResponseEntity<Faculty> addStudent(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Faculty> putStudent(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.putFaculty(id, faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @GetMapping("/filter/{color}")
    public ResponseEntity<List<Faculty>> getFaculty(@PathVariable String color) {
        return ResponseEntity.ok(facultyService.getFilterFaculty(color));
    }
}