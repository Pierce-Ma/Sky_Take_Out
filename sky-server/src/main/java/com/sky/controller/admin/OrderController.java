package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "店铺订单")
@RestController
@Slf4j
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @ApiOperation("订单搜索")
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单功能测试:{}", ordersPageQueryDTO);
        PageResult pageResult =  orderService.Search(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    @ApiOperation("订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {

        OrderStatisticsVO orderStatisticsVO = orderService.getCount();
        return Result.success(orderStatisticsVO);
    }
    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("查询订单详情:{}",id);
        OrderVO orderVO = orderService.query(id);
        return Result.success(orderVO);
    }
}
