package xyz.linyh.webmedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.entity.WmSensitive;
import xyz.linyh.webmedia.mapper.WmSensitiveMapper;
import xyz.linyh.webmedia.service.WmSensitiveService;

import java.nio.channels.WritePendingException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    /**
     * 查询所有敏感词
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult listAllSensitiveByPage(CommonPageDto dto) {
//        0. 参数校验
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        dto.checkParam();

//        1. 构造查询和分页条件
        Page<WmSensitive> queryPage = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmSensitive> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WmSensitive::getCreatedTime);
        wrapper.like(StringUtils.isNotBlank(dto.getName()),WmSensitive::getSensitives,dto.getName());

        Page<WmSensitive> page = page(queryPage, wrapper);

//        构造返回结果返回
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        return pageResponseResult.ok(page.getRecords());
    }

    /**
     * 新增敏感词
     *
     * @param sensitives
     * @return
     */
    @Override
    public ResponseResult saveSensitive(String sensitives) {
//        0. 参数校验
        if(StringUtils.isBlank(sensitives)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        3. 判断是否有重复的
        List<WmSensitive> list = list(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, sensitives));
        if(list.size()>0){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

//        2. 新增
        WmSensitive wmSensitive = new WmSensitive();
        wmSensitive.setSensitives(sensitives);
        wmSensitive.setCreatedTime(new Date());
        save(wmSensitive);
        return ResponseResult.okResult(null);
    }

    /**
     * 修改敏感词
     *
     * @param id
     * @param sensitives
     * @return
     */
    @Override
    public ResponseResult updateSensitive(String id, String sensitives) {
//        0. 参数校验
        if(StringUtils.isBlank(id) || StringUtils.isBlank(sensitives)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 判断敏感词是否已经存在
        WmSensitive byId = getById(id);
        WmSensitive DBWmSensitive = getOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, sensitives));
        if(DBWmSensitive!=null && !DBWmSensitive.getSensitives().equals(byId.getSensitives())){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

//        2. 修改
        update(Wrappers.<WmSensitive>lambdaUpdate().eq(WmSensitive::getId,id).set(WmSensitive::getSensitives,sensitives));
        return ResponseResult.okResult(null);
    }

    /**
     * 通过敏感词id删除敏感词
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delSensitiveById(Long id) {
//        0. 参数校验
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        删除
        removeById(id);
        return ResponseResult.okResult(null);
    }
}
