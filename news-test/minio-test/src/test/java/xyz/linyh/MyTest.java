package xyz.linyh;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.minio.test.MinIOTestApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = MinIOTestApplication.class)
public class MyTest {

    @Autowired
    private FileStorageService fileStorageService;
    @Test
    public void test() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:/plugins/js/index.js");

            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "adminabc")
                    .endpoint("http://47.120.8.78:9000")
                    .build();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket("news")
                    .object("plugins/js/index.js")
                    .contentType("application/javascript")
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();

            ObjectWriteResponse response = minioClient.putObject(putObjectArgs);
            System.out.println("Object uploaded. Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("C:/Users/lin/Desktop/axios.min.js");

        String s = fileStorageService.uploadHtmlFile("/plugins/js", "axios.min.js", fileInputStream);
        System.out.println(s);
    }
}
