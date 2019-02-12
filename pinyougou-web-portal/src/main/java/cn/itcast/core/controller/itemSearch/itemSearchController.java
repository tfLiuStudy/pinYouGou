package cn.itcast.core.controller.itemSearch;

import cn.itcast.core.service.itemSearch.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemsearch")
public class itemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search.do")
    public Map<String,Object> search(@RequestBody Map<String,String> searchMap ){
        return itemSearchService.search(searchMap);
    }
}
