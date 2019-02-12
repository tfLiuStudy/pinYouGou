package cn.itcast.core.service.seller;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDao sellerDao;

    /**
     * 商户注册
     * @param seller
     */
    @Transactional
    @Override
    public void add(Seller seller) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        seller.setCreateTime(new Date());
        seller.setStatus("0");
        sellerDao.insertSelective(seller);
    }

    /**
     * 查询未审核用户
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    @Override
    public PageResult search(int page, int rows, Seller seller) {
        PageHelper.startPage(page,rows);

        SellerQuery sellerQuery = new SellerQuery();

        if (seller.getStatus()!=null && !"".equals(seller.getStatus())){
            sellerQuery.createCriteria().andStatusEqualTo(seller.getStatus().trim());

        }

        Page<Seller> sellerPage= (Page<Seller>) sellerDao.selectByExample(sellerQuery);

        return new PageResult(sellerPage.getResult(),sellerPage.getTotal());
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @Override
    public Seller findOne(String id) {
        return sellerDao.selectByPrimaryKey(id);
    }

    /**
     * 审核状态
     * @param sellerId
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }


}
