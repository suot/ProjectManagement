var Edc = function () {
};

Edc.InitECharts = function(dataReturned, color, legendData, xAxisData){
    var myChart = echarts.init(document.getElementById('mainb'));

    var option = {
        color:  color,
        title: {
            text: '临床研究单位取号统计图',
            subtext: '按天统计'
        },
        tooltip : {
            trigger: 'axis',
            //axisPointer: {
            //    type: 'cross',
            //    label: {
            //        backgroundColor: '#6a7985'
            //    }
            //}
        },
        legend: {
            data: legendData
        },
        toolbox: {
            show : true,
        },
        calculable : true,
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xAxisData
            }
        ],
        yAxis : [
            {
                type : 'value',
            }
        ],
        //dataZoom: [{
        //    startValue: 0
        //}, {
        //    type: 'inside'
        //}],
        series : dataReturned
    };
    myChart.setOption(option);

    $(window).on('resize', function(){
        myChart.resize();
    });
}


//加载时触发
$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "/mainpage/getEChartsData",
        success: function (result1) {
            console.log("get EChartData successfully.");
            //if(result1 == null || result1=="null" || result1==""){
            //    console.log("没有取号记录");
            //    $('#mainb').hide();
            //}else{
                var data = result1;
                $.ajax({
                    type: "GET",
                    url: "/mainpage/getEChartsOptions",
                    success: function (result2) {
                        console.log("get EChartsOptions successfully.");
                        Edc.InitECharts(data, result2.color, result2.legendData, result2.xAxisData);
                    }
                });
            //}
        }
    });

    //最近消息
    $(".recentActivities").bootstrapNews({
        newsPerPage: 3,
        autoplay: true,
        pauseOnHover:true,
        direction: 'up',
        newsTickerInterval: 5000
    });
});

