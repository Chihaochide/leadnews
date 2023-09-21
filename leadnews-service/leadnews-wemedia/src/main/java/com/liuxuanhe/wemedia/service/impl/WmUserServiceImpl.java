package com.liuxuanhe.wemedia.service.impl;

import com.liuxuanhe.common.dtos.AppHttpCodeEnum;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.common.exception.LeadNewsException;
import com.liuxuanhe.model.wemedia.dtos.WmLoginDto;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import com.liuxuanhe.utils.common.BCrypt;
import com.liuxuanhe.utils.common.JwtUtils;
import com.liuxuanhe.utils.common.RsaUtils;
import com.liuxuanhe.wemedia.mapper.WmUserMapper;
import com.liuxuanhe.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class WmUserServiceImpl implements WmUserService {

    @Autowired
    private WmUserMapper wmUserMapper;

    @Value("${leadnews.jwt.privateKeyPath}")
    private String privateKeyPath; // 私钥路径
    @Value("${leadnews.jwt.expire}")
    private Integer expire; // 过期时间


    @Override
    public ResponseResult login(WmLoginDto dto) {
        if (StringUtils.isEmpty(dto.getName())||StringUtils.isEmpty(dto.getPassword())){
            throw new LeadNewsException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser loginUser = wmUserMapper.findUserByName(dto);
        if (loginUser==null){
            throw new LeadNewsException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        if (!BCrypt.checkpw(dto.getPassword(),loginUser.getPassword())){
            throw new LeadNewsException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        try {
            PrivateKey privateKey = Rsa*Utils.getPrivateKey(privateKeyPath);
            loginUser.setPassword(null);
            String token = JwtUtils.generateTokenExpireInMinutes(loginUser, privateKey,expire);
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            map.put("user",loginUser);

            return ResponseResult.okResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加载密钥失败！");
        }
    }
}
