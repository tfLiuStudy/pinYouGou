//控制层
app.controller('seckillController' ,function($scope,$controller,$location,seckillService){


    $scope.search = function () {
        seckillService.search().success(
            function(response){
                $scope.list=response;
            }
        )
    }

    $scope.SeckillGoods = {}
    $scope.findOne = function (id) {
        seckillService.findOne(id).success(
            function (response) {
                $scope.SeckillGoods.goodsId = response.goodsId;
                $scope.SeckillGoods.itemId = response.id;
                $scope.SeckillGoods.title = response.title;
                $scope.SeckillGoods.price = response.price;
                $scope.SeckillGoods.smallPic = response.image;
                $scope.SeckillGoods.stockCount = response.num;
            }
        )
    }

    $scope.commitSeckill = function () {
        alert($scope.SeckillGoods.price)
        alert($scope.SeckillGoods.endTime)
        seckillService.commitSeckill($scope.SeckillGoods).success(
            function (response) {
                alert(response.flag)
            }
        )
    }
}
)