package cn.itcast.core.task;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RedisTask {

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    //将分类数据数据加载至缓存
    @Scheduled(cron = "00 07 09 14 1 * ")
    public void autoItemCatToRedis(){
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        if (itemCats!=null && itemCats.size()>0){
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
            }
        }
    }

    //将模板数据数据加载至缓存
    @Scheduled(cron = "00 07 09 14 1 *")
    public void autoTypeToRedis(){
        List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
        if (typeTemplates!=null && typeTemplates.size()>0){
            for (TypeTemplate template : typeTemplates) {
                List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
                //品牌列表放入缓存
                redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);
                List<Map> specList = findBySpecList(template.getId());
                //规格以及规格项放入缓存
                redisTemplate.boundHashOps("specList").put(template.getId(),specList);
            }
        }
    }


    public List<Map> findBySpecList(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        String specIds = typeTemplate.getSpecIds();
        List<Map> specList = JSON.parseArray(specIds, Map.class);
        if (specList != null && specList.size() > 0) {
            for (Map map : specList) {
                long specid = Long.parseLong(map.get("id").toString());
                SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
                specificationOptionQuery.createCriteria().andSpecIdEqualTo(specid);
                List<SpecificationOption> options = specificationOptionDao.selectByExample(specificationOptionQuery);
                map.put("options",options);
            }
        }
        return specList;
    }


}
