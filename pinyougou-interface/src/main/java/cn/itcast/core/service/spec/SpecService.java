package cn.itcast.core.service.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecVo;

import java.util.List;
import java.util.Map;

public interface SpecService {

    PageResult search(int page,int rows,Specification specification);

    void add(SpecVo specVo);

    SpecVo findOne(Long id);

    void update(SpecVo specVo);

    void delete(Long[] ids);

    //新增规格列表初始化
    List<Map<String,String>> selectOptionList();
}
