package com.liuxuanhe.wemedia.controller.v1;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.pojos.WmChannel;
import com.liuxuanhe.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult channels(){
        List<WmChannel> list = wmChannelService.getList();
        System.err.println(list);
        return ResponseResult.okResult(list);
    }

}
