package com.liuxuanhe.wemedia.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuxuanhe.common.dtos.AppHttpCodeEnum;
import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.common.exception.LeadNewsException;
import com.liuxuanhe.common.minio.MinIOFileStorageService;
import com.liuxuanhe.model.wemedia.dtos.WmMaterialDto;
import com.liuxuanhe.model.wemedia.pojos.WmMaterial;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import com.liuxuanhe.utils.common.ThreadLocalUtils;
import com.liuxuanhe.wemedia.mapper.WmMaterialMapper;
import com.liuxuanhe.wemedia.service.WmMaterialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl implements WmMaterialService {

    @Autowired
    private MinIOFileStorageService storageService;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 判断校验
        if (multipartFile==null) throw new LeadNewsException(AppHttpCodeEnum.PARAM_INVALID);

        // 从ThreadLocal中取出登录用户的信息
        WmUser wmUser = (WmUser) ThreadLocalUtils.get();

        if (wmUser==null){
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }

        try {
            // 把图片上传到Minio
            String uuId = UUID.randomUUID().toString().replaceAll("-","");
            // 获取原名
            String originalFilename = multipartFile.getOriginalFilename();
            // 获取后缀名
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = uuId+ extName;
            String url = storageService.uploadImgFile(null, fileName, multipartFile.getInputStream());

            // 写入DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(wmUser.getId());
            wmMaterial.setType((short)0);
            wmMaterial.setIsCollection((short)0);
            wmMaterial.setUrl(url);
            wmMaterial.setCreatedTime(new Date());

            wmMaterialMapper.save(wmMaterial);

            return ResponseResult.okResult(wmMaterial);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponseResult list(WmMaterialDto dto) {
        // 获取登录的用户信息
        WmUser wmUser = (WmUser) ThreadLocalUtils.get();
        if (wmUser==null){
            throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);
        }

        // 加载分页插件
        PageHelper.startPage(dto.getPage(),dto.getSize());
        // 执行查询
        List<WmMaterial> wmMaterialList = wmMaterialMapper.findById(wmUser,dto);
        PageInfo<WmMaterial> pageInfo = new PageInfo<>(wmMaterialList);
        PageResponseResult responseResult = new PageResponseResult(pageInfo.getPageNum(), pageInfo.getSize(), (int) pageInfo.getTotal());
        responseResult.setCode(200);
        responseResult.setErrorMessage("查询成功！");
        responseResult.setData(pageInfo.getList());
        return responseResult;
    }

    @Override
    public void collect(int id) {
        WmUser wmUser = (WmUser) ThreadLocalUtils.get();
        if (StringUtils.isEmpty(String.valueOf(id))) throw new LeadNewsException(AppHttpCodeEnum.PARAM_INVALID);
        wmMaterialMapper.updateCollect(wmUser,id);
    }

}
