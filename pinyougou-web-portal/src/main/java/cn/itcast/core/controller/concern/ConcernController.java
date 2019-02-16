package cn.itcast.core.controller.concern;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.concern.ConcernService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/15$ 18:46$
 */
@RestController
@RequestMapping("/concern")
public class ConcernController {

    @Reference
    private ConcernService concernService;

    @RequestMapping("/add.do")
    public Result addConcern(@RequestBody Item item){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)){
            return new Result(false,"你尚未登陆，请先登陆！");
        }
        //将关注的商品加入redis缓存中
        String msg = concernService.addConcern(item, name);
        return new Result(true,msg);
    }

}
