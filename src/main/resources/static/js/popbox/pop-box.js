/**
 * Created by wanghuidong on 2018/12/10.
 * 弹窗层js文件
 * 页面必须有id=popupDiv的DIV层
 */


/**
 * 打开遮罩层弹窗
 * wanghuidong add 2018-12-07
 */
function showPopBox(){
    var popupDivObj = $("#popupDiv");
    var windowWidth = $(window).width();
    var windowHeight = $(window).height();
    //火狐浏览器不需要增加滚动条高度
    // var scroll2Left = $(document).scrollLeft();
    // var scroll2Top = $(document).scrollTop();
    var popupDivHeight = popupDivObj.height();
    var popupDivWidth = popupDivObj.width();
    //添加并显示遮罩层
    if($("#pop-box-maskDiv")){
        //解决空格键焦点无限添加遮罩层
        $("#pop-box-maskDiv").remove();
    }
    $("<div id='pop-box-maskDiv'></div>").addClass("mask").width($(document).width())
        .height($(document).height()).click(function(){
        hidePopBox();
    }).appendTo("body").fadeIn(200);
    //火狐浏览器不需要增加滚动条高度
    // popupDivObj.css({"position": "absolute"}).animate({top:scroll2Top+(windowHeight/2)-popupDivHeight/2, left:scroll2Left+(windowWidth/2)-popupDivWidth/2, opacity: "show" }, "fast");
    popupDivObj.css({"position": "absolute"}).animate({top:(windowHeight/2)-popupDivHeight/2, left:(windowWidth/2)-popupDivWidth/2, opacity: "show" }, "fast");
}

/**
 * 关闭遮罩层弹窗
 * wanghuidong add 2018-12-07
 */
function hidePopBox() {
    $("#pop-box-maskDiv").remove();
    $("#popupDiv").animate({left: 0, top: 0, opacity: "hide" }, "fast");
}