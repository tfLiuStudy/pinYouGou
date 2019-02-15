package cn.itcast.core.service.seckillOrders;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;

public interface SeckillOrdersService {
    PageResult search(int page, int rows, SeckillOrder seckillOrder);
}
