package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    public FacultyServiceImpl(FacultyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Faculty post(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        return repository.save(faculty);
    }

    @Override
    public Faculty findById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public Faculty edit(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        return repository.save(faculty);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Collection<Faculty> findAll() {
        return List.copyOf(repository.findAll());
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return repository.findAll().stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toList());
    }

    @Override
    public Faculty findByName(String name) {
        return repository.findFirstByNameIgnoreCase(name);
    }

    @Override
    public Faculty findByColor(String color) {
        return repository.findFirstByColorIgnoreCase(color);
    }

    @Override
    public Collection<Student> findStudentsOfFacultyById(long id) {
        return List.copyOf(repository.findById(id).get().getStudents());
    }

    @Override
    public String longestName() {
        return repository.findAll().stream()
                .map(Faculty::getName)
                .sorted(
                        Comparator
                                .comparing(String::length)
                                .reversed())
                .toList()
                .get(0);
    }
}
