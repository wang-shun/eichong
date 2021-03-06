var menuId = getUrlParam('menuId');
//根据menuId查菜单编辑
toLoadMenuInfoById();
function toLoadMenuInfoById() {
    $.ajax({
        type: "post",
        url: basePath + getMenuByIdUrl,
        async: true,
        data: {
            menuId: menuId
        },
        success: function (req) {
            if (req.success == true) {
                var data = req.dataObject;
                var contents = data.contents;
                var menuType = data.menuType;
                var parentMenuId = data.parentMenuId;
                var parentMenuName = data.parentMenuName;
                var sortNumber = data.sortNumber;
                var url = data.url;
                $('input[name=contents]').val(contents);
                $('input[name=parentMenuId]').val(parentMenuId);
                $('input[name=url]').val(url);
                $('input[name=sortNumber]').val(sortNumber);
                $('#parentMenuInput').val(parentMenuName);
                if (menuType == 1) {
                    $('#menuTypeHtml').html('菜单权限');
                    $('#menuTypeHtml').attr('data-value', '1');

                } else if (menuType == 2) {
                    $('#menuTypeHtml').html('按钮权限');
                    $('#menuTypeHtml').attr('data-value', '2');
                }
            }
        }
    });
}

//点击上级菜单input出发弹框
$('body').off('click', '#parentMenuInput').on('click', '#parentMenuInput', function () {
    selectMenu();
    layer.closeAll();
    layer.open({
        type: 1,
        offset: '100px',
        title: ["查找带回", "font-size:12px;text-align:left;padding-left:20px;"],
        shadeClose: false,
        closeBtn: 1,
        area: ['310px', '380px'],//宽高
        content: $('.menuBlock')
    });
})
function selectMenu() {
    var setting = {
        treeId: '#treeDemo',
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            },
            key: {
                checked: "isSelected",
                children: "menuList",
                name: "contents"
            }

        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    var zNodes = "";
    getZNodes();
    function getZNodes() {
        $.ajax({
            type: "post",
            url: basePath + getMeunTreeUrl,
            async: true,
            success: function (req) {
                if (req.success == true) {
                    zNodes = req.dataObject;
                    zNodes.open = true;
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                }
            }
        });
    }

    //点击每个节点获得每个节点的id和name
    function zTreeOnClick(event, treeId, zNodes) {
        $('input[name=parentMenuId]').val(zNodes.menuId);
        $('#parentMenuInput').val(zNodes.contents);
        layer.closeAll();
    }
}
//菜单类别
function toUnbindEvent() {
    $('.menuTypeBlock').unbind();
    selectModel();
}
toUnbindEvent();
$('.menuTypeUl').on("click", "li", function () {
    $(this).parent().siblings('div.model-select-text').text($(this).text())
        .attr('data-value', $(this).attr('data-option'));
});

//菜单编辑
function toModifyMenu() {
    if (toTestIsEmpty()) {
        var dataObj = {
            contents: $('input[name=contents]').val(),
            parentMenuId: $('input[name=parentMenuId]').val(),
            menuType: $('#menuTypeHtml').attr('data-value'),
            sortNumber: $('input[name=sortNumber]').val(),
            url: $('input[name=url]').val(),
            menuId: menuId
        };
        $.ajax({
            type: "post",
            url: basePath + modifyMenuUrl,
            async: true,
            data: dataObj,
            success: function (data) {
                if (data.success == true) {
                    layer.open({
                        type: 1,
                        offset: '100px',
                        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                        shadeClose: false,
                        closeBtn: 1,
                        area: ['310px', '160px'],//宽高
                        content: data.msg,
                        btn: ["确定"],
                        yes:function(index,layero){
                            window.location.href = 'menu_list.html';
                        },
                        cancel: function (index, layero) {
                            layer.closeAll();
                            window.location.href = 'menu_list.html';
                        }
                    });
                }else if(data.status == 9001) {
                    layer.closeAll();
                    layer.open({
                        type: 1,
                        offset: '100px',
                        title: ["提示", "font-size:12px;text-align:left;padding-left:20px;"],
                        shadeClose: false,
                        closeBtn: 1,
                        area: ['310px', '160px'],//宽高
                        content: '会话超时，请重新登陆！',
                        btn: ["确定"],
                        yes:function(index,layero){
                            layer.closeAll();
                            window.top.location.href = basePath + toLoginUrl;
                        },
                        cancel:function(index,layero){
                            layer.closeAll();
                            window.top.location.href = basePath + toLoginUrl;
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
};
//取消
$('body').off('click', '#goback').on('click', '#goback', function () {
    window.location.href = 'menu_list.html';
})
//文本框必须填写校验函数
function toTestIsEmpty() {
    var contents = $('input[name=contents]').val();
    var sortNumber = $('input[name=sortNumber]').val();
    if (contents == "") {
        $('input[name=contents]').focus();
        $('#contentsTip').html('请填写菜单名称');
        return false;
    }
    if (sortNumber == "") {
        $('#sortNumberTip').html('请填写菜单排序');
        return false;
    }
    return true;
}
$('input[name=contents]').on('blur',function(){
    $('#contentsTip').html('');
    var contents = $('input[name=contents]').val();
    if (contents == "") {
        $('input[name=contents]').focus();
        $('#contentsTip').html('请填写菜单名称');
    }
})
$('input[name=sortNumber]').on('blur',function(){
    $('#sortNumberTip').html('');
    var sortNumber = $('input[name=sortNumber]').val();
    var reg=/^[0-9]*[1-9][0-9]*$/;
    if (sortNumber == "") {
        $('#sortNumberTip').html('请填写菜单排序');
    }else if(!reg.test(sortNumber)) {
        $('#sortNumberTip').html('输入有误，请重新填写菜单排序！');
    }
})