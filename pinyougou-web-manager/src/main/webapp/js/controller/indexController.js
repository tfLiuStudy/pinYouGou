app.controller("indexController",function($scope,loginService){
	
	$scope.showName = function(){
		loginService.showName().success(function(response){
			$scope.loginName = response.username;
		});
	}

    $scope.showTime = function(){
		// alert(1);
        loginService.showTime().success(function(response){
            $scope.lastLoginTime = response.loginTime;
        });
    }
});