package com.liuxuanhe.wemedia.mapper;

import com.liuxuanhe.model.wemedia.dtos.WmMaterialDto;
import com.liuxuanhe.model.wemedia.pojos.WmMaterial;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface WmMaterialMapper {

    @Insert("insert into wm_material " +
            "values(null,#{wmMaterial.userId},#{wmMaterial.url}" +
            ",#{wmMaterial.type},#{wmMaterial.isCollection},#{wmMaterial.createdTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void save(@Param("wmMaterial") WmMaterial wmMaterial);

    List<WmMaterial> findById(@Param("wmUser") WmUser wmUser, @Param("dto") WmMaterialDto dto);

    @Update("update wm_material set is_collection = if(is_collection=1,0,1) where id = #{id}")
    void updateCollect(@Param("wmUser") WmUser wmUser, @Param("id") int id);

    List<WmMaterial> selectByUrl(@Param("images") List<String> images);
}
