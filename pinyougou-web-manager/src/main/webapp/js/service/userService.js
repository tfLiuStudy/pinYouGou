app.service('userService',function($http){

         this.search=function (page,rows) {
             return $http.post('../user/search.do?page='+page+'&rows='+rows);
         }

         this.downLoad = function () {
             $http.get("../user/downLoad.do")
         }



    }
)