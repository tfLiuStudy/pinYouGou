package cn.itcast.core.cartList;



import cn.itcast.core.pojo.order.Order;

import java.util.*;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 14:30$
 */
public class ListCat {
    public static Map<Date, List<Order>> getMap(List<Order> orderList){
        if (orderList==null||orderList.size()<=0){
            return null;
        }
        Map<Date,List<Order>> map = new HashMap<>();
        //根据方法分类
        for (Order order : orderList) {
            //查看是否包含这个key
            if (map.get(order.getPaymentTime())!=null){
                //得到原先的
                List<Order> oldOrders = map.get(order.getPaymentTime());
                oldOrders.add(order);
                map.put(order.getPaymentTime(),oldOrders);
            }else{
                //添加进去
                List<Order> newOrdsers = new ArrayList<>();
                newOrdsers.add(order);
                map.put(order.getPaymentTime(),newOrdsers);
            }
        }
        return map;
    }
}
