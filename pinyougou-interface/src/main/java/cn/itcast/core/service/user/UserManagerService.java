package cn.itcast.core.service.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
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

    /**m
     * 查询所有用户的订单
     * @param page   顶球页
     * @param rows   每页显示行数
     * @return
     */
    PageResult searchOrders(Long page, Long rows) throws Exception;

    List<Order> queryAll();
}
