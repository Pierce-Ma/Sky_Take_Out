package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.MealSetService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MealSetServiceImpl implements MealSetService {
    @Autowired
    MealSetService mealSetService;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;


    public void addNewMealSet(SetmealDTO setmealDTO) {
        //将DTO里的数据放到setmeal里，
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //将这个setmeal数据更新到表单里，这里不用管createtime什么的，因为之前写的mybatis已经自动帮忙管理了
        setmealMapper.insert(setmeal);
        //获取setmealID,用这个id和setmeal_dish表连接
        Long id = setmeal.getId();
        log.info("获得当前id:{}",id);
        //对于DTO里的数组数据，是用来存放菜品和对应的口味的，获取它
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //这里检查这个数组数据是否空，如果不为空的话，我们
        //使用foreach或的每个元素，并将每个元素的id设置为当前mealset的id，这样的话
        //setmeal表就和setmeal_dish表连接到一起了。
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
            //接下来我们需要吧这个setmealdish插入到setmealdish表中，
            setmealDishMapper.insertBatch(setmealDishes);
        }



    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }
}
