package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口碑数据
     */

    void insertBatch(List<DishFlavor> flavors);
}
