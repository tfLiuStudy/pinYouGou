package cn.itcast.core.service.user;

import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {


    void sendCode(String phone);

    void add(String smscode, User user);
}
