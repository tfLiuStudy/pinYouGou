package cn.itcast.core.service.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

import java.util.List;

public interface OrderService {

    void add(String username, Order order);

    //查询所有订单
    List<Order> findAll(String username);


}
