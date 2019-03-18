/**
 * 富文本编辑器summernote的初始化等操作
 * wanghuidong modify 2018-12-13
 */
$(document).ready(function () {
    /////////////////wanghuidong add 注释:即使引入了子页面,但是主页面依然要写初始化方法,不然页面不初始化
    // 初始化富文本编辑器
    $('#textContent').summernote({
        toolbar: [], // 自定义工具栏。
        placeholder: '请把内容粘贴到这里...',
        height: 300,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,             // set maximum height of editor
        focus: true,                 // set focus to editable area after initializing summernote
        lang: 'zh-CN', // 默认为英文
        callbacks: {
            onKeydown: function (e) {
                var t = e.currentTarget.innerText;
                if (t.trim().length > 1000) {
                    //delete key
                    if (e.keyCode != 8)
                        e.preventDefault();
                }
            },
            onKeyup: function (e) {
                var t = e.currentTarget.innerText;
                if (t.trim().length - 1000 > 0) {
                    $('#btn01').attr("disabled", "disabled");
                    $('#maxContentPost').text('提示：检索内容超长（超过' + (t.trim().length - 1000) + '个字符），请检查后重新输入！');
                } else {
                    $('#btn01').removeAttr("disabled");
                    $('#maxContentPost').text('提示：你还可以输入' + (1000 - t.trim().length) + '个字符！');
                }
            },
            // onPaste: function (e) {
            //     var t = e.currentTarget.innerText;
            //     var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
            //     e.preventDefault();
            //     var all = t + bufferText;
            //     document.execCommand('insertText', false, all.trim().substring(0, 1000));
            //     $('#maxContentPost').text(1000 - t.length);
            // },
            onFocus: function (e) {
                var t = e.currentTarget.innerText;
                //wanghuidong modify 2018-12-10 原等式>=0,修改为>0,解决提示错误
                if (t.trim().length - 1000 > 0) {
                    $('#btn01').attr("disabled", "disabled");
                    $('#maxContentPost').text('提示：检索内容超长（超过' + (t.trim().length - 1000) + '个字符），请检查后重新输入！');
                } else {
                    $('#btn01').removeAttr("disabled");
                    $('#maxContentPost').text('提示：你还可以输入' + (1000 - t.trim().length) + '个字符！');
                }
            }
        }
    });
    // 输入内容的校验
    $("#contentForm").validate({
        rules: {
            textContent: {
                required: true,
                minlength: 10,
                maxlength: 1000
            }
        },
        messages: {
            textContent: {
                required: "必须输入搜索内容",
                minlength: "请输入不少于2个字符",
                maxlength: "请输入不多于1000个字符"
            }
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            // Add `has-feedback` class to the parent div.form-group
            // in order to add icons to inputs
            element.parents(".col-sm-5").addClass("has-feedback");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                error.insertAfter(element);
            }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-error").removeClass("has-success");
            $(element).next("span").addClass("glyphicon-remove").removeClass("glyphicon-ok");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-success").removeClass("has-error");
            $(element).next("span").addClass("glyphicon-ok").removeClass("glyphicon-remove");
        }
    });
});