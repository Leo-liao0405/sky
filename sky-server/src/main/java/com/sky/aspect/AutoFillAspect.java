package com.sky.aspect;


import java.time.LocalDateTime;
import java.lang.reflect.Method;
import java.security.Signature;


import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import com.sky.constant.AutoFillConstant;


import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Join;

/*
 * 切面类：实现功能字段的自动填充
 */


@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    
    /*
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /*
     * 前置通知，进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行自动填充功能......");
        
        
        // 获取操作类型(因为获取的是实体，向下转型成MethodSignature)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取类名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得注解对象
        OperationType operationType = autoFill.value();//获取操作类型

        // 获取被拦截方法的参数
        Object [] args = joinPoint.getArgs();//获取所有参数
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0]; // 获取第一个参数，就是实体对象
        
        
        // 准备数据
        LocalDateTime now = LocalDateTime.now(); // 当前时间
        Long currentUserId = BaseContext.getCurrentId(); // 当前登录用户id


        // 根据操作类型，进行不同的自动填充
        if (operationType == OperationType.INSERT) {
            // 插入操作,需要填充4个字段
            try {
                // 获取方法
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
               // 通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentUserId);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType == OperationType.UPDATE) {
            // 修改操作,需要填充2个字段
            try {
                // 获取方法
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
