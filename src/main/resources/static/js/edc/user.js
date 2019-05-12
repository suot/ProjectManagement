/**
 * Created by Suo Tian on 2018-05-18.
 */
var User = function () {
};

User.ListAllUsers = function(){
    console.log('进入人员信息列表页面');
    $('.page-title').fadeOut("slow");
    //$("#rightBlock").hide();
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/user/list_all_users", function () {
        $('#module-title').html("人员列表");

        $("#userDataTable").DataTable({
            language: {
                "url": "/js/dataTables/dataTablesLanguage.json"   //中文提示信息
            },
            bLengthChange: false,
            ajax: function (data, callback) {
                $.ajax({
                    url: "/user/list_all_users/queryUserInfoList",
                    success: function (result) {
                        var returnData = {};
                        returnData.data = result;
                        callback(returnData);
                    }
                });
            },
            //列表表头字段
            columns: [
                { "data": "user.username" },
                { "data": "user.name" },
                { "data": "user.phone" },
                { "data": "user.cellphone" },
                { "data": "user.email" },
                { "data": "organization.name" },
                { "data": "user.role" },
                { "data": "user.valid" },
                { "data": "user.activated" },
                //新建列的定义
                {
                    className : "td-operation text-center",
                    data: null,
                    defaultContent:"",
                    orderable : false
                }
            ],
            //新建列的 数据内容
            "createdRow": function (row, data, index) {
                //行渲染回调,在这里可以对该行dom元素进行任何操作
                var $btn = $('<button type="button" class="btn btn-sm btn-primary btn-edit" onclick="User.ModifyUserByAdmin(this, '+index+')">修改</button>');
                $('td', row).eq(9).append($btn);
            }
        });
        //试用editor来编辑dataTable,报错找不到Editor这个constructor.
        //editor = new $.fn.dataTable.Editor( {
        //    //ajax: "../php/staff.php",
        //    table: "#userDataTable",
        //    fields: [ {
        //        label: "用户名:",
        //        name: "username"
        //    }, {
        //        label: "姓名:",
        //        name: "name"
        //    }, {
        //        label: "座机:",
        //        name: "phone"
        //    }, {
        //        label: "手机号:",
        //        name: "cellphone"
        //    }, {
        //        label: "邮箱:",
        //        name: "email"
        //    }, {
        //        label: "单位:",
        //        name: "organizationId",
        //        //type: "datetime"
        //    }, {
        //        label: "有效性:",
        //        name: "valid"
        //    }, {
        //        label: "已激活:",
        //        name: "activated"
        //    }
        //    ],
        //    formOptions: {
        //        bubble: {
        //            title: 'Edit',
        //            buttons: false
        //        }
        //    }
        //} );
        //
        //
        //$('#userDataTable').on( 'click', 'tbody td', function (e) {
        //    if ( $(this).index() > 0 ) {
        //        editor.bubble( this );
        //    }
        //} );
        //
        //var table = $("#userDataTable").DataTable({
        //    language: lang,  //中文提示信息
        //    dom: "Bfrtip",
        //    ajax: function (data, callback) {
        //        $.ajax({
        //            url: "/user/list_all_users/queryUserInfoList",
        //            success: function (result) {
        //                var returnData = {};
        //                returnData.data = result;
        //                callback(returnData);
        //            }
        //        });
        //    },
        //    //列表表头字段
        //    columns: [
        //        {
        //            data: null,
        //            defaultContent: '',
        //            className: 'select-checkbox',
        //            orderable: false
        //        },
        //        { "data": "username" },
        //        { "data": "name" },
        //        { "data": "phone" },
        //        { "data": "cellphone" },
        //        { "data": "email" },
        //        { "data": "organizationId" },
        //        { "data": "valid" },
        //        { "data": "activated" }
        //    ],
        //    select: {
        //        style:    'os',
        //        selector: 'td:first-child'
        //    },
        //    buttons: [
        //        { extend: "create", editor: editor },
        //        { extend: "edit", editor: editor }
        //    ]
        //});
    });
};


