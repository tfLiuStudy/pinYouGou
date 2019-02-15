package cn.itcast.core.controller.seckill;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.seckill.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 10:49$
 */
@RestController
@RequestMapping("/seckill")
public class SeckillGoodsController {

    @Reference
    private SeckillService seckillService;

    @RequestMapping("/findAllBySeller.do")
    public List<Item> findAllBySeller(){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillService.findAllSKUBySeller(sellerId);
    }

    @RequestMapping("/findOne.do")
    public Item findOne(Long id){
        return seckillService.findOne(id);
    }

    @RequestMapping("/commitSeckill.do")
    public Result commitSeckill(@RequestBody SeckillGoods seckillGoods){
        try{
            if (seckillGoods==null){
                throw new RuntimeException("空对象");
            }
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            seckillGoods.setSellerId(sellerId);
            seckillService.commitSeckill(seckillGoods);
            return new Result(true,"成功");
        }catch (Exception e){
            return new Result(false,"失败");
        }
    }
}
