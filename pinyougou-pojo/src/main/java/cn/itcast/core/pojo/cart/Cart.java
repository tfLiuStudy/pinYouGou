package cn.itcast.core.pojo.cart;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

//购物车对象
public class Cart implements Serializable {

    private String sellerId;
    private String sellerName;
    private List<OrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(sellerId, cart.sellerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sellerId);
    }
}
