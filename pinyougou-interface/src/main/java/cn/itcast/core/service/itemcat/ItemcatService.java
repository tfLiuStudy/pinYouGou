package cn.itcast.core.service.itemcat;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemcatService {

    List<ItemCat> findByParentId(Long parentId);

    ItemCat findOne(Long id);

    void add(ItemCat itemCat);

    void update(ItemCat itemCat);

    PageResult search(int page,int rows,ItemCat itemCat);

    List<ItemCat> findAll();

}
