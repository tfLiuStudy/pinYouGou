package cn.itcast.core.service.address;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressDao addressDao;

    @Override
    public List<Address> findListByLoginUser(String username) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(username);
        List<Address> addresses = addressDao.selectByExample(addressQuery);
        return addresses;
    }

    //地址管理之地址添加
    @Override
    public void add(Address address) {
        addressDao.insertSelective(address);
    }

    //地址管理之地址删除
    @Transactional
    @Override
    public void del(Long[] ids) {
        if (ids != null && ids.length>0){
            for (Long id : ids) {
                addressDao.deleteByPrimaryKey(id);
            }
        }
    }

}
