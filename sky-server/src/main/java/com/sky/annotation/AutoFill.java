package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义的注解，用于标识某个方法，需要进行功能字段自动填充处理
//target让这个注解只能加载在方法上
//🔍 @Retention(RetentionPolicy.RUNTIME) 是什么意思？
//它的意思是：这个注解在运行时依然保留，可以通过反射拿到这个注解的信息
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {
    //指定数据库操作类型：Update,Insert
     OperationType value();
}
