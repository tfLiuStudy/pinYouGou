package cn.itcast.core.controller.cart;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @RequestMapping("/addGoodsToCartList.do")
    @CrossOrigin(origins = {"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request,HttpServletResponse response){
//        System.out.println(111);
        try {
            //声明一个购物车
            //获取到购物车
            String username = SecurityContextHolder.getContext().getAuthentication().getName();  //获得用户名
            System.out.println("------------"+username+"-------------");
            List<Cart> cartList=null;
            Cookie[] cookies = request.getCookies();
            boolean flag=false;
            if (cookies!=null && cookies.length>0){
                for (Cookie cookie : cookies) {
                    if ("BUYER-CART".equals(cookie.getName())){
                        String value = cookie.getValue();
                        String decode = URLDecoder.decode(value);
                        cartList = JSON.parseArray(decode, Cart.class); //如果cookie中有购物车,则拿到原来的购物车
                        flag=true;
                        break;
                    }
                }
            }
            //新建一个购物车
            if (cartList==null){
                cartList=new ArrayList<>();
            }
            //创建一个商家购物区
            //将请求的商品进行封装
            Cart cart = new Cart();
            Item item = cartService.findOne(itemId);
            cart.setSellerId(item.getSellerId());
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(itemId);
            orderItem.setNum(num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //商品装车
            //判断封装的商品是否属于同一商家
            int sellerIndex = cartList.indexOf(cart);
            if (sellerIndex!=-1){
                //同一商家
                Cart oldCart = cartList.get(sellerIndex);
                List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();
                int orderItemIndex = oldOrderItemList.indexOf(orderItem);
                //判断是否为同一商品
                if (orderItemIndex!=-1){
                    //同一商品,累计数量
                    OrderItem oldOrderItem = oldOrderItemList.get(orderItemIndex);
                    oldOrderItem.setNum(oldOrderItem.getNum()+num);
                }else {
                    //不是同一商品,添加商品
                    oldOrderItemList.add(orderItem);
                }
            }else {
                //不是同一商家
                cartList.add(cart);
            }
            //判断用户是否登录
            if (!"anonymousUser".equals(username)){
                //用户已登录
                cartService.saveCartListToRedis(username,cartList);
                if (flag){
                    Cookie cookie = new Cookie("BUYER-CART",null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }else {
                //用户未登录
                String cookievalue = JSON.toJSONString(cartList);
                String encode = URLEncoder.encode(cookievalue, "utf-8");
                Cookie cookie = new Cookie("BUYER-CART",encode);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24);
                response.addCookie(cookie);
            }

            return new Result(true,"购物车添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"购物车添加失败");
        }
    }

    //回显购物车(未登录)
    @RequestMapping("/findCartList.do")
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        //未登录
        List<Cart> cartList=null;
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if ("BUYER-CART".equals(cookie.getName())){
                    String value = cookie.getValue();
                    String decode = URLDecoder.decode(value, "utf-8");
                    cartList = JSON.parseArray(decode, Cart.class);
                    break;
                }
            }
        }
        //已登录
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(username)){
            //若登陆之前是未登录状态,需要将本地购物车的数据放到缓存中
            if (cartList!=null && cartList.size()>0){
                cartService.saveCartListToRedis(username,cartList);
                Cookie cookie = new Cookie("BUYER-CART",null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            //从缓存中获取到购物车
            cartList=cartService.findCartListFromRedis(username);
        }

        if (cartList!=null && cartList.size()>0){
            cartList = cartService.addItemToCart(cartList);
        }
        return cartList;
    }

}
