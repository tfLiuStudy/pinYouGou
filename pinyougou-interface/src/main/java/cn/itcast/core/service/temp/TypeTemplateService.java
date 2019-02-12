package cn.itcast.core.service.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

    PageResult search(int page, int rows, TypeTemplate typeTemplate);

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(Long id);

    void update(TypeTemplate typeTemplate);

    void delete(Long[] ids);

    List<TypeTemplate> findAll();

    List<Map> findBySpecList(Long id);
}
