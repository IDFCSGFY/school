package ru.hogwarts.school.controller;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FacultyRepository repository;

    @SpyBean
    FacultyServiceImpl service;

    @InjectMocks
    FacultyController controller;

    static final String URI = "/faculty";
    static final Integer DEFAULT_ID = 1;
    static final String DEFAULT_NAME = "Faculty Name";
    static final String DEFAULT_COLOR = "Faculty color";

    @Test
    void add_shouldReturnAddedFaculty() throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", DEFAULT_NAME);
        json.put("color", DEFAULT_COLOR);

        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);

        Mockito.when(repository.save(any(Faculty.class)))
                .thenReturn(facultyFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI)
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(DEFAULT_ID),
                        jsonPath("$.name").value(DEFAULT_NAME),
                        jsonPath("$.color").value(DEFAULT_COLOR)
                );
    }

    @Test
    void filterByColor_shouldReturnCollectionOfFacultyFilteredByColor() throws Exception {
        List<Faculty> collectionFromRepository = new ArrayList<>();
        collectionFromRepository.add(new Faculty(DEFAULT_ID, DEFAULT_NAME + "1", DEFAULT_COLOR));
        collectionFromRepository.add(new Faculty(DEFAULT_ID + 1, DEFAULT_NAME + "2", DEFAULT_COLOR));
        collectionFromRepository.add(new Faculty(DEFAULT_ID + 2, DEFAULT_NAME + "3", "NOT-def-color"));

        Mockito.when(repository.findAll())
                .thenReturn(collectionFromRepository);

        //у репозитория запрашивается полный список, после чего фильтрация на цвет проходит уже в сервисе, через стримы

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI + "/filter?color=" + DEFAULT_COLOR))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(Matchers.containsInAnyOrder(DEFAULT_ID, DEFAULT_ID + 1)),
                        jsonPath("$.*.name").value(Matchers.containsInAnyOrder(DEFAULT_NAME + "1", DEFAULT_NAME + "2")),
                        jsonPath("$.*.color").value(Matchers.containsInAnyOrder(DEFAULT_COLOR, DEFAULT_COLOR))
                );
    }

    @Test
    void findStudentsOfFaculty_shouldReturnCollectionOfStudentsOfFacultyFoundById() throws Exception {
        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);
        Collection<Student> students = new ArrayList<>();
        students.add(new Student(1, "1", 11));
        students.add(new Student(2, "2", 12));
        students.add(new Student(3, "3", 13));
        facultyFromRepository.setStudents(students);

        Mockito.when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(facultyFromRepository));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI + "/" + DEFAULT_ID + "/students"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(Matchers.containsInAnyOrder(1, 2, 3)),
                        jsonPath("$.*.name").value(Matchers.containsInAnyOrder("1", "2", "3")),
                        jsonPath("$.*.age").value(Matchers.containsInAnyOrder(11, 12, 13))
                );
    }

    @Test
    void get_shouldReturnFacultyById() throws Exception {
        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);

        Mockito.when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(facultyFromRepository));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI + "/" + DEFAULT_ID))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(DEFAULT_ID)
                );
    }

    @Test
    void findFacultyByName_shouldReturnFacultyByName() throws Exception {
        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);

        Mockito.when(repository.findFirstByNameIgnoreCase(any(String.class)))
                .thenReturn(facultyFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI + "?name=" + DEFAULT_NAME))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value(DEFAULT_NAME)
                );
    }

    @Test
    void findFacultyByColor_shouldReturnFacultyByColor() throws Exception {
        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);

        Mockito.when(repository.findFirstByColorIgnoreCase(any(String.class)))
                .thenReturn(facultyFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI + "?color=" + DEFAULT_COLOR))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.color").value(DEFAULT_COLOR)
                );
    }

    @Test
    void all_shouldReturnCollectionOfAllFaculty() throws Exception {
        Collection<Faculty> collectionFromRepository = new ArrayList<>();
        collectionFromRepository.add(new Faculty(DEFAULT_ID + 2, DEFAULT_NAME + "3", DEFAULT_COLOR));
        collectionFromRepository.add(new Faculty(DEFAULT_ID, DEFAULT_NAME + "1", "some color"));
        collectionFromRepository.add(new Faculty(DEFAULT_ID + 1, DEFAULT_NAME + "2", "another color"));

        Mockito.when(repository.findAll())
                .thenReturn((List<Faculty>) collectionFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.*.id").value(Matchers.containsInAnyOrder(DEFAULT_ID, DEFAULT_ID + 1, DEFAULT_ID + 2)),
                        jsonPath("$.*.name").value(Matchers.containsInAnyOrder(DEFAULT_NAME + "1", DEFAULT_NAME + "2", DEFAULT_NAME + "3")),
                        jsonPath("$.*.color").value(Matchers.containsInAnyOrder(DEFAULT_COLOR, "some color", "another color"))
                );
    }

    @Test
    void edit_shouldReturnEditedFaculty() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", DEFAULT_ID);
        json.put("name", DEFAULT_NAME);
        json.put("color", DEFAULT_COLOR);

        Faculty facultyFromRepository = new Faculty(DEFAULT_ID, DEFAULT_NAME, DEFAULT_COLOR);

        ArgumentCaptor<Faculty> argumentCaptor = ArgumentCaptor.forClass(Faculty.class);

        Mockito.when(repository.save(any(Faculty.class)))
                .thenReturn(facultyFromRepository);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI)
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(DEFAULT_ID),
                        jsonPath("$.name").value(DEFAULT_NAME),
                        jsonPath("$.color").value(DEFAULT_COLOR)
                );

        Mockito.verify(repository).save(argumentCaptor.capture());
        Assertions.assertEquals((long) DEFAULT_ID, argumentCaptor.getValue().getId());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URI + "/" + DEFAULT_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }
}