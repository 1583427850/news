package xyz.linyh.file.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 需要在配置文件中配置相关参数，才可以使用
 */
@Data
@ConfigurationProperties(prefix = "minio")  // 文件上传 配置前缀file.oss
public class MinIOConfigProperties implements Serializable {

//    minio账号
    private String accessKey;
//    minio密码
    private String secretKey;
//    保存的文件夹目录
    private String bucket;
//    结尾
    private String endpoint;
//    读取的路径
    private String readPath;
}

