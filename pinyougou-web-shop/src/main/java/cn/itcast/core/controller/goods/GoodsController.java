package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 添加商品
     * @param goodsVo
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody GoodsVo goodsVo){
        String sellerid = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsVo.getGoods().setSellerId(sellerid);
        try {
            goodsService.add(goodsVo);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /**
     * 商家商品查找
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult searchForShop(int page, int rows, @RequestBody Goods goods){
        String sellerid = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerid);
        return goodsService.searchForShop(page,rows,goods);
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    /**
     * 商品修改
     * @param goodsVo
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {
            goodsService.update(goodsVo);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
}
