package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student post(Student student);

    Student findById(long id);

    Student edit(Student student);

    Student deleteById(long id);

    Collection<Student> findAll();

    Collection<Student> filterByAge(int age);
}
