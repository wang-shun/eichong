var ucCpyId = getUrlParam('cpyId');
var cpyName = getUrlParam('cpyName');
$('#cpyCompanyName').html(cpyName);
$('#cpyCompanyName').attr('data-value', ucCpyId);
toGiveHiddenInput();
//获取卡列表的menuId
var getMenuMap = window.localStorage.getItem('menuMap');
var getCurruntMap = JSON.parse(getMenuMap);
var menuId = getCurruntMap['卡列表'];
$(function () {
    ctrlMenu(menuId);
    setTimeout('cardListSearch()', 90);
    setTimeout('getBaseInfo()', 290);
})
function ctrlMenu(menuId) {
    $.ajax({
        type: "post",
        url: basePath + getSelfButtonTreeUrl,
        async: true,
        data: {
            menuId: menuId
        },
        success: function (data) {
            var data = data.dataObject;
            if (data == null) {
                return;
            }
            if (data.length == 0) {
                return;
            } else {
                for (var i = 0; i < data.length; i++) {
                    var contents = data[i].contents;
                    if (contents.indexOf('绑定') > -1) {
                        $('#bindBtn').show();
                    }
                    if (contents.indexOf('挂失') > -1) {
                        $('#cardLossBtn').show();
                    }
                    if (contents.indexOf('显示') > -1) {
                        $('#showSet').show();
                    }
                    if (contents.indexOf('导出') > -1) {
                        $('#dataExport').show();
                    }
                    if (contents.indexOf('修改支付密码') > -1) {
                        $('#editPayBtn').show();
                    }

                }
            }
        }
    });
}


function toUnbindEvent() {
    $('.cpyProvinceBlock').unbind();
    $('.cypCityBlock').unbind();
    $('.cpyCompanyBlock').unbind();
    $('.ucStatusBlock').unbind();
    $('.isAppBlock').unbind();
    $('.ucTypeBlock').unbind();
    selectModel();
}

//去加载表格的数据
//cardListSearch();

