/**
 * Created by Suo Tian on 2018-05-18.
 */

var Organization = function () {};

Organization.ListAllOrganizations = function(){
    console.log('进入机构管理页面');
    $('.tile_count').fadeOut("slow");
    //$("#rightBlock").hide();
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/organization/list_all_organizations", function () {
        $('#module-title').html("机构列表");
        $("#organizationDataTable").DataTable({
            language: {
                "url": "/js/dataTables/dataTablesLanguage.json"   //中文提示信息
            },
            bLengthChange: false,
            ajax: function (data, callback) {
                $.ajax({
                    url: "/organization/list_all_organizations/queryOrganizationList",
                    success: function (result) {
                        var returnData = {};
                        returnData.data = result;
                        callback(returnData);
                    }
                });
            },
            //列表表头字段
            columns: [
                { "data": "id" },
                { "data": "organizationNumber" },
                { "data": "name" },
                { "data": "address" },
                { "data": "contactName" },
                { "data": "contactPhone" },
                { "data": "contactDepartment" },
                { "data": "property" },
                //新建列的定义
                {
                    className : "td-operation text-center",
                    data: null,
                    defaultContent:"",
                    orderable : false
                }
            ],
            //新建行增加一列button
            "createdRow": function (row, data, index) {
                var $btn = $('<button type="button" class="btn btn-sm btn-primary btn-edit" onclick="Organization.ModifyOrganization(this, '+index+')">修改</button>');
                $('td', row).eq(8).append($btn);
            }
        });
    });
};


Organization.CreateOrganization = function(){
    console.log('进入新增机构页面');
    var getUrl = "/organization/create";

    $("#organizationBlock").empty();
    $("#organizationBlock").load(getUrl, function () {
        $('#organizationModalLabel').text("填写机构信息");
        $('#organizationModal').modal({
            keyboard: true
        });
        $('#form_create_organization').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                organizationNumber:{
                    validators:{
                        notEmpty:{
                            message:'请输入机构编码'
                        }
                    }
                },
                name:{
                    validators:{
                        notEmpty:{
                            message:'请输入名称'
                        }
                    }
                },
                contactPhone:{
                    validators:{
                        numeric: {
                            message: '电话号码格式错误'
                        }
                    }
                },
                property:{
                    validators:{
                        notEmpty:{
                            message:'请选择属性'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/organization/create/submit',
                    data: $("#form_create_organization").serialize(),
                    success: function(data) {
                        validator.resetForm();
                        document.getElementById("form_create_organization").reset();

                        $('#organizationModal').modal('hide');

                        //前端增加一行
                        var rowNode = $("#organizationDataTable").DataTable().row.add({
                            "id": data.id,
                            "organizationNumber": data.organizationNumber,
                            "name": data.name,
                            "address": data.address,
                            "contactName": data.contactName,
                            "contactPhone": data.contactPhone,
                            "contactDepartment": data.contactDepartment,
                            "property": data.property
                        }).draw().node();

                        $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
                    }
                });
            }
        });
        $('.reset').click(function() {
            $('#form_create_organization').data('bootstrapValidator').resetForm(true);
        });
    });
};


Organization.ModifyOrganization = function(obj, rowIndex){
    var tds = $(obj).parents("tr").children('td');
    //rowIndex在删除行操作后会混乱，所以在有删除功能的datatable中要reload。
    var id = tds.eq(0).text();
    var organizationNumber = tds.eq(1).text();
    var name = tds.eq(2).text();
    var address = tds.eq(3).text();
    var contactName = tds.eq(4).text();
    var contactPhone = tds.eq(5).text();
    var contactDepartment = tds.eq(6).text();
    var property = tds.eq(7).text();

    var url = "/organization/modify?id="+id+"&organizationNumber="+organizationNumber+"&name="+name+"&address="+address+"&contactName="+contactName+"&contactPhone="+contactPhone+"&contactDepartment="+contactDepartment+"&property="+property;

    $("#organizationBlock").empty();
    $("#organizationBlock").load(url, function () {
        console.log('进入机构信息修改页面，organization\'s id: ' + id);
        $('#organizationModal').modal({
            keyboard: true
        });
        $('#form_modify_organization').bootstrapValidator({
            //根据验证结果显示的各种图标
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                id:{
                    validators:{
                        notEmpty:{
                            message:'机构号不能为空'
                        }
                    }
                },
                organizationNumber:{
                    validators:{
                        notEmpty:{
                            message:'请输入机构编码'
                        }
                    }
                },
                name:{
                    validators:{
                        notEmpty:{
                            message:'请输入名称'
                        }
                    }
                },
                contactPhone:{
                    validators:{
                        numeric: {
                            message: '电话号码格式错误'
                        }
                    }
                },
                property:{
                    validators:{
                        notEmpty:{
                            message:'请选择属性'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/organization/modify/submit',
                    data: $("#form_modify_organization").serialize(),
                    success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                        $('#organizationModal').modal('hide');

                        //刷新机构信息列表页面
                        var table = $("#organizationDataTable").DataTable();
                        table.cell(rowIndex,1).data(data.organizationNumber);
                        table.cell(rowIndex,2).data(data.name);
                        table.cell(rowIndex,3).data(data.address);
                        table.cell(rowIndex,4).data(data.contactName);
                        table.cell(rowIndex,5).data(data.contactPhone);
                        table.cell(rowIndex,6).data(data.contactDepartment);
                        table.cell(rowIndex,7).data(data.property);

                        var rowNode = table.row(rowIndex).draw().node()
                        $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
                    }
                });
            }
        });
        $('.reset').click(function() {
            $('#form_modify_organization').data('bootstrapValidator').resetForm(true);
        });
    });
};


//Organization.CreateOrganizationSubmit = function(){
//    event.preventDefault();
//    $.ajax({
//        type: 'post',
//        url: '/organization/create/submit',
//        data: $("#form_create_organization").serialize(),
//        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
//            $('#organizationModal').modal('hide');
//
//            //前端增加一行
//            var rowNode = $("#organizationDataTable").DataTable().row.add({
//                "id": data.id,
//                "organizationNumber": data.organizationNumber,
//                "name": data.name,
//                "address": data.address,
//                "contactName": data.contactName,
//                "contactPhone": data.contactPhone,
//                "contactDepartment": data.contactDepartment,
//                "property": data.property
//            }).draw().node();
//
//            $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
//        }
//    });
//};


Organization.ModifyOrganizationSubmit = function(rowIndex){
    event.preventDefault();
    $.ajax({
        type: 'post',
        url: '/organization/modify/submit',
        data: $("#form_modify_organization").serialize(),
        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
            $('#organizationModal').modal('hide');

            //刷新机构信息列表页面
            var table = $("#organizationDataTable").DataTable();
            table.cell(rowIndex,1).data(data.organizationNumber);
            table.cell(rowIndex,2).data(data.name);
            table.cell(rowIndex,3).data(data.address);
            table.cell(rowIndex,4).data(data.contactName);
            table.cell(rowIndex,5).data(data.contactPhone);
            table.cell(rowIndex,6).data(data.contactDepartment);
            table.cell(rowIndex,7).data(data.property);

            var rowNode = table.row(rowIndex).draw().node()
            $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
        }
    });
};
