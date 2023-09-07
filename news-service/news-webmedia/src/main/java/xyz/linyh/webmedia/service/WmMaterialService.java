package xyz.linyh.webmedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmMaterialDto;
import xyz.linyh.model.webmedia.entity.WmMaterial;

/**
* @author lin
* @description 针对表【wmb_material(自媒体图文素材信息表)】的数据库操作Service
* @createDate 2023-07-11 22:30:59
*/
@Mapper
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 上传素材
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadMaterial(MultipartFile multipartFile,Long userId);

    /**
     * 查看素材
     * @param dto
     * @return
     */
    public ResponseResult list(WmMaterialDto dto);

}
