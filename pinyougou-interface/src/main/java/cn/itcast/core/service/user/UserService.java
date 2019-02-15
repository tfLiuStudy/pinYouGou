package cn.itcast.core.service.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {


    void sendCode(String phone);

    void add(String smscode, User user);


    User findOne(String userId);

    void updateUser(User user);
}
