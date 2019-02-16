app.controller('saleslineMapsController' ,function($scope,$controller,$location,saleslineMapsService){

    $scope.time = []

    $scope.data = []

    $scope.initMap = function(){
        var dom = document.getElementById("container");
        var myChart = echarts.init(dom);
    }

    /**m
     * 获得销售折线图需要的时间和数据
     * @param start  开始时间
     * @param end    结束时间
     */
    $scope.getTimeAndData=function(start,end){
        //alert(start)
        saleslineMapsService.getTimeAndData(start,end).success(
            function (response) {
                $scope.time = response.time;
                //遍历时间
                for (var i =0;i<$scope.time.length;i++){
                    $scope.data[i] = response.data[$scope.time[i]];
                }
            }
        )
    }
    
    $scope.maps=function () {

        var dom = document.getElementById("container");
        var myChart = echarts.init(dom);

        $scope.getTimeAndData($scope.start,$scope.end);

        var app = {};
        option = null;
        option = {
            title: {
                text: '折线图(单位:元)',
                x: 'center'
            },
            xAxis: {
                type: 'category',
                data: $scope.time,
                axisLabel: {
                    rotate: 30, // 旋转角度
                    interval: 0  //设置X轴数据间隔几个显示一个，为0表示都显示
                },
            },
            grid: {
                top: '16%',
                left: '3%',
                right: '8%',
                bottom: '10%',
                containLabel: true
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#171c85'
                    }
                }
            },
            yAxis: {
                type: 'value'
            },
            dataZoom: [   //滚动轴和缩放
                {
                    show: true,
                    realtime: true,
                    height: 15,
                    start: 0,
                    end: 100,
                    bottom: '1%',
                    showDetail: true
                },
                {
                    type: 'inside',
                    realtime: true,
                }
            ],
            series: [{
                data: $scope.data,
                type: 'line',
                smooth: true
            }]
        };
        ;
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    }
})