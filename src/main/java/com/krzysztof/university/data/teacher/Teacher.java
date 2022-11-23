package com.krzysztof.university.data.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krzysztof.university.data.student.Student;
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
@Table(name = "teachers")
public class Teacher {
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
    private String subject;

    @JsonIgnore
    @ManyToMany(mappedBy = "teachers",
            cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Student> students = new HashSet<>();

    public Teacher(String name, String surname, int age, String email, String subject) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.subject = subject;
    }

    public void updateFrom(Teacher source) {
        name = source.getName();
        surname = source.getSurname();
        age = source.getAge();
        email = source.getEmail();
        subject = source.getSubject();
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getTeachers().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getTeachers().remove(this);
    }

    @PreRemove
    private void removeStudentFromTeachers() {
        for (Student s : students) {
            s.getTeachers().remove(this);
        }
    }
}
