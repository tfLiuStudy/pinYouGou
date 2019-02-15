package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.address.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;
@RequestMapping("/add.do")
    public Result add(@RequestBody Address address){
    try {
        addressService.add(address);
        return new Result(true,"添加成功");
    } catch (Exception e) {
        return new Result(false,"添加失败");
    }
}
@RequestMapping("/del.do")
    public Result del(Long[] ids){
    try {
        addressService.del(ids);
        return new Result(true,"删除失败");
    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false,"删除失败");
    }
}

}
