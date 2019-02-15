package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 运营商后台---用户管理模块
 */
@Service
public class UserManagerServiceImpl implements UserManagerService{

    @Resource
    private UserDao userDao;

    /**
     * 用户管理模块的查询
     * @return
     */
    @Override
    public List<User> findAll() {
        List<User> users = userDao.selectByExample(null);
        for (User user : users) {
            System.out.println(user);
        }
        return users;
    }
}
