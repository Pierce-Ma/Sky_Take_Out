package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MealSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class MealSetController {

    @Autowired
    private MealSetService mealSetService;

    @PostMapping
    @ApiOperation("新增套餐")
    public Result AddMealSet(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}",setmealDTO);
        mealSetService.addNewMealSet(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> Query(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询:{}",setmealPageQueryDTO);
        PageResult pageResult = mealSetService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

}
