package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 17:28$
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Override
    public PageResult search(Long page, Long rows, String sellerId) {
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = seckillOrderQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerId);
        PageHelper.startPage(page.intValue(),rows.intValue());
        Page pageList = (Page) seckillOrderDao.selectByExample(seckillOrderQuery);
        return new PageResult(pageList.getResult(),pageList.getTotal());
    }
}
