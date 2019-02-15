app.service("seckillService",function($http){
    this.findMarket = function () {
        return $http.get('www.baidu.com')
    }

    this.search = function (page,rows) {
        return $http.get('../seckill/findAllBySeller.do')
    }

    this.findOne = function (id) {
        return $http.get('../seckill/findOne.do?id='+id);
    }

    this.commitSeckill = function (seckillGoods) {
        return $http.post('../seckill/commitSeckill.do',seckillGoods )
    }
})