function cardListSearch() {
    toGiveHiddenInput();
    initTable("cardListForm", "cardListPage", getUsercardListUrl, cardListCallback);
}
function cardListCallback(req) {
    var data = req.dataObject;
    var pageNum = req.pager.pageNo;
    var listTr = "";
    for (var i = 0; i < data.length; i++) {
        var isAppHtml = '';
        var finnalHomeUrl='';
        var ucInternalCardNumberUrl='';
        if (data[i].isApp == 1) {
            isAppHtml = '已绑定';//app用户跳用户主页
            finnalHomeUrl= '<a class="userHome" onclick="return false;" href="'
                + basePath + '/static/html/userList/user_home.html?userId='
                + data[i].newUserId + '&cpyType=' + '0' + '&cpyId='
                + data[i].ucCpyId + '">' + data[i].ucExternalCardNumber + '</a>';
            ucInternalCardNumberUrl= '<a class="userHome" onclick="return false;" href="'
                + basePath + '/static/html/userList/user_home.html?userId='
                + data[i].newUserId + '&cpyType=' + '0' + '&cpyId='
                + data[i].ucCpyId + '">' + data[i].ucInternalCardNumber + '</a>';
            newTab(".userHome", "用户主页");
        }
        if (data[i].isApp == 0) {
            isAppHtml = '未绑定';
             finnalHomeUrl= '<a class="cardHome" onclick="return false;" href="'
                + basePath + '/static/html/cardList/card_home.html?ucId='
                + data[i].ucId + '&ucUserId=' + data[i].ucUserId + '&ucCpyId='
                + data[i].ucCpyId + '">' + data[i].ucExternalCardNumber + '</a>';
             ucInternalCardNumberUrl= '<a class="cardHome" onclick="return false;" href="'
                + basePath + '/static/html/cardList/card_home.html?ucId='
                + data[i].ucId + '&ucUserId=' + data[i].ucUserId + '&ucCpyId='
                + data[i].ucCpyId + '">' + data[i].ucInternalCardNumber + '</a>';
            newTab(".cardHome", "卡主页");
        }
        var ucStatusHtml = '';
        if (data[i].ucStatus == 0) {
            ucStatusHtml = '正常';
        }
        if (data[i].ucStatus == 1) {
            ucStatusHtml = '挂失';
        }
        if (data[i].ucStatus == 2) {
            ucStatusHtml = '冻结';
        }
        var ucTypeHtml = "";
        if (data[i].ucType == 10) {
            ucTypeHtml = "爱充普通公共储蓄卡";
        }
        if (data[i].ucType == 11) {
            ucTypeHtml = "爱充普通专属储蓄卡";
        }
        if (data[i].ucType == 12) {
            ucTypeHtml = "爱充企业信用卡";
        }
        if (data[i].ucType == 13) {
            ucTypeHtml = "爱充合作公共储蓄卡";
        }
        if (data[i].ucType == 14) {
            ucTypeHtml = "爱充合作专属储蓄卡";
        }
        var gmtCreateTime = data[i].gmtCreate.time;
        listTr += '<tr><td><input type="checkbox" name="ids" class="selectedBox" value="' + data[i].ucId + '"/></td>'
        + '<td class="cardList_ucExternalCardNumber">'+finnalHomeUrl
        + '</td><td class="cardList_ucInternalCardNumber">'+ucInternalCardNumberUrl
        + '</td><td class="cardList_ucType">' + ucTypeHtml
        + '</td><td class="cardList_cpyCompanyname">' + data[i].cpyCompanyname
        + '</td><td class="cardList_ucStatus">' + ucStatusHtml
        + '</td><td class="cardList_isApp">' + isAppHtml
        + '</td><td class="cardList_userAccount">' + data[i].userAccount
        + '</td><td class="cardList_cardBalance">' + data[i].cardBalance
        + '</td><td class="cardList_gmtCreate">' + new Date(gmtCreateTime).format("yyyy-MM-dd hh:mm:ss")
        + '</td></tr>';

    }
    $("#myTbogy").html(listTr);
    var arr = ['cardList_ucExternalCardNumber', 'cardList_ucInternalCardNumber', 'cardList_ucType', 'cardList_cpyCompanyname', 'cardList_ucStatus', 'cardList_isApp',
        'cardList_userAccount', 'cardList_cardBalance', 'cardList_gmtCreate'];
    toGetLocalStorageInfo(arr);
}
function toGiveHiddenInput() {
    var ucStatusValue = $('#ucStatus').attr('data-value');
    var cpyCompanyValue = $('#cpyCompanyName').attr('data-value');
    var isAppHtmlValue = $('#isAppHtml').attr('data-value');
    var ucTypeHtmlValue = $('#ucTypeHtml').attr('data-value');

    if (ucStatusValue == "") {
        $('input[name=ucStatus]').val('');
    } else {
        $('input[name=ucStatus]').val(ucStatusValue);
    }
    if (cpyCompanyValue == "") {
        $('input[name=ucCpyId]').val('');
    } else {
        $('input[name=ucCpyId]').val(cpyCompanyValue);
    }
    if (isAppHtmlValue == "") {
        $('input[name=isApp]').val('');
    } else {
        $('input[name=isApp]').val(isAppHtmlValue);
    }
    if (ucTypeHtmlValue == "") {
        $('input[name=ucType]').val('');
    } else {
        $('input[name=ucType]').val(ucTypeHtmlValue);
    }

}
//卡状态
$('.ucStatusUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
});
//isAppUl
$('.isAppUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
});
//卡类型ucTypeUl
$('.ucTypeUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
});

//根据管理员类型判断是否去加载对应的渠道公司

function getBaseInfo() {
    var loginUser = window.localStorage.getItem('loginUser');
    var getLoginUser = JSON.parse(loginUser);
    var userLevel = getLoginUser.userLevel;
    isLoadCompany(userLevel);
}
function isLoadCompany(userLevel) {
    if (userLevel == 1) {
        toLoadProvince('', '#cpyProvinceCode', '.cpyProvinceUl', 'toUnbindEvent');
    }
    if (userLevel == 2) {
        toLoadProvince('', '#cpyProvinceCode', '.cpyProvinceUl', 'toUnbindEvent');
    } else if (userLevel == 8) {
        toLoadCpy();
    }
}
function toLoadCpy() {
    $.ajax({
        type: "post",
        url: basePath + getCompanyListUrl,
        async: true,
        data: {
            provinceCode: '',
            cityCode: ''
        },
        success: function (data) {
            if (data.success == true) {
                var dataModule = data.dataObject;
                var cypCompanyLi = '<li data-option="">请选择</li>';
                for (var i = 0; i < dataModule.length; i++) {
                    cypCompanyLi += '<li data-option="' + dataModule[i].cpyId + '" data-cpyNumber="' + dataModule[i].cpyNumber + '">' + dataModule[i].cpyName + '</li>';
                }
                $('.cpyCompanyUl').html(cypCompanyLi);
                toUnbindEvent();
            }
        }
    });
};

