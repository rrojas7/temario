package com.chapterntt.temario.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Builder
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int age;

    @Column(name = "register_date")
    private LocalDate registerDate;

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
}
