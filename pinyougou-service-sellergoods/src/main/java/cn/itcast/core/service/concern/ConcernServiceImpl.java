package cn.itcast.core.service.concern;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/15$ 19:47$
 */
@Service
public class ConcernServiceImpl implements ConcernService {

    @Autowired
    private RedisTemplate redisTemplate;   //关注的key是 concern  中的小key是 用户的名字

    @Transactional
    @Override
    public String addConcern(Item item, String userId) {
        //redisTemplagte中的数据结构：
        //  key=concern value=[key=userId,value=itemId+itemId ]

        //先判断是否存在这个
        List<Item> itemIdList = (List<Item>) redisTemplate.boundHashOps("CONCERN").get(userId);
        //判断当前商品是否已经关注
        if (itemIdList!=null&&itemIdList.size()>0){
            for (Item itemTar : itemIdList) {
                if (item.getId().equals(itemTar.getId())){
                    return "当前商品已经关注";
                }
            }
        }

        //不存在进行收藏
        //清空购物车中的当前购物项
        //添加到redis中
        //判断是否为空
        if (itemIdList!=null&&itemIdList.size()>0){
            //合并进去
            itemIdList.add(item);
            redisTemplate.boundHashOps("CONCERN").put(userId,itemIdList);
        }else{
            //第一次关注
            List<Item> newList = new ArrayList();
            newList.add(item);
            redisTemplate.boundHashOps("CONCERN").put(userId,newList);
        }
        return "已关注";
    }
}
