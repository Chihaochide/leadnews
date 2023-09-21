package com.liuxuanhe.user.service.impl;

import com.liuxuanhe.common.dtos.AppHttpCodeEnum;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.common.exception.LeadNewsException;
import com.liuxuanhe.model.user.pojos.ApUser;
import com.liuxuanhe.user.mapper.ApUserMapper;
import com.liuxuanhe.user.service.ApUserService;
import com.liuxuanhe.utils.common.BCrypt;
import com.liuxuanhe.utils.common.JwtUtils;
import com.liuxuanhe.utils.common.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.liuxuanhe.model.user.dtos.LoginDto;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl implements ApUserService {
    @Autowired
    private ApUserMapper apUserMapper;

    @Value("${leadnews.jwt.privateKeyPath}")
    private String privateKeyPath; // 私钥路径
    private String test = "E:\\MyProject\\Key\\rsa-key";
    @Value("${leadnews.jwt.expire}")
    private Integer expire; // 过期时间


    @Override
    public ResponseResult login(LoginDto dto) {
        if (StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())){
            // 有登录信息
            ApUser loginUser = apUserMapper.findUserByPhone(dto.getPhone());
            if (loginUser==null){
//                throw new RuntimeException("用户不存在！");
//                throw new LeadNewsException(404,"用户名不存在！");
                throw new LeadNewsException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
            }
            // 判断密码是否正确

            String password = loginUser.getPassword();

            if (!BCrypt.checkpw(dto.getPassword(), password)) {
//                throw new RuntimeException("密码错误！");
//                throw new LeadNewsException(505,"密码错误！");
                throw new LeadNewsException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);

            }

            // 返回token给它
            // Ⅰ.获取私钥文件
            try {
                System.out.println("私钥路径是："+privateKeyPath);
                System.out.println("对比路径："+test);
                System.out.println(test.equals(privateKeyPath));
                PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyPath);
                System.out.println("私钥是："+privateKey.getFormat());
                // 生成token
                // 去掉敏感数据  因为要传到前端
                loginUser.setPassword(null);
                String token = JwtUtils.generateTokenExpireInMinutes(loginUser, privateKey, expire);

                Map map = new HashMap<>();
                map.put("token",token);
                map.put("user",loginUser);

                // 返回信息给前端
                return ResponseResult.okResult(map);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("加载私钥失败！");
            }
        }else{
            // 没有登录信息
            System.out.println("无登录信息-访客登录");
            // 返回token给它
            // Ⅰ.获取私钥文件
            try {
                PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyPath);
                // 生成token
                // 去掉敏感数据  因为要传到前端
                ApUser user = new ApUser();
                user.setId(0); // 0 代表访客
                String token = JwtUtils.generateTokenExpireInMinutes(user, privateKey, expire);

                Map map = new HashMap<>();
                map.put("token",token);

                // 返回信息给前端
                return ResponseResult.okResult(map);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("加载私钥失败！");
            }
        }
    }
}
