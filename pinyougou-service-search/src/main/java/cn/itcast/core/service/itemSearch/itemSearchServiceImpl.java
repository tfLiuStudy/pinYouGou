package cn.itcast.core.service.itemSearch;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import javax.annotation.Resource;
import java.util.*;

@Service
public class itemSearchServiceImpl implements ItemSearchService {

    @Resource
    private SolrTemplate solrTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ItemDao itemDao;

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();
        //处理检索的关键字
        String keywords = searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            keywords = keywords.replace(" ", "");
            searchMap.put("keywords", keywords);
        }

//        Map<String,Object> map=searchForPage(searchMap);
        //检索关键字高亮显示
        Map<String, Object> map = searchForHighlightPage(searchMap);
        resultMap.putAll(map);
        //获取商品的分类
        List<String> categoryList = searchForGroupPage(searchMap);
        if (categoryList != null && categoryList.size() > 0) {
            resultMap.put("categoryList", categoryList);
            //获取品牌结果集和规格结果集
            Map<String, Object> brandAndSpecMap = searchBrandListAndSpecListByCategoryName(categoryList.get(0));
            resultMap.putAll(brandAndSpecMap);
        }
        return resultMap;
    }

    //商品审核完成后更新索引库
    @Override
    public void updateItemToSolr(Long id) {
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

    //商品删除时更新索引
    @Override
    public void deleteItemToSolr(long id) {
        SimpleQuery query = new SimpleQuery("item_goodsid:"+id);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    //获取品牌结果集和规格结果集
    private Map<String, Object> searchBrandListAndSpecListByCategoryName(String categoryName) {
        Map<String, Object> map = new HashMap<>();
        Object typeId = redisTemplate.boundHashOps("itemCat").get(categoryName);
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    //获取分类列表
    private List<String> searchForGroupPage(Map<String, String> searchMap) {
        List<String> categoryList = new ArrayList<>();
        String keywords = searchMap.get("keywords");
        Criteria criteria = new Criteria("item_keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");      //根据类型item_category进行分组
        query.setGroupOptions(groupOptions);
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);  //可参考debug

        //将分类结果封装到list中
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();     //获得分类名称
            categoryList.add(groupValue);
        }
        return categoryList;
    }

    //高亮显示检索的关键字
    private Map<String, Object> searchForHighlightPage(Map<String, String> searchMap) {
        //1 - 封装检索关键字
        String keywords = searchMap.get("keywords");
        Criteria criteria = new Criteria("item_keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);

        //2 - 封装根据排序条件sort,sortField(排序字段)
        if (searchMap.get("sort") != null && !"".equals(searchMap.get("sort"))) {
            if ("ASC".equals(searchMap.get("sort"))) {
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + searchMap.get("sortField"));
                query.addSort(sort);
            } else {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + searchMap.get("sortField"));
                query.addSort(sort);
            }
        }

        //3 - 封装分页条件
        int pageNo = Integer.parseInt(searchMap.get("pageNo"));
        int pageSize = Integer.parseInt(searchMap.get("pageSize"));
        int offset = (pageNo - 1) * pageSize;
        query.setOffset(offset);    //起始行
        query.setRows(pageSize);    //每页显示行数
        HighlightOptions highlightOptions = new HighlightOptions();

        //设置检索关键词高亮显示
        //4 - 封装高亮条件
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        highlightOptions.addField("item_title");
        query.setHighlightOptions(highlightOptions);

        //5 - 前台系统根据条件进行筛选
        //根据分类条件筛选
        String category = searchMap.get("category");
        if (category != null && !"".equals(category)) {
            Criteria cri = new Criteria("item_category");
            cri.is(category);
            SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(simpleFilterQuery);
        }

        //6 - 根据品牌条件进行筛选
        String brand = searchMap.get("brand");
        if (brand != null && !"".equals(brand)) {
            Criteria cri = new Criteria("item_brand");
            cri.is(brand);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery();
            query.addFilterQuery(filterQuery);
        }

        //7 - 根据规格以及规格项进行筛选
        String spec = searchMap.get("spec");
        if (spec != null && !"".equals(spec)) {
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                Criteria cri = new Criteria("item_spec_" + entry.getKey());
                criteria.is(entry.getValue());
                SimpleFilterQuery filterQuery = new SimpleFilterQuery();
                query.addFilterQuery(filterQuery);
            }
        }

        //8 - 根据价格进行筛选
        String price = searchMap.get("price");
        if (price!=null && !"".equals(price)){
            String[] prices = price.split("-");
            if (price.contains("*")){
                Criteria cri = new Criteria();
                cri.greaterThanEqual(Long.parseLong(prices[0]));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
                query.addFilterQuery(filterQuery);
            }else {
                Criteria cri = new Criteria("item_price");
                cri.between(Long.parseLong(prices[0]),Long.parseLong(prices[1]),true,true);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
                query.addFilterQuery(filterQuery);
            }
        }

        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);     //可参考debug

        //获取高亮结果,并且将高亮结果重新赋值给item_title属性
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        if (highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
                Item item = itemHighlightEntry.getEntity();         //获取普通item_title
                List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
                if (highlights != null && highlights.size() > 0) {
                    String newtotal = highlights.get(0).getSnipplets().get(0);      //获取高亮item_title
                    item.setTitle(newtotal);        //重新将高亮item_title设置到pojo中
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", highlightPage.getTotalElements());
        map.put("rows", highlightPage.getContent());
        map.put("totalPages", highlightPage.getTotalPages());
        return map;
    }

    private Map<String, Object> searchForPage(Map<String, String> searchMap) {
        //封装检索的关键字
        String keywords = searchMap.get("keywords");
        Criteria criteria = new Criteria("item_keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);

        //封装分页条件
        int pageNo = Integer.parseInt(searchMap.get("pageNo"));
        int pageSize = Integer.parseInt(searchMap.get("pageSize"));
        int offset = (pageNo - 1) * pageSize;
        query.setOffset(offset);            //起始行
        query.setRows(pageSize);            //每页显示条数

        //根据条件进行检索
        ScoredPage<Item> scoredPage = solrTemplate.queryForPage(query, Item.class);
        Map<String, Object> map = new HashMap<>();
        map.put("total", scoredPage.getTotalElements());
        map.put("rows", scoredPage.getContent());
        map.put("totalPages", scoredPage.getTotalPages());
        return map;
    }
}
