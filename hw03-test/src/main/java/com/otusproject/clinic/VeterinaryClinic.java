package com.otusproject.clinic;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VeterinaryClinic {

    public List<String> getAllPets(List<Person> persons) {
        return persons.stream()
                .map(Person::getPets)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public long getAverageAge(List<Person> persons) {
        int sum = persons.stream().mapToInt(Person::getAge).sum();
        return Math.round((double) sum / persons.size());
    }
}
