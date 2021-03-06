//获取黑白名单列表的menuId
var getMenuMap = window.localStorage.getItem('menuMap');
var getCurruntMap = JSON.parse(getMenuMap);
var menuId = getCurruntMap['黑白名单列表'];

$(function(){
    ctrlMenu(menuId);
    //去加载表格的数据
    setTimeout('blackWhiteListSearch()', 150);
})

function ctrlMenu(menuId) {
    $.ajax({
        type: "post",
        url: basePath + getSelfButtonTreeUrl,
        async: true,
        data: {
            menuId: menuId
        },
        success: function (req) {
            var data = req.dataObject;
            if(data==null){
                return;
            }
            if (data.length == 0) {
                return;
            } else {
                for (var i = 0; i < data.length; i++) {
                    var contents = data[i].contents;
                    if (contents.indexOf('新建') > -1) {
                        $('#addBlackWhite').show();
                    }
                    if (contents.indexOf('删除') > -1) {
                        $('#deleteListBtn').show();
                    }

                }
            }


        }
    });
}

var cpyId = getUrlParam('cpyId');
var cpyName = getUrlParam('cpyName');
var userAccount = getUrlParam('userAccount');
$('#userAccount').val(userAccount);
$('#companyCode').html(cpyName);
$('#companyCode').attr('data-value',cpyId);
toGiveHiddenInput();

function blackWhiteListSearch() {
    toGiveHiddenInput();
    initTable("blackWhiteListForm", "blackWhiteListPage", getUserBlackWhiteUrl, blackWhiteListCallback);
}
//下拉选项
toUnbindEvent();
function toUnbindEvent() {
    $('.TypeBlock').unbind();
    $('.provinceBlock').unbind();
    $('.cityBlock').unbind();
    $('.cpyNameBlock').unbind();
    $('.addTypeBlock').unbind();
    $('.addFlagBlock').unbind();
    selectModel();
}
//表格数据
function blackWhiteListCallback(req) {
    var data = req.dataObject;
    var pageNum = req.pager.pageNo;
    var listTr = "";
    for (var i = 0; i < data.length; i++) {
        listTr += '<tr><td><input type="checkbox" name="ids" class="selectedBox" value="' + data[i].id + '"/></td>'
            + '<td class="bwList_userAccount">'+data[i].userAccount
            + '</td><td class="bwList_electricPileCode">'+data[i].electricPileCode
            + '</td><td class="bwList_cpyName">' + data[i].cpyName
            + '</td><td class="bwList_typeName">' + data[i].typeName
            + '</td></tr>';
    }
    $("#myCompanyTb").html(listTr);
}
//新建黑白名单
$('#userAccountInput').on('blur',function(){
    var objValue = $('#userAccountInput').val();
    var reg = /^[0-9][\d]*$/;
    $('#userAccountInputTip').html('');
    if (!objValue) {
        $('#userAccountInput').focus();
        $('#userAccountInputTip').html('请输入数字！');
        return false;
    } else if (!reg.test(objValue)) {
        $('#userAccountInput').focus();
        $('#userAccountInputTip').html('请输入数字！');
        return false;
    }
})
$('#electricPileCodeInput').on('blur',function(){
    var objValue = $('#electricPileCodeInput').val();
    var reg = /^[0-9][\d]*$/;
    $('#electricPileCodeInputTip').html('');
    if (!objValue) {
        $('#electricPileCodeInput').focus();
        $('#electricPileCodeInputTip').html('请输入数字！');
        return false;
    } else if (!reg.test(objValue)) {
        $('#electricPileCodeInput').focus();
        $('#electricPileCodeInputTip').html('请输入数字！');
        return false;
    }
})
$("body").off("click", "#addBlackWhite").on("click", "#addBlackWhite", function () {
    layer.closeAll();
    layer.open({
        type: 1,
        offset: '100px',
        title: ["新建黑白名单", "text-align:center"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '340px'],//宽高
        content: $(".toAddBlackWhiteList"),
        btn: ["确定", "取消"],
        yes: function () {
            addTest();
        },
        cancel: function (index, layero) {
            layer.closeAll();
        }
    });
})
function addTest(){
    if(!$('#userAccountInput').val()){
        $('#userAccountInput').focus();
        $('#userAccountInputTip').html('请输入数字！');
        return false;
    }
    if($('#userAccountInputTip').html()){
        $('#userAccountInput').focus();
        return false;
    }
    if($('#addFlag').attr('data-value')==0){
        if(!$('#electricPileCodeInput').val()){
            $('#electricPileCodeInput').focus();
            $('#electricPileCodeInputTip').html('请输入数字！');
            return false;
        }
    }
    if($('#electricPileCodeInputTip').html()){
        $('#electricPileCodeInput').focus();
        return false;
    }
    toAddBlackWhiteList();
    layer.closeAll();
}
function toAddBlackWhiteList(){
    var type = $('#addType').attr('data-value');
    var userAccount = $('#userAccountInput').val();
    var electricPileCode = $('#electricPileCodeInput').val();
    var flag = $('#addFlag').attr('data-value');
    $.ajax({
        type: "post",
        url: basePath + addUserBlackWhiteUrl,
        async: true,
        data: {
            type: type,
            userAccount: userAccount,
            electricPileCode: electricPileCode,
            flag:flag
        },
        success: function (req) {
            if (req.success == true) {
                layer.closeAll();
                layer.open({
                    type: 1,
                    offset: '100px',
                    title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                    shadeClose: false,
                    closeBtn: 1,
                    area: ['320px', '180px'],//宽高
                    content: "设置成功",
                    btn: ["确定"],
                    yes: function (index, layero) {
                        layer.closeAll();
                        toGiveHiddenInput();
                    },
                    cancel: function (index, layero) {
                        layer.closeAll();
                        toGiveHiddenInput();
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
                    content: req.msg,
                    time: 3000,
                    btn: ["确定"]
                });
            }
        }
    });
}
//新建黑白名单类型
$('.addTypeUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
})
//新建黑白名单桩类型
$('.addFlagUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
    var flag = $('#addFlag').attr('data-value');
    if(flag==0){
        $('#electricPileCodeInput').removeAttr('disabled');
    }
    if(flag==1){
        $('#electricPileCodeInput').attr('disabled','disabled');
        $('#electricPileCodeInput').val('');
        $('#electricPileCodeInputTip').html('');
    }
})
//点击删除
$("body").off("click", "#deleteListBtn").on("click", "#deleteListBtn", function () {
    layer.closeAll();
    layer.open({
        type: 1,
        offset: '100px',
        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '180px'],//宽高
        content: "确认删除该记录？",
        btn: ["确定", "取消"],
        yes: function (index, layero) {
            layer.closeAll();
            toSubmitDisAbleCompany();
        },
        cancel: function (index, layero) {
            layer.closeAll();
        }
    });
});
function toSubmitDisAbleCompany() {
    var ids = '';
    $('input[name=ids]').each(function () {
        var flag = $(this).prop('checked');
        if (flag == true) {
            ids = $(this).attr('value');
        }
    });
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
            btn: ["确定"]
        });
    } else {
        $.ajax({
            type: "post",
            url: basePath + removeUserBlackWhiteUrl,
            async: true,
            data: {
                id: ids,
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
                        content: '删除成功',
                        btn: ["确定"],
                        yes: function (index, layero) {
                            layer.closeAll();
                            window.location.reload();
                        },
                        cancel: function (index, layero) {
                            layer.closeAll();
                            window.location.reload();
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
                        btn: ["确定"]
                    });
                }
            }
        });
    }
}

