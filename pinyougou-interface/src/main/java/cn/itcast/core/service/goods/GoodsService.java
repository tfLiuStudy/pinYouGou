package cn.itcast.core.service.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

public interface GoodsService {

    void add(GoodsVo goodsVo);

    PageResult searchForShop(int page,int rows,Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo goodsVo);

    PageResult searchForManager(int page,int rows,Goods goods);

    void updateStatus(Long[] ids,String status);

    void delete(Long[] ids);
}
