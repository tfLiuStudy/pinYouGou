package cn.itcast.core.controller.map;

import cn.itcast.core.service.map.SaleMapService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.opensaml.ws.wssecurity.impl.SecurityUnmarshaller;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 9:47$
 */
@RestController
@RequestMapping("/map")
public class SaleMapController {

    @Reference
    private SaleMapService saleMapService;

    /**m
     * 获得销售折线图需要的时间和数据
     * @param   start  开始时间
     * @param   end    结束时间
     */
    @RequestMapping("/saleslineMap.do")
    public Map getDataAndTime(String start, String end) throws Exception {
        //得到当前已经登陆的商家的Id
        Map map = new HashMap();
        try{

            String startVal = start.split(" G")[0];
            String endVal = end.split(" G")[0];
            Date startDate = new Date(startVal);
            Date endDate = new Date(endVal);
            //判断是否合法
            isTrue(startDate,endDate);
            String seller = SecurityContextHolder.getContext().getAuthentication().getName();
            map.put("flag","SUCCESS");
            map = saleMapService.getDataAndTime(seller,startDate,endDate);
            return map;
        }catch (Exception e){
            map.put("flag","FALSE");
            map.put("msg",e.getMessage());
            return map;
        }
    }

    private void isTrue(Date startDate, Date endDate) throws Exception {
        int res = startDate.compareTo(endDate);//返回0相等，返回一大于，返回-1小于
        if (res!=-1){
            throw new Exception("时间非法，从新输入");
        }
    }
}
