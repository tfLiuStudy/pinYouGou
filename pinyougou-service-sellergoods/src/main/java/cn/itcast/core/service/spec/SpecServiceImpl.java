package cn.itcast.core.service.spec;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService {
    @Resource
    private SpecificationDao specificationDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    /**
     * 条件分页查询
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    @Override
    public PageResult search(int page, int rows, Specification specification) {
        PageHelper.startPage(page, rows);

        SpecificationQuery specificationQuery = new SpecificationQuery();

        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();

        if (specification.getSpecName() != null && !"".equals(specification.getSpecName().trim())) {
            criteria.andSpecNameLike("%" + specification.getSpecName().trim() + "%");
        }

        specificationQuery.setOrderByClause("id desc");

        Page<Specification> specpage = (Page<Specification>) specificationDao.selectByExample(specificationQuery);

        return new PageResult(specpage.getResult(), specpage.getTotal());
    }

    /**
     * 新增规格
     *
     * @param specVo
     */
    @Transactional
    @Override
    public void add(SpecVo specVo) {
//        保存规格
        Specification specification = specVo.getSpecification();
//          返回自增主键id给specification对象中
        specificationDao.insertSelective(specification);

        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();

        if (specificationOptionList != null && specificationOptionList.size() > 0) {

            for (SpecificationOption specificationOption : specificationOptionList) {
                //给specificationOption的外键设置id
                specificationOption.setSpecId(specification.getId());

//                specificationOptionDao.insertSelective(specificationOption);
            }
            //批量插入specificationoption数据
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    /**
     * 修改数据回显
     *
     * @param id
     * @return
     */
    @Override
    public SpecVo findOne(Long id) {
//       根据id查询规格对象
        Specification specification = specificationDao.selectByPrimaryKey(id);
//       创建规格项条件查询对象
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
//        设置查询条件
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);

        List<SpecificationOption> specificationOptionList = specificationOptionDao.selectByExample(specificationOptionQuery);

        SpecVo specVo = new SpecVo();

        specVo.setSpecification(specification);

        specVo.setSpecificationOptionList(specificationOptionList);

        return specVo;
    }

    /**
     * 规格及规格项更新
     * @param specVo
     */
    @Transactional
    @Override
    public void update(SpecVo specVo) {
        //获取规格对象
        Specification specification = specVo.getSpecification();
        //修改规格内容
        specificationDao.updateByPrimaryKeySelective(specification);
        //获得一个规格项查询对象
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        //为规格项查询对象设置一个id=?的查询条件
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(specification.getId());
        //根据id值删除所有规格项
        specificationOptionDao.deleteByExample(specificationOptionQuery);

        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();

        if (specificationOptionList!=null && specificationOptionList.size()>0){

            for (SpecificationOption specificationOption : specificationOptionList) {
                //为规格项设置specid
                specificationOption.setSpecId(specification.getId());
            }
            //批量插入规格项数据
            specificationOptionDao.insertSelectives(specificationOptionList);
        }

    }

    /**
     * 删除规格及规格项
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids!=null && ids.length>0){

            for (Long id : ids) {

                SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();

                specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);

                specificationOptionDao.deleteByExample(specificationOptionQuery);

                specificationDao.deleteByPrimaryKey(id);

            }
//            specificationOptionDao.deleteByPrimaryKeys(ids);
        }
    }

    /**
     * 新增规格列表初始化
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
