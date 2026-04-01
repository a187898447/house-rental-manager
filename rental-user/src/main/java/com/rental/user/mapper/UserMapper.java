package com.rental.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 Mapper 接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 openid 查询用户
     *
     * @param openid 微信 openid
     * @return 用户信息
     */
    User selectByOpenid(@Param("openid") String openid);

    /**
     * 根据 unionid 查询用户
     *
     * @param unionid 微信 unionid
     * @return 用户信息
     */
    User selectByUnionid(@Param("unionid") String unionid);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(@Param("phone") String phone);
}
