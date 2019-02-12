app.service("loginService",function($http){
	
	this.showName = function(){
		return $http.get("../login/showName.do");
	}

    this.showTime = function(){
        return $http.get("../login/showTime.do");
    }
	
});