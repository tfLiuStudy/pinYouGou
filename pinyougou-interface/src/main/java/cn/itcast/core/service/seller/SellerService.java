package cn.itcast.core.service.seller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;

public interface SellerService {

    void add(Seller seller);

    PageResult search(int page,int rows,Seller seller);

    Seller findOne(String id);

    void updateStatus(String sellerId,String status);
}
