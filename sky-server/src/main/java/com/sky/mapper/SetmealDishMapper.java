package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品ID查询对应的套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 把setmealDish表单进行更新插入
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
