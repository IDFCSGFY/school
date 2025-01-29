package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Student> add(@RequestBody Student student) {
        Student target = service.post(student);
        if (target == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(target);
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Student>> filterByAge(@RequestParam int age, @RequestParam(required = false) Integer ageMax) {
        if (ageMax == null) {
            return ResponseEntity.ok(service.filterByAge(age));
        }
        return ResponseEntity.ok(service.filterByAge(age, ageMax));
    }

    @GetMapping("count")
    public ResponseEntity<Integer> countAllStudents() {
        return ResponseEntity.ok(service.countAll());
    }

    @GetMapping("averageAge")
    public ResponseEntity<Double> getAverageAgeOfAllStudents() {
        return ResponseEntity.ok(service.getAverageAgeOfAllStudents());
    }

    @GetMapping("getLastFive")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(service.getLastFiveStudents());
    }

    @GetMapping("getByFirstLetter")
    public ResponseEntity<Collection<String>> getByFirstLetter(@RequestParam String letter) {
        return ResponseEntity.ok(service.getByFirstLetter(letter));
    }

    @GetMapping("getAvgAge")
    public ResponseEntity<Double> getAvgAge() {
        return ResponseEntity.ok(service.getAvgAgeUsingStream());
    }

    @GetMapping("formula")
    public ResponseEntity<Integer> formula() {
        return ResponseEntity.ok(service.formula());
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> findFacultyOfStudent(@PathVariable long id) {
        return ResponseEntity.ok(service.findFacultyOfStudentById(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> get(@PathVariable long id) {
        Student target = service.findById(id);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(target);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping
    public ResponseEntity<Student> edit(@RequestBody Student student) {
        Student target = service.edit(student);
        if (target == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(target);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> delete(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
