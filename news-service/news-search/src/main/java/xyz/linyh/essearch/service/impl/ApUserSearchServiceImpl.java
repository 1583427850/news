package xyz.linyh.essearch.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.essearch.dto.ApUserSearchDto;
import xyz.linyh.essearch.service.ApUserSearchService;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ApUserSearchServiceImpl implements ApUserSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 用来保存用户搜索记录
     *
     * @param userId
     * @param searchWorld
     * @return
     */
    @Async //开启异步调用
    @Override
    public void insertHistory(Long userId, String searchWorld) {
//        1. 参数校验
        if(userId ==null || searchWorld==null){
            log.error("保存搜索历史用户id和搜索词不能为空");
        }

//        2. 判断搜索词是否已经存在
        Query query = Query.query(Criteria.where("userId").is(userId).and("keyword").is(searchWorld));
        ApUserSearchDto apUserSearch = mongoTemplate.findOne(query, ApUserSearchDto.class);

//        如果已经存在那么就直接更新创建时间
        if(apUserSearch!=null){
            apUserSearch.setCreatedTime(new Date());
            mongoTemplate.save(apUserSearch);
            return;
        }

//        3. 如果没存在，那么就判断这个用户的搜索关键词是否超过10个
        Query query2 = Query.query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC, "createdTime"));
        List<? extends ApUserSearchDto> apUserSearchDtos = mongoTemplate.find(query2, ApUserSearchDto.class);

        ApUserSearchDto saveApUserSearch = new ApUserSearchDto();
        saveApUserSearch.setKeyword(searchWorld);
        saveApUserSearch.setUserId(userId);
        saveApUserSearch.setCreatedTime(new Date());
        String id = String.join("", UUID.randomUUID().toString().split("-"));
        saveApUserSearch.setId(id);

//        如果没超过10个，那么直接添加
        if(apUserSearchDtos.size()>=0 && apUserSearchDtos.size()<10){

            mongoTemplate.save(saveApUserSearch);
        }else{

//            4. 如果超过10个，那么需要将最早的那个关键词删除，然后新增新的关键词
//            获取这个人最早的搜索记录，因为上面查找是按照创建时间排序的
            ApUserSearchDto apUserSearchDto = apUserSearchDtos.get(apUserSearchDtos.size() - 1);
            deleteSearchHistory(apUserSearchDto.getId(),userId);

//            将查找到的替换为saveApUserSearch
            mongoTemplate.save(saveApUserSearch);

        }


    }

    /**
     * 查找这个用户所有的搜索记录
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult findAllHistory(Long userId) {
        Query query = Query.query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApUserSearchDto> apUserSearchDtos = mongoTemplate.find(query, ApUserSearchDto.class);
        return ResponseResult.okResult(apUserSearchDtos);
    }

    /**
     * 删除历史搜索记录
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteSearchHistory(String id,Long userId) {
//        1. 参数校验
        if(id==null || userId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        2. 删除
        mongoTemplate.remove(Query.query(Criteria.where("userId").is(userId).and("id").is(id)),ApUserSearchDto.class);

        return ResponseResult.okResult(true);
    }
}