User.CreateUser = function(){
    $("#userBlock").empty();
    $("#userBlock").load("/user/create", function () {
        console.log('进入新建人员页面');
        $('#userModalLabel').text("填写人员信息");
        $('#userModal').modal({
            keyboard: true
        });
        $('#form_create_user').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                username:{
                    validators:{
                        notEmpty:{
                            message:'请输入用户名'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]+$/,
                            message: '用户名只能包含字母或数字'
                        },
                        callback: {
                            message: '用户名不能是admin',
                            callback: function(value, validator) {
                                var usernameInput = $('#username').val();
                                return usernameInput != "admin";
                            }
                        }
                    }
                },
                password:{
                    validators:{
                        notEmpty:{
                            message:'请输入密码'
                        },
                        different: {
                            field: 'username',
                            message: '密码不能与用户名相同'
                        }
                    }
                },
                name:{
                    validators:{
                        notEmpty:{
                            message:'请输入姓名'
                        }
                    }
                },
                phone:{
                    validators:{
                        numeric: {
                            message: '座机号码格式错误'
                        }
                    }
                },
                cellphone:{
                    validators:{
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                },
                email:{
                    validators:{
                        emailAddress: {
                            message: '邮箱地址格式有误'
                        }
                    }
                },
                organizationId:{
                    validators:{
                        notEmpty:{
                            message:'请选择机构'
                        }
                    }
                },
                role:{
                    validators:{
                        notEmpty:{
                            message:'请选择角色'
                        }
                    }
                },
                valid:{
                    validators:{
                        notEmpty:{
                            message:'请选择有效性'
                        }
                    }
                },
                activated:{
                    validators:{
                        notEmpty:{
                            message:'请选择激活状态'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/user/create/submit',
                    data: $("#form_create_user").serialize(),
                    success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                        validator.resetForm();
                        document.getElementById("form_create_user").reset();
                        $('#userModal').modal('hide');
                        //前端增加一行
                        var rowNode = $("#userDataTable").DataTable().row.add(data).draw().node();
                        $(rowNode).css('color', 'blue').animate({ color: 'black' });
                    }
                });
            }
        });
        $('.reset').click(function() {
            $('#form_create_user').data('bootstrapValidator').resetForm(true);
        });
    });
};


User.ModifyUserByAdmin = function(obj, rowIndex){
    var tds = $(obj).parents("tr").children('td');
    //rowIndex在删除行操作后会混乱，所以在有删除功能的datatable中要reload或者用username查找该行。
    var username = tds.eq(0).text();
    var url = "/user/modify_admin?username="+username;

    $("#userBlock").empty();
    $("#userBlock").load(url,function () {
        console.log('进入人员信息修改页面，username: ' + tds.eq(0).text());
        $('#userModal').modal({
            keyboard: true
        });
        $('#form_modify_user_admin').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                username:{
                    validators:{
                        notEmpty:{
                            message:'用户名不能为空'
                        }
                    }
                },
                name:{
                    validators:{
                        notEmpty:{
                            message:'请输入姓名'
                        }
                    }
                },
                phone:{
                    validators:{
                        numeric: {
                            message: '座机号码格式错误'
                        }
                    }
                },
                cellphone:{
                    validators:{
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                },
                email:{
                    validators:{
                        emailAddress: {
                            message: '邮箱地址格式有误'
                        }
                    }
                },
                organizationId:{
                    validators:{
                        notEmpty:{
                            message:'请选择机构'
                        }
                    }
                },
                role:{
                    validators:{
                        notEmpty:{
                            message:'请选择角色'
                        }
                    }
                },
                valid:{
                    validators:{
                        notEmpty:{
                            message:'请选择有效性'
                        }
                    }
                },
                activated:{
                    validators:{
                        notEmpty:{
                            message:'请选择激活状态'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/user/modify_admin/submit',
                    data: $("#form_modify_user_admin").serialize(),
                    success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                        validator.resetForm();
                        document.getElementById("form_modify_user_admin").reset();
                        $('#userModal').modal('hide');
                        //刷新人员信息列表页面
                        var table = $("#userDataTable").DataTable();
                        table.cell(rowIndex,1).data(data.user.name);
                        table.cell(rowIndex,2).data(data.user.phone);
                        table.cell(rowIndex,3).data(data.user.cellphone);
                        table.cell(rowIndex,4).data(data.user.email);
                        table.cell(rowIndex,5).data(data.organization.name);
                        table.cell(rowIndex,6).data(data.user.role);
                        table.cell(rowIndex,7).data(data.user.valid);
                        table.cell(rowIndex,8).data(data.user.activated);

                        var rowNode = table.row(rowIndex).draw().node();
                        $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
                    }
                });
            }
        });
        $('.reset').click(function() {
            $('#form_modify_user_admin').data('bootstrapValidator').resetForm(true);
        });
    });
};

