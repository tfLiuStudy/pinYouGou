package cn.itcast.core.controller.map;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody Order order){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.add(username,order);
            return new Result(true,"订单提交成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"提交订单失败");
        }
    }
    //查询所有的订单
    @RequestMapping("/findAll.do")
    public List<Order> findAll(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order>OrderList = orderService.findAll(username);
        return OrderList;
    }
}
