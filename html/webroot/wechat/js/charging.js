wxloadStart();
setTimeout("wxloadStop()",3000);

setTimeout("orderInit()",3000);
var sec_to_time = function (s) {
    var t;
    if (s > -1) {
        var hour = Math.floor(s / 3600);
        var min = Math.floor(s / 60) % 60;
        var sec = s % 60;
        if (hour < 10) {
            t = '0' + hour + ":";
        } else {
            t = hour + ":";
        }

        if (min < 10) {
            t += "0";
        }
        t += min + ":";
        if (sec < 10) {
            t += "0";
        }
        t += sec;
    }
    return t;
}
// 获取实时信息========================
function orderInit() {
    var elPiChargingMode = localStorage.getItem("elPiChargingMode");
   
    
    $.ajax({
        type: "get",
        url: basePath + orderInfo,
        async: true,
        dataType: "json",
        data: {
            openId: localStorage.getItem("openId"),
            pileCode: localStorage.getItem("pileCode"),
            gunCode: localStorage.getItem("gunCode")
        },
        success: function (datas) {
        	var datas=JSON.parse(datas);
            if (datas.status == 100) {
                var req = datas.data;
              
                if (req.status == 1) {
                    var socValue = req.soc;
                    var power = req.power;
                    var feeTotal = req.feeTotal;
                    var timeCharge = req.timeCharge;
                    var tC = sec_to_time(timeCharge);
                    if(elPiChargingMode==5){
                    $('#socValue').html(socValue + '%');
                    }
                    $('#timeCharge').html(tC);
                    $('#power').html(parseFloat(power).toFixed(2));
                    $('#feeTotal').html(parseFloat(feeTotal).toFixed(2));
                } else if (req.status == 2) {
                    localStorage.setItem('endChargeInfo', req);

                    var timeChargeF = req.timeCharge;
                    var tC = sec_to_time(timeChargeF);
                    localStorage.setItem('tC', tC);

                    var power = req.power;
                    localStorage.setItem('powerF', power);

                    var feeTotal = req.feeTotal;
                    localStorage.setItem('feeTotalF', feeTotal);

                    var feeBack = (parseFloat(req.chargAmt) / 100 - parseFloat(req.feeTotal));
                    localStorage.setItem('feeBack', feeBack);

                    toPage("finishedCharge.html");
                }
                else if (req.status == 3) {
                    $('#doneBtn').off('click');
                    $('#doneBtn').addClass('doneBtnDisabled').removeClass('doneBtn');

                    layer.closeAll();
                    layer.open({
                        content: '<div class="guyStyle">充电枪未连接</div><div class="endTip">请连接充电枪</div>',
                        style: 'border:none; background-color:#cccccc; color:#000;font-size:36/@r;',
                        btn: ['我知道了'],
                        shadeClose: false,
                        anim: 'up',
                        fixed: true
                    });
                }
            } else {
                $('#doneBtn').off('click');
                $('#doneBtn').addClass('doneBtnDisabled').removeClass('doneBtn');

                layer.closeAll();
                layer.open({
                    content: datas.msg,
                    style: 'border:none; background-color:#cccccc; color:#000;font-size:36/@r;',
                    btn: ['我知道了'],
                    shadeClose: false,
                    anim: 'up',
                    fixed: true
                });
            }
        }
    });
}

$('#doneBtn').on('click', function () {
    layer.closeAll();
    layer.open({
        content: '<div class="end">结束充电</div><div class="endTip">结束充电后，请及时放回充电枪</div>',
        style: 'border:none; background-color:#cccccc; color:#000;font-size:36/@r;',
        btn: ['结束', '暂时不'],
        shadeClose: false,
        anim: 'up',
        fixed: true,
        no: function (index) {
            layer.close(index);
        },
        yes: function (index) {
            wxChargeStop();

            layer.close(index);
        }
    });
})
function toPages(url) {
    return window.location.href = url;
}

// 获取信息========================
function wxChargeStop() {

    $.ajax({
        type: "get",
        url: basePath + chargeStop,
        async: true,
        dataType: "json",
        data: {
            openId: localStorage.getItem("openId"),
            pileCode: localStorage.getItem("pileCode"),
            gunCode: localStorage.getItem("gunCode")
        },
        success: function (datas) {
        	var datas=JSON.parse(datas);
            if (datas.status == 100) {
                var req = datas.data;
            	/*alert(req);*/
                var timeChargeF = req.timeCharge;
                var tC = sec_to_time(timeChargeF);

                localStorage.setItem('tC', tC);

                var power = req.power;
                localStorage.setItem('powerF', power);

                var feeTotal = req.feeTotal;
                localStorage.setItem('feeTotalF', feeTotal);


                var chargAmt=parseFloat(req.chargAmt) / 100;
                var feeTotal=parseFloat(req.feeTotal);
                var feeBack = parseFloat(chargAmt - feeTotal);
                localStorage.setItem('feeBack', feeBack);

                toPage("finishedCharge.html");

            }
        }
    });
}
function toPage(url) {
    window.location.href = url;
}