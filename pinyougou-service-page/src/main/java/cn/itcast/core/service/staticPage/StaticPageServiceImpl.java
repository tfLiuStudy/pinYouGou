package cn.itcast.core.service.staticPage;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//使用配置文件交给spring容器管理
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsDescDao goodsDescDao;
    @Resource
    private ItemCatDao itemCatDao;
    @Resource
    private ItemDao itemDao;

    private Configuration configuration;

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }
    //注入servletContext
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }

    @Override
    public void getPage(Long id) {

        try {
            //1.创建configuration对象,指定模板位置
//        Configuration configuration=new Configuration();    //硬编码
            //2.获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //3.准备数据
            Map<String, Object> dataModel = new HashMap<>();
            dataModel=getDataModel(id);
            String pathname="/"+id+".html";
            String realPath=servletContext.getRealPath(pathname);
            //4.输出静态页面
            File file = new File(realPath);
            Writer out=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            template.process(dataModel,out);
//            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取数据
    private Map<String,Object> getDataModel(Long id) {
        Map<String, Object> dataModel = new HashMap<>();
        //商品数据查询
        Goods goods = goodsDao.selectByPrimaryKey(id);
        dataModel.put("goods",goods);
        //商品描述数据查询
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        dataModel.put("goodsDesc",goodsDesc);
        //分类数据查询
        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());
        ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());
        ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
        dataModel.put("itemCat1",itemCat1);
        dataModel.put("itemCat2",itemCat2);
        dataModel.put("itemCat3",itemCat3);
        //库存数据查询
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1").andNumGreaterThan(0);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        dataModel.put("itemList",itemList);
        return dataModel;
    }



}
