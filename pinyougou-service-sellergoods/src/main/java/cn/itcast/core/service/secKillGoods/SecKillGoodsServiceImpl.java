package cn.itcast.core.service.secKillGoods;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;

@Service
public class SecKillGoodsServiceImpl implements SecKillGoodsService {

    @Resource
    private SeckillGoodsDao seckillGoodsDao;
    @Resource
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicPageAndSolrDestination;
    @Resource
    private Destination queueSolrDeleteDestination;


    @Override
    public PageResult search(int page, int rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page, rows);
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        seckillGoodsQuery.setOrderByClause("id desc");
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        if (seckillGoods.getStatus() != null && !"".equals(seckillGoods.getStatus().trim())) {
            criteria.andStatusEqualTo(seckillGoods.getStatus().trim());
        }

        Page<SeckillGoods> p = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(seckillGoodsQuery);
        return new PageResult(p.getResult(), p.getTotal());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null && ids.length > 0) {
            SeckillGoods seckillGoods = new SeckillGoods();
            seckillGoods.setStatus(status);
            for (final Long id : ids) {
                seckillGoods.setId(id);
                seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
                if ("1".equals(status)) {
                    //商品上架
//                    updateItemToSolr(id);
//                    saveItemDataToSolr();
                    //商品静态页面生成
//                    staticPageService.getPage(id);
                    //MQ生产者
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });
                }
            }
        }
    }
}
