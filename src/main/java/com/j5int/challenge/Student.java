package com.j5int.challenge;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class Student {
    private String name;
    private String surname;
    private double score;

    public Student(List<String> details) {
        name = details.get(0);
        surname = details.get(1);
        score = Double.parseDouble(details.get(2));
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
