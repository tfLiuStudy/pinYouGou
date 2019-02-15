package cn.itcast.core.service.order;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.utils.uniquekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private IdWorker idWorker;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private PayLogDao payLogDao;

    /**
     * 保存订单信息
     * @param username
     * @param order
     */
    @Override
    public void add(String username, Order order) {
        //保存订单信息,商家为单位为一个订单
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER-CART").get(username);
        if (cartList!=null && cartList.size()>0){
            double logTotalFee=0;
            List<Long> orderIdList = new ArrayList<>();
            for (Cart cart : cartList) {
                long orderId = idWorker.nextId();
                orderIdList.add(orderId);
                order.setOrderId(orderId);
                double payment=0;
                order.setPaymentType("1");
                order.setStatus("1");
                order.setCreateTime(new Date());
                order.setUserId(username);
                order.setSourceType("2");
                order.setSellerId(cart.getSellerId());
                List<OrderItem> orderItemList = cart.getOrderItemList();
                //保存订单明细
                if (orderItemList!=null && orderItemList.size()>0){
                    for (OrderItem orderItem : orderItemList) {
                        long orderItemId = idWorker.nextId();
                        orderItem.setId(orderItemId);
                        Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        orderItem.setGoodsId(item.getGoodsId());
                        orderItem.setOrderId(orderId);
                        orderItem.setTitle(item.getTitle());
                        orderItem.setPrice(item.getPrice());
                        orderItem.setPicPath(item.getImage());
                        orderItem.setSellerId(cart.getSellerId());
                        double totakFee=item.getPrice().doubleValue()*orderItem.getNum();
                        orderItem.setTotalFee(new BigDecimal(totakFee));
                        payment+=totakFee;
                        orderItemDao.insertSelective(orderItem);
                    }
                }
                order.setPayment(new BigDecimal(payment));
                orderDao.insertSelective(order);
                logTotalFee+=payment;
            }
            //提交订单完成之后要创建订单日志
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
            payLog.setCreateTime(new Date());
            payLog.setTotalFee((long) logTotalFee);
            payLog.setUserId(username);
            payLog.setOrderList(orderIdList.toString().replace("[","").replace("]",""));
            payLog.setPayType("1");
            payLog.setTradeState("0");
            payLogDao.insertSelective(payLog);
            //将日志数据存入redis
            redisTemplate.boundHashOps("payLog").put(username,payLog);
        }

        //将购物车内的商品清空
        redisTemplate.boundHashOps("BUYER-CART").delete(username);
    }

    //根据用户查询所有订单
    @Override
    public List<Order> findAll(String username) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andUserIdEqualTo(username);
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        for (Order order:orderList){
            Long orderId = order.getOrderId();
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(orderId);
            List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
            order.setOrderItems(orderItemList);
        }
        return orderList;
    }
}
