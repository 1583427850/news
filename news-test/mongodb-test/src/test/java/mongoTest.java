import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.linyh.mongotest.MongoDBTestApplication;

@SpringBootTest(classes = MongoDBTestApplication.class)
public class mongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void opTest(){
//
////        保存
//        mogoEntity mogoEntity = new mogoEntity();
//        mogoEntity.setId("2");
//        mogoEntity.setName("test2");
//        mongoTemplate.save(mogoEntity);
//
////        根据id查找
//        mogoEntity byId = mongoTemplate.findById("1", mogoEntity.class);
//        System.out.println(byId);
//
////        条件查询
//        Query query = new Query(Criteria.where("name").is("test1"))
//                .with(Sort.by(Sort.Direction.DESC, "根据哪个字段降序查找"));
//        List<mogoEntity> mogoEntities = mongoTemplate.find(query, mogoEntity.class);
//        System.out.println(mogoEntities);
//
////        删除
//        DeleteResult remove = mongoTemplate.remove(Query.query(Criteria.where("name").is("test1")), mogoEntity.class);
//        System.out.println(remove);
    }


}
