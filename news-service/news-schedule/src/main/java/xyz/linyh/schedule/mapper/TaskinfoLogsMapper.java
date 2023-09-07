package xyz.linyh.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import xyz.linyh.model.schedule.eitity.TaskinfoLogs;

/**
* @author lin
* @description 针对表【taskinfo_logs】的数据库操作Mapper
* @createDate 2023-07-16 01:16:05
* @Entity generator.entity.TaskinfoLogs
*/

@Mapper
public interface TaskinfoLogsMapper extends BaseMapper<TaskinfoLogs> {

}




