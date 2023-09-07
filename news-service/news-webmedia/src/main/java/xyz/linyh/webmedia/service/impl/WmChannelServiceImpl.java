package xyz.linyh.webmedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.dto.WmChannelSaveDto;
import xyz.linyh.model.webmedia.dto.WmChannelUpdateDto;
import xyz.linyh.model.webmedia.entity.WmChannel;
import xyz.linyh.model.webmedia.entity.WmSensitive;
import xyz.linyh.webmedia.mapper.WmChannelMapper;
import xyz.linyh.webmedia.service.WmChannelService;

import java.util.Date;
import java.util.List;

@Transactional
@Service
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    /**
     * 查看所有频道
     *
     * @return
     */
    @Override
    public ResponseResult channels() {
        return ResponseResult.okResult(list());
    }

    /**
     * 条件查询所有频道
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult listAllChanelByPage(CommonPageDto dto) {
//        0. 参数校验
        if(dto==null ){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        dto.checkParam();

//        1. 构造查询条件
        Page<WmChannel> page = new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmChannel> wrapper = new LambdaQueryWrapper<>();
//        如果模糊查询名字不为空才会添加这个条件
        wrapper.like(StringUtils.isNotBlank(dto.getName()),WmChannel::getName,dto.getName());
        wrapper.orderByDesc(WmChannel::getCreatedTime);

//        2. 查询
        Page<WmChannel> wmChannelPage = page(page, wrapper);

//        3. 返回
        PageResponseResult response = new PageResponseResult((int) wmChannelPage.getCurrent(), (int) wmChannelPage.getSize(), (int) wmChannelPage.getTotal());
        return response.ok(wmChannelPage.getRecords());
    }

    /**
     * 新增频道
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveChannel(WmChannelSaveDto dto) {
//        0. 参数校验
        if(dto==null || StringUtils.isBlank(dto.getName()) || dto.getStatus()==null || StringUtils.isBlank(dto.getDescription())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if(dto.getOrd()==null) dto.setOrd(0);

//        1. 判断是否已经存在
        List<WmChannel> list = list(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName, dto.getName()));
        if(list.size()>0){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

//        2. 参数复制和增加
        WmChannel wmChannel = new WmChannel();
        BeanUtils.copyProperties(dto,wmChannel);
        if(dto.getStatus()) wmChannel.setStatus(1);
        else wmChannel.setStatus(0);
        wmChannel.setCreatedTime(new Date());
        wmChannel.setIsDefault(0);


//        3. 保存
        save(wmChannel);
        return ResponseResult.okResult(null);
    }

    /**
     * 删除频道（如果是启用的，那么无法删除）
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delChannelById(Long id) {
//        0. 参数校验
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 判断是否启用
        WmChannel byId = getById(id);
        if(byId.getStatus()==1){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_IS_ENABLE);
        }

//        2. 删除
        removeById(id);
        return ResponseResult.okResult(null);
    }

    /**
     * 修改频道
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult updateChannel(WmChannelUpdateDto dto) {
//        0. 参数校验
        if(dto==null || StringUtils.isBlank(dto.getName())|| dto.getId()== null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        if(dto.getStatus()==null)dto.setStatus(false);


//        1. 更新
        WmChannel byId = getById(dto.getId());
        if(byId ==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Integer updateStatus=0;
        if(dto.getStatus()) updateStatus=1;

//        1.1 如果只有修改状态
        if(dto.getDescription()==null){
            update(Wrappers.<WmChannel>lambdaUpdate().eq(WmChannel::getId,dto.getId()).set(WmChannel::getStatus,updateStatus));
            return ResponseResult.okResult(null);
        }

//        1.2 如果不是只更新状态，需要判断要更新的频道是否已经存在
        WmChannel dBWmChannel = getOne(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName, dto.getName()));
        if(dBWmChannel!=null && !dBWmChannel.getName().equals(byId.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

        LambdaUpdateWrapper<WmChannel> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(WmChannel::getId,dto.getId())
                        .set(WmChannel::getName,dto.getName())
                                .set(WmChannel::getDescription,dto.getDescription())
                                        .set(WmChannel::getStatus,updateStatus)
                                                .set(WmChannel::getOrd,dto.getOrd());
        update(wrapper);
        return ResponseResult.okResult(null);
    }




}
