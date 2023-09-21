package com.liuxuanhe.wemedia.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuxuanhe.common.dtos.AppHttpCodeEnum;
import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.common.exception.LeadNewsException;
import com.liuxuanhe.model.wemedia.dtos.WmNewsDto;
import com.liuxuanhe.model.wemedia.dtos.WmNewsPageReqDto;
import com.liuxuanhe.model.wemedia.pojos.WmMaterial;
import com.liuxuanhe.model.wemedia.pojos.WmNews;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import com.liuxuanhe.utils.common.JsonUtils;
import com.liuxuanhe.utils.common.ThreadLocalUtils;
import com.liuxuanhe.wemedia.mapper.WmMaterialMapper;
import com.liuxuanhe.wemedia.mapper.WmNewsMapper;
import com.liuxuanhe.wemedia.mapper.WmNewsMaterialMapper;
import com.liuxuanhe.wemedia.service.WmNewsAutoScanService;
import com.liuxuanhe.wemedia.service.WmNewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class WmNewsServiceImpl implements WmNewsService {
    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Override
    public PageResponseResult findList(WmNewsPageReqDto dto) {
        dto.checkParam();
        WmUser wmUser = (WmUser) ThreadLocalUtils.get();
        if (wmUser==null) throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);

        PageHelper.startPage(dto.getPage(),dto.getSize());
        List<WmNews> wmNewsList = wmNewsMapper.findList(wmUser,dto);
        PageInfo<WmNews> pageInfo = new PageInfo<>(wmNewsList);
        PageResponseResult responseResult = new PageResponseResult(pageInfo.getPageNum(), pageInfo.getSize(), (int) pageInfo.getTotal());
        responseResult.setCode(200);
        responseResult.setErrorMessage("查询成功！");
        responseResult.setData(pageInfo.getList());
        return responseResult;

    }

    @Override
    public ResponseResult submit(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        // 获取登录用户信息
        WmUser wmUser = (WmUser) ThreadLocalUtils.get();
        if (wmUser==null) throw new LeadNewsException(AppHttpCodeEnum.NEED_LOGIN);

        // 作者id
        wmNews.setUserId(wmUser.getId());
        List<String> contentImage = getImagesForContent(wmNews);
        // 判断是否是自动封面，如果是自动封面，要处理自动封面
        if (dto.getType()==-1){
            int size = contentImage.size();
            if (size == 0 ){
                // 无图片
                wmNews.setType((short) 0);
                wmNews.setImages(null);
            }
            if (size>=1&&size<=2){
                // 单图
                wmNews.setType((short)1);
                wmNews.setImages(contentImage.get(0)); // 截取第一张做封面
            }
            if (size>=3){
                wmNews.setType((short)3);
                // 截取前三张
                wmNews.setImages(contentImage.subList(0,3).stream().collect(Collectors.joining()));
            }
            // 自动封面
        }else {
            // 非自动封面
            List<String> images = dto.getImages();
            // 拼接成String
            if (!CollectionUtils.isEmpty(images)){
                // joining（） 拼接：使用指定分隔符来拼接字符串
                String coverImage = images.stream().collect(Collectors.joining(","));
                wmNews.setImages(coverImage);
            }
        }
        // 判断是新增还是修改
        if (dto.getId()==null){
            wmNews.setCreatedTime(new Date());
            // 新增
            wmNews.setStatus((short)1);
            wmNews.setSubmitedTime(new Date());
            wmNewsMapper.save(wmNews);


        }else {
            // 修改，并且删除文章和素材的所有关系
            wmNewsMapper.update(wmNews);
            // 删除当前文章和素材的关系
            Integer id = wmNews.getId();
            wmNewsMaterialMapper.deleteFromNewsId(id);
        }


        // 内容素材与文章绑定 (type=0)
        if (!CollectionUtils.isEmpty(contentImage)){
            // 把url转换为id
            List<Integer> materialIDs = getMaterialIdsFromUrl(contentImage);
            wmNewsMaterialMapper.saveNewsMaterials(materialIDs,wmNews.getId(),0);
        }
        // 封面素材与文章绑定 (type=1)
        if (!StringUtils.isEmpty(wmNews.getImages())){
            List<String> images = Arrays.asList(wmNews.getImages().split(","));
            // 把url转换为id
            List<Integer> materialIDs = getMaterialIdsFromUrl(images);
            wmNewsMaterialMapper.saveNewsMaterials(materialIDs,wmNews.getId(),1);
        }

        // 点击审核触发自动审核业务
        ThreadPoolExecutor pools = new ThreadPoolExecutor(3, 5, 8, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(6), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        if (dto.getStatus()==1){
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public Object getById(Integer id) {
        WmNews wmNews = wmNewsMapper.getById(id);
        return wmNews;
    }


    /**
     * 根据Url查询id
     */
    private List<Integer> getMaterialIdsFromUrl(List<String> contentImage) {
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectByUrl(contentImage);
        if (!CollectionUtils.isEmpty(wmMaterials)){
//            return wmMaterials.stream().map(wmMaterial -> wmMaterial.getId()).collect(Collectors.toList());
            return wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
        }
        return null;
    }

    private List<String> getImagesForContent(WmNews wmNews) {
        List<String> images = new ArrayList<>();
        if (!StringUtils.isEmpty(wmNews)){
            List<Map> list = JsonUtils.toList(wmNews.getContent(), Map.class);
            for (Map map : list) {
                if (map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        return images;

    }

    // 文章下架
    @Override
    public void down(WmNewsDto wmNewsDto) {

    }


    // 删除new
    @Override
    public void delNews(Integer id) {
        wmNewsMaterialMapper.deleteFromNewsId(id);
        wmNewsMapper.deleteNews(id);
    }
}
