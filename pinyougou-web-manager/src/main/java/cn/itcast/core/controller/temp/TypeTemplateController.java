package cn.itcast.core.controller.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.temp.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 商品模板多条件查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(int page, int rows, @RequestBody TypeTemplate typeTemplate){
        return typeTemplateService.search(page,rows,typeTemplate);
    }

    /**
     * 添加商品模板
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /**
     * 修改--回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public TypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     * 修改数据
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TypeTemplate typeTemplate){

        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            typeTemplateService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    /**
     * 全查
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<TypeTemplate> findAll(){
        return typeTemplateService.findAll();
    }

}
