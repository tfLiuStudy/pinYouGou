package cn.itcast.core.controller.login;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 登录后用户名回显
     * @return
     */
    @RequestMapping("/showName.do")
    public Map<String,String> showName(){

        Map<String, String> map = new HashMap<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        map.put("username",username);

        return map;
    }

    @RequestMapping("/showTime.do")
    public Map<String,String> showTime(){

        Map<String, String> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = new Date();

        map.put("loginTime",simpleDateFormat.format(date));

        return map;
    }


}
