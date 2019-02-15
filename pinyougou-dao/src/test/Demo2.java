import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.utils.data.FindDates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensaml.ws.wssecurity.impl.SaltImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 11:04$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/*.xml")
public class Demo2 {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void test2(){
        String sellerID = "liuzejin";
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerID);
        List<Order> orders = orderDao.selectByExample(orderQuery);
        orders.forEach(System.out::println);
    }

    @Test
    public  void test1() throws ParseException {
        //2019-01-22
        //2019-02-12
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andSellerIdEqualTo("liuzejin");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = simpleDateFormat.parse("2019-01-22 00:00:00");
        Date end = simpleDateFormat.parse("2019-02-12 23:59:59");
        criteria.andPaymentTimeBetween(start,end);
        criteria.andStatusEqualTo("2");
        List<Order> orders = orderDao.selectByExample(orderQuery);
        orders.forEach(System.out::println);
        //----------------分组
        System.out.println("====================");
        //数据整合
        List<Order> orderList = getOrder(orders);
        orderList.forEach(System.out::println);

        //对数据进行分组
        Map<Date, List<Order>> map = ListCat.getMap(orderList);
        //遍历分组
        System.out.println("------遍历分组--------");
        Set<Map.Entry<Date, List<Order>>> entries = map.entrySet();
        for (Map.Entry<Date, List<Order>> entry : entries) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
        System.out.println("========得到有用的数据");
        //天数
        List<Date> dateList = FindDates.findDates(start, end);
        dateList.forEach(System.out::println);
        System.out.println("--------数据合并");
        //合并数据
        Map<Date,BigDecimal> dataAndTime = new HashMap<>();
        for (Date date : dateList) {
            dataAndTime.put(date,getTotalByDay(map.get(date)));
        }
        dataAndTime.forEach((t,v)-> System.out.println(t+"="+v));

    }
    public List<Order> getOrder(List<Order> list) throws ParseException {
        SimpleDateFormat pre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
        List<Order> newList = new ArrayList<>();
        for (Order order : list) {
            String preDate = pre.format(order.getPaymentTime());
            //字符串截取
            String endStr = preDate.substring(0, 10);
            Date parse = end.parse(endStr);
            order.setPaymentTime(parse);
            newList.add(order);
        }
        return newList;
    }

    //得到每一天的钱数
    public BigDecimal getTotalByDay(List<Order> orderList){
        BigDecimal total = new BigDecimal(0);
        if (orderList!=null){
            for (Order order : orderList) {
                total = total.add(order.getPayment());
            }
            return total;
        }
        return total;
    }

}
