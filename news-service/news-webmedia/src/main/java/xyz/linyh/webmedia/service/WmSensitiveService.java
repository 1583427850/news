package xyz.linyh.webmedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.entity.WmSensitive;

public interface WmSensitiveService extends IService<WmSensitive> {

    /**
     * 分页和条件查询所有敏感词
     * @param dto
     * @return
     */
    ResponseResult listAllSensitiveByPage(CommonPageDto dto);

    /**
     * 新增敏感词
     * @param sensitives
     * @return
     */
    ResponseResult saveSensitive(String sensitives);

    /**
     * 修改敏感词
     * @param id
     * @param sensitives
     * @return
     */
    ResponseResult updateSensitive(String id, String sensitives);

    /**
     * 通过敏感词id删除敏感词
     * @param id
     * @return
     */
    ResponseResult delSensitiveById(Long id);
}
