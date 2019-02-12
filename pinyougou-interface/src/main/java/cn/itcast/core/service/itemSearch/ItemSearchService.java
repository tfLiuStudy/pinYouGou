package cn.itcast.core.service.itemSearch;

import java.util.Map;

public interface ItemSearchService {

    Map<String,Object> search(Map<String,String> searchMap);

    void updateItemToSolr(Long id);

    void deleteItemToSolr(long id);
}
