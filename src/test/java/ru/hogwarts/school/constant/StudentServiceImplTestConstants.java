package ru.hogwarts.school.constant;

import ru.hogwarts.school.model.Student;

public class StudentServiceImplTestConstants {
    public static final Student NULL_STUDENT = null;
    public static final Student CORRECT_FIRST_STUDENT = new Student(1, "First", 16);
    public static final Student CORRECT_SECOND_STUDENT = new Student(2, "Second", 16);
    public static final Student ID1_EX1_STUDENT = new Student(1, "old one", 16);
    public static final Student ID1_EX2_STUDENT = new Student(1, "new one", 17);
    public static final Student EX1_AGE_16_STUDENT = new Student(1, "im16", 16);
    public static final Student EX2_AGE_16_STUDENT = new Student(2, "im16 too", 16);
    public static final Student EX1_AGE_17_STUDENT = new Student(3, "im NOT 16", 17);
}
