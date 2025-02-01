package ru.hogwarts.school.controller;

import org.assertj.core.api.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private StudentController controller;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/student";
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    public void addNewStudent() {
        Student student = new Student();
        student.setName("TestName");
        student.setAge(10);

        Assertions.assertThat(this.template.postForObject(baseUrl, student, Student.class))
                .isNotNull();
    }

    @Test
    public void filterBySpecificAge() {
        ResponseEntity<Collection<Student>> response =
                template.exchange(
                        baseUrl + "/filter?age=16",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Collection<Student>>() {
                        }
                );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        ArrayList<Student> actual = new ArrayList<>(response.getBody());
        Assertions.assertThat(actual.size()).isEqualTo(2);

        response = this.template.exchange(
                baseUrl + "/filter?age=16&ageMax=17",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        actual = new ArrayList<>(response.getBody());
        Assertions.assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    public void getFacultyOfSpecificStudent() {
        ResponseEntity<Faculty> response =
                template.exchange(
                        baseUrl + "/1/faculty",
                        HttpMethod.GET,
                        null,
                        Faculty.class
                );
        Faculty actual = response.getBody();
        Assertions.assertThat(actual.getId()).isEqualTo(1);
        Assertions.assertThat(actual.getName()).isEqualTo("Gryf");
        Assertions.assertThat(actual.getColor()).isEqualTo("red");
    }

    @Test
    public void getStudentById() {
        ResponseEntity<Student> response =
                template.exchange(
                        baseUrl + "/1",
                        HttpMethod.GET,
                        null,
                        Student.class
                );
        Student actual = response.getBody();
        Assertions.assertThat(actual.getId()).isEqualTo(1);
        Assertions.assertThat(actual.getName()).isEqualTo("Larry");
        Assertions.assertThat(actual.getAge()).isEqualTo(14);
        Assertions.assertThat(actual.getFaculty().getId()).isEqualTo(1);
    }

    @Test
    public void getAll() {
        ResponseEntity<Collection<Student>> response =
                template.exchange(
                        baseUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Collection<Student>>() {
                        }
                );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Collection<Student> actual = response.getBody();
        Assertions.assertThat(actual.size()).isEqualTo(10);
    }

    @Test
    public void editStudentByConsumingStudentWithSameId() {

        Student student = new Student();
        student.setName("TestName");
        student.setAge(1);
        Student created = this.template.postForObject(baseUrl, student, Student.class);
        Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created.getId()).isNotEqualTo(0);

        Student expected = new Student();
        expected.setId(created.getId());
        expected.setName("Edited");
        expected.setAge(100);
        HttpEntity<Student> entity = new HttpEntity<>(expected);

        ResponseEntity<Student> response = this.template.exchange(
                baseUrl,
                HttpMethod.PUT,
                entity,
                Student.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Student actual = response.getBody();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deleteStudentById() {
        Student student = new Student();
        student.setName("TestName");
        student.setAge(1);
        Student created = this.template.postForObject(baseUrl, student, Student.class);
        Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created.getId()).isNotEqualTo(0);
        System.out.println(created.getId());

        ResponseEntity<Student> response = this.template.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Student.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

}