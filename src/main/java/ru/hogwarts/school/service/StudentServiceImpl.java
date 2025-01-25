package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student post(Student student) {
        logger.info("StudentService post method is invoked");
        if (student == null) {
            logger.error("Student object is null");
            return null;
        }
        return repository.save(student);
    }

    @Override
    public Student findById(long id) {
        logger.info("StudentService findById method is invoked");
        return repository.findById(id).get();
    }

    @Override
    public Student edit(Student student) {
        logger.info("StudentService edit method is invoked");
        if (student == null) {
            logger.info("Student object is null");
            return null;
        }
        return repository.save(student);
    }

    @Override
    public void deleteById(long id) {
        logger.info("StudentService deleteById method is invoked");
        repository.deleteById(id);
    }

    @Override
    public Collection<Student> findAll() {
        logger.info("StudentService findAll method is invoked");
        return List.copyOf(repository.findAll());
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        logger.info("StudentService filterByAge method is invoked");
        return repository.findAll().stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Student> filterByAge(int age, int ageMax) {
        logger.info("StudentService filterByAge method is invoked");
        return List.copyOf(repository.findByAgeBetween(age, ageMax));
    }

    public Faculty findFacultyOfStudentById(long id) {
        logger.info("StudentService findFacultyOfStudentById method is invoked");
        return findById(id).getFaculty();
    }

    @Override
    public Integer countAll() {
        logger.info("StudentService countAll method is invoked");
        return repository.countAllStudents();
    }

    @Override
    public Double getAverageAgeOfAllStudents() {
        logger.info("StudentService getAverageAgeOfAllStudents method is invoked");
        return repository.getAverageAgeOfAllStudents();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("StudentService getLastFiveStudents method is invoked");
        return List.copyOf(repository.getLastFiveStudents());
    }
}
