package cn.itcast.core.service.concern;

import cn.itcast.core.pojo.item.Item;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/15$ 19:46$
 */
public interface ConcernService {
    String addConcern(Item item, String userId);
}
