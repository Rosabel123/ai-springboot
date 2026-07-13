package com.xxs.aispringbooot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxs.aispringbooot.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
