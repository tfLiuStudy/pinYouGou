//控制层
app.controller('seckillOrderController' ,function($scope,$controller,$location,seckillOrderService){

    $controller('baseController',{$scope:$scope});//继承

    $scope.search = function (page,rows) {
        seckillOrderService.search(page,rows).success(
            function (response) {
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        )
    }
})
