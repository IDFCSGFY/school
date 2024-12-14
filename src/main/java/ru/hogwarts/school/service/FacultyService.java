package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty post(Faculty faculty);

    Faculty findById(long id);

    Faculty edit(Faculty faculty);

    Faculty deleteById(long id);

    Collection<Faculty> findAll();

    Collection<Faculty> filterByColor(String color);
}