//获取渠道公司卡cpyProvinceUl=================================================
$('.cpyProvinceUl').on("click", "li", function () {
    $('#cypCityCode').html('请选择');
    $('#cypCityCode').attr('data-value', '');
    $('.cypCityUl').html('');
    $('#cpyCompanyName').html('请选择');
    $('.cpyCompanyUl').html('');
    $('#cpyCompanyName').attr("data-value", '');
    $('input[name=ucCpyId]').val('');
    var provinceCodeId = $(this).attr('data-option');
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
    var flag = $(this).attr('data-option');
    if (flag == "") {
        $('#cypCityCode').html('请选择');
        $('#cypCityCode').attr('data-value', '');
        $('.cypCityUl').html('');
        $('#cpyCompanyName').html('请选择');
        $('.cpyCompanyUl').html('');
        $('#cpyCompanyName').attr("data-value", '');
        $('input[name=ucCpyId]').val('');

    } else {
        toLoadCity(provinceCodeId, '', '#cypCityCode', '.cypCityUl', 'toUnbindEvent');
    }
});
$('.cypCityUl').on("click", "li", function () {
    $('#cpyCompanyName').html('请选择');
    $('.cpyCompanyUl').html('');
    $('#levelId').html("请选择");
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
    var flag = $(this).attr('data-option');
    var cypCityCodeValue = $('#cypCityCode').attr('data-value');
    var cpyProvinceCodeValue = $('#cpyProvinceCode').attr('data-value');
    if (flag == "") {
        $('#cpyCompanyName').html('请选择');
        $('.cpyCompanyUl').html('');
        $('#levelId').html("请选择");
    } else {
        toLoadComponyName(cypCityCodeValue, cpyProvinceCodeValue);
    }
});
function toLoadComponyName(cypCityCodeValue, cpyProvinceCodeValue) {
    var cypCompanyLi = "";
    var cpyObject = {
        provinceCode: cpyProvinceCodeValue,
        cityCode: cypCityCodeValue
    };
    if (JSON.stringify(cpyObject) == "{}") {
        $('#cpyCompanyName').html("请选择");
    } else {
        toAjaxCompany(cpyObject);
    }
}
function toAjaxCompany(cpyObject) {
    $.ajax({
        type: "post",
        url: basePath + getCompanyListUrl,
        async: true,
        data: cpyObject,
        success: function (data) {
            if (data.success == true) {
                var dataModule = data.dataObject;
                var cypCompanyLi = '<li data-option="">请选择</li>';
                for (var i = 0; i < dataModule.length; i++) {
                    cypCompanyLi += '<li data-option="' + dataModule[i].cpyId + '" data-cpyNumber="' + dataModule[i].cpyNumber + '">' + dataModule[i].cpyName + '</li>';
                }
                $('.cpyCompanyUl').html(cypCompanyLi);
                toUnbindEvent();
            }
        }
    });
}
$('.cpyCompanyUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
})
//渠道公司加载完毕

//监听回车键
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        cardListSearch();
    }
});


