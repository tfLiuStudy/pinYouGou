package cn.itcast.core.controller.seller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.seller.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * 商家注册
     * @param seller
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Seller seller){
        try {
            sellerService.add(seller);
            return new Result(true,"提交注册申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"提交注册申请失败");
        }
    }


}
