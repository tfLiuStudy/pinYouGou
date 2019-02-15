package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.export.GoodsExport;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Autowired
    private GoodsExport goodsExport;

    /**
     * 运营商查询商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(int page, int rows, @RequestBody Goods goods){
        return goodsService.searchForManager(page,rows,goods);
    }

    /**
     * 运营商审核商品
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids,String status){
        try {
            goodsService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }

    /**
     * 对商品进行逻辑删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
        try {
            goodsService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/searchByMarket.do")
    public PageResult searchByMarket(Integer page,Integer rows){
        return goodsService.searchByMarket(page,rows);
    }

    @RequestMapping(value="/downLoad.do",produces="application/octet-stream;charset=UTF-8")
    public ResponseEntity<byte[]> download() throws IOException {
        Workbook workBook = goodsExport.getWorkBook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workBook.write(out);
        HttpHeaders headers = new HttpHeaders();
        String fileName=new String("品优购订单.xls".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        //headers.add("Content-Type", "application/octet-stream; charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);//二进制流
        return new ResponseEntity<byte[]>(out.toByteArray(),
                headers, HttpStatus.OK);
    }
}
