package cn.itcast.core.service.cart;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ItemDao itemDao;
    @Resource
    private SellerDao sellerDao;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Item findOne(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    @Override
    public List<Cart> addItemToCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            String sellerId = cart.getSellerId();
            Seller seller = sellerDao.selectByPrimaryKey(sellerId);
            String sellerName = seller.getNickName();
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                Long itemId = orderItem.getItemId();
                Item item = itemDao.selectByPrimaryKey(itemId);
                orderItem.setPicPath(item.getImage());
                orderItem.setTitle(item.getTitle());
                orderItem.setPrice(item.getPrice());
                Double totalFee=orderItem.getPrice().doubleValue()*orderItem.getNum();
                orderItem.setTotalFee(new BigDecimal(totalFee));
            }
        }
        return cartList;
    }

    /**
     * 将购物车中的内容存入redis
     * @param username
     * @param newCartList
     */
    @Override
    public void saveCartListToRedis(String username, List<Cart> newCartList) {
        //从redis中获得老购物车
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("BUYER-CART").get(username);
        //合并新老购物车
        oldCartList=moveNewCartListToOldCartList(newCartList,oldCartList);
        //将老购物车重新存到redis
        redisTemplate.boundHashOps("BUYER-CART").put(username,oldCartList);
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER-CART").get(username);
        return cartList;
    }


    private List<Cart> moveNewCartListToOldCartList(List<Cart> newCartList, List<Cart> oldCartList) {
        if (newCartList!=null && newCartList.size()>0){
            //新购物车不为空
            if (oldCartList!=null && oldCartList.size()>0){
                //新旧购物车都不为空时
                for (Cart cart : newCartList) {
                    //判断是否为同一商家
                    int sellerIndex = oldCartList.indexOf(cart);
                    if (sellerIndex!=-1){
                        //同一商家
                        List<OrderItem> newOrderItemList = cart.getOrderItemList();
                        List<OrderItem> oldOrderItemList = oldCartList.get(sellerIndex).getOrderItemList();
                        //判断是否为同一商品
                        for (OrderItem orderItem : newOrderItemList) {
                            int orderItemIndex = oldOrderItemList.indexOf(orderItem);
                            if (orderItemIndex!=-1){
                                //同一商品
                                //更新数量
                                Integer newNum = orderItem.getNum();
                                Integer oldNum = oldOrderItemList.get(orderItemIndex).getNum();
                                oldOrderItemList.get(orderItemIndex).setNum(newNum+oldNum);
                            }else {
                                //不同商品
                                oldOrderItemList.add(orderItem);
                            }
                        }
                    }else {
                        //不同商家
                        oldCartList.add(cart);
                    }
                }
            }else {
                //旧购物车为空,返回新购物车
                return newCartList;
            }
        }else {
            //新购物车为空,返回旧购物车
            return oldCartList;
        }
        return oldCartList;
    }
}
