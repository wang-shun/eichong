
$(function(){
    setTimeout(function(){
        loadshishiData();
        var intervalIdloadRunTime;
        function loadshishiData(){
            realData();
            clearInterval(intervalIdloadRunTime);
            intervalIdloadRunTime=setInterval('realData()',1000*60);
        }
    },1000*60);

    map.setFeatures(['bg','road','building']);//去掉地图默认标注
})

function realData(){
    var provinceCode = window.localStorage.getItem('provinceCode');
    var cityCode = window.localStorage.getItem('cityCode');
    var powerstationId = window.localStorage.getItem('powerstationId');
    var cpyId = window.localStorage.getItem('cpyId')?undefined:'';
    getElectricPileMap(provinceCode,cityCode,cpyId,2);
}
var map = new AMap.Map('container', {
    resizeEnable: true,
    zoom:5,
    zooms:[5,14],//5-9 9-11 12-14
    mapStyle: 'amap://styles/blue',
    doubleClickZoom:false,
    center: [114.31620010268132, 30.58108412692075]
});

// 百度地图的开发者秘钥
var token = 'fHrNQj6DHTjZtfTvfqbsuvTzKc5V9SBl';
var url = 'http://api.map.baidu.com/geocoder/v2/?output=json&ak=' + token + '&address=';
var chartData = [];
var mapData;
var markers = [];

//请求所有省级数据获取到所有返回城市到经纬度(第一级)
function getElectricPileMap(provinceCode,cityCode,cpyId,type) {
    if(map.getZoom() < 12){
        toLoadPoint(provinceCode,cityCode,cpyId,type);
    }else if(map.getZoom() >= 12){
        toLoadPointRepeat(provinceCode,cityCode,cpyId,type);
    }
}

var _onZoomEnd = function(e) {//地图层级变化
    provinceCode = window.localStorage.getItem('provinceCode');
    cityCode = window.localStorage.getItem('cityCode');
    powerstationId = window.localStorage.getItem('powerstationId');
    cpyId = window.localStorage.getItem('cpyId');
    if(map.getZoom()==5){
        var centerA = [114.31620010268132, 30.58108412692075];
        map.setCenter(centerA);
        clearLocalStorageCon();
        toLoadPoint('','',cpyId,2)
    }else if(map.getZoom()==10){
        var cpyId = window.localStorage.getItem('cpyId');
        if(provinceCode == '' ){
            return;
        }else{
            toLoadPoint(provinceCode,'',cpyId,2)
        }
    }
}
AMap.event.addListener(map, 'zoomend', _onZoomEnd);//监听地图zoom变化


//重置缓存
function clearLocalStorageCon(){
    window.localStorage.setItem('cityCode', '');
    window.localStorage.setItem('provinceCode', '');
    window.localStorage.setItem('powerstationId', '');
}
//获取到数据渲染地图
function drawMap(place){
    var createMarker = function(data){
        centerPoint=[];
        centerPoint.push(data);
        var div = document.createElement('div');
        div.className = 'circle';
        //根据电量的大小画不同大小的圆
        if(map.getZoom()>=5 && map.getZoom()<9){
            if(data.electricCount >0 && data.electricCount <= 100){
                var size = 16;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 100 && data.electricCount <= 1000){
                var size = 26;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 1000){
                var size = 36;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }
        }else if(map.getZoom()>=9 && map.getZoom()<11){
            if(data.electricCount >0 && data.electricCount <= 50){
                var size = 16;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 50 && data.electricCount <= 200){
                var size = 26;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 200){
                var size = 36;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }
        }else{
            if(data.electricCount >0 && data.electricCount <= 5){
                var size = 16;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 5 && data.electricCount <= 20){
                var size = 26;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }else if(data.electricCount > 20){
                var size = 36;
                div.style.backgroundColor = '#ff7d00';
                div.style.width = size + 'px';
                div.style.height = size + 'px';
                div.style.borderRadius = size/2 + 'px';
                div.style.opacity = 0.8;
            }
        }
        var marker = new AMap.Marker({
            content: div,
            animation:'',
            title: data.name + '  电桩数量:'+data.electricCount,
            position:data.value,
            offset: new AMap.Pixel(-24, -35),
            extData:{
                name:data.name,
                center:data.value,
                provinceCode:data.provinceCode || window.localStorage.getItem('provinceCode'),
                cityCode:data.cityCode || window.localStorage.getItem('cityCode'),
                powerstationId:data.powerstationId || window.localStorage.getItem('powerstationId'),
                cpyId:window.localStorage.getItem('cpyId') || ''
            }
        });
        marker.content = data.name + '<br/>' + '电桩数量：'+data.electricCount;
        marker.on('mouseover', markerClick);
        marker.emit('mouseout', {target: marker});
        marker.setMap(map);
        function markerClick(e) {
            infoWindow.setContent(e.target.content);
            infoWindow.open(map, e.target.getPosition());
        }
        marker.off('click').on('click',markerdblClick);
        //点击进入下一层
        function markerdblClick(e){
            var provinceCode = e.target.G.extData.provinceCode;
            var cityCode = e.target.G.extData.cityCode;
            var powerstationId = e.target.G.extData.powerstationId;
            var cpyId = e.target.G.extData.cpyId;

            window.localStorage.setItem('cityCode', cityCode);
            window.localStorage.setItem('provinceCode', provinceCode);
            window.localStorage.setItem('powerstationId', powerstationId);
            window.localStorage.setItem('cpyId', cpyId);
            if(provinceCode && cityCode=='' && powerstationId== ''){//进入第二层
                map.clearMap();
                //map.setZoomAndCenter(9, center);
                //进入第二级图层
                map.setZoom(9);
                toLoadPoint(provinceCode,'',cpyId,2);
                //删除第一级信息窗
                $('.amap-info-content').parent().remove();
            }
            if(provinceCode &&cityCode && powerstationId== ''){//进入第三层
                map.clearMap();
                map.setZoom(12);

                toLoadPointRepeat(provinceCode,cityCode,cpyId,2);
                $('.amap-info-content').parent().remove();
            }
            if(provinceCode &&cityCode && powerstationId != ''){//跳出地图
                window.parent.location.href = 'chargingPoint/chargePointHomePage.html?powerstationId='+powerstationId;
            }
        }
    }
    var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30),class:infoWindow});
    var chartDataLast = chartData[chartData.length - 1];
    var marker = createMarker(chartDataLast);
    markers.push(marker);
    //设置地图的中心点
    //[108.95309827919623, 34.277799897830626]
    var center = centerPoint[0].value;
    map.setCenter(center);
}

