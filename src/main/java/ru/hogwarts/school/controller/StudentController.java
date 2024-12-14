package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<Collection<Student>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> get(@PathVariable long id) {
        Student target = service.findById(id);
        if (target==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(target);
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

    @GetMapping("filter")
    public ResponseEntity<Collection<Student>> filterByAge(@RequestParam("age") int age) {
        return ResponseEntity.ok(service.filterByAge(age));
    }
}
