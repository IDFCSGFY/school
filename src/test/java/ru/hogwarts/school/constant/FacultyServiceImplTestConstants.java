package ru.hogwarts.school.constant;

import ru.hogwarts.school.model.Faculty;

public class FacultyServiceImplTestConstants {
    public static final Faculty NULL_FACULTY = null;
    public static final Faculty CORRECT_FIRST_FACULTY = new Faculty(1, "First", "red");
    public static final Faculty CORRECT_SECOND_FACULTY = new Faculty(2, "Second", "red");
    public static final Faculty ID1_EX1_FACULTY = new Faculty(1, "old one", "red");
    public static final Faculty ID1_EX2_FACULTY = new Faculty(1, "new one", "blue");
    public static final Faculty EX1_COLOR_RED_FACULTY = new Faculty(1, "imRED", "red");
    public static final Faculty EX2_COLOR_RED_FACULTY = new Faculty(2, "imRED too", "red");
    public static final Faculty EX1_COLOR_BLUE_FACULTY = new Faculty(3, "im NOT RED", "blue");
}
