package cn.itcast.core.controller.seckill;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.service.seckill.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 10:49$
 */
@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/search.do")
    public PageResult search(Long page,Long rows){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

        return seckillOrderService.search(page,rows,sellerId);
    }
}
