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
            FileInputStream fileInputStream = new FileInputStream("D:\\freemarker\\list.html");
            MinioClient build = MinioClient.builder().credentials("minio", "minioabc").endpoint("http://47.120.8.78:9001").build();
            PutObjectArgs news = PutObjectArgs.builder()
                    .object("list.html")
                    .contentType("text/html")
                    .bucket("news")
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = build.putObject(news);
            System.out.println(objectWriteResponse);
            System.out.println("http://47.120.8.78:9000/news/list.html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("C:/Users/lin/Desktop/index.css");

        String s = fileStorageService.uploadHtmlFile("/plugins/css", "index.css", fileInputStream);
        System.out.println(s);
    }
}