//User.ModifyUserByAdminSubmit = function(rowIndex){
//    event.preventDefault();
//    $.ajax({
//        type: 'post',
//        url: '/user/modify_admin/submit',
//        data: $("#form_modify_user_admin").serialize(),
//        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
//            $('#userModal').modal('hide');
//
//            //刷新人员信息列表页面
//            var table = $("#userDataTable").DataTable();
//            table.cell(rowIndex,1).data(data.user.name);
//            table.cell(rowIndex,2).data(data.user.phone);
//            table.cell(rowIndex,3).data(data.user.cellphone);
//            table.cell(rowIndex,4).data(data.user.email);
//            table.cell(rowIndex,5).data(data.organization.name);
//            table.cell(rowIndex,6).data(data.user.role);
//            table.cell(rowIndex,7).data(data.user.valid);
//            table.cell(rowIndex,8).data(data.user.activated);
//
//            var rowNode = table.row(rowIndex).draw().node();
//            $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
//        }
//    });
//};


User.ModifyUser = function(){
    console.log('进入修改个人资料页面');
    $('.page-title').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/user/modify", function () {
        $('#module-title').html("修改个人资料");
        $('#form_modify_user').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                name:{
                    validators:{
                        notEmpty:{
                            message:'请输入姓名'
                        }
                    }
                },
                phone:{
                    validators:{
                        numeric: {
                            message: '座机号码格式错误'
                        }
                    }
                },
                cellphone:{
                    validators:{
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                },
                email:{
                    validators:{
                        emailAddress: {
                            message: '邮箱地址格式有误'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/user/modify/submit',
                    data: $("#form_modify_user").serialize(),
                    //success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                    //
                    //}
                });
            }
        });
        $('.reset').click(function() {
            $('#form_modify_user').data('bootstrapValidator').resetForm(true);
        });
    });
};

User.ModifyPassword = function(){
    console.log('进入修改密码页面');
    $('.page-title').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/user/modify_password", function () {
        $('#module-title').html("修改密码");

        $('#form_modify_password').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            //校验状态：未校验 NOT_VALIDATED 正在校验 VALIDATING  校验成功 VALID 校验失败 INVALID
            // fields 字段 --->  表单内的元素
            fields:{
                /*指定需要校验的元素  通过name数据去指定*/
                oldPasswordInput:{
                    /*配置校验规则  规则不止一个*/
                    validators:{
                        /*配置具体的规则*/
                        notEmpty:{
                            /*校验不成功的提示信息*/
                            message:'请输入旧密码'
                        },
                        identical: {
                            field: 'oldPasswordInput',
                            message: '旧密码输入错误'
                        }
                    }
                },
                password:{
                    validators:{
                        notEmpty:{
                            message:'请输入新密码'
                        }
                    }
                },
                confirmPassword:{
                    validators:{
                        notEmpty:{
                            message:'请输入新密码'
                        },
                        identical: {
                             field: 'password',
                             message: '两次输入的新密码不一致'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                var oldPasswordInput = $("#oldPasswordInput").val();
                var password = $("#password").val();
                $.ajax({
                    type: 'get',
                    url: '/user/modify_password/match?oldPasswordInput=' + oldPasswordInput + '&password=' + password,
                    success: function(result) {
                        if(result){
                            validator.resetForm();
                            document.getElementById("form_modify_password").reset();
                            alert("密码修改成功");
                        }else{
                            validator.updateStatus('oldPasswordInput', 'INVALID', 'identical').validateField('oldPasswordInput');
                        }
                    }
                });
            }
        });

        $('.reset').click(function() {
            $('#form_modify_password').data('bootstrapValidator').resetForm(true);
        });
    });
};


User.ResetPassword = function(){
    console.log('进入重置密码页面');
    $('.page-title').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/user/reset_password", function () {
        $('#module-title').html("重置密码");
            $(".select2-theme").select2({
                language: "zh-CN",
                //width: "100%", //设置下拉框的宽度
                //theme: "classic",
            });

            $('#form_reset_password').bootstrapValidator({
                fields:{
                    username:{
                        validators:{
                            notEmpty:{
                                message:'请选择要重置密码的用户'
                            }
                        }
                    }
                },
                submitHandler: function (validator, form, submitButton) {
                    event.preventDefault();
                    $.ajax({
                        type: 'post',
                        url: '/user/reset_password/submit',
                        data: $("#form_reset_password").serialize(),
                        success: function(result) {
                            alert(result);
                        }
                    });
                }
            });
    });
};