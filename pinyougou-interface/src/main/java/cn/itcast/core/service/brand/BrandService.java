package cn.itcast.core.service.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<Brand> findAll();

    PageResult findByPage(int pageNo, int pageSize);

    PageResult searchByPage(int pageNo, int pageSize,Brand brand);

    void add(Brand brand);

    Brand findOne(Long id);

    void update(Brand brand);

    void delete(Long[] ids);

    List<Map<String,String>> selectOptionList();
}
