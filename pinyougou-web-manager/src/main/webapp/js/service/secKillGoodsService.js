//服务层
app.service('secKillGoodsService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../secKillGoods/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../secKillGoods/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../secKillGoods/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../secKillGoods/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../secKillGoods/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../secKillGoods/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../secKillGoods/search.do?page='+page+"&rows="+rows, searchEntity);
	}    
	
	this.updateStatus = function(ids,status){
		return $http.get('../secKillGoods/updateStatus.do?ids='+ids+"&status="+status);
	}
});
