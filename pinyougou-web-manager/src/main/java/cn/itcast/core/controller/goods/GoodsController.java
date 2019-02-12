package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 运营商查询商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(int page, int rows, @RequestBody Goods goods){
        return goodsService.searchForManager(page,rows,goods);
    }

    /**
     * 运营商审核商品
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids,String status){
        try {
            goodsService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }

    /**
     * 对商品进行逻辑删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            goodsService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
}
