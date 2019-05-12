/**
 * Created by Suo Tian on 2018-05-17.
 */

var Project = function () {};

Project.ListAllProjects = function(){
    console.log('进入项目管理页面');
    $('.page-title').fadeOut("slow");
    //$("#rightBlock").hide();
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/project/list_all_projects", function () {
        $('#module-title').html("项目列表");

        $("#projectDataTable").DataTable({
            language: {
                "url": "/js/dataTables/dataTablesLanguage.json"   //中文提示信息
            },
            bLengthChange: false,
            ajax: function (data, callback) {
                $.ajax({
                    url: "/project/list_all_projects/queryProjectList",
                    success: function (result) {
                        var returnData = {};
                        returnData.data = result;
                        callback(returnData);
                    }
                });
            },
            //列表表头字段
            columns: [
                {"data": "project.id"},
                {"data": "project.projectNumber"},
                {"data": "project.researchId"},
                {"data": "project.abbreviation"},
                {"data": "organization.name"},
                {"data": "projectManagerNames"},
                {"data": "researchOrganizationNames"},
                {"data": "project.status"},
                {"data": "project.moduleList"},
                {"data": "project.planNumber"},
                {"data": "project.batchNumber"},
                {"data": "project.ethicNumber"},
                {"data": "project.description"},
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
                //行渲染回调,在这里可以对该行dom元素进行任何操作
                var $btn = $('<button type="button" class="btn btn-sm btn-primary btn-edit" onclick="Project.ModifyProject(this, '+index+')">修改</button>');
                $('td', row).eq(13).append($btn);
            }
        });

    });
};

Project.CreateProject = function(){
    $("#projectBlock").empty();
    $("#projectBlock").load("/project/create", function () {
        console.log('进入新建项目页面');
        $('#projectModalLabel').text("填写项目信息");
        $('#projectModal').modal({
            keyboard: true
        });

        $(".select2-theme").select2({
            language: "zh-CN",
            width: "100%", //设置下拉框的宽度
            theme: "classic",
            //allowClear: true
            //placeholder: '单击...',
        });

        $('#form_create_project').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                projectNumber:{
                    validators:{
                        notEmpty:{
                            message:'请输入项目编码'
                        }
                    }
                },
                researchId:{
                    validators:{
                        notEmpty:{
                            message:'请输入研究编号'
                        }
                    }
                },
                abbreviation:{
                    validators:{
                        notEmpty:{
                            message:'请输入项目简称'
                        }
                    }
                },
                organizationId:{
                    validators:{
                        notEmpty:{
                            message:'请选择申办单位'
                        }
                    }
                },
                projectManagerUserNames:{
                    validators:{
                        notEmpty:{
                            message:'请选择项目经理'
                        }
                    }
                },
                researchOrganizationIds:{
                    validators:{
                        notEmpty:{
                            message:'请选择临床研究单位'
                        }
                    }
                },
                status:{
                    validators:{
                        notEmpty:{
                            message:'请选择状态'
                        }
                    }
                },
                moduleList:{
                    validators:{
                        notEmpty:{
                            message:'请选择启用模块'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();
                $.ajax({
                    type: 'post',
                    url: '/project/create/submit',
                    data: $("#form_create_project").serialize(),
                    success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                        validator.resetForm();
                        document.getElementById("form_create_project").reset();

                        $('#projectModal').modal('hide');
                        //前端增加一行
                        var rowNode = $("#projectDataTable").DataTable().row.add(data).draw().node();
                        $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
                    }
                });
            }
        });
        $('.reset').click(function() {
            $('#form_create_project').data('bootstrapValidator').resetForm(true);
        });
    });
};

