package xyz.linyh.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.common.constants.TaskInfoConstants;
import xyz.linyh.model.schedule.eitity.Taskinfo;
import xyz.linyh.model.webmedia.entity.WmNews;
import xyz.linyh.schedule.ScheduleApplication;
import xyz.linyh.schedule.service.TaskinfoService;
import xyz.linyh.utils.common.ProtostuffUtil;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= ScheduleApplication.class)
class TaskinfoServiceImplTest {

    @Autowired
    private TaskinfoService taskinfoService;

    @Test
    void addTask() {
//        Taskinfo taskinfo = new Taskinfo();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE,6);
//        taskinfo.setPriority(1);
//        taskinfo.setTaskType(1);
//        taskinfo.setExecuteTime(calendar.getTime());
//        taskinfo.setParameters(" world!".getBytes());
//        System.out.println(taskinfoService.addTask(taskinfo));

    }

    @Test
    void cancelTask() {

        System.out.println(taskinfoService.cancelTask(1682661491353767937L));
    }

    @Test
    void poll() {
        Taskinfo taskinfo = JSON.parseObject("{\"executeTime\":1690130307000,\"parameters\":\"CKMxEzDOCBQaHOWwseaYr+S4gOS4quaZrumAmueahOagh+mimDcioAFbeyJ0eXBlIjoidGV4dCIsInZhbHVlIjoi5bCx5piv5LiA5Liq5pmu6YCa55qE5qCH6aKY5bCx5piv5LiA5Liq5pmu6YCa55qE5qCH6aKY5bCx5piv5LiA5Liq5pmu6YCa55qE5qCH6aKYIn0seyJ0eXBlIjoidGV4dCIsInZhbHVlIjoi6K+35Zyo6L+Z6YeM6L6T5YWl5q2j5paHIn1dKAAwBDoG5rWL6K+VQUDrnYOJAQAASUDrnYOJAQAAUAFZuNedg4kBAAByAHgB\",\"priority\":1,\"taskId\":1683154804229984257,\"taskType\":1001}", Taskinfo.class);
//        System.out.println(taskinfo);
        System.out.println(ProtostuffUtil.deserialize(taskinfo.getParameters(), WmNews.class));
    }
}