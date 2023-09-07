import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import xyz.linyh.mongotest.MongoDBTestApplication;
import xyz.linyh.mongotest.entity.ApAssociateWorld;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = MongoDBTestApplication.class)
public class InitAssociateWordTest {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 批量将联想词导入
     * @throws Exception
     */
    @Test
    void initAssociateWordTest() throws Exception {
        FileReader fileReader = new FileReader(new File("C:/Users/lin/Desktop/数据.txt"));

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String s = null;
        while((s = bufferedReader.readLine())!=null){
            System.out.println(s);
            ApAssociateWorld apAssociateWorld = new ApAssociateWorld();
            apAssociateWorld.setId(String.join("",UUID.randomUUID().toString().split(",")));
            apAssociateWorld.setAssociateWords(s);
            apAssociateWorld.setCreatedTime(new Date());
            mongoTemplate.save(apAssociateWorld);
        }




//        System.out.println("1");
    }

    @Test
    public void getData(){
        List<ApAssociateWorld> apAssociateWorlds = mongoTemplate.find(Query.query(Criteria.where("associateWord").is("我的世界java版")), ApAssociateWorld.class);
        System.out.println(apAssociateWorlds);
    }
}
