/**
 * Created by chenjie on 2017/10/25 0025.
 */
//初始绑定
$(document).ready(function(){
    $('#saveBtn').bind("click", foo);
});

//日期选择
laydate.render({
    elem: '#datePicker',
    range: true,
    theme: '#ff7d00'
    /* ,min: -90,
     max:0//0 代表今天 -1代表昨天，-2代表前天，以此类推*/
});

//基本后台反馈交互
function CommonCaution(data){
    var status = true;

    return status;
}

//状态切换
$('.radio-inline').on('click', function() {
    var target = $(this).attr('state');
    if (target == '2') {
        $('#saveBtn').unbind("click", foo);
        $('input[name="add_FestivalName"]').attr("disabled", true); //活动名称
        $('input[name="score"]').attr("disabled", true); //分值
        $('#datePicker').attr("disabled", true); //时间选择器
    } else {
        $('#saveBtn').bind("click", foo);
        $('input[name="add_FestivalName"]').attr("disabled", false);
        $('input[name="score"]').attr("disabled", false);
        $('#datePicker').attr("disabled", false);
    }
});

//分值取整提醒
$('input[name="score"]').on({
    keyup: function() {
        if (this.value.length == 1) {
            this.value = this.value.replace(/[^1-9]/g, '')
        } else {
            this.value = this.value.replace(/\D/g, '')
        }
    },
    afterpaste: function() {
        if (this.value.length == 1) {
            this.value = this.value.replace(/[^1-9]/g, '0')
        } else {
            this.value = this.value.replace(/\D/g, '')
        }
    },
    blur: function() {
        if (this.value >= 100) {
            layer.open({
                type: 1,
                offset: '100px',
                title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                shadeClose: false,
                closeBtn: 1,
                area: ['310px', '160px'],
                //宽高
                content: '过高的分值可能会导致损失，请注意操作。',
                time: 0,
                btn: ["确定", "取消"],
                btn1: function(index, layero) {
                    layer.closeAll();
                },
                btn2: function(index, layero) {
                    $('input[name="score"]').val('');
                    layer.closeAll();
                },
                cancel: function(index, layero) {
                    $('input[name="score"]').val('');
                    layer.closeAll();
                }
            })
        } else if (this.value < 50 && this.value != '') {
            layer.open({
                type: 1,
                offset: '100px',
                title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                shadeClose: false,
                closeBtn: 1,
                area: ['310px', '160px'],
                //宽高
                content: '节假日分值请勿设置低于普通日期，会降低用户体验。',
                time: 0,
                btn: ["确定"],
                btn1: function(index, layero) {
                    layer.closeAll();
                },
                cancel: function(index, layero) {
                    layer.closeAll();
                }
            })
        } else if (this.value == '') {
            return true;
        }
    }
});

//错误提示
function layerCase(msg){
    layer.open({
        type: 1,
        offset: '100px',
        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '160px'],
        //宽高
        content: msg,
        time: 0,
        btn: ["确定"],
        btn1: function(index, layero) {
            layer.closeAll();
        },
        cancel: function(index, layero) {
            layer.closeAll();
        }
    });
}

//数据提交
var foo = function() {
    var data_one = $('input:radio:checked').val(),
    //状态
        data_two = 8,
    //活动名称
        data_three = 0,
    //积分方法
        day = $('#datePicker').val(),
    //时间
        data_four = $('input[name="add_FestivalName"]').val(),
    //活动名称
        data_five = $('input[name = "score"]').val();
    //固定分值
    var start_date = slice_date(day)[0],
        end_day = slice_date(day)[1];

    if (data_four == '') {
        layerCase('活动名称不能为空');
        return false;
    }else if(day == ''){
        layerCase('活动时间不能为空');
        return false;
    }else if(data_five == ''){
        layerCase('分值不能为空');
        return false;
    }

    var data = {
        'pkId': data_two,
        'activityStatus': data_one,
        'activityName': '节假日',
        'direction': data_three,
        'fixedIntegralValue': data_five,
        'strStartDate': start_date,
        'strEndDate': end_day,
        'contents': data_four
    };
    $.ajax({
        url: basePath + addIntegralActivityUrl,
        type: "post",
        dataType: 'json',
        data: data,
        success: function(data) {
            if (data.status == 1000) {
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],
                    //宽高
                    content: data.msg,
                    btn: ["确定"],
                    yes: function(index, layero) {
                        layer.closeAll();
                        iframeClose();
                    },
                    cancel: function(index, layero) {
                        layer.closeAll();
                        iframeClose();
                    }
                });
            } else if (data.status == 9001) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],
                    //宽高
                    content: '会话超时，请重新登陆！',
                    btn: ["确定"],
                    yes: function(index, layero) {
                        layer.closeAll();
                        window.top.location.href = basePath + toLoginUrl;
                    },
                    cancel: function(index, layero) {
                        layer.closeAll();
                        window.top.location.href = basePath + toLoginUrl;
                    }
                });
            }else if(data.status == 2001){
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],
                    //宽高
                    content: data.msg,
                    time: 0,
                    btn: ["确定"],
                    btn1: function(indexs, layero) {
                        layer.closeAll();
                    },
                    cancel: function(index, layero) {
                        layer.closeAll();
                    }
                });
            } else {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],
                    //宽高
                    content: data.msg,
                    btn: ["确定"],
                    yes: function(index, layero) {
                        window.location.reload();
                        layer.closeAll();
                    },
                    cancel: function(index, layero) {
                        window.location.reload();
                        layer.closeAll();
                    }
                });
            }
        }
    })
};

//监听回车键
$(document).keyup(function(event) {
    if (event.keyCode == 13) {
        foo();
    }
});
