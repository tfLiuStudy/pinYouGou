package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.phone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/sendCode.do")
    public Result sendCode(String phone){
        try {
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)){
                return new Result(false,"手机号码格式不正确");
            }
            userService.sendCode(phone);
            return new Result(true,"发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送失败");
        }
    }

    @RequestMapping("/add.do")
    public Result add(String smscode, @RequestBody User user){
        try {
            userService.add(smscode,user);
            return new Result(true,"注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }
}
