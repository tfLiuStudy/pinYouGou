package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.phone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/sendCode.do")
    public Result sendCode(String phone) {
        try {
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new Result(false, "手机号码格式不正确");
            }
            userService.sendCode(phone);
            return new Result(true, "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送失败");
        }
    }

    @RequestMapping("/add.do")
    public Result add(String smscode, @RequestBody User user) {
        try {
            userService.add(smscode, user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }

    @RequestMapping("/findOneByUserName.do")
    public User findOneByUserName() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User one = userService.findOne(userId);
        if (one != null) {
            return one;
        }
        return null;
    }

    @RequestMapping("/updateUserInfo.do")
    public Result updateUserInfo(@RequestBody User user) {
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            user.setUsername(userId);
            userService.updateUser(user);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    //查询当前登陆账户的收藏商品
    @RequestMapping("/findAllConcern.do")
    public List<Item> getAll(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findAllConcern(userId);
    }

}
