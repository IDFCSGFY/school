package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private FacultyController controller;

    private String url;
    private static Faculty createEditDeleteTarget;
    private long newId;

    @BeforeAll
    static void beforeAll() {
        createEditDeleteTarget = new Faculty();
        createEditDeleteTarget.setName("Test Faculty");
        createEditDeleteTarget.setColor("transparent");
    }

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/faculty";
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    public void addNewFaculty() {
        HttpEntity<Faculty> entity = new HttpEntity<>(createEditDeleteTarget);

        ResponseEntity<Faculty> response = this.template.exchange(
                url,
                HttpMethod.POST,
                entity,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Faculty actual = response.getBody();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isNotEqualTo(0);
        Assertions.assertThat(actual.getName()).isEqualTo(createEditDeleteTarget.getName());
        Assertions.assertThat(actual.getColor()).isEqualTo(createEditDeleteTarget.getColor());

        createEditDeleteTarget.setId(actual.getId());
    }

    @Test
    public void editFaculty() {
        String expectedName = "Edited Name";
        String expectedColor = "Edited Color";
        createEditDeleteTarget.setName(expectedName);
        createEditDeleteTarget.setColor(expectedColor);
        HttpEntity<Faculty> entity = new HttpEntity<>(createEditDeleteTarget);
        ResponseEntity<Faculty> response = this.template.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Faculty actual = response.getBody();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actual.getColor()).isEqualTo(expectedColor);
    }

    @Test
    public void deleteFacultyById() {
        ResponseEntity<Faculty> response = this.template.exchange(
                url + "/" + createEditDeleteTarget.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    public void filterByColor() {
        ResponseEntity<Collection<Faculty>> response = this.template.exchange(
                url + "/filter?color=red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        ArrayList<Faculty> actual = new ArrayList<>(response.getBody());
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isNotEqualTo(0);
        Assertions.assertThat(actual.get(0)).isNotNull();
        Assertions.assertThat(actual.get(0).getColor()).isEqualTo("red");
    }

    @Test
    public void getStudentsByFacultyId() {
        ResponseEntity<Collection<Student>> response = this.template.exchange(
                url + "/1/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        ArrayList<Student> actual = new ArrayList<>(response.getBody());
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isNotEqualTo(0);
        Assertions.assertThat(actual.get(1).getFaculty().getId()).isEqualTo(1);
    }

    @Test
    public void getFacultyById() {
        ResponseEntity<Faculty> response = this.template.exchange(
                url + "/1",
                HttpMethod.GET,
                null,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Faculty actual = response.getBody();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isEqualTo(1);
    }

    @Test
    public void getFacultyByName() {
        String targetName = "Gryf";
        ResponseEntity<Faculty> response = this.template.exchange(
                url + "?name=" + targetName,
                HttpMethod.GET,
                null,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Faculty actual = response.getBody();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(targetName);
    }

    @Test
    public void getFacultyByColor() {
        String targetColor = "blue";
        ResponseEntity<Faculty> response = this.template.exchange(
                url + "?color=" + targetColor,
                HttpMethod.GET,
                null,
                Faculty.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Faculty actual = response.getBody();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getColor()).isEqualTo(targetColor);
    }

    @Test
    public void getAll() {
        ResponseEntity<Collection<Faculty>> response = this.template.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        ArrayList<Faculty> actual = new ArrayList<>(response.getBody());
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isNotEqualTo(0);
    }

}