//加载第一二级
function toLoadPoint(provinceCode,cityCode,cpyId,type){
    map.clearMap();
    $('.chargiePoint').hide();
    $('#chargieCurve').hide();
    $('#chargeRecord').hide()
    $('#historicalDataDiv').show();
    $('#realTimeDataDiv').show();
    toLoadPointData(provinceCode,cityCode,cpyId,type);
}
//加载第三级
function toLoadPointRepeat(provinceCode,cityCode,cpyId,type){
    map.clearMap();
    $('#historicalDataDiv').hide();
    $('#realTimeDataDiv').hide();
    $('.chargiePoint').show();
    $('#chargieCurve').show();
    $('#chargeRecord').show();
    getChargePowerCurve(provinceCode,cityCode,cpyId,type);
    getEpStartChargeRecord(provinceCode,cityCode,cpyId,type);
    toLoadPointData(provinceCode,cityCode,cpyId,type);

}
//输入地点名返回数据
function toLoadPointData(provinceCode,cityCode,cpyId,type){
    $.ajax({
        type: "post",
        url: basePath + getMapDataUrl,
        async: false,
        data:{
            provinceCode:provinceCode,
            cityCode:cityCode,
            cpyId:cpyId,
            type:type
        },
        success: function (req) {
            mapData = req.dataObject;
            exceptionHandle(mapData);
            //判断data参数是否为空，三种情况
            for (var i = 0; i < mapData.length; i++) {
                var place = mapData[i].provinceName || mapData[i].cityName || mapData[i].powerstationName;
                var provinceCode = mapData[i].provinceCode;
                var cityCode = mapData[i].cityCode;
                var powerstationId = mapData[i].powerstationId;
                var electricCount = mapData[i].electricCount;
                if (place) {
                    (function (place, provinceCode, cityCode, powerstationId, electricCount) {
                        $.getJSON(url + place + '&callback=?', function (res) {
                            var loc;
                            //console.log('hou'+place);
                            if (res.status === 0) {
                                loc = res.result.location;
                                chartData.push({
                                    name: place,
                                    value: [loc.lng, loc.lat],
                                    provinceCode: provinceCode,
                                    cityCode:cityCode,
                                    powerstationId:powerstationId,
                                    electricCount: electricCount
                                });
                                drawMap(place);
                            } else {
                                layer.closeAll();
                                layer.open({
                                    type: 1,
                                    offset: '100px',
                                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                                    shadeClose: false,
                                    closeBtn: 1,
                                    area: ['310px', '160px'],//宽高
                                    content: '高德没有找到地址信息',
                                    time: 3000,
                                    btn: ["确定"]
                                });
                            }
                        })
                    }(place, provinceCode,cityCode,powerstationId, electricCount));
                }

            }

        }
    });
    toLoadHistoryData(provinceCode,cityCode,cpyId,type);

}
//加载历史数据
function toLoadHistoryData(provinceCode,cityCode,cpyId,type){
    var index=layer.load(1);
    $.ajax({
        type: "post",
        url: basePath + getHistoryDataUrl,
        async: false,
        data: {
            type:type,
            provinceCode:provinceCode,
            cityCode:cityCode,
            cpyId:cpyId
        },
        success: function (req) {
            layer.close(index);
            var data = req.dataObject;
            exceptionHandle(data);
            $('.powerStationCount').html(data.powerStationCount);
            $('.electircHeadCount').html(data.electircHeadCount);
            $('.electircCount').html(data.electircCount);
            $('.accumulativeCharge').html(data.chargeCount);//累计充电量
            $('#cityChargeCount').html(data.chargeCount);

        }
    });
    toLoadRealTimeData(provinceCode,cityCode,cpyId,type);

}

