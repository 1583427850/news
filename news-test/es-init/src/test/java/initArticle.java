import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.es.EsInitApplication;
import xyz.linyh.es.mapper.ApArticleMapper;
import xyz.linyh.model.article.vo.SearchArticleVo;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = EsInitApplication.class)
public class initArticle {



    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

//    将aparticle里面的内容上传到es中，可以更好更快查找到
    @Test
    public void init() throws IOException {
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();

//        选择去哪里找到对应的索引映射
        BulkRequest bulkRequest = new BulkRequest("app_article_info");
//        DeleteRequest deleteRequest = new DeleteRequest("app_info_article");


//        遍历所有内容，然后上床到es的模板中
        for(SearchArticleVo searchArticleVo: searchArticleVos){

//            添加到es模板中，需要指定id
            IndexRequest indexRequest = new IndexRequest().id(searchArticleVo.getId().toString())
                    .source(JSON.toJSONString(searchArticleVo), XContentType.JSON);

//            批量添加
            bulkRequest.add(indexRequest);

        }

        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
}
