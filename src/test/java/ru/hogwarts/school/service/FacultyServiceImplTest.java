package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.constant.FacultyServiceImplTestConstants.*;
import static ru.hogwarts.school.constant.FacultyServiceImplTestConstants.CORRECT_FIRST_FACULTY;

class FacultyServiceImplTest {

    private FacultyServiceImpl out;

    @Test
    public void post_shouldReturnNull() {
        assertNull(out.post(NULL_FACULTY));
    }

    @Test
    public void post_shouldPlaceCorrectID() {
        Faculty target = new Faculty(0, "Name", "color");
        assertEquals(1, out.post(target).getId());
    }

    @Test
    public void findByID_shouldReturnNull() {
        assertNull(out.findById(0));
        assertNull(out.findById(1));
        out.post(CORRECT_FIRST_FACULTY);
        assertNotNull(out.findById(1));
    }

    @Test
    public void findByID_shouldReturnCorrectFaculty() {
        out.post(CORRECT_FIRST_FACULTY);
        out.post(CORRECT_SECOND_FACULTY);
        assertEquals(CORRECT_FIRST_FACULTY, out.findById(1));
        assertEquals(CORRECT_SECOND_FACULTY, out.findById(2));
    }

    @Test
    public void edit_shouldReturnNull() {
        assertNull(out.edit(NULL_FACULTY));
        assertNull(out.edit(CORRECT_FIRST_FACULTY));
        out.post(CORRECT_FIRST_FACULTY);
        assertNotNull(out.edit(CORRECT_FIRST_FACULTY));
    }

    @Test
    public void edit_shouldReturnNewFaculty() {
        out.post(ID1_EX1_FACULTY);
        assertEquals(ID1_EX2_FACULTY, out.edit(ID1_EX2_FACULTY));
    }

    @Test
    public void deleteById_shouldReturnNull() {
        assertNull(out.findById(0));
        assertNull(out.findById(1));
        out.post(CORRECT_FIRST_FACULTY);
        assertNotNull(out.findById(1));
    }

    @Test
    public void deleteById_shouldReturnCorrectFaculty() {
        out.post(CORRECT_FIRST_FACULTY);
        out.post(CORRECT_SECOND_FACULTY);
        //assertEquals(CORRECT_FIRST_FACULTY, out.deleteById(1));
    }

    @Test
    public void findAll_shouldReturnAllInList() {
        out.post(CORRECT_FIRST_FACULTY);
        out.post(CORRECT_SECOND_FACULTY);
        List<Faculty> expected = new ArrayList<>(List.of(CORRECT_FIRST_FACULTY, CORRECT_SECOND_FACULTY));
        assertEquals(expected, out.findAll());
    }

    @Test
    public void findAll_shouldReturnCorrectList() {
        out.post(EX1_COLOR_RED_FACULTY);
        out.post(EX2_COLOR_RED_FACULTY);
        out.post(EX1_COLOR_BLUE_FACULTY);
        List<Faculty> expected = new ArrayList<>(List.of(EX1_COLOR_RED_FACULTY, EX2_COLOR_RED_FACULTY));
        assertEquals(expected, out.filterByColor("red"));
        expected = new ArrayList<>(List.of(EX1_COLOR_BLUE_FACULTY));
        assertEquals(expected, out.filterByColor("blue"));
    }

}