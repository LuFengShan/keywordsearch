/**
 * 右侧树结构加载选中等操作
 * wanghuidong modify 2018-12-13
 */
$(document).ready(function () {
    // 控制关键字的行为，如果选择关键字，则关键字库标签隐藏，选择关键字库，关键字标签隐藏
    $('input[type=radio][name=bedStatus]').on('change', function () {
        switch ($(this).val()) {
            case 'option1':
                $("#select01").show();
                $("#select02").hide();
                break;
            case 'option2':
                $("#select01").hide();
                $("#select02").css('dispaly', 'block');
                //折叠全部节点
                $('#tree').treeview('collapseAll',{silent:true});
                $("#select02").show();
                break;
        }
        $("#keyWordGroup").val("");//wanghuidong add 2018-12-14 切换单选框,清空传递后台的隐藏域
    });
    //初始化关键字库的树结构
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "queryKeyWordDocker",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (result) {
            // alert(JSON.stringify(result));
            $('#tree').treeview({
                data: result,
                showIcon: false,
                multiSelect: true,//支持多选
                onNodeSelected: changeNodeSelectedState,//节点选中事件
                onNodeUnselected: changeNodeSelectedState//节点取消选中事件
            });
        },
        error: function () {
            alert("关键字库列表加载失败！！！");
        }
    });

    //父子节点的选中和取消选中事件
    var nodeSelectedSilent = false;
    function changeNodeSelectedState(event, node) {
        var nodeSelectedState = "";//选中取消选中方法名
        var nodeECState = "";//展开折叠方法名
        if(node.state.selected){
            nodeSelectedState = "selectNode";//选中节点
            nodeECState = "expandNode";//折叠节点
        }else {
            nodeSelectedState = "unselectNode";//取消选中节点
            nodeECState = "collapseNode";//折叠取消选中节点
        }
        if (nodeSelectedSilent) {
            return;
        }
        nodeSelectedSilent = true;
        if(node.parentId==undefined){//点击选中父级节点(关键字库名称)
            changeAllChildNodeSelected(node,nodeSelectedState);//选中全部子节点(关键字)
            //选中展开,取消选中折叠
            $('#tree').treeview(nodeECState, [node.nodeId,{silent:true}]);
            //$('#tree').treeview("buildStyleOverride", node);//渲染背景
        }else{//点击选中子节点(关键字)
            //判断是否需要选中父节点(关键字库名称)
            var parentNode = $("#tree").treeview("getParent", node.nodeId);//得到点击节点的父节点
            var parentNodeChildSelectedCount = 0;//记录选中的子节点个数
            var parentNodeChildsLen = parentNode.nodes.length;//父节点下所有子节点数
            if(parentNodeChildsLen>0){
                for (var x in parentNode.nodes) {
                    if (parentNode.nodes[x].state.selected) {
                        parentNodeChildSelectedCount++;
                    }
                }
            }
            if (parentNodeChildSelectedCount == parentNodeChildsLen){//当前点击的兄弟节点全部被选
                $('#tree').treeview('selectNode', [parentNode.nodeId,{silent:true}]);//子节点全部选中则选中父节点
            }else {//子节点存在不选中状态的则取消选中父节点
                $('#tree').treeview('unselectNode', [parentNode.nodeId,{silent:true}]);
            }
        }
        nodeSelectedSilent = false;
    }

    //改变所有子节点选中或者取消选中
    function changeAllChildNodeSelected(node,nodeSelectState) {
        $('#tree').treeview(nodeSelectState, node.nodeId, {silent: true});
        if (node.nodes != null && node.nodes.length > 0) {
            for (var i in node.nodes) {
                changeAllChildNodeSelected(node.nodes[i],nodeSelectState);
            }
        }
    }
});

/**
 * 文件和文字检索的js文件调用此方法
 * 获取要提交检索的关键字信息
 * @returns 返回关键字字符串格式:关键字1,关键字2,关键字3...
 */
function getKeyWords(){
    //判断选择的是关键字还是关键字库
    var optionsRadios1 = $("#optionsRadios1").is(":checked");
    var optionsRadios2 = $("#optionsRadios2").is(":checked");
    if (optionsRadios1) {//关键字下拉框处理
        return $("#sel01").val();
    }
    if (optionsRadios2) {//关键字库处理
        var selectedNodes = $('#tree').treeview('getSelected');
        var returnKeywords = "";
        for(var i in selectedNodes){
            if (selectedNodes[i].parentId!=undefined) {//父节点(关键字库名称)不参与关键字校验
                if(returnKeywords.indexOf(selectedNodes[i].text)<0){//不存在此关键字则追加
                    returnKeywords += selectedNodes[i].text+",";//拼接逗号
                }
            }
        }
        //去除多余的逗号
        returnKeywords=returnKeywords.substring(0,returnKeywords.length-1);
        return returnKeywords;
    }
}