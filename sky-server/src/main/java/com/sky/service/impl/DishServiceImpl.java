package com.sky.service.impl;


import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
/*
 * 菜品管理业务实现类
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    
    /*
     * 新增菜品，同时保存对应的口味数据
     * @param dishDTO
     * @return
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        // 向菜品表插入数据（插入一条）
        

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        

        //获取insert语句生成的主键值
        long dishId = dish.getId(); // 菜品id



        // 向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0 ){
            //插入n条数据
            //实际上也要遍历，因为要插入ID
            flavors.forEach(dishflavor -> {
                dishflavor.setDishId(dishId);
            });
            //直接传集合对象，然后批量插入（在SQL层实现批量处理）
            dishFlavorMapper.insertBatch(flavors);

        }


    }
}