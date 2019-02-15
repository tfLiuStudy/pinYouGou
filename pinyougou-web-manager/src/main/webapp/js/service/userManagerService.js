// 定义用户管理服务层:
app.service("userManagerService",function($http){
    this.findAll = function(){
        return $http.get("../userManager/findAll.do");
    }
});