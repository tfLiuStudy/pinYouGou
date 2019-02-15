// 定义控制器:
app.controller("goodsImportController",function($scope,$controller,$http,goodsImportService){

    $controller('baseController',{$scope:$scope});//继承

    $scope.search = function (page,rows) {
        goodsImportService.search(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        )
    }

    $scope.downLoad = function () {
        goodsImportService.downLoad().success(
            function () {
                alert("文件正在下载")
            }
        )
    }
})