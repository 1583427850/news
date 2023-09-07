package xyz.linyh.wmMedia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.common.autoaudit.ImageAudit;
import xyz.linyh.common.autoaudit.TextAudit;
import xyz.linyh.webmedia.WebMediaApplication;

import java.util.ArrayList;

@SpringBootTest(classes = WebMediaApplication.class)
public class WmMediaTest {

    @Autowired
    private ImageAudit imageAudit;

    @Autowired
    private TextAudit textAudit;

    @Test
    public void test1(){
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        System.out.println(imageAudit.auditImage(list));
        System.out.println(textAudit.auditTest("1234"));
    }
}
