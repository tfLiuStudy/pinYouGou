package cn.itcast.core.controller.user;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserManagerService;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userManager")
public class UserManagerController {

    @Reference
    private UserManagerService userManagerService;

    /**
     * 用户管理信息查询
     */
    @RequestMapping("/findAll")
    public List<User> findAll(){
        return userManagerService.findAll();
    }
}