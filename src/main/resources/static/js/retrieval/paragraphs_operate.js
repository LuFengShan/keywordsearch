/**
 * 段落文字检索的验证提交等操作的js
 * wanghuidong modify 2018-12-13
 */
$(document).ready(function () {
    //wanghuidong modify 2018-12-13校验内容以及Ajax提交
    $("#btn01").click(function () {
        //调用right_tree.js中方法获取关键字集合写入隐藏域
        $("#keyWordGroup").val(getKeyWords());
        //wanghuidong add 2018-12-10 添加判断是否填写了待检查文字
        var flag = "1";
        if ($("#keyWordGroup").val() == '' || $("#keyWordGroup").val() == '[]') {
            $("#alertDiv").empty();
            $("#alertDiv").append("请选择需要检索的【关键字/关键字库】！");
            flag = "0";
        }
        if ($('#textContent').val() == null || $('#textContent').val().length < 1) {
            $("#alertDiv").empty();
            $("#alertDiv").append("请输入检索内容！");
            flag = "0";
        }
        if (flag == "0") {
            showPopBox();
            return false;
        }
        processParagraphsAjax();
    });

    //wanghuidong modify 2018-12-13 Ajax提交form后台处理关键字,刷新显示的DIV,不刷新整个页面,保留关键字和关键字库选中状态
    function processParagraphsAjax() {
        var formData = new FormData($("#contentForm")[0]);
        $.ajax({
            url: basePath + "paragraphs",
            type: "POST",
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                $("#changeDiv").empty();
                $("#changeDiv").append(data);
            },
            error: function (e) {
                alert("检索过程发生错误！");
            }
        });
    }
});