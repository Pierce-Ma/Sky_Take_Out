package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登陆")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "退出功能")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     *
     * 新增员工
     *（1）为什么要加Requestbody：前端发过来的 JSON 数据要转换成 Java 对象，并赋值给这个参数。，一般前端默认发给后端的都是json数据
     * (2) 由于这个接口使用的是post方法，所以我们要加PostMapping，因为前面已经制定了路径，这里我们就不用管了
     * （3）给API添加注释，方便后续查看管理
     * (4)新增员工{}的作用可以把后面的employeeDTO的值动态的吸进去。
     * （5）这个I:EmployeeController,II:EmployeeService,III:EmployeeServiceImpl有这样的关系:
     *      I:用来处理前端的请求，调用Service来完成具体的业务逻辑
     *      II:只是写接口，不用写实现。
     *      III：具体实现 EmployeeService 接口里定义的方法；会操作数据库、调用其它类来完成业务
     *      如何连接？：
     *      Controller 注入 Service（通过 @Autowired）
     *       Service 是一个接口（解耦）
     *       @Service 注解的实现类 Impl 会自动被 Spring 扫描并注入
     *
     *       Controller 层（控制层）	接收前端请求，调用 Service 层	EmployeeController
     *      Service 层（业务逻辑层）	处理业务逻辑，调用 Mapper 层	EmployeeServiceImpl
     *      Mapper 层（持久层 / DAO）	操作数据库，执行 SQL	EmployeeMapper
     *
     *
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        System.out.println("当前线程ID：" + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }
    //这里由于是query的，不是json的，所以不用加@Request。
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询:{}",employeePageQueryDTO);
         PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    //路径参数要加@pathVariable
    public Result startOrStop(@PathVariable Integer status,long id){
        log.info("启用禁用员工账号:{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工信息")
    //这里由于接口文档设计的时候是把id作为路径参数的，所以要加@pathVariabke，具体的话还要在搜一下gpt了解原理
    public Result<Employee> getById(@PathVariable Long id) {

        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
