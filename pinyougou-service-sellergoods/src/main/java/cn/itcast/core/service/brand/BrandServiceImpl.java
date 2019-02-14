package cn.itcast.core.service.brand;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    //    @Autowired
    @Resource
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        List<Brand> brandList = brandDao.selectByExample(null);
        return brandList;
    }

    /**
     * 使用分页助手完成品牌分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findByPage(int pageNo, int pageSize) {
//        设置分页条件
        PageHelper.startPage(pageNo, pageSize);
//        根据分页条件查询,分页助手已经完成分页sql
        List<Brand> brands = brandDao.selectByExample(null);
//         page实现了arraylist
        Page<Brand> page = (Page<Brand>) brands;

        return new PageResult(page.getResult(), page.getTotal());
    }

    /**
     * 使用分页助手实现条件分页查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult searchByPage(int pageNo, int pageSize, Brand brand) {
        PageHelper.startPage(pageNo,pageSize);

        BrandQuery brandQuery = new BrandQuery();

        BrandQuery.Criteria criteria = brandQuery.createCriteria();

        if (brand.getName()!=null && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%"+brand.getName()+"%");
        }

        if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar().trim())){
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }

        brandQuery.setOrderByClause("id desc");

        Page<Brand> page= (Page<Brand>) brandDao.selectByExample(brandQuery);

        return new PageResult(page.getResult(),page.getTotal());
    }

    /**
     * 保存品牌信息
     * @param brand
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * 修改数据回显
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * 完成数据修改
     * @param brand
     */
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * 批量修改
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids!=null && ids.length>0){
//            for (Long id : ids) {
//                brandDao.deleteByPrimaryKey(id);
//            }
            brandDao.deleteByPrimaryKeys(ids);
        }
    }

    /**
     * 商品模板新增品牌列表初始化
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandDao.selectOptionList();
    }

    /**m
     * 像数据库中添加商品列表
     * @param brandList  list集合
     * @return
     */
    @Override
    public void addList(List<Brand> brandList) {
        if (brandList!=null&&brandList.size()>0){
            for (Brand brand : brandList) {
                brandDao.insertSelective(brand);
            }
        }
    }
}
