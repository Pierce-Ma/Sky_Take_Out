package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
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
    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接受订单号:{}",ordersConfirmDTO.getId());
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO.getId());
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable("id") Long id) {
        orderService.delivery(id);
        return Result.success();
    }
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
