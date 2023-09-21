package com.liuxuanhe.wemedia.controller.v1;

import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmNewsDto;
import com.liuxuanhe.model.wemedia.dtos.WmNewsPageReqDto;
import com.liuxuanhe.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;
    /**
     * 查询文章
     */
    @PostMapping("/list")
    public PageResponseResult findList(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.findList(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        return wmNewsService.submit(dto);
    }

    /**
     * 根据ID查询文章，用于回显
     */
    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable Integer id){
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

    @GetMapping("/del_news/{id}")
    public ResponseResult delNews(@PathVariable Integer id){

        wmNewsService.delNews(id);
        return null;
    }
}
