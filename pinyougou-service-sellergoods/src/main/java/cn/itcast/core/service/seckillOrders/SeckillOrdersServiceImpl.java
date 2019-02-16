package cn.itcast.core.service.seckillOrders;

import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SeckillOrdersServiceImpl implements SeckillOrdersService {

    @Resource
    private SeckillOrderDao seckillOrderDao;

    @Override
    public PageResult search(int page, int rows, SeckillOrder seckillOrder) {
        PageHelper.startPage(page,rows);
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        seckillOrderQuery.setOrderByClause("id desc");
        SeckillOrderQuery.Criteria criteria = seckillOrderQuery.createCriteria();
        if (seckillOrder.getStatus()!=null &&!"".equals(seckillOrder.getStatus().trim())){
            criteria.andStatusEqualTo(seckillOrder.getStatus().trim());
        }
        Page<SeckillOrder> p = (Page<SeckillOrder>) seckillOrderDao.selectByExample(seckillOrderQuery);
        return new PageResult(p.getResult(),p.getTotal());
    }
}
