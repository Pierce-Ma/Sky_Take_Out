package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {




    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    /**
     * 菜品的批量删除
     */
    void deleteBatch(List<Long> ids);



    /**
     根据ID和菜品查询指定的数据
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 根据ID修改菜品基本信息和对应的口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    List<Dish> list(Long categoryId);
}
