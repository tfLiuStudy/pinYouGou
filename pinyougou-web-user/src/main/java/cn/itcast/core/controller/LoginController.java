package cn.itcast.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 获取登录人名称并展示
     * @return
     */
    @RequestMapping("/name.do")
    public Map<String,String> showName(){
        Map<String, String> map = new HashMap<>();
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("loginName",loginName);
        return map;
    }
}
