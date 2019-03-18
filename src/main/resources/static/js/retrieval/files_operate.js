/**
 * 文件检索的验证提交操作js
 * wanghuidong modify 2018-12-13
 */
$(document).ready(function () {
    //wanghuidong modify 2018-12-10 检索按钮点击事件判断校验文件和关键字信息
    $("#btn_sub").click(function () {
        // 判断文件是否已经选择
        if ($("#file").val()==null||$("#file").val().length<1) {
            $("#alertDiv").empty();
            $("#alertDiv").append("请选择需要检索的【文件】！");
            showPopBox();
            return false;
        }
        var arr = new Array(".pdf", ".xls", ".xlsx", ".et", ".txt", ".doc", ".docx", ".wps", ".zip");
        var count = 0;
        var fileFormatStr = "";
        for (var i in arr) {
            //判断文件是否符合类型
            if ($("#file").val().indexOf(arr[i])>0) {//文件路径符合条件的一定>0
                count++;
                break;
            }
            //添加提示弹窗的文件格式字符串
            fileFormatStr = fileFormatStr+arr[i];
            if(i!=arr.length-1){
                fileFormatStr = fileFormatStr+"\\";
            }
        }
        if (count == 0) {
            $("#alertDiv").empty();
            $("#alertDiv").append("系统仅支持【"+fileFormatStr+"】格式文档，请重新选择！");
            showPopBox();
            return false;
        }
        //调用right_tree.js中方法获取关键字集合写入隐藏域
        $("#keyWordGroup").val(getKeyWords());
        if ($("#keyWordGroup").val() == '' || $("#keyWordGroup").val() == '[]') {
            $("#alertDiv").empty();
            $("#alertDiv").append("请选择需要检索的【关键字/关键字库】！");
            showPopBox();
            return false;
        }
        processFilesAjax();
    });


    //wanghuidong modify 2018-12-13 Ajax提交form后台处理关键字,刷新显示的DIV,不刷新整个页面,保留关键字和关键字库选中状态
    function processFilesAjax() {
        var formData = new FormData($("#contentForm")[0]);
        $.ajax({
            url: basePath + "files",
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