package xyz.linyh.webmedia.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.webmedia.WebMediaApplication;
import xyz.linyh.webmedia.service.NewsAutoAuditService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebMediaApplication.class)
class NewsAutoAuditServiceImplTest {

    @Autowired
    private NewsAutoAuditService newsAutoAuditService;

    @Test
    void auditText() {
        newsAutoAuditService.auditText(6247L);
    }
}