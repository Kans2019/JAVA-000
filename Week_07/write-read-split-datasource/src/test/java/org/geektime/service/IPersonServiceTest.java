package org.geektime.service;

import org.geektime.pojo.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IPersonServiceTest {
    @Autowired
    private IPersonService personService;

    @Test
    public void testFindById() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + ",  " + personService.getById(10000L));
        }
    }

    @Test
    public void testListAll() {
        System.out.println(personService.listAll());
    }

    @Test
    public void testSave() {
        Person person = new Person();
        person.setName("凯亚");
        person.setId(4L);
        personService.save(person);
    }
}