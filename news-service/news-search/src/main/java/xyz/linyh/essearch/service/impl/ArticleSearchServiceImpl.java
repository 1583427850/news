package xyz.linyh.essearch.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.essearch.service.ApUserSearchService;
import xyz.linyh.essearch.service.ArticleSearchService;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ArticleSearchServiceImpl implements ArticleSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Autowired
    private ApUserSearchService apUserSearchService;


    /**
     * 查询es下的文章
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto,Long userId) {
//        1. 参数校验
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if(dto.getPageSize()>20 || dto.getPageSize()<=0){
            dto.setPageSize(20);
        }
        ArrayList<Map> maps = null;

//        将搜索关键词保存到搜索记录数据库中
        apUserSearchService.insertHistory(userId,dto.getSearchWords());

        try {
//        2. 设置查询的条件
            SearchRequest searchRequest = new SearchRequest("app_article_info");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        因为有多个查询条件，所以需要用到bool查询
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

//        设置分词查询和去哪里查询和多个搜索词间用or匹配（只要有一个就可以查询到）
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords()).field("title").field("content").defaultOperator(Operator.OR);
            queryBuilder.must(queryStringQueryBuilder);

//        设置时间查询
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
            queryBuilder.must(rangeQueryBuilder);

//        设置查询的分页
//        从头开始查询
            searchSourceBuilder.from(0);
//        查询的大小
            searchSourceBuilder.size(dto.getPageSize());

//        3. 查询
            searchSourceBuilder.query(queryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

//        将查询的结果保存到list中然后返回
            maps = new ArrayList<>();

            SearchHit[] hits = searchResponse.getHits().getHits();
            for(SearchHit hit: hits){
                String sourceJson = hit.getSourceAsString();
                Map map = JSON.parseObject(sourceJson, Map.class);

    //            设置标题
                map.put("h_title",map.get("title"));

                maps.add(map);
            }
        } catch (Exception e) {
            log.error("es查询文章错误");
            e.printStackTrace();
        }

        return ResponseResult.okResult(maps);
    }
}
