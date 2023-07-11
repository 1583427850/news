package xyz.linyh.freemarker.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.linyh.freemarker.test.entity.Student;

import java.util.ArrayList;

@Controller
public class FreemarkerTestController {


    @GetMapping("/basic")
    public String test1(Model model){
        Student student = new Student();
        student.setName("小张");
        student.setAge("18");
        model.addAttribute("stu",student);
//        这里已经配置过后缀名字了，所以不需要写了
        return "basic01";
    }

    @GetMapping("/list")
    public String list(Model model){
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
        model.addAttribute("stus",lists);
        model.addAttribute("stu",student);
//        这里已经配置过后缀名字了，所以不需要写了
        return "basic01";
    }
}
