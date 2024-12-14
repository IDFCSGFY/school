package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.constant.StudentServiceImplTestConstants.*;

class StudentServiceImplTest {

    private StudentServiceImpl out;

    @Test
    public void post_shouldReturnNull() {
        assertNull(out.post(NULL_STUDENT));
    }

    @Test
    public void post_shouldPlaceCorrectID() {
        Student target = new Student(0, "Name", 10);
        assertEquals(1, out.post(target).getId());
    }

    @Test
    public void findByID_shouldReturnNull() {
        assertNull(out.findById(0));
        assertNull(out.findById(1));
        out.post(CORRECT_FIRST_STUDENT);
        assertNotNull(out.findById(1));
    }

    @Test
    public void findByID_shouldReturnCorrectStudent() {
        out.post(CORRECT_FIRST_STUDENT);
        out.post(CORRECT_SECOND_STUDENT);
        assertEquals(CORRECT_FIRST_STUDENT, out.findById(1));
        assertEquals(CORRECT_SECOND_STUDENT, out.findById(2));
    }

    @Test
    public void edit_shouldReturnNull() {
        assertNull(out.edit(NULL_STUDENT));
        assertNull(out.edit(CORRECT_FIRST_STUDENT));
        out.post(CORRECT_FIRST_STUDENT);
        assertNotNull(out.edit(CORRECT_FIRST_STUDENT));
    }

    @Test
    public void edit_shouldReturnNewStudent() {
        out.post(ID1_EX1_STUDENT);
        assertEquals(ID1_EX2_STUDENT, out.edit(ID1_EX2_STUDENT));
    }

    @Test
    public void deleteById_shouldReturnNull() {
        assertNull(out.findById(0));
        assertNull(out.findById(1));
        out.post(CORRECT_FIRST_STUDENT);
        assertNotNull(out.findById(1));
    }

    @Test
    public void deleteById_shouldReturnCorrectStudent() {
        out.post(CORRECT_FIRST_STUDENT);
        out.post(CORRECT_SECOND_STUDENT);
        //assertEquals(CORRECT_FIRST_STUDENT, out.deleteById(1));
    }

    @Test
    public void findAll_shouldReturnAllInList() {
        out.post(CORRECT_FIRST_STUDENT);
        out.post(CORRECT_SECOND_STUDENT);
        List<Student> expected = new ArrayList<>(List.of(CORRECT_FIRST_STUDENT, CORRECT_SECOND_STUDENT));
        assertEquals(expected, out.findAll());
    }

    @Test
    public void findAll_shouldReturnCorrectList() {
        out.post(EX1_AGE_16_STUDENT);
        out.post(EX2_AGE_16_STUDENT);
        out.post(EX1_AGE_17_STUDENT);
        List<Student> expected = new ArrayList<>(List.of(EX1_AGE_16_STUDENT, EX2_AGE_16_STUDENT));
        assertEquals(expected, out.filterByAge(16));
        expected = new ArrayList<>(List.of(EX1_AGE_17_STUDENT));
        assertEquals(expected, out.filterByAge(17));
    }
}