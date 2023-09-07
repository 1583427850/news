package xyz.linyh.freemarker.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.freemarker.test.entity.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest(classes = FreemarkerTestApplication.class)

public class myTest {

    @Autowired
    Configuration configuration;

    @Test
    public void test1() throws IOException, TemplateException {
//        获取template模板
        Template template = configuration.getTemplate("basic01.ftl");

        ArrayList<Student> lists = new ArrayList<>();
        Student student = new Student();
        student.setName("小张");
        student.setAge("18");
        Student student2 = new Student();
        student2.setName("小红");
        student2.setAge("19");
        Student student3 = new Student();
        student3.setName("小林");
        student3.setAge("18");
        lists.add(student);
        lists.add(student2);
        lists.add(student3);
        HashMap<String, Object> map = new HashMap<>();
        map.put("stus",lists);
        map.put("stu",student);
        map.put("publishTime",new Date());
        //        将数据保存到map里面就可以了，然后就可以对模板里面注入值，然后保存文件到对应的磁盘
        template.process(map,new FileWriter("D:/freemarker/list.html"));
    }
}
