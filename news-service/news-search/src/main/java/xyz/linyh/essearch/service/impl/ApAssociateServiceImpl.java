package xyz.linyh.essearch.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.essearch.dto.ApAssociateWorld;
import xyz.linyh.essearch.service.ApAssociateService;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional
public class ApAssociateServiceImpl implements ApAssociateService {


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询搜索的联想词·
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) {
//        1. 参数检查
        if(dto==null || dto.getSearchWords()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        2. 去mongoDB里面查询(利用正则表达式）
        String regex = ".*?" + Pattern.quote(dto.getSearchWords()) + ".*";
        Query query = Query.query(Criteria.where("associateWords").regex(regex));
        List<ApAssociateWorld> apAssociateWorlds = mongoTemplate.find(query, ApAssociateWorld.class);
//        3. 返回结果
        return ResponseResult.okResult(apAssociateWorlds);
    }
}
