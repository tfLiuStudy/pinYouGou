package cn.itcast.core.controller.seckillOrders;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.seckillOrders.SeckillOrdersService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secKillOrders")
public class SeckillOrdersController {

    @Reference
    private SeckillOrdersService seckillOrdersService;

    @RequestMapping("/search.do")
    public PageResult search(int page, int rows, @RequestBody SeckillOrder seckillOrder){
        return seckillOrdersService.search(page,rows,seckillOrder);
    }
}
