package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student post(Student student) {
        if (student == null) {
            return null;
        }
        return repository.save(student);
    }

    @Override
    public Student findById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public Student edit(Student student) {
        if (student == null) {
            return null;
        }
        return repository.save(student);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Collection<Student> findAll() {
        return List.copyOf(repository.findAll());
    }

    @Override
    public Collection<Student> filterByAge(int age) {
//        return repository.findAll().stream()
//                .filter(e -> e.getAge() == age)
//                .collect(Collectors.toList());
        return repository.findByAge(age);
    }

    @Override
    public Collection<Student> filterByAge(int age, int ageMax) {
        return List.copyOf(repository.findByAgeBetween(age, ageMax));
    }

    public Faculty findFacultyOfStudentById(long id) {
        return findById(id).getFaculty();
    }
}
