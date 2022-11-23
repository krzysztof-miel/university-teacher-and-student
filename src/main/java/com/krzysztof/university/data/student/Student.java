package com.krzysztof.university.data.student;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krzysztof.university.data.teacher.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Size(min = 2, message = "Name should have 2 or more characters")
    private String name;
    @NotBlank
    @Size(min = 2, message = "Surname should have 2 or more characters")
    private String surname;
    @Min(value = 19, message = "Age must be greater than 18")
    private int age;
    @NotBlank
    @Email(message = "Must be a well-formed email address")
    private String email;
    @NotBlank
    private String degreeCourse;

    @JsonIgnore
    @ManyToMany(
            cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "students_teachers",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    public Student(String name, String surname, int age, String email, String degreeCourse) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.degreeCourse = degreeCourse;
    }

    public void updateFrom(Student source) {
        name = source.getName();
        surname = source.getSurname();
        age = source.getAge();
        email = source.getEmail();
        degreeCourse = source.getDegreeCourse();
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getStudents().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getStudents().remove(this);
    }
}
