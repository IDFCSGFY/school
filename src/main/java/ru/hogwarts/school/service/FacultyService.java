package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty post(Faculty faculty);

    Faculty findById(long id);

    Faculty edit(Faculty faculty);

    void deleteById(long id);

    Collection<Faculty> findAll();

    Collection<Faculty> filterByColor(String color);

    Faculty findByName(String name);

    Faculty findByColor(String color);
}
