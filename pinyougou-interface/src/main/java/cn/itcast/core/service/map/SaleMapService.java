package cn.itcast.core.service.map;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 9:50$
 */
public interface SaleMapService {

    /**m
     * 获得销售折线图需要的时间和数据
     *
     * @param s
     * @param   start  开始时间
     * @param   end    结束时间
     * @return   map集合,一个属性是  data，一个属性是time
     */
    Map getDataAndTime(String sellerId, Date start, Date end) throws ParseException;
}
