package cn.itcast.core.controller.itemcat;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemcat.ItemcatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemcatController {

    @Reference
    private ItemcatService itemcatService;

    /**
     * 根据parentId查询分类列表数据
     * @param parentId
     * @return
     */
    @RequestMapping("/findByParentId.do")
    public List<ItemCat> findByParentId(Long parentId){
        return itemcatService.findByParentId(parentId);
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public ItemCat findOne(Long id){
        return itemcatService.findOne(id);
    }

    /**
     * 添加分类
     * @param itemCat
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody ItemCat itemCat){
        try {
            itemcatService.add(itemCat);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败 ");
        }
    }

    /**
     *更新操作
     * @param itemCat
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody ItemCat itemCat){
        try {
            itemcatService.update(itemCat);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败 ");
        }
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param itemCat
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(int page,int rows,@RequestBody ItemCat itemCat){

        return itemcatService.search(page,rows,itemCat);
    }

    /**
     * 查找全部分类
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<ItemCat> findAll(){
        return itemcatService.findAll();
    }



}
