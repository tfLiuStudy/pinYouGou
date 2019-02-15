package cn.itcast.core.service.user;

import cn.itcast.core.pojo.user.User;

import java.util.List;

/**
 * 运营商后台管理---用户管理模块
 */
public interface UserManagerService {
    /**
     * 用户管理模块的查询
     * @return
     */
    List<User> findAll();
}
