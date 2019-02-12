package cn.itcast.core.service.itemcat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemcatServiceImpl implements ItemcatService {
    @Resource
    private ItemCatDao itemCatDao;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 根据parentId查找商品分类信息
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //将模板信息放入到缓存
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        List<ItemCat> itemCatList = itemCatDao.selectByExample(itemCatQuery);
        return itemCatList;
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param itemCat
     */
    @Transactional
    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    /**
     * 修改
     * @param itemCat
     */
    @Override
    public void update(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKey(itemCat);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param itemCat
     * @return
     */
    @Override
    public PageResult search(int page, int rows, ItemCat itemCat) {
        PageHelper.startPage(page,rows);
        Page<ItemCat> p= (Page<ItemCat>) itemCatDao.selectByExample(null);
        return new PageResult(p.getResult(),p.getTotal());
    }

    /**
     * 查找全部分类
     * @return
     */
    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }
}
