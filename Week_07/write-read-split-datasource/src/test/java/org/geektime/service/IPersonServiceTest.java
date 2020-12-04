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
        Assert.notNull(personService.getById(10000L), "数据库访问错误");
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