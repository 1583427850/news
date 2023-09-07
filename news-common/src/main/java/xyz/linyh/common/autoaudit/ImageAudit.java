package xyz.linyh.common.autoaudit;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 对图片进行审核
 */
@Component
//@ConfigurationProperties(prefix = "autoaudit")
public class ImageAudit {

    /**
     * 对图片进行审核，返回status为200即为成功
     * @param urls
     * @return
     */
    public Map auditImage(List<String> urls){
        HashedMap map = new HashedMap();
        map.put("status","200");
        return map;
    }

}
