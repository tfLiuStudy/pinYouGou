//服务层
app.service('saleslineMapsService',function($http){

    /**m
     * 获得销售折线图需要的时间和数据
     * @param start  开始时间
     * @param end    结束时间
     */
    this.getTimeAndData=function (start,end) {
        return $http.get('../map/saleslineMap.do?start='+start+"&end="+end);
    }
})


