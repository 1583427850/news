package xyz.linyh.webmedia.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.linyh.model.common.dtos.PageResponseResult;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmMaterialDto;
import xyz.linyh.webmedia.interceptor.SaveIdInterceptor;
import xyz.linyh.webmedia.service.WmMaterialService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/material")
public class MediaMaterialController  {

    @Autowired
    private WmMaterialService wmMaterialService;

    @ApiOperation("上传素材")
    @PostMapping("/upload_picture")
    public ResponseResult uploadMaterial(@RequestBody MultipartFile multipartFile, HttpServletRequest request){
//        除了这里直接获取id，还可以将id通过拦截器保存到线程中,可以减少方法的一个参数
        Long userId = SaveIdInterceptor.WM_USER_THREAD.get();
//        String userId = request.getHeader("userId");
        return wmMaterialService.uploadMaterial(multipartFile,userId);
    }

    /**
     * 查看所有素材
     * @return
     */
    @ApiOperation("查看所有素材")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto dto){

        return wmMaterialService.list(dto);
    }
}
