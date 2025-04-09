package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

        @Autowired
        private EmployeeMapper employeeMapper;

        /**
         * 员工登录
         *
         * @param employeeLoginDTO
         * @return
         */
        public Employee login(EmployeeLoginDTO employeeLoginDTO) {
            String username = employeeLoginDTO.getUsername();
            String password = employeeLoginDTO.getPassword();

            //1、根据用户名查询数据库中的数据
            Employee employee = employeeMapper.getByUsername(username);

            //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
            if (employee == null) {
                //账号不存在
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }

            //密码比对
            // 对前端传入的密码进行md5加密处理
            password  = DigestUtils.md5DigestAsHex(password.getBytes());
            if (!password.equals(employee.getPassword())) {
                //密码错误
                throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
            }

            if (employee.getStatus() == StatusConstant.DISABLE) {
                //账号被锁定
                throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
            }

            //3、返回实体对象
            return employee;
        }

        /**
         * 新增员工
         * 这里后面调用了Mapper来操作数据库，前面也声明了  @Autowired
         *     private EmployeeMapper employeeMapper;这样会直接让Srping找到我们之前定义的EMployeeMapper类，
         *     从而让我们直接使用

         * @param employeeDTO
         */
        @Override
        public void save(EmployeeDTO employeeDTO) {
            System.out.println("当前线程ID："+ Thread.currentThread().getId());
            Employee employee = new Employee();
            //使用对象属性拷贝，因为dto和employye他们的属性都一样
            BeanUtils.copyProperties(employeeDTO,employee);
            //设置账号状态,1正常，0锁定(这里定义了常量类供我们使用)
            employee.setStatus(StatusConstant.ENABLE);
            //设置账号密码，默认都给123456,由于需要加密，所以使用这个DigestUtils,这个123456这个常量已经在sky-common里定义的有了。
            employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
            //设置当前时间
            employee.setCreateTime(LocalDateTime.now());
            employee.setUpdateTime(LocalDateTime.now());
            //设置当前创建人id和修改人id

            employee.setCreateUser(BaseContext.getCurrentId());
            employee.setUpdateUser(BaseContext.getCurrentId());
            employeeMapper.insert(employee);
        }

        /**
         * 分页查询
         * @param employeePageQueryDTO
         * @return
         * pagehelper
         */
        public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
            //开始分页查询
            PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
            //这里一定要写Page的原因是因为前面的PageHelper要求这么做的，而由于我们查询的是employee表，所以要给写Java实体类的Employee
            //简而言之《》这里的东西是Page里的数据类型
            Page<Employee> page =  employeeMapper.pageQuery(employeePageQueryDTO);
            long total = page.getTotal();
            List<Employee> records = page.getResult();
            return new PageResult(total,records);
        }

        /**
         *
         * @param status
         * @param id
         */
            public void startOrStop(Integer status, long id) {
                //update employee set status =? where id = ?

                Employee employee = new Employee();
                employee.setStatus(status);
                employee.setId(id);

                employeeMapper.update(employee);
            }
}
