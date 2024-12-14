package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private int count = 0;

    @Override
    public Student post(Student student) {
        if (student == null) {
            return null;
        }
        student.setId(++count);
        studentMap.put((long) count, student);
        return student;
    }

    @Override
    public Student findById(long id) {
        if (!studentMap.containsKey(id)) {
            return null;
        }
        return studentMap.get(id);
    }

    @Override
    public Student edit(Student student) {
        if (student == null) {
            return null;
        }
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public Student deleteById(long id) {
        if (!studentMap.containsKey(id)) {
            return null;
        }
        return studentMap.remove(id);
    }

    @Override
    public Collection<Student> findAll() {
        return List.copyOf(studentMap.values());
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        return studentMap.values().stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
    }
}
