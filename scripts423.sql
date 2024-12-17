SELECT
    Student.name AS student_name,
    Student.age AS student_age,
    Faculty.name AS faculty_name
FROM Student
         JOIN Faculty ON Student.faculty_id = Faculty.id;

SELECT
    Student.name AS student_name,
    Student.age AS student_age
FROM Student JOIN Faculty ON Student.faculty_id = Faculty.id
WHERE Student.avatar_id IS NOT NULL;
