package cn.itcast.core.service.cart;

import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CartService {

    Item findOne(Long itemId);

    List<Cart> addItemToCart(List<Cart> cartList);

    void saveCartListToRedis(String username, List<Cart> newCartList);

    List<Cart> findCartListFromRedis(String username);
}
