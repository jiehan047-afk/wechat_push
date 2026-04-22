package com.example.repository;

import com.example.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户账号数据访问接口
 * 对应 user_accounts 表
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    /**
     * 根据用户名（工号）查询账号信息
     *
     * @param username 用户名（工号）
     * @return 账号信息 Optional
     */
    Optional<UserAccount> findByUsername(String username);
}
