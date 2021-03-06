//获取集中器列表的menuId
var getMenuMap = window.localStorage.getItem('menuMap');
var getCurruntMap = JSON.parse(getMenuMap);
var menuId = getCurruntMap['集中器列表'];
$(function () {
    ctrlMenu(menuId);
    //去加载表格的数据
    setTimeout("concentratorListSearch()", 100);
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
            if (data.length == 0) {
                return;
            } else {
                for (var i = 0; i < data.length; i++) {
                    var contents = data[i].contents;
                    if (contents.indexOf('新建') > -1) {
                        $('#add').show();
                    }
                    if (contents.indexOf('删除') > -1) {
                        $('#delete').show();
                    }

                }
            }


        }
    });
}


function concentratorListSearch() {
    toGiveHiddenInput();
    initTable("concentratorListForm", "concentratorListPage", getConcentratorListUrl, concentratorListCallback);
}
//下拉选项
toUnbindEvent();
function toUnbindEvent() {
    $('.areaCodeBlock').unbind();
    selectModel();
}
//表格数据
function concentratorListCallback(req) {
    var data = req.dataObject;
    var pageNum = req.pager.pageNo;
    var listTr = "";
    for (var i = 0; i < data.length; i++) {
        listTr += '<tr><td><input type="checkbox" name="ids" class="selectedBox" value="' + data[i].concentratorId + '"/></td>'
        + '<td class="concentratorList_concentratorId" ><t class="concentratorIdHref" data-value="' + data[i].concentratorId + '">' + data[i].concentratorId
        + '</t></td><td class="concentratorList_concentratorName"><t class="concentratorIdHref" data-value="'+ data[i].concentratorId+ '">' + data[i].concentratorName
        + '</t></td><td class="concentratorList_state">' + data[i].state
        + '</td><td class="concentratorList_creatTime">' + data[i].creatTime
        + '</td></tr>';
    }
    $("#myTbogy").html(listTr);
}

//新增
$('#add').on('click', function () {
    window.location.href = "add_concentrator.html";
});

//点击删除
$("body").off("click", "#delete").on("click", "#delete", function () {
    layer.closeAll();
    layer.open({
        type: 1,
        offset: '100px',
        title: ["确认", "font-size:12px;text-align:left;padding-left:20px;"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '180px'],//宽高
        content: "确定删除吗",
        btn: ["确定", "取消"],
        yes: function (index, layero) {
            layer.closeAll();
            deleteConcentrator();
        },
        cancel: function (index, layero) {
            layer.closeAll();
        }
    });
});
function deleteConcentrator() {
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
            content: '请选择删除表单元素',
            time: 3000,
            btn: ["确定"]
        });
    } else {
        $.ajax({
            type: "post",
            url: basePath + removeConcentratorUrl,//删除集中器的接口？？？？？？需要的参数？？？？？
            async: true,
            data: {
                concentratorId: ids
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
    var concentratorName = $('#concentratorName').val();
    var simMac = $('#simMac').val();
    var areaCodeHtmlValue = $('#areaCodeHtml').attr('data-value');
    if (concentratorName == "") {
        $('input[name=concentratorName]').val('');
    } else {
        $('input[name=concentratorName]').val(concentratorName);
    }
    if (simMac == "") {
        $('input[name=simMac]').val('');
    } else {
        $('input[name=simMac]').val(simMac);
    }
    if (areaCodeHtmlValue == "") {
        $('input[name=simType]').val('');
    } else {
        $('input[name=simType]').val(areaCodeHtmlValue);
    }
}
//sim运营商的类别
$('.areaCodeUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
})

//监听回车键
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        concentratorListSearch();
    }
});
//点击跳转
$("body").on("click", ".concentratorIdHref", function () {
    var concentratorId = $(this).attr('data-value');
    window.location.href = 'concentrator_detail.html?concentratorId=' + concentratorId;
})
//单个禁用
$('body').on('click', 'input[name=ids]', function () {
    var flag = $(this).prop('checked');
    if (flag == true) {
        $(this).parents('td').parents('tr').siblings().find('input[name=ids]').prop('checked', false).attr('disabled', true);
    } else {
        $(this).parents('td').parents('tr').siblings().find('input[name=ids]').attr('disabled', false);
    }
})

