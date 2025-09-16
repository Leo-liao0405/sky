package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper {

    /*
     * 批量插入口味数据
     */
    void insertBatch(Object flavors);
}