//查询条件部分=========================
function toGiveHiddenInput() {
    var userAccountValue = $('#userAccount').val();
    var electricPileCodeValue = $('#electricPileCode').val();
    var typeValue = $('#listType').attr('data-value');
    var cpyIdValue = $('#companyCode').attr('data-value');
    if (userAccountValue == "") {
        $('input[name=userAccount]').val('');
    } else {
        $('input[name=userAccount]').val(userAccountValue);
    }
    if (electricPileCodeValue == "") {
        $('input[name=electricPileCode]').val('');
    } else {
        $('input[name=electricPileCode]').val(electricPileCodeValue);
    }
    if (typeValue == "") {
        $('input[name=type]').val('');
    } else {
        $('input[name=type]').val(typeValue);
    }
    if (cpyIdValue == "") {
        $('input[name=cpyId]').val('');
    } else {
        $('input[name=cpyId]').val(cpyIdValue);
    }
}
//黑白名单类型
$('.typeUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
})
//渠道筛选部分=============
setTimeout("toLoadProvince('','#provinceCode','.provinceUl','toUnbindEvent')", 250);
$('.provinceUl').on("click", "li", function () {
    $('#cityCode').attr('data-value', '');
    $('#cityCode').html('请选择市');
    $('input[name=cityCode]').val('');
    $('#companyCode').attr('data-value', '');
    $('#companyCode').html('请选择公司');
    $('input[name=cpyId]').val('');
    $('.cpyIdUl').html('');
    var provinceCodeId = $(this).attr('data-option');
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
    var flag = $(this).attr('data-option');
    if (flag == "") {
        $('#provinceCode').html('请选择省');
        $('#provinceCode').attr('data-value', '');
        $('#cityCode').html('请选择市');
        $('#cityCode').attr('data-value', '');
        $('.cityUl').html('');
        $('input[name=cityCode]').val('');
        $('#companyCode').attr('data-value', '');
        $('#companyCode').html('请选择公司');
        $('input[name=cpyId]').val('');
        $('.cpyIdUl').html('');
    } else {
        toLoadCity(provinceCodeId, '', '#cityCode', '.cityUl', 'toUnbindEvent');
    }
})
$('.cityUl').on("click", "li", function () {
    $('#companyCode').attr('data-value', '');
    $('#companyCode').html('请选择公司');
    $('input[name=cpyId]').val('');
    $('.cpyIdUl').html('');
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
    var flag = $(this).attr('data-option');
    var cypCityCodeValue = $('#cityCode').attr('data-value');
    var cpyProvinceCodeValue = $('#provinceCode').attr('data-value');
    if (flag == "") {
        $('#companyCode').attr('data-value', '');
        $('#companyCode').html('请选择公司');
        $('input[name=cpyId]').val('');
        $('.cpyIdUl').html('');
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
        $('#companyCode').html("请选择公司");
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
                var cypCompanyLi = '<li data-option="">请选择公司</li>';
                for (var i = 0; i < dataModule.length; i++) {
                    cypCompanyLi += '<li data-option="' + dataModule[i].cpyId + '" data-cpyNumber="' + dataModule[i].cpyNumber + '">' + dataModule[i].cpyName + '</li>';
                }
                $('.cpyIdUl').html(cypCompanyLi);
                toUnbindEvent();
            }
        }
    });
}
$('.cpyIdUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
})


//监听回车键
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        blackWhiteListSearch();
    }
});
//单个禁用
$('body').on('click', 'input[name=ids]', function () {
    var flag = $(this).prop('checked');
    if (flag == true) {
        $(this).parents('td').parents('tr').siblings().find('input[name=ids]').prop('checked', false).attr('disabled', true);
    } else {
        $(this).parents('td').parents('tr').siblings().find('input[name=ids]').attr('disabled', false);
    }

})