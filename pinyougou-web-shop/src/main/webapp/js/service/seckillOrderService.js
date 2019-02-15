//服务层
app.service('seckillOrderService',function($http){

    this.search=function (page,rows) {
        return $http.get('../order/search.do?page='+page+'&rows='+rows);
    }

})