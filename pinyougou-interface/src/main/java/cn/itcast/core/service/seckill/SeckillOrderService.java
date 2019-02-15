package cn.itcast.core.service.seckill;

import cn.itcast.core.entity.PageResult;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 17:27$
 */
public interface SeckillOrderService {
    PageResult search(Long page, Long rows, String sellerId);
}
