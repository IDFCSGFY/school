package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student post(Student student);

    Student findById(long id);

    Student edit(Student student);

    void deleteById(long id);

    Collection<Student> findAll();

    Collection<Student> filterByAge(int age);

    Collection<Student> filterByAge(int age, int ageMax);

    Faculty findFacultyOfStudentById(long id);
}
