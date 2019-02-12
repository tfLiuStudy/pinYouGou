package cn.itcast.core.service.temp;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Resource
    private TypeTemplateDao typeTemplateDao;
    @Resource
    private SpecificationOptionDao specificationOptionDao;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 商品模板分页查询
     *
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @Override
    public PageResult search(int page, int rows, TypeTemplate typeTemplate) {
        //将商品品牌列表和规格以及规格项放入缓存
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
        PageHelper.startPage(page, rows);
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        if (typeTemplate.getName() != null && !"".equals(typeTemplate.getName().trim())) {
            criteria.andNameLike("%" + typeTemplate.getName() + "%");
        }
        typeTemplateQuery.setOrderByClause("id desc");
        Page<TypeTemplate> templatePage = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        return new PageResult(templatePage.getResult(), templatePage.getTotal());
    }

    /**
     * 商品模板添加
     *
     * @param typeTemplate
     */
    @Transactional
    @Override
    public void add(TypeTemplate typeTemplate) {

        typeTemplateDao.insertSelective(typeTemplate);

    }

    /**
     * 修改--回显
     *
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    /**
     * 更新数据
     *
     * @param typeTemplate
     */
    @Transactional
    @Override
    public void update(TypeTemplate typeTemplate) {

        typeTemplateDao.deleteByPrimaryKey(typeTemplate.getId());

        typeTemplateDao.insertSelective(typeTemplate);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {

        if (ids != null && ids.length > 0) {

            typeTemplateDao.deleteByPrimaryKeys(ids);

        }
    }

    /**
     * 全查
     *
     * @return
     */
    @Override
    public List<TypeTemplate> findAll() {
        return typeTemplateDao.selectByExample(null);
    }

    /**
     * 查询指定模板的所有规格及其规格项
     *
     * @param id
     * @return
     */
    @Override
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
