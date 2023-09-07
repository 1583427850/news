package xyz.linyh.article.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.linyh.article.service.ApArticleConfigService;
import xyz.linyh.common.constants.WmKafkaTopic;
import xyz.linyh.model.article.entity.ApArticleConfig;

import java.util.Map;

@Component
@Slf4j
public class MyKafkaListener {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    /**
     * 接收wmnew发送的文章上下架消息
     * @param message
     */
    @KafkaListener(topics = {WmKafkaTopic.WM_KAFKA_DOWN_OR_UP_TOPIC})
    public void getWmNewsUpOrDown(String message){
        if(StringUtils.isNotBlank(message)){
            Map map = JSON.parseObject(message, Map.class);
            String articleId = map.get("articleId").toString();
            String enable = map.get("enable").toString();

            if(articleId==null){
                log.error("文章上下架，文章id不能为空");
                return;
            }

//            最初是不下架，如果enable为0，那么就是下架
            Short isDown = 0;
            if("0".equals(enable)){
                isDown=1;
            }

            boolean isUpdate = apArticleConfigService.update(Wrappers.<ApArticleConfig>lambdaUpdate().eq(ApArticleConfig::getArticleId, articleId).set(ApArticleConfig::getIsDown, isDown));

            if(isUpdate) {
                log.info("文章上下架成功。。。。");
            }

        }
    }
}
