package cn.itcast.core.service.user;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * 运营商后台---用户管理模块
 */
@Service
public class UserManagerServiceImpl implements UserManagerService{

    @Resource
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

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

    /**m
     * 查询所有用户的订单
     * @param page   顶球页
     * @param rows   每页显示行数
     * @return
     */
    @Override
    public PageResult searchOrders(Long page, Long rows) throws Exception {
        if (page!=null&&!"".equals(page)&&rows!=null&&!"".equals(rows)){
            //设置分页条件
            PageHelper.startPage(page.intValue(),rows.intValue());
            Page<Order> pageList = (Page) orderDao.selectByExample(null);
            return new PageResult(pageList.getResult(),pageList.getTotal());
        }else{
            throw new Exception();
        }
    }

    /**m
     * 查询全部订单
     */
    @Override
    public List<Order> queryAll() {
        return  orderDao.selectByExample(null);
    }

}
