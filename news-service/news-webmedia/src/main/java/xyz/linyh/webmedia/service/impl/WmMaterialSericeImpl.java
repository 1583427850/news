package xyz.linyh.webmedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.linyh.common.exception.CustomException;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.webmedia.dto.WmMaterialDto;
import xyz.linyh.model.webmedia.entity.WmMaterial;
import xyz.linyh.webmedia.interceptor.SaveIdInterceptor;
import xyz.linyh.webmedia.mapper.WmMaterialMapper;
import xyz.linyh.webmedia.service.WmMaterialService;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class WmMaterialSericeImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传素材,将素材上传到minio中，然后将对应的url保存到数据库中
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadMaterial(MultipartFile multipartFile,Long userId) {
//        0.参数校验
        if(multipartFile==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(userId==null){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }

//        1. 将图片或视频保存到minio
//        获取存到minio中的文件名
        String preFilename = UUID.randomUUID().toString();
        String fileName = multipartFile.getOriginalFilename();
        String sufFilename = fileName.substring(fileName.lastIndexOf("."));

        String url="";
        try {
//        2. 获取对应的url
            url = fileStorageService.uploadImgFile("", preFilename + sufFilename, multipartFile.getInputStream());
        } catch (IOException e) {
            log.info(e.getMessage());
            log.error("上传文件失败");
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }

//        3. 保存到数据库中
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(url);
        wmMaterial.setCreatedTime(new Date());
//        素材类型 0 图片 1 视频
        wmMaterial.setType(0);
        wmMaterial.setUserId(userId);
        wmMaterial.setIsCollection(0);
        boolean save = save(wmMaterial);
        return ResponseResult.okResult(url);
    }

    /**
     * 查看素材
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult list(WmMaterialDto dto) {
//        1. 参数校验
        if(dto.getIsCollection()!=1 && dto.getIsCollection()!=0){
            dto.setIsCollection(0);
        }
        dto.checkParam();

//        2. 查询内容
        LambdaQueryWrapper<WmMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        wrapper.eq(WmMaterial::getUserId, SaveIdInterceptor.WM_USER_THREAD.get());
        wrapper.orderByDesc(WmMaterial::getCreatedTime);

        Page<WmMaterial> page = new Page<>(dto.getPage(),dto.getSize());
        Page<WmMaterial> result = page(page, wrapper);

        PageResponseResult pageResponseResult = new PageResponseResult((int) result.getCurrent(), (int) result.getSize(), (int) result.getTotal());
        pageResponseResult.setData(result.getRecords());
        return pageResponseResult;
    }
}
