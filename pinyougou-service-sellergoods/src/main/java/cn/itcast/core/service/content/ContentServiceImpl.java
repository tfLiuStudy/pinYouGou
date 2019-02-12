package cn.itcast.core.service.content;

import java.util.List;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.ContentQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.pojo.ad.Content;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentDao contentDao;

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	@Override
	public List<Content> findAll() {
		List<Content> list = contentDao.selectByExample(null);
		return list;
	}

	@Override
	public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<Content> page = (Page<Content>)contentDao.selectByExample(null);
		return new PageResult(page.getResult(),page.getTotal());
	}

    /**
     * 添加,先清除缓存
     * @param content
     */
    @Transactional
	@Override
	public void add(Content content) {
	    clearCache(content.getCategoryId());
		contentDao.insertSelective(content);
	}

    /**
     * 修改,先清除缓存
     * @param content
     */
    @Transactional
	@Override
	public void edit(Content content) {
        Long newCategoryId = content.getCategoryId();
        Long oldCategoryId = contentDao.selectByPrimaryKey(content.getId()).getCategoryId();
        if (newCategoryId!=oldCategoryId){
            clearCache(newCategoryId);
            clearCache(oldCategoryId);
        }
        contentDao.updateByPrimaryKeySelective(content);
	}

	@Override
	public Content findOne(Long id) {
		Content content = contentDao.selectByPrimaryKey(id);
		return content;
	}

    /**
     * 删除,先清除缓存
     * @param ids
     */
    @Transactional
	@Override
	public void delAll(Long[] ids) {
		if(ids != null){
			for(Long id : ids){
                Content content = contentDao.selectByPrimaryKey(id);
                clearCache(content.getCategoryId());
				contentDao.deleteByPrimaryKey(id);
			}
		}
	}

    /**
     * 清理缓存
     * @param categoryId
     */
    private void clearCache(Long categoryId) {
	    redisTemplate.boundHashOps("content").delete(categoryId);
    }

    /**
	 * 查找满足条件的广告项
	 * @param categoryId
	 * @return
	 */
    @Override
    public List<Content> findByCategoryId(Long categoryId) {
    	List<Content> list= (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
    	synchronized (this){
    	    list= (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
            if (list==null){
                ContentQuery contentQuery = new ContentQuery();
                contentQuery.setOrderByClause("sort_order desc");
                contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
                list = contentDao.selectByExample(contentQuery);
                redisTemplate.boundHashOps("content").put(categoryId,list);
            }
        }
        return list;
    }


}
