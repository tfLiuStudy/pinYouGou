package cn.itcast.core.service.address;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String username);
}
