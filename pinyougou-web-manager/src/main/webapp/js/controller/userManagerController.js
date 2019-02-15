//品牌管理控制层
app.controller('userManagerController' ,function($scope,$controller,userManagerService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        userManagerService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }
});
