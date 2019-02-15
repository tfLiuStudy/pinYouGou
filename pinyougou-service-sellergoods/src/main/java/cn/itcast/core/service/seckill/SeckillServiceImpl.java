package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 13:39$
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public List<Item> findAllSKUBySeller(String sellerId) {
        //查询这个商家所有的已经上架的商品
        Goods goods = new Goods();
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerId);
        criteria.andAuditStatusEqualTo("1");  //1是上架
        //查询商品
        List<Goods> goodsList = goodsDao.selectByExample(goodsQuery);

        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria1 = itemQuery.createCriteria();
        criteria1.andStatusEqualTo("1");
        List<Item> resultList = new ArrayList<>();
        for (Goods goods1 : goodsList) {
            criteria1.andGoodsIdEqualTo(goods1.getId());
            List<Item> itemList = itemDao.selectByExample(itemQuery);
            resultList.addAll(itemList);
        }
        return resultList;
    }

    @Override
    public Item findOne(Long id) {
        Item item = itemDao.selectByPrimaryKey(id);
        return item;
    }

    @Transactional
    @Override
    public void commitSeckill(SeckillGoods seckillGoods) {
        if (seckillGoods!=null){
            seckillGoods.setCreateTime(new Date());  //添加日期
            seckillGoods.setStatus("0");             //初始化审核状态
            seckillGoodsDao.insertSelective(seckillGoods);
        }
    }
}
