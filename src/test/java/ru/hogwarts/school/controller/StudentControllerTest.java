package ru.hogwarts.school.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository repository;

    @SpyBean
    private StudentServiceImpl service;

    @InjectMocks
    private StudentController controller;

    static final String URL = "/student";
    static final Long ID = 1L;
    static final String NAME = "Test Name";
    static final Integer AGE = 100;

    @Test
    void addNewStudent_shouldReturnStudent() throws Exception {
        JSONObject consumable = new JSONObject();
        consumable.put("name", NAME);
        consumable.put("age", AGE);

        Student expected = new Student();
        expected.setId(ID);
        expected.setName(NAME);
        expected.setAge(AGE);

        when(repository.save(any(Student.class))).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(consumable.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE));
    }

    @Test
    void filterByAge_shouldReturnCollectionOfStudentGivenSpecificAge() throws Exception {
        Collection<Student> collectionFromRepository = new ArrayList<>();
        collectionFromRepository.add(new Student(1, "One", 10));
        collectionFromRepository.add(new Student(2, "Two", 10));
        collectionFromRepository.add(new Student(4, "Four", 10));

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(4);

        when(repository.findByAge(10)).thenReturn(collectionFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/filter?age=10"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(expected)
                );
    }

    @Test
    void filterByAge_shouldReturnCollectionOfStudentGivenRangeOfAges() throws Exception {
        Collection<Student> collectionFromRepository = new ArrayList<>();
        collectionFromRepository.add(new Student(1, "One", 10));
        collectionFromRepository.add(new Student(2, "Two", 12));

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);

        when(repository.findByAgeBetween(10, 14)).thenReturn(collectionFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/filter?age=10&ageMax=14"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(new ArrayList<>(List.of(1, 2)))
                );
    }

    @Test
    void findFacultyOfStudent_shouldReturnFaculty() throws Exception {
        Student s = new Student(ID, NAME, AGE);
        Faculty expected = new Faculty(1, "FacultyName", "FacultyColor");
        s.setFaculty(expected);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(s));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + ID + "/faculty"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("FacultyName"),
                        jsonPath("$.color").value("FacultyColor")
                );
    }

    @Test
    void get_shouldReturnStudentById() throws Exception {
        Student s = new Student(ID, NAME, AGE);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(s));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + ID))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.name").value(NAME),
                        jsonPath("$.age").value(AGE)
                );
    }

    @Test
    void all_shouldReturnCollectionOfAllStudents() throws Exception {
        List<Student> collectionFromRepository = new ArrayList<>();
        collectionFromRepository.add(new Student(1, "One", 10));
        collectionFromRepository.add(new Student(2, "Two", 12));
        collectionFromRepository.add(new Student(4, "Four", 16));

        when(repository.findAll()).thenReturn(collectionFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(new ArrayList<>(List.of(1,2,4))),
                        jsonPath("$.*.name").value(new ArrayList<>(List.of("One", "Two", "Four"))),
                        jsonPath("$.*.age").value(new ArrayList<>(List.of(10,12,16)))
                );
    }

    @Test
    void edit_shouldReturnEditedStudent() throws Exception {
        JSONObject consumable = new JSONObject();
        consumable.put("id", ID);
        consumable.put("name", NAME);
        consumable.put("age", AGE);

        Student s = new Student(ID, NAME, AGE);

        when(repository.save(isNotNull(Student.class))).thenReturn(s);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(consumable.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(ID),
                        jsonPath("$.name").value(NAME),
                        jsonPath("$.age").value(AGE)
                );
    }

    @Test
    void delete_shouldReturnDeletedStudent() throws Exception {
        Student s = new Student(ID, NAME, AGE);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/" + ID))
                .andExpectAll(
                        status().isOk()
                );
    }
}