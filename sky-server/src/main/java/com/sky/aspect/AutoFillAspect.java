package com.sky.aspect;
//自定义切面类，实现公共字段，自动填充

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 为什么要加Component?
 * 只有被 Spring 管理的类（叫“Bean”）才能实现依赖注入、生命周期控制、AOP 功能等等。
 * 因为你想用这个类做 切面（Aspect）功能，而切面是靠 Spring 的 AOP 机制实现的。
 *
 * 如果你不加 @Component，Spring 就不会扫描到它，也不会帮你织入逻辑，切面就不起作用了
 */

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点:PointCut告诉Spring哪些方法需要被AOP拦截
     * execution(访问修饰符 返回值类型 包名.类名.方法名(参数))
     * 下面的意思：
     * * 表示返回值任意
     * com.sky.mapper.* 表示 mapper 包下所有类
     * .*(..) 表示所有方法，所有参数
     *
     * @annotation:只拦截被 @AutoFill 注解的方法。
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){

    }

    /**
     * 自定的前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的自动填充....");


        //Spring AOP 会在方法调用前，根据你 @Pointcut 里定义的规则来匹配目标方法，一旦匹配成功，就去执行 @Before 的逻辑
        //获得当前被拦截方法的数据库操作类型
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);

        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();


        //根据当前不同的操作类型，对对应的属性通过反射赋值
        if(operationType == OperationType.INSERT){
        //为四个公共字段赋值
            //entity.getClass()
            //👉 获取这个对象的类，比如是 Employee.class
            //
            //.getDeclaredMethod("setCreateTime", LocalDateTime.class)
            //👉 查找这个类中名字叫 setCreateTime，参数是 LocalDateTime 的方法
            try {
                Method setCreateTime= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //通过反射为对象属性进行赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(operationType == OperationType.UPDATE){
            //为两个赋值
            try {

                Method setUpdateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //通过反射为对象属性进行赋值

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


}
