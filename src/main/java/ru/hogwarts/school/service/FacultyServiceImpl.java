package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private int count = 0;

    @Override
    public Faculty post(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        faculty.setId(++count);
        facultyMap.put((long) count, faculty);
        return faculty;
    }

    @Override
    public Faculty findById(long id) {
        if (!facultyMap.containsKey(id)) {
            return null;
        }
        return facultyMap.get(id);
    }

    @Override
    public Faculty edit(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty deleteById(long id) {
        if (!facultyMap.containsKey(id)) {
            return null;
        }
        return facultyMap.remove(id);
    }

    @Override
    public Collection<Faculty> findAll() {
        return List.copyOf(facultyMap.values());
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return facultyMap.values().stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
