 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService,uploadService){
	$scope.entity = {};
	//注册用户
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}

	$scope.user={}
	//查询当前用户的信息
	$scope.findOneByUserName=function () {
		alert("111")
		userService.findOneByUserName().success(
			function (response) {
				$scope.user = response;
				$scope.user.headPic = response.headPic;
            }
		)
    }

    $scope.url;

    $scope.uploadPhoto = function(){
        // 调用uploadService的方法完成文件的上传=
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                alert(response.message);
                // 获得url
                $scope.url =  response.message;
                $scope.user.headPic=$scope.url;
            }else{
                alert(response.message);
            }
        });
    }

    $scope.updateUserInfo = function () {
		userService.updateUserInfo($scope.user).success(
			function (response) {
				alert(response.flag);
            }
		)
    }
	
});	
