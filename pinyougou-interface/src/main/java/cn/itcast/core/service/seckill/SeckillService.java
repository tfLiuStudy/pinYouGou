package cn.itcast.core.service.seckill;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 13:38$
 */
public interface SeckillService {
    List<Item> findAllSKUBySeller(String sellerId);

    Item findOne(Long id);

    void commitSeckill(SeckillGoods sellerId);
}
