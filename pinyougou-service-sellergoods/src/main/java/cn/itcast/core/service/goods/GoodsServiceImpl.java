package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.itemSearch.ItemSearchService;
import cn.itcast.core.vo.GoodsVo;
import cn.itcast.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.tools.javac.comp.Todo;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsDescDao goodsDescDao;
    @Resource
    private ItemDao itemDao;
    @Resource
    private ItemCatDao itemCatDao;
    @Resource
    private BrandDao brandDao;
    @Resource
    private SellerDao sellerDao;
    @Resource
    private SolrTemplate solrTemplate;
    @Resource
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicPageAndSolrDestination;
    @Resource
    private Destination queueSolrDeleteDestination;

    /**
     * 商品添加
     *
     * @param goodsVo
     */
    @Transactional
    @Override
    public void add(GoodsVo goodsVo) {
        //保存商品信息
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");
        goodsDao.insertSelective(goods);
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        //保存商品明细
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);
        //保存商品库存
        if ("1".equals(goods.getIsEnableSpec())) {
            //启用规格,一个spu对应多个sku
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    String spec = item.getSpec();
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    //保存标题
                    item.setTitle(title);
                    setItemAttribute(goods, goodsDesc, item);
                    itemDao.insertSelective(item);
                }
            } else {
                //不启用规格,一个spu对应一个sku
                Item item = new Item();
                String title = goods.getGoodsName() + " " + goods.getCaption();
                item.setPrice(goods.getPrice());
                item.setNum(666);
                item.setStatus("1");
                item.setIsDefault("1");
                item.setSpec("{}");
                setItemAttribute(goods, goodsDesc, item);
                itemDao.insertSelective(item);
            }
        }
    }

    /**
     * 商家商品查找
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult searchForShop(int page, int rows, Goods goods) {
        PageHelper.startPage(page, rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        goodsQuery.createCriteria().andSellerIdEqualTo(goods.getSellerId());
        Page<Goods> goodsPage = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(goodsPage.getResult(), goodsPage.getTotal());
    }

    /**
     * 回显
     *
     * @param id
     * @return
     */
    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        Goods goods = goodsDao.selectByPrimaryKey(id);
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        goodsVo.setGoods(goods);
        goodsVo.setGoodsDesc(goodsDesc);
        goodsVo.setItemList(itemList);
        return goodsVo;
    }

    /**
     * 更新商品
     *
     * @param goodsVo
     */
    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        Goods goods = goodsVo.getGoods();
        goodsDao.updateByPrimaryKeySelective(goods);
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        //先删除规格以及规格项
        itemDao.deleteByExample(itemQuery);
        if ("1".equals(goods.getIsEnableSpec())) {
            //启用规格,一个spu对应多个sku
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    String spec = item.getSpec();
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    //保存标题
                    item.setTitle(title);
                    setItemAttribute(goods, goodsDesc, item);
                    itemDao.insertSelective(item);
                }
            } else {
                //不启用规格,一个spu对应一个sku
                Item item = new Item();
                String title = goods.getGoodsName() + " " + goods.getCaption();
                item.setPrice(goods.getPrice());
                item.setNum(9999);
                item.setStatus("1");
                item.setIsDefault("1");
                item.setSpec("{}");
                setItemAttribute(goods, goodsDesc, item);
                itemDao.insertSelective(item);
            }
        }
    }

    /**
     * 运营商查询商品列表
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult searchForManager(int page, int rows, Goods goods) {
        PageHelper.startPage(page, rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        goodsQuery.setOrderByClause("id desc");
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus().trim())) {
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());
        }
        criteria.andIsDeleteIsNull();
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(p.getResult(), p.getTotal());
    }

    /**
     * 运营商审核商品
     *
     * @param ids
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null && ids.length > 0) {
            Goods goods = new Goods();
            goods.setAuditStatus(status);
            for (final Long id : ids) {
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
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

    //商品审核完成后更新索引库
    private void updateItemToSolr(Long id) {
        ItemQuery itemQuery = new ItemQuery();
        //设置条件,上架+默认+刚审核的商品
        itemQuery.createCriteria().andStatusEqualTo("1").andIsDefaultEqualTo("1").andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        if (itemList != null && itemList.size() > 0) {
            for (Item item : itemList) {
                String spec = item.getSpec();
                Map map = JSON.parseObject(spec, Map.class);
                item.setSpecMap(map);
            }
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();
        }
    }

    //将库存数据保存至索引库
    private void saveItemDataToSolr() {
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andStatusEqualTo("1");
        List<Item> items = itemDao.selectByExample(itemQuery);
        if (items != null && items.size() > 0) {
            for (Item item : items) {
                String spec = item.getSpec();
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                item.setSpecMap(map);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    /**
     * 批量逻辑删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0) {
            Goods goods = new Goods();
            goods.setIsDelete("1");
            for (final Long id : ids) {
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
                //删除后更新索引库
                //消息生产者
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                        return textMessage;
                    }
                });
                //删除静态页面
            }
        }
    }

    //抽取
    public void setItemAttribute(Goods goods, GoodsDesc goodsDesc, Item item) {
        //[{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"}
        // ,{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]
        String itemImages = goodsDesc.getItemImages();
        List<Map> images = JSON.parseArray(itemImages, Map.class);
        if (images != null && images.size() > 0) {
            String url = images.get(0).get("url").toString();
            item.setImage(url);
        }
        item.setCategoryid(goods.getCategory3Id());
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setGoodsId(goods.getId());
        item.setSellerId(goods.getSellerId());
        item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
        item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
        item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getNickName());
    }
}
