app.service('goodsImportService',function($http){

    this.search=function (page,rows) {
        return $http.post("../goods/searchByMarket.do?page="+page+"&rows="+rows);
    }

    this.downLoad = function () {
        return $http.post('../goods/downLoad.do');
    }

})
