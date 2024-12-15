package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Faculty> add(@RequestBody Faculty faculty) {
        Faculty target = service.post(faculty);
        if (target == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(target);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Faculty> findFacultyByName(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @GetMapping(params = {"color"})
    public ResponseEntity<Faculty> findFacultyByColor(@RequestParam(required = false) String color) {
        return ResponseEntity.ok(service.findByColor(color));
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping
    public ResponseEntity<Faculty> edit(@RequestBody Faculty faculty) {
        Faculty target = service.edit(faculty);
        if (target == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(target);
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Faculty>> filterByColor(@RequestParam String color) {
        return ResponseEntity.ok(service.filterByColor(color));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> delete(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> get(@PathVariable long id) {
        Faculty target = service.findById(id);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(target);
    }
}