Project.ModifyProject = function(obj, index){
    var tds = $(obj).parents("tr").children('td');
    //rowIndex在删除行操作后会混乱，所以在有删除功能的datatable中要reload。
    var id = tds.eq(0).text();
    var url = "/project/modify?id="+id;

    $("#projectBlock").empty();
    $("#projectBlock").load(url, function () {
        console.log('进入项目信息修改页面，project\'s id: ' + tds.eq(0).text());
        $('#projectModal').modal({
            keyboard: true
        });

        $(".select2-theme").select2({
            language: "zh-CN",
            width: "100%", //设置下拉框的宽度
            theme: "classic"
        });

        $.ajax({
            type: 'get',
            url: '/project/modify/queryCurrentProjectManagers?projectId='+id,
            success: function(data) {
                $("#projectManagerUserNames").val(data).trigger('change');
            }
        });

        $.ajax({
            type: 'get',
            url: '/project/modify/queryCurrentResearchOrganizations?projectId='+id,
            success: function(data) {
                $("#researchOrganizationIds").val(data).trigger('change');
            }
        });

        $.ajax({
            type: 'get',
            url: '/project/modify/queryCurrentResearchOrganizations?projectId='+id,
            success: function(data) {
                $("#researchOrganizationIds").val(data).trigger('change');
            }
        });

        $.ajax({
            type: 'get',
            url: '/project/modify/queryCurrentRandomSettings?projectId='+id,
            success: function(data) {
                if(data) {
                    //如果已经设置了randomSettings，参试机构就不能改了
                    $("#researchOrganizationIds").attr({"disabled": "disabled"});
                }
            }
        });

        $('#form_modify_project').bootstrapValidator({
            /*根据验证结果显示的各种图标*/
            //feedbackIcons: {
            //    valid: 'glyphicon glyphicon-ok',
            //    invalid: 'glyphicon glyphicon-remove',
            //    validating: 'glyphicon glyphicon-refresh'
            //},
            fields:{
                id:{
                    validators:{
                        notEmpty:{
                            message:'项目号不能为空'
                        }
                    }
                },
                projectNumber:{
                    validators:{
                        notEmpty:{
                            message:'请输入项目编码'
                        }
                    }
                },
                researchId:{
                    validators:{
                        notEmpty:{
                            message:'请输入研究编号'
                        }
                    }
                },
                abbreviation:{
                    validators:{
                        notEmpty:{
                            message:'请输入项目简称'
                        }
                    }
                },
                organizationId:{
                    validators:{
                        notEmpty:{
                            message:'请选择申办单位'
                        }
                    }
                },
                projectManagerUserNames:{
                    validators:{
                        notEmpty:{
                            message:'请选择项目经理'
                        }
                    }
                },
                researchOrganizationIds:{
                    validators:{
                        notEmpty:{
                            message:'请选择临床研究单位'
                        }
                    }
                },
                status:{
                    validators:{
                        notEmpty:{
                            message:'请选择状态'
                        }
                    }
                },
                moduleList:{
                    validators:{
                        notEmpty:{
                            message:'请选择启用模块'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                event.preventDefault();

                //去掉参试机构id的disabled属性，否则取不到数。
                $('#researchOrganizationIds').removeAttr("disabled");

                $.ajax({
                    type: 'post',
                    url: '/project/modify/submit',
                    data: $("#form_modify_project").serialize(),
                    success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
                        validator.resetForm();
                        document.getElementById("form_modify_project").reset();

                        $('#projectModal').modal('hide');

                        //刷新机构信息列表页面
                        var table = $("#projectDataTable").DataTable();
                        table.cell(index,1).data(data.project.projectNumber);
                        table.cell(index,2).data(data.project.researchId);
                        table.cell(index,3).data(data.project.abbreviation);
                        table.cell(index,4).data(data.organization.name);
                        table.cell(index,5).data(data.projectManagerNames);
                        table.cell(index,6).data(data.researchOrganizationNames);
                        table.cell(index,7).data(data.project.status);
                        table.cell(index,8).data(data.project.moduleList);
                        table.cell(index,9).data(data.project.planNumber);
                        table.cell(index,10).data(data.project.batchNumber);
                        table.cell(index,11).data(data.project.ethicNumber);
                        table.cell(index,12).data(data.project.description);

                        var rowNode = table.row(index).draw().node();
                        $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
                    }
                });
            }
        });

        $('.reset').click(function() {
            $('#form_modify_project').data('bootstrapValidator').resetForm(true);
        });
    });
};

Project.ListMyProjects = function(){
    console.log('进入切换项目页面');
    $('.page-title').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/project/list_my_projects", function () {
        $('#module-title').html("项目列表");
    });
};

//Project.ModifyProjectSubmit = function(rowIndex){
//    event.preventDefault();
//    $.ajax({
//        type: 'post',
//        url: '/project/modify/submit',
//        data: $("#form_modify_project").serialize(),
//        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
//            $('#projectModal').modal('hide');
//
//            //刷新机构信息列表页面
//            var table = $("#projectDataTable").DataTable();
//            table.cell(rowIndex,1).data(data.project.projectNumber);
//            table.cell(rowIndex,2).data(data.project.researchId);
//            table.cell(rowIndex,3).data(data.project.abbreviation);
//            table.cell(rowIndex,4).data(data.organization.name);
//            table.cell(rowIndex,5).data(data.researchOrganizationNames);
//            table.cell(rowIndex,6).data(data.project.status);
//            table.cell(rowIndex,7).data(data.project.moduleList);
//            table.cell(rowIndex,8).data(data.project.planNumber);
//            table.cell(rowIndex,9).data(data.project.batchNumber);
//            table.cell(rowIndex,10).data(data.project.ethicNumber);
//            table.cell(rowIndex,11).data(data.project.description);
//
//            var rowNode = table.row(rowIndex).draw().node()
//            $( rowNode ).css('color', 'blue').animate( { color: 'black' } );
//        }
//    });
//};