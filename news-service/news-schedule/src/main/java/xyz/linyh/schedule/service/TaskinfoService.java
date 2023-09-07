package xyz.linyh.schedule.service;


import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.schedule.eitity.Taskinfo;

import java.util.List;

/**
* @author lin
* @description 针对表【taskinfo】的数据库操作Service
* @createDate 2023-07-16 01:16:02
*/
public interface TaskinfoService extends IService<Taskinfo> {

    /**
     * 添加任务到数据库中
     * @param taskinfo
     * @return
     */
    public Long addTask(Taskinfo taskinfo);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    public boolean cancelTask(Long taskId);

    /**
     * 根据优先级和任务类型去redis的list中拉取任务
     * @param priority
     * @param type
     * @return
     */
    public Taskinfo poll(int priority,int type);

    /**
     * 查询redis中的未来数据，然后刷新到redis的list中
     */
    public void refresh();

    /**
     * 将数据库里面的数据刷新到redis中
     */
    public void reloadData();

}
