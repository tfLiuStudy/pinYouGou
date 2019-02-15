package cn.itcast.core.service.secKillGoods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;

public interface  SecKillGoodsService {

    PageResult search(int page, int rows, SeckillGoods seckillGoods);

    void updateStatus(Long[] ids, String status);
}
