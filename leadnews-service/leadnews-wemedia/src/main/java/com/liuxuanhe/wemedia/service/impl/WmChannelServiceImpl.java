package com.liuxuanhe.wemedia.service.impl;

import com.liuxuanhe.model.wemedia.pojos.WmChannel;
import com.liuxuanhe.wemedia.mapper.WmChannelMapper;
import com.liuxuanhe.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmChannelServiceImpl implements WmChannelService {

    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Override
    public List<WmChannel> getList() {
        ArrayList<WmChannel> wmChannels = wmChannelMapper.getList();

        return wmChannels;
    }
}
