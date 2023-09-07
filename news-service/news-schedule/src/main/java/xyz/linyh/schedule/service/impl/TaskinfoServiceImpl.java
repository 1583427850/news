package xyz.linyh.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.common.constants.TaskInfoConstants;
import xyz.linyh.common.exception.CustomException;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.schedule.eitity.Taskinfo;
import xyz.linyh.model.schedule.eitity.TaskinfoLogs;
import xyz.linyh.schedule.mapper.TaskinfoMapper;
import xyz.linyh.schedule.service.CacheService;
import xyz.linyh.schedule.service.TaskinfoLogsService;
import xyz.linyh.schedule.service.TaskinfoService;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
* @author lin
* @description 针对表【taskinfo】的数据库操作Service实现
* @createDate 2023-07-16 01:16:02
*/
@Service
@Transactional
@Slf4j
public class TaskinfoServiceImpl extends ServiceImpl<TaskinfoMapper, Taskinfo>
    implements TaskinfoService {

    @Autowired
    private TaskinfoService taskinfoService;

    @Autowired
    private TaskinfoLogsService taskinfoLogsService;

    @Autowired
    private CacheService cacheService;


    /**
     * 添加任务到数据库中
     *
     * @param taskinfo
     * @return
     */
    @Override
    public Long addTask(Taskinfo taskinfo) {
//        0. 参数校验
        if(taskinfo==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

//        1. 先把数据添加到mysql数据库中
        boolean isAddToDB = addTaskToDB(taskinfo);
        if(!isAddToDB)return null;
//
//        2. 然后添加到redis数据库中
        Date executeTime = taskinfo.getExecuteTime();
        //        获取未来5分钟后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);

        String key = taskinfo.getPriority()+"_"+taskinfo.getTaskType();

//        3. 在添加到redis前，要判断发布时间，如果发布时间大于当前时间，且小于5分钟，那么存到zset中
        if(executeTime.getTime()>new Date().getTime() && executeTime.getTime()<=calendar.getTimeInMillis()){
            cacheService.zAdd(TaskInfoConstants.FUTURE_PRE_KEY+key, JSON.toJSONString(taskinfo),executeTime.getTime());
        }else if(executeTime.getTime()<=new Date().getTime()){
//        4. 如果小于当前时间，那么存到list中
            cacheService.lRightPush(TaskInfoConstants.TOPIC_PRE_KEY+key,JSON.toJSONString(taskinfo));
        }




        return taskinfo.getTaskId();
    }

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    @Override
    public boolean cancelTask(Long taskId) {
        Boolean flag = true;
        try {
            if(taskId==null) flag=false;

            Taskinfo taskInfo = taskinfoService.getById(taskId);
//        1. 将数据库里面的任务日志表更新和删除任务表中的对应任务
            boolean b = taskinfoService.removeById(taskId);
            LambdaUpdateWrapper<TaskinfoLogs> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(TaskinfoLogs::getTaskId,taskId)
                            .set(TaskinfoLogs::getStatus,TaskInfoConstants.TASK_STATUS_CANCELLED);
            taskinfoLogsService.update(wrapper);

//        2. 删除redis的对应value
            if(taskInfo==null) flag=false;

            String key = taskInfo.getPriority()+"_"+taskInfo.getTaskType();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE,5);
//        判断当前任务是在list中还是在zset中
            if(taskInfo.getExecuteTime().getTime()<=System.currentTimeMillis()){
                cacheService.lRemove(TaskInfoConstants.TOPIC_PRE_KEY+key,0,JSON.toJSONString(taskInfo));
            }else if(taskInfo.getExecuteTime().getTime()>System.currentTimeMillis() && taskInfo.getExecuteTime().getTime()<=calendar.getTimeInMillis()) {
                cacheService.zRemove(TaskInfoConstants.FUTURE_PRE_KEY+key,JSON.toJSONString(taskInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

    /**
     * 根据优先级和任务类型去redis的list中拉取任务
     *
     * @param priority
     * @param type
     * @return
     */
    @Override
    public Taskinfo poll(int priority, int type) {
        String key = TaskInfoConstants.TOPIC_PRE_KEY+priority+"_"+type;
//       todo  这里一次只取一条，如果任务很多的话，需要取很久
        String values = cacheService.lLeftPop(key);
//        Taskinfo taskinfo = JSON.parseObject(values, Taskinfo.class);
        Taskinfo taskinfo = JSON.parseObject(values, Taskinfo.class);
        if(values!=null) {
            updateDBTask(taskinfo);
        }
        return taskinfo;
    }

    private void updateDBTask(Taskinfo taskinfo) {
//        修改对应的taskInfoLogs里面的状态
            LambdaUpdateWrapper<TaskinfoLogs> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(TaskinfoLogs::getTaskId,taskinfo.getTaskId())
                            .set(TaskinfoLogs::getStatus,TaskInfoConstants.TASK_STATUS_EXECUTED);
            taskinfoLogsService.update(wrapper);

//        删除taskinfo里面的数据
        taskinfoService.remove(Wrappers.<Taskinfo>lambdaQuery().eq(Taskinfo::getTaskId,taskinfo.getTaskId()));

    }

    /**
     * 将任务添加到taskInfo表和taskInfoLogs表
     * @param taskinfo
     * @return
     */
    private boolean addTaskToDB(Taskinfo taskinfo) {
        try {
//        1. 添加到taskInfo表
            taskinfoService.save(taskinfo);

//        2. 添加到taskInfoLogs表
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo,taskinfoLogs);
            taskinfoLogs.setStatus(TaskInfoConstants.TASK_STATUS_INIT);
            taskinfoLogs.setVersion(1);
            taskinfoLogsService.save(taskinfoLogs);
            return true;
        } catch (BeansException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 查询redis中的未来数据，然后刷新到redis的list中
     */
    @Override
//    @Scheduled(cron = "0 */1 * * * ?")//每隔一分钟执行一次这个任务 秒 分 时 天 月 星期
    public void refresh() {
//        对这个方法进加锁
        String token = cacheService.tryLock("FUTRUE_TASK_SYNC", 1000 * 30);

        if (StringUtils.isNotBlank(token)) {
            log.info("开始扫描redis中是否又到期的任务。。。。。。");

//        查询所有未来数据的key
            Set<String> scan = cacheService.scan(TaskInfoConstants.FUTURE_PRE_KEY + "*");
            for (String key : scan) {
//            根据key查询里面小于当前时间的value
                Set<String> values = cacheService.zRangeByScore(key, 0, System.currentTimeMillis());

//            将数据保存到redis的list集合中（利用redis的管道）
                String topicKey = TaskInfoConstants.TOPIC_PRE_KEY + key.split("_")[1] + "_" + key.split("_")[2];
                if (!values.isEmpty()) {
                    cacheService.refreshWithPipeline(key, topicKey, values);
                    log.info("成功将redis里面到期的任务刷新到list中。。。。。。");
                }
            }

        }
    }

    /**
     * 将数据库里面的任务刷新到redis中
     */
    @PostConstruct //服务器如果启动，就执行这个方法
    @Scheduled(cron = "0 */5 * * * ?")//每五分钟执行一次这个方法3
    @Override
    public void reloadData(){
        log.info("开始刷新数据库里面的信息到redis中。。。。。。。。。。。。。");
//        将redis里面原先的数据清除 list和zset中
            clearCache();

//        查询mysql中的所有数据，查询如果小于5分钟的，那么存到redis中
        List<Taskinfo> taskInfos = taskinfoService.list();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);

        for(Taskinfo taskinfo:taskInfos){
            String subKey = taskinfo.getPriority()+"_"+taskinfo.getTaskType();
            if (taskinfo.getExecuteTime().getTime()<=calendar.getTimeInMillis() && taskinfo.getExecuteTime().getTime()>System.currentTimeMillis()) {
                cacheService.zAdd(TaskInfoConstants.FUTURE_PRE_KEY+subKey,JSON.toJSONString(taskinfo),taskinfo.getExecuteTime().getTime());
                log.info("有数据刷新到redis中。。。。。。。。。。。。。");
            }else if(taskinfo.getExecuteTime().getTime()<=System.currentTimeMillis()){
                cacheService.lLeftPush(TaskInfoConstants.TOPIC_PRE_KEY+subKey,JSON.toJSONString(taskinfo));
                log.info("有数据刷新到redis中。。。。。。。。。。。。。");
            }
        }



    }

    /**
     * 将清除redis中原先有的数据
     * @return
     */
    public Boolean clearCache(){
        boolean flag = true;
        try {
            Set<String> futureKeys = cacheService.scan(TaskInfoConstants.TOPIC_PRE_KEY + "*");
            Set<String> topicKeys = cacheService.scan(TaskInfoConstants.TOPIC_PRE_KEY + "*");
            cacheService.delete(futureKeys);
            cacheService.delete(topicKeys);
        } catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }
}




