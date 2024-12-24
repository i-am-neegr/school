package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findStudentsByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "SELECT AVG(student.age) FROM student", nativeQuery = true)
    Double getAverageAgeStudents();

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer countStudent();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    Collection<Student> getLastStudent(Integer limit);
}