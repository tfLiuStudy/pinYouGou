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

    @Override
    public Map getDataAndTime(String sellerId, Date start, Date end) throws ParseException {
        return null;
    }


}
