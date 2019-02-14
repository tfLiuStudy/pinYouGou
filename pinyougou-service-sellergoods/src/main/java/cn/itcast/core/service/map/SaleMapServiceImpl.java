package cn.itcast.core.service.map;

import cn.itcast.core.cartList.ListCat;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.utils.data.FindDates;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 9:51$
 */
@Service
public class SaleMapServiceImpl implements SaleMapService {


    @Autowired
    private OrderDao orderDao;

    /**m
     * 获得销售折线图需要的时间和数据
     */
    @Override
    public Map getDataAndTime(String sellerId, Date start, Date end) throws ParseException {
        //根据指定的时间和所属的商家查询这个商家的订单
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andSellerIdEqualTo("liuzejin");
        criteria.andPaymentTimeBetween(start,end);
        criteria.andStatusEqualTo("2");
        List<Order> orders = orderDao.selectByExample(orderQuery);
        //对数据进行过滤
        getOrder(orders);
        //得到指定日期之间的日期
        List<Date> dateList = FindDates.findDates(start, end);
        //对数据进行分组
        Map<Date, List<Order>> map = ListCat.getMap(orders);
        //从新创建map集合，key是日期，value是当日享受额
        Map<Date,BigDecimal> dataAndTime = new HashMap<>();
        for (Date date : dateList) {
            dataAndTime.put(date,getTotalByDay(map.get(date)));
        }
        //创建一个map集合，用于返回数据
        Map resultMap = new HashMap();
        resultMap.put("time",dateList);
        resultMap.put("data",dataAndTime);
        return resultMap;
    }

    //去除时分秒，只保留年月日
    public void getOrder(List<Order> list) throws ParseException {
        SimpleDateFormat pre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
        if (list!=null&&list.size()>0){
            for (Order order : list) {
                String preDate = pre.format(order.getPaymentTime());
                //字符串截取
                String endStr = preDate.substring(0, 10);
                Date parse = end.parse(endStr);
                order.setPaymentTime(parse);
            }
        }else{
            throw new NullPointerException("指定日期之内没有数据");
        }
    }

    //得到每一天的销售额
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