//加载实时数据
function toLoadRealTimeData(provinceCode,cityCode,cpyId,type){
    //var index=layer.load(1);
    $.ajax({
        type: "post",
        url: basePath + getChargeRealTimeDateUrl,
        async: false,
        data: {
            type:type,
            provinceCode:provinceCode,
            cityCode:cityCode,
            cpyId:cpyId
        },
        success: function (req) {
            //layer.close(index);
            removeLoading();
            exceptionHandle(req);
            $('.chargeCount').html(req.chargeCount);
            $('.errorCount').html(req.errorCount);
            $('.realTimeChargeHead').html(req.realTimeChargeHead);
            $('#newChargeCount').html(req.chargeCount);
        }
    });
}

var chargeAmountOne;
function getChargePowerCurve(provinceCode,cityCode,cpyId,type){//实时充电电量曲线
    var cpyId = window.localStorage.getItem('cpyId');
    var index=layer.load(1);
    $.ajax({
        type: "post",
        url: basePath + getChargePowerCurveUrl,
        async: false,
        data: {
            cityCode:cityCode,//110100
            cpyId:cpyId//295
        },
        success: function (req) {
            layer.close(index);
            var data = req.dataObject;
            var myChartTwo = echarts.init(document.getElementById('chargeAmountOne'));
            myChartTwo.resize();
            chargeAmountOne = {
                backgroundColor: '#383838',
                title: {
                    text: '实时充电电量曲线',
                    x: '20px',
                    y: 'top',
                    textStyle: {
                        color: '#fff',
                        fontStyle: 'normal',
                        fontWeight: 'normal',
                        fontFamily: '微软雅黑',
                        fontSize: 14
                    }
                },
                color: ['#ff7d00', '#ff8900', '#b7a3df', '#6cbce9'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'line',
                        lineStyle: {
                            color: '#cccccc',
                            width: 1,
                            type: 'dashed'
                        }
                    }
                },
                legend: {
                    bottom: 2,
                    data: []
                },
                grid: {
                    show: false,
                    left: '50',
                    right: '40',
                    bottom: '10%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    show:true,
                    boundaryGap: true,
                    textStyle: {
                        color: '#fff'
                    },
                    data: [],
                    axisLine:{
                        lineStyle:{
                            color:'#fff',
                            width:2//这里是为了突出显示加上的
                        }
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: true
                    }
                },
                yAxis: [{
                    type: 'value',
                    show:false,
                    minInterval: 1,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    }
                }],
                series: [{
                    data: [],
                    type: 'line',
                    smooth: true
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            chargeAmountOne.xAxis.data = data.time;
            for(var i = 0; i < chargeAmountOne.series.length; i ++){
                chargeAmountOne.series[i].data = data.value;
            }
            $("#chargeAmountOne").css( 'width', $("#chargeAmountOne").width() );
            myChartTwo.setOption(chargeAmountOne);
            myChartTwo.resize();
            $(window).resize(function() {
                //重置容器高宽
                myChartTwo.resize();
            });
        }
    });
}


function timestampToTime(timestamp) {//时间戳转化
    var date = new Date(timestamp);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return Y+M+D+h+m+s;
}

//电桩维度开始充电记录列表
function getEpStartChargeRecord(provinceCode,cityCode,cpyId,type){
    var cpyId = window.localStorage.getItem('cpyId');
    var index=layer.load(1);
    $.ajax({
        type: "post",
        url: basePath + getEpStartChargeRecordUrl,
        async: false,
        data: {
            cityCode:cityCode,//330100
            cpyId:cpyId//253
        },
        success: function (req) {
            layer.close(index);
            var data = req.dataObject;
            exceptionHandle(data);
            var beginLi = '';
            for(var i = 0; i < data.length; i ++ ){
                timestampToTime(data[i].time);
                var timeData = timestampToTime(data[i].time);
                beginLi += '<li>'+
                        '<i>'+data[i].chargeBegintime+'</i>'+
                        '<i>'+data[i].epCode+'</i>'+
                        '<i>启动充电</i>'+
                    '</li>'
            }
            $('.beginData1').html(beginLi);

            $("#chargeAmountTwo").myScroll({
                speed:60, //数值越大，速度越慢
                rowHeight:36 //li的高度
            });
        }
    });
}






