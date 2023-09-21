package com.liuxuanhe.wemedia.controller.v1;

import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmMaterialDto;
import com.liuxuanhe.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 上传素材
     */
    @PostMapping("upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }

    /**
     * shucai
     */

    @PostMapping("/list")
    public PageResponseResult list(@RequestBody WmMaterialDto dto){
        return wmMaterialService.list(dto);
    }

    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable("id") int id){
        System.out.println(id);
        wmMaterialService.collect(id);
        return ResponseResult.okResult("测试");
    }
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable("id") int id){
        System.out.println(id);
        wmMaterialService.collect(id);
        return ResponseResult.okResult("测试");
    }

}
