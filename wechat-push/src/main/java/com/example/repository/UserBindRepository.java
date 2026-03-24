package com.example.repository;

import com.example.entity.UserBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户绑定数据访问接口
 * 用于操作user_bind表
 */
@Repository
public interface UserBindRepository extends JpaRepository<UserBind, Long> {
    
    /**
     * 根据用户工号查询绑定关系
     * @param userId 用户工号
     * @return 绑定关系Optional
     */
    Optional<UserBind> findByUserId(String userId);
    
    /**
     * 根据微信openid查询绑定关系
     * @param openId 微信openid
     * @return 绑定关系Optional
     */
    Optional<UserBind> findByOpenId(String openId);
    
    /**
     * 判断用户工号是否已绑定
     * @param userId 用户工号
     * @return 是否已绑定
     */
    boolean existsByUserId(String userId);
    
    /**
     * 判断微信openid是否已绑定
     * @param openId 微信openid
     * @return 是否已绑定
     */
    boolean existsByOpenId(String openId);
}