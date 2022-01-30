package com.otusproject.clinic;

import com.otusproject.testframework.Assertions;
import com.otusproject.testframework.annotations.After;
import com.otusproject.testframework.annotations.Before;
import com.otusproject.testframework.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class VeterinaryClinicTest {
    private VeterinaryClinic clinic;
    private List<Person> persons;

    @Before
    public void setUp() {
        clinic = new VeterinaryClinic();
        persons = Arrays.asList(
                new Person("Alena", 29, Arrays.asList("Snegka")),
                new Person("Dasha", 28, Arrays.asList("Cat1", "Cat2")),
                new Person("Ivan", 25, null)
        );
    }

    @After
    public void tearDown() {
        System.out.println("TearDown is invoked!");
    }

    @After
    public void tearDown2() {
        System.out.println("TearDown2 is invoked!");
    }

    @Test
    public void testGetAllPets() {
        List<String> actualResult = clinic.getAllPets(persons);
        Assertions.assertEquals(Arrays.asList("Snegka", "Cat1", "Cat2"), actualResult);
    }

    @Test
    public void testGetAverageAge() {
        long actualResult = clinic.getAverageAge(persons);
        Assertions.assertEquals(27, actualResult);
    }
}
