package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 自定义注解：用于表示某个方法需要功能字段自动填充
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 标记当前操作是新增还是修改
    // 枚举的方法已经定义好了，在common中OperationType
    OperationType value(); // "insert" 或 "update"
}
