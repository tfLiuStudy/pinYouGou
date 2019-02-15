package cn.itcast.core.controller.user;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.export.OrderExport;
import cn.itcast.core.service.export.Export;
import cn.itcast.core.service.user.UserManagerService;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/1/30$ 20:09$
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserManagerService userManagerService;


    @Autowired
    private OrderExport orderExport;

    /**m
     * 查询所有的订单
     */
    @RequestMapping("/search.do")
    public PageResult search(Long page, Long rows) throws Exception {
        try {
            PageResult pageResoult = userManagerService.searchOrders(page, rows);
            return pageResoult;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value="/downLoad.do",produces="application/octet-stream;charset=UTF-8")
    public ResponseEntity<byte[]> download() throws IOException {
        Workbook workBook = orderExport.getWorkBook();
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
