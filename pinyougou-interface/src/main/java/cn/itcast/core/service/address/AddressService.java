package cn.itcast.core.service.address;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String username);


    //地址添加
    void add(Address address);

    //地址删除
    void del(Long[] ids);
}
