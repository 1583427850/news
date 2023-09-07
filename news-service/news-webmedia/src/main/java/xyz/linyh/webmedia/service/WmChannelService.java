package xyz.linyh.webmedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.dto.WmChannelSaveDto;
import xyz.linyh.model.webmedia.dto.WmChannelUpdateDto;
import xyz.linyh.model.webmedia.entity.WmChannel;

/**
* @author lin
* @description 针对表【wmb_channel(频道信息表)】的数据库操作Service
* @createDate 2023-07-11 22:30:48
*/
@Mapper
public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查看所有频道
     * @return
     */
    public ResponseResult channels();

    /**
     * 条件查询所有频道
     * @param dto
     * @return
     */
    public ResponseResult listAllChanelByPage(CommonPageDto dto);

    /**
     * 新增频道
     * @param dto
     * @return
     */
    ResponseResult saveChannel(WmChannelSaveDto dto);

    /**
     * 删除频道（如果是启用的，那么无法删除）
     * @param id
     * @return
     */
    ResponseResult delChannelById(Long id);

    /**
     * 修改频道
     * @param dto
     * @return
     */
    ResponseResult updateChannel(WmChannelUpdateDto dto);


}
