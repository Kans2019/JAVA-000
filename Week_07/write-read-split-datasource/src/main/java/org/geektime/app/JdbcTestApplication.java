package org.geektime.app;

import org.geektime.pojo.Person;
import org.geektime.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 进行读写分离测试的类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
@Component
public class JdbcTestApplication implements ApplicationRunner {
    @Autowired
    private IPersonService personService;

    @Autowired
    private Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(Arrays.toString(env.getActiveProfiles()));

        test(); // 走从库

        Person person = new Person();
        person.setId(4L);
        person.setName("温蒂");

        personService.save(person);

        test(); // 走主库

        Thread.sleep(10000); // 10s 主从同步完毕

        test(); // 走从库
    }

    private void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + ",  " + personService.getById(10000L));
        }

        System.out.println(personService.listAll());
    }
}
