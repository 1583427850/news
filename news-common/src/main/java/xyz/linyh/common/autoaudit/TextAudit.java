package xyz.linyh.common.autoaudit;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TextAudit {

    /**
     * 对文本进行审核，返回status为200即为成功
     *              返回o即为失败
     * @param content
     * @return
     */
    public Map auditTest(String content){
        HashedMap map = new HashedMap();
        if(content.contains("违规")){
            map.put("status","0");
            map.put("reason","违规");
        }else {
            map.put("status", "200");
        }
        return map;
    }
}
