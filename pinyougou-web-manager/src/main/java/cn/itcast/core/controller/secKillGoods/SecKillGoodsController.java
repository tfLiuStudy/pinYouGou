package cn.itcast.core.controller.secKillGoods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.secKillGoods.SecKillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secKillGoods")
public class SecKillGoodsController {

    @Reference
    private SecKillGoodsService secKillGoodsService;

    @RequestMapping("/search.do")
    public PageResult search(int page, int rows, @RequestBody SeckillGoods seckillGoods){
        return secKillGoodsService.search(page,rows,seckillGoods);
    }

    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status){
        try {
            secKillGoodsService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }
}
