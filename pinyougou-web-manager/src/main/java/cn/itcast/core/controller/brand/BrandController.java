package cn.itcast.core.controller.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.brand.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询全部品牌
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Brand> findAll(){
        List<Brand> brandList = brandService.findAll();
        System.out.println("1");//测试git提交
        return brandList;
    }

    /**
     * 分页查询全部品牌
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/findByPage.do")
    public PageResult findByPage(int pageNo,int pageSize){
        PageResult pageResult = brandService.findByPage(pageNo, pageSize);
        return pageResult;
    }

    /**
     * 分页条件查询品牌
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @RequestMapping("/searchByPage.do")
    public PageResult searchByPage(int pageNo,int pageSize,@RequestBody Brand brand){
        PageResult pageResult = brandService.searchByPage(pageNo, pageSize,brand);
        return pageResult;
    }

    /**
     * 新建品牌数据
     * @param brand
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /**
     * 修改品牌信息回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public Brand findOne(Long id){
        Brand brand = brandService.findOne(id);
        return brand;
    }

    /**
     * 进行数据修改
     * @param brand
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
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
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }


    /**
     * 新增品牌列表初始化
     * @return
     */
    @RequestMapping("/selectOptionList.do")
    public List<Map<String,String>> selectOptionList(){
        return brandService.selectOptionList();
    }


    /**m
     * 像数据库中添加商品列表
     * @param brandList  list集合
     * @return
     */
    @RequestMapping("/addBrands.do")
    public Result addBrands(@RequestBody List<Brand> brandList){
        try {
            brandService.addList(brandList);
            return new Result(true,"成功");
        }catch (Exception e){
            return new Result(false,"失败");
        }
    }



}
