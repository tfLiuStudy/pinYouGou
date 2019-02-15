// 定义控制器:
app.controller("userController",function($scope,$controller,userService){

    $controller('baseController',{$scope:$scope});//继承

    /**m
     * 分页查询订单
     * @param page
     * @param rows
     */
    $scope.search=function (page,rows) {
            userService.search(page,rows).success(
                function (response) {
                    $scope.entityList = response.rows;
                    $scope.paginationConf.totalItems = response.total;
                }
            )
    }
    /**m
     * 导出订单未xls
     */
    $scope.downLoad = function () {
        userService.downLoad();
        alert("下载")
    }
    }


)