//卡挂失
$("body").off("click", "#cardLossBtn").on("click", "#cardLossBtn", function () {
    layer.closeAll();
    layer.open({
        type: 1,
        offset: '100px',
        title: ["确认", "font-size:12px;text-align:left;padding-left:20px;"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '180px'],//宽高
        content: $('.cardLossContent'),
        btn: ["确定", "取消"],
        yes: function (index, layero) {
            layer.closeAll();
            toSubmitCardLoss();
        },
        cancel: function (index, layero) {
            layer.closeAll();
        }
    });
});
function toSubmitCardLoss() {
    var ids = ''
    $('input[name=ids]').each(function () {
        var flag = $(this).prop('checked');
        if (flag == true) {
            ids += $(this).attr('value') + ',';
        }
    })
    if (!ids) {
        layer.open({
            type: 1,
            offset: '100px',
            title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
            shadeClose: false,
            closeBtn: 1,
            area: ['310px', '160px'],//宽高
            content: '请指定表单元素',
            time: 3000,
            btn: ["确定", "取消"]
        });
    } else {
        $.ajax({
            type: "post",
            url: basePath + cardLossForListUrl,
            async: true,
            data: {
                ids: ids
            },
            success: function (data) {
                if (data.success == true) {
                    layer.closeAll();
                    layer.open({
                        type: 1,
                        offset: '100px',
                        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                        shadeClose: false,
                        closeBtn: 1,
                        area: ['310px', '160px'],//宽高
                        content: '挂失成功',
                        btn: ["确定"],
                        yes: function (index, layero) {
                            layer.closeAll();
                            window.location.reload();
                        },
                        cancel: function (index, layero) {
                            layer.closeAll();
                        }
                    });

                } else {
                    layer.open({
                        type: 1,
                        offset: '100px',
                        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                        shadeClose: false,
                        closeBtn: 1,
                        area: ['310px', '160px'],//宽高
                        content: data.msg,
                        time: 3000,
                        btn: ["确定", "取消"]
                    });
                }
            }
        });
    }
}
//绑定公司
$('#bindBtn').off('click').on("click", function () {
    $('input[name=cpyNumber]').val('');
    $('input[name=paymentRule]').val('');
    $('input[name=companyLevel]').val('');
    $('#setTradeType').html('');
    $('.companyTip').html('');
    $('#companyLevel').html('');
    var ids = ''
    $('input[name=ids]').each(function () {
        var flag = $(this).prop('checked');
        if (flag == true) {
            ids += $(this).attr('value') + ',';
        }
    })
    ids = ids.substring(0, ids.length - 1);
    if (!ids) {
        layer.open({
            type: 1,
            offset: '100px',
            title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
            shadeClose: false,
            closeBtn: 1,
            area: ['310px', '160px'],//宽高
            content: '请选择充电卡',
            time: 3000,
            btn: ["确定"]
        });
    } else {
        layer.closeAll();
        layer.open({
            type: 1,
            offset: '100px',
            title: ["卡绑公司", "font-size:12px;text-align:center"],
            shadeClose: false,
            closeBtn: 1,
            area: ['310px', '420px'],//宽高
            content: $('.newBindCompany'),
            btn: ["确定", "取消"],
            yes: function (index, layero) {
                var cpyNumberValue = $('input[name=cpyNumber]').val();
                var tradeTypeValue = $('#setTradeType').val();
                var levelId=$('#companyLevel').val();
                toBindCompany(ids, cpyNumberValue, tradeTypeValue,levelId);
            },
            cancel: function (index, layero) {
                layer.closeAll();
            }
        });
    }

})
$('body').off('change', 'input[name=cpyNumber]').on('change', 'input[name=cpyNumber]', function () {
    testCpyNumber();
})
function testCpyNumber() {
    var cpyNumberValue = $('input[name=cpyNumber]').val();
    var reg = /^[0-9]{4}$/;
    if (cpyNumberValue == '') {
        $('.companyTip').html('公司标识不能为空');
        $('#setTradeType').html('');
        $('input[name=cpyNumber]').focus();
        return false;
    }
    if (!reg.test(cpyNumberValue)) {
        $('.companyTip').html('公司标识必须为纯数字');
        $('#setTradeType').html('');
        $('input[name=cpyNumber]').focus();
        return false;
    }
    if (cpyNumberValue.length > 4) {
        $('.companyTip').html('公司标识不能超过4位');
        $('#setTradeType').html('');
        $('input[name=cpyNumber]').focus();
        return false;
    } else {
        $('.companyTip').html('');
        getCpyPayRule(cpyNumberValue);
    }
}
function getCpyPayRule(cpyNumberValue) {
    $.ajax({
        type: "post",
        url: basePath + getCpyPayRuleUrl,
        async: true,
        data: {
            cpyNumber: cpyNumberValue
        },
        success: function (data) {
            getLevelByCpyNum(cpyNumberValue);
            if (data.success == true) {
                var data = data.dataObject;
                var html = '';
                var paymentRuleText = data.A;
                var tradeTypeValue = data.B;
                for (var k in paymentRuleText) {
                    $('input[name=paymentRule]').val(paymentRuleText[k]);
                    $('input[name=paymentRule2]').val(k);
                }
                for (var k in tradeTypeValue) {
                    html += '<option value="' + k + '">' + tradeTypeValue[k] + '</option>';
                }
                $('#setTradeType').html(html);
            } else if (data.success == false) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: data.msg,
                    time: 2000,
                    btn: ["确定"]
                });
            }
        }
    });
}
//根据公司标识查公司等级
function getLevelByCpyNum(cpyNumberValue){
    $.ajax({
        type: "post",
        url: basePath + getLevelByCpyNumUrl,
        async: true,
        data: {
            cpyNumber: cpyNumberValue
        },
        success: function (data) {
            if (data.success == true) {
                var data=data.dataObject;
                var html=''
                for(var i=0;i<data.length;i++){
                    html += '<option value="' + data[i].levelId + '">' + data[i].levelName + '</option>';
                }
                $('#companyLevel').html(html);

            } else if (data.success == false) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: data.msg,
                    time: 2000,
                    btn: ["确定"]
                });
            }
        }
    });
}
//卡绑定公司
function toBindCompany(ids, cpyNumberValue, tradeTypeValue,levelId) {
    $.ajax({
        type: "post",
        url: basePath + bindCompanyForCardUrl,
        async: true,
        data: {
            ids: ids,
            cpyNumber: cpyNumberValue,
            tradeType: tradeTypeValue,
            levelId:levelId
        },
        success: function (data) {
            if (data.success == true) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ['卡绑公司', "font-size:12px;text-align:center"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: '卡绑公司成功',
                    btn: ["确定"],
                    yes: function (index, layero) {
                        layer.closeAll();
                        cardListSearch();
                    },
                    cancel: function (index, layero) {
                        layer.closeAll();
                        cardListSearch();
                    }

                });
            } else if (data.success == false) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: data.msg,
                    time: 2000,
                    btn: ["确定"]
                });

            }
        }
    });
}
//修改支付密码
$('#editPayBtn').off('click').on("click", function () {
    $('input[name=accountPwd]').val('');
    var ids = ''
    $('input[name=ids]').each(function () {
        var flag = $(this).prop('checked');
        if (flag == true) {
            ids += $(this).attr('value') + ',';
        }
    })
    ids = ids.substring(0, ids.length - 1);
    if (!ids) {
        layer.open({
            type: 1,
            offset: '100px',
            title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
            shadeClose: false,
            closeBtn: 1,
            area: ['310px', '160px'],//宽高
            content: '请选择充电卡',
            time: 3000,
            btn: ["确定"]
        });
    } else {
        layer.closeAll();
        layer.open({
            type: 1,
            offset: '100px',
            title: ["修改支付密码", "font-size:12px;text-align:left;padding-left:20px;"],
            shadeClose: false,
            closeBtn: 1,
            area: ['310px', '200px'],//宽高
            content: $('.editPassword'),
            btn: ["确定", "取消"],
            yes: function (index, layero) {
                var accountPwdValue = $('input[name=accountPwd]').val();
                testAccountPassWord(ids, accountPwdValue);
            },
            cancel: function (index, layero) {
                layer.closeAll();
            }
        });
    }

})
function testAccountPassWord(ids, accountPwdValue) {
    var reg = /^[0-9]{6}$/;
    if (!accountPwdValue) {
        $('#editPassTip').html('支付密码不能为空');
        $('input[name=accountPwd]').focus();
        return false;
    } else if (!reg.test(accountPwdValue)) {
        $('#editPassTip').html('支付密码必须是6位纯数字');
        $('input[name=accountPwd]').focus();
        return false;
    } else {
        $('#editPassTip').html('');
        toEditAccountPwd(ids, accountPwdValue);
        return true;
    }
}
function toEditAccountPwd(ids, accountPwdValue) {
    $.ajax({
        type: "post",
        url: basePath + resetCardPasswordForListUrl,
        async: true,
        data: {
            ids: ids,
            accountPwd: accountPwdValue
        },
        success: function (data) {
            if (data.success == true) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ['提示', "font-size:12px;text-align:center"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: '支付密码重置成功',
                    btn: ["确定"],
                    yes: function (index, layero) {
                        layer.closeAll();
                        cardListSearch();
                    },
                    cancel: function (index, layero) {
                        layer.closeAll();
                        cardListSearch();
                    }

                });
            } else if (data.success == false) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['310px', '160px'],//宽高
                    content: data.msg,
                    time: 2000,
                    btn: ["确定"]
                });

            }
        }
    });
}
//数据导出	=====================
$('#dataExport').on("click", function () {
    toGiveHiddenInput();
    var obj = {
        ucExternalCardNumber: $('input[name=ucExternalCardNumber]').val(),
        ucInternalCardNumber: $('input[name=ucInternalCardNumber]').val(),
        ucCpyId: $('input[name=ucCpyId]').val(),
        ucStatus: $('input[name=ucStatus]').val(),
        isApp: $('input[name=isApp]').val(),
        ucType: $('input[name=ucType]').val(),
        userAccount:$('input[name=userAccount]').val()
    };
    window.location.href = basePath + exportCardUrl + '?ucExternalCardNumber='
    + obj.ucExternalCardNumber + '&ucInternalCardNumber='
    + obj.ucInternalCardNumber + '&ucCpyId='
    + obj.ucCpyId + '&ucStatus='
    + obj.ucStatus + '&isApp='
    + obj.isApp + '&ucType='
    + obj.ucType+ '&userAccount='
    + obj.userAccount;
});