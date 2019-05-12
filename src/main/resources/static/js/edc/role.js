/**
 * Created by Suo Tian on 2018-05-20.
 */

var Role = function () {
};

Role.SetRoles = function(){
    console.log('进入权限设置页面');
    $('.tile_count').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/roles/set", function () {
        $('#module-title').html("设置人员权限");
        //ajax获得已经设置好的权限
        $.ajax({
            type: 'get',
            url: '/roles/set/queryCurrentRoles',
            success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                $(".select2-theme").select2({
                    language: "zh-CN",
                    width: "100%", //设置下拉框的宽度
                    theme: "classic",
                    allowClear: true
                    //placeholder: '单击...',
                });

                $("#projectManager").val(data.projectManager).trigger('change');
                $("#doctor").val(data.doctor).trigger('change');
                $("#statistician").val(data.statistician).trigger('change');
            }
        });

    });
};

Role.SetRolesSubmit = function(){
    event.preventDefault();
    $.ajax({
        type: 'post',
        url: "/roles/set/submit",
        data: $("#form_set_roles").serialize(),
        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
            alert(data);
            document.getElementById('return_back_to_mainpage').click();
        }
    });
};

//Role.resetSelect2 = function () {
//    //select2插件需要手工重置。
//    $(".select2-theme").val("").trigger("change");
//};