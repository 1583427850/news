package xyz.linyh.webmedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.common.constants.WmKafkaTopic;
import xyz.linyh.common.constants.WmMediaConstans;
import xyz.linyh.common.exception.CustomException;
import xyz.linyh.feign.IScheduledClient;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.enums.TaskTypeEnum;
import xyz.linyh.model.schedule.eitity.Taskinfo;
import xyz.linyh.model.webmedia.dto.WmNewsDto;
import xyz.linyh.model.webmedia.dto.WmNewsListDto;
import xyz.linyh.model.webmedia.dto.WmUpOrDownDto;
import xyz.linyh.model.webmedia.entity.WmMaterial;
import xyz.linyh.model.webmedia.entity.WmNews;
import xyz.linyh.model.webmedia.entity.WmNewsMaterial;
import xyz.linyh.utils.common.ProtostuffUtil;
import xyz.linyh.webmedia.interceptor.SaveIdInterceptor;
import xyz.linyh.webmedia.mapper.WmMaterialMapper;
import xyz.linyh.webmedia.mapper.WmNewsMapper;
import xyz.linyh.webmedia.mapper.WmNewsMaterialMapper;
import xyz.linyh.webmedia.service.NewsAutoAuditService;
import xyz.linyh.webmedia.service.WmMaterialService;
import xyz.linyh.webmedia.service.WmNewsMaterialService;
import xyz.linyh.webmedia.service.WmNewsService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;



    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private NewsAutoAuditService newsAutoAuditService;

    @Autowired
    private IScheduledClient scheduledClient;

    /**
     * 条件查询所有文章
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult listAllNews(WmNewsListDto dto) {
//        1. 参数校验
        dto.checkParam();
//        2. 查询文章
        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        if(dto.getBeginPubDate()!=null && dto.getEndPubDate()!=null){
            wrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());
        }
        if(dto.getStatus()!=null){
            wrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        if(StringUtils.isNotBlank(dto.getKeyword())){
            wrapper.like(WmNews::getTitle,dto.getKeyword());
        }
        if(dto.getChannelId()!=null){
            wrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        wrapper.orderByDesc(WmNews::getCreatedTime);
        Page<WmNews> result = wmNewsMapper.selectPage(page, wrapper);
//        3. 返回结果
        PageResponseResult responseResult = new PageResponseResult((int) result.getCurrent(), (int) result.getSize(), (int) result.getTotal());
        responseResult.setData(result.getRecords());
        return responseResult;
    }

    /**
     * 新增或存草稿文章
     *
     * 如果封面传的是自动（-1），那么就封面选取规则：
     *          如果文章里面没有图片，那么就是无图 0
     *          如果文章里面图片大于等于1，小于3，那么就是单图0
     *          如果文章里面图片大于等于3，那么是多图
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submitOrSubmitDraftNew(WmNewsDto dto) {
//        1. 参数校验
        if(StringUtils.isBlank(dto.getTitle())|| StringUtils.isBlank(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if(dto.getChannelId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if(dto.getStatus()!=1 && dto.getStatus()!=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
//        发布时间不能早于今天 todo


//        2. 获取文章的所有图片信息，而dto里面的images只有封面图片
        List<String> newImages = getNewImages(dto);

//        3. 判断传过来的是否携带文章id,如果携带文章id，那么需要把原先关联的素材删除
        if(dto.getId()!=null){
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,dto.getId()));
        }

//        4. 获取文章封面，并添加文章
        Long newId = getImageAndSaveNew(dto,newImages);

//        4. 然后新增关联的素材
        saveNewMaterial(newImages,newId);

//        将添加文章添加到任务中
        addNewToTask(newId);
////        开始自动审核（异步调用）
//        newsAutoAuditService.auditText(newId);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 从redis中获取待消费的任务
     *
     * @return
     */
    @Override
    @Scheduled(fixedRate = 1000) //每一秒执行一次
    public void getTasksToCache() {
        log.info("查询redis获取将要执行的wmnews。。。。。。");
        ResponseResult<Taskinfo> response = scheduledClient.pollTask(TaskTypeEnum.NEWS_SCAN_TIME.getPriority(), TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        if(response.getData()!=null && "200".equals(response.getCode().toString())){

//           获取文章内容，然后审核审核对应文章
            Taskinfo taskinfo = JSON.parseObject(JSON.toJSONString(response.getData()), Taskinfo.class);
            WmNews wmNews = ProtostuffUtil.deserialize(taskinfo.getParameters(), WmNews.class);
            log.info("获取到即将要执行的wmnews，将开始审核。。。。。。");
            boolean isAudit = newsAutoAuditService.auditText(wmNews.getId());
//            if(isAudit){
////                更新任务状态
//
//            }else{
//
//            }

        }
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    /**
     * 对媒体端的文章进行上下架操作
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult upOrDownNews(WmUpOrDownDto dto) {
//        1. 参数校验
        if(dto.getId()==null || dto.getEnable()==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getEnable()<0 || dto.getEnable()>1){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

//        2. 判断文章是否存在
        WmNews wmNews = wmNewsMapper.selectById(dto.getId());
        if(wmNews==null){
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

//        3. 判断文章是提交和审核通过
        if(wmNews.getStatus()!=WmMediaConstans.NEWS_STATUS_OK){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//        4. 修改文章状态
        boolean isUpdate = update(Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId, dto.getId()).set(WmNews::getEnable, dto.getEnable()));
        if(!isUpdate){
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

//        5. 发送到kafka
        Long articleId = wmNews.getArticleId();
        HashMap<String, String> map = new HashMap<>();
        map.put("articleId",wmNews.getArticleId().toString());
        map.put("enable",dto.getEnable().toString());
        kafkaTemplate.send(WmKafkaTopic.WM_KAFKA_DOWN_OR_UP_TOPIC,JSON.toJSONString(map));
        return ResponseResult.okResult(isUpdate);
    }

    /**
     * 将文章添加到延迟任务中
     * @param newId
     */
    private void addNewToTask(Long newId) {
        WmNews wmNews = wmNewsMapper.selectById(newId);
        if(wmNews==null)return;
        Taskinfo taskinfo = new Taskinfo();
        taskinfo.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        taskinfo.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        taskinfo.setExecuteTime(wmNews.getPublishTime());
        taskinfo.setParameters(ProtostuffUtil.serialize(wmNews));
        scheduledClient.addTask(taskinfo);
    }

    /**
     * 获取文章封面，并提交文章，返回文章id
     * @param dto
     * @param newImages 保存文章所有的图片
     *        dto里面的images只是封面图片
     * @param
     * @return
     * todo
     */
    private Long getImageAndSaveNew(WmNewsDto dto, List<String> newImages) {
        WmNews wmNews = new WmNews();
        //        将相同参数和属性名的值赋给wmNews
        BeanUtils.copyProperties(dto,wmNews);
        wmNews.setUserId(SaveIdInterceptor.WM_USER_THREAD.get());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setImages(String.join(",",dto.getImages()));

//        封面的图片
        List<String> image=null;
        if (dto.getType()==WmMediaConstans.NEWS_IMAGE_CONFIG_AUTO) {
//            如果是自动选取封面，那么需要根据文章里面有多少张图片，判断选取几张作为封面，规则为，如果文章没图片，那么就是0张，如果文章图片大于等于1小于3，那么就是选一张，如果文章图片大于等于3，那么选取3张作为封面
            if(newImages.size()==0){
                wmNews.setType(WmMediaConstans.NEWS_IMAGE_CONFIG_NO);
            }
            if(newImages.size()>=1 && newImages.size()<3){
                wmNews.setType(WmMediaConstans.NEWS_IMAGE_CONFIG_ONE);
                image = newImages.stream().limit(1).collect(Collectors.toList());
                wmNews.setImages(image.get(0));
            }
            if(newImages.size()>=3){
                wmNews.setType(WmMediaConstans.NEWS_IMAGE_CONFIG_MORE);
                image = newImages.stream().limit(3).collect(Collectors.toList());
                String join = String.join(",", image);
                wmNews.setImages(join);
            }

        }

        saveOrUpdate(wmNews);
        return wmNews.getId();
    }



    /**
     * 将文章里面的所有图片添加到素材文章关联表
     * @param newImages
     * todo
     */
    private void saveNewMaterial(List<String> newImages,Long newId) {
//        判断素材是否还是有效的
        for(String imageUrl:newImages){
            WmMaterial wmMaterial = wmMaterialMapper.selectOne(Wrappers.<WmMaterial>lambdaQuery().eq(WmMaterial::getUrl, imageUrl));
            if (wmMaterial==null){
//                不能直接返回结果，需要抛出异常，因为前面有很多数据库操作已经执行了，抛出异常会中断事务的执行
                throw new CustomException(AppHttpCodeEnum.PARAM_IMAGE_NO);
            }

            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setNewsId(newId);
            wmNewsMaterial.setType(0);
//            还需要获取素材id
            wmNewsMaterial.setMaterialId(wmMaterial.getId());
            wmNewsMaterialMapper.insert(wmNewsMaterial);

        }

    }

    /**
     * 获取文章的所有图片
     * @param dto
     * @return
     */
    private List<String> getNewImages(WmNewsDto dto) {
        List<String> images = new ArrayList<>();
//        根据json字符串里面的对象，每个对象转换为一个map
//        获取文章里面的图片
        for (Map map : JSON.parseArray(dto.getContent(), Map.class)) {
            if("image".equals(map.get("type"))){
                images.add((String) map.get("value"));
            }
        }
        if(dto.getImages()==null && dto.getImages().size() == 0){
            return images;
        }
//        将文章封面也添加进去
        dto.getImages().stream().forEach(image->{
            images.add(image);
        });
        return images;

    }
}
