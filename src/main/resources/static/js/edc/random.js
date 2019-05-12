/**
 * Created by Suo Tian on 2018-05-31.
 */

var Random = function () {
};


//随机分组
Random.RandomGrouping = function(){
    console.log('进入随机分组页面');
    $('.tile_count').fadeOut("slow");
    //$("#rightBlock").hide();
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/random/randomGrouping", function () {
        $('#module-title').html("随机分组");
        var validator = $("#commentForm").validate({
            rules: {
                simple_size: {
                    required: true,
                    digits:true,
                    min:1
                },
                random_seed: {
                    required: true,
                    digits:true,
                    min:1
                },
                'group_type[]': {
                    required: true,
                },
                'group_name[]': {
                    required: true,
                },
                'occupy[]': {
                    required: true,
                    digits:true,
                    min:1
                },
                'subject_number[]': {
                    required: true,
                    digits:true,
                    min:1
                },
                'group_type[]': {
                    required: true,
                },
                'enroll_num[]':{
                    required: true,
                    digits:true
                },
                num_of_length:{
                    required: true,
                    digits:true,
                    min:1
                },
                num_of_block:{
                    required: true,
                    digits:true,
                    min:1
                }
            },
            submitHandler:function(form){
                //点生成按钮后禁用该按钮，防止反复提交
                $("#generateRandomButton").attr({"disabled":"disabled"});
                event.preventDefault();
                console.log('点击了随机分组生成按钮');

                $.ajax({
                    type: 'post',
                    url: '/random/generate',
                    data: $("#commentForm").serialize(),
                    success: function(data) {
                        alert(data);
                        //$('#commentForm').data('bootstrapValidator').resetForm(true);
                        //$("#generateRandomButton").removeAttr("disabled");
                    }
                });
            }
        });

        $('#rootwizard').bootstrapWizard({
      	    'tabClass': 'bwizard-steps',
      	    //'tabClass': 'nav nav-tabs',
      	    'onNext':function(tab,navigation,index){
                var status=true;
      	        $('#tab2_simple_size').val($('#simple_size').val());
                $('#tab5_simple_size').val($('#simple_size').val());
      	        $('#tab5_fzbl').val(showProp());

                if(!$("#commentForm").valid()) {
                    validator.focusInvalid();
                    return false;
                }else{
                    if(index==2){
                        var elementValidateResult = true;
                        //因为tab2中的table里的每一行input name相同，jquery validator只校验第1行，因此手动遍历校验其余行。
                        $('#syfz_table tbody tr').each(function(){
                            tds=$(this).children();
                            var groupNumber = tds.eq(0).find('input').val();
                            var groupType = tds.eq(1).find('input').val();
                            var groupingRatio =  tds.eq(2).find('input').val();

                            if(groupNumber == ""){
                                elementValidateResult = validator.element(tds.eq(0).find('input'));
                                validator.focusInvalid();
                            }
                            if(groupType == ""){
                                elementValidateResult = validator.element(tds.eq(1).find('input'));
                                validator.focusInvalid();
                            }
                            if(groupingRatio == ""){
                                elementValidateResult = validator.element(tds.eq(2).find('input'));
                                validator.focusInvalid();
                            }
                        });

                        if(!elementValidateResult){
                            return false;
                        }
                    }
                }
      	    },
            onTabClick:function(tab, navigation, index) {
                return false;
            }
        });

        //$('#commentForm').bootstrapValidator({
        //    fields:{
        //        //tab1
        //        unblind_mgr:{
        //            validators:{
        //                notEmpty:{
        //                    message:'请输入揭盲负责人'
        //                }
        //            }
        //        },
        //        simple_size:{
        //            validators:{
        //                notEmpty:{
        //                    message:'请输入样本量'
        //                },
        //                integer:{
        //                    message:'样本量必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'样本量必须是正整数'
        //                },
        //            }
        //        },
        //        random_seed:{
        //            validators:{
        //                notEmpty:{
        //                    message:'请输入随机种子'
        //                },
        //                integer:{
        //                    message:'随机种子必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'随机种子必须是正整数'
        //                }
        //            }
        //        },
        //        //tab2
        //        tab2_simple_size:{
        //            validators:{
        //                notEmpty:{
        //                    message:'样本量不能为空'
        //                },
        //                integer:{
        //                    message:'样本量必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'样本量必须是正整数'
        //                },
        //                identical: {
        //                    field: 'simple_size',
        //                    message: '样本量必须与前一页所设的值一致'
        //                }
        //            }
        //        },
        //        num_of_group:{
        //            validators:{
        //                notEmpty:{
        //                    message:'试验组数不能为空'
        //                },
        //                integer:{
        //                    message:'试验组数必须是大于等于2的正整数'
        //                },
        //                greaterThan:{
        //                    value: 1,
        //                    message:'试验组数必须是大于等于2的正整数'
        //                }
        //            }
        //        },
        //        'group_type[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'试验组号不能为空'
        //                },
        //            }
        //        },
        //        'group_name[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'试验组名不能为空'
        //                },
        //            }
        //        },
        //        'occupy[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'分组比例不能为空'
        //                },
        //                integer:{
        //                    message:'分组比例必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'分组比例必须是正整数'
        //                }
        //            }
        //        },
        //        'subject_number[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'受试者数不能为空'
        //                },
        //                integer:{
        //                    message:'受试者数必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'受试者数必须是正整数'
        //                }
        //            }
        //        },
        //        //tab3
        //        'org_id[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'中心编号不能为空'
        //                }
        //            }
        //        },
        //        'org_name[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'中心名称不能为空'
        //                }
        //            }
        //        },
        //        'enroll_num[]':{
        //            validators:{
        //                notEmpty:{
        //                    message:'中心受试者控制数不能为空'
        //                },
        //                integer:{
        //                    message:'中心受试者控制数必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'中心受试者控制数必须是正整数'
        //                }
        //            }
        //        },
        //        //tab5
        //        tab5_simple_size:{
        //            validators:{
        //                notEmpty:{
        //                    message:'受试者总数不能为空'
        //                },
        //                integer:{
        //                    message:'样本量必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'样本量必须是正整数'
        //                },
        //                identical: {
        //                    field: 'simple_size',
        //                    message: '样本量必须与前一页所设的值一致'
        //                }
        //            }
        //        },
        //        num_of_length:{
        //            validators:{
        //                notEmpty:{
        //                    message:'区段长度不能为空'
        //                },
        //                integer:{
        //                    message:'区段长度必须是正整数'
        //                },
        //                greaterThan:{
        //                    value: 0,
        //                    message:'区段长度必须是正整数'
        //                },
        //            }
        //        },
        //        num_of_block:{
        //            validators: {
        //                notEmpty: {
        //                    message: '区段个数不能为空'
        //                },
        //            }
        //        }
        //    },
        //    submitHandler: function (validator, commentForm, generateRandomButton) {
        //        //禁用取号按钮
        //        $("#generateRandomButton").attr({"disabled":"disabled"});
        //        event.preventDefault();
        //        console.log('点击了随机分组生成按钮');
        //
        //        $.ajax({
        //            type: 'post',
        //            url: '/random/generate',
        //            data: $("#commentForm").serialize(),
        //            success: function(data) {
        //                alert(data);
        //                $('#commentForm').data('bootstrapValidator').resetForm(true);
        //                $("#generateRandomButton").removeAttr("disabled");
        //            }
        //        });
        //    }
        //});

        //tab2分组页面功能,分组行数小于3之后，删除按钮被禁用
        var syfz_count=2;
    	$('#syfz_add').click(function(){
    	    console.log('syfz add is clicked');
    	    html_string='<tr><td><input type="text" name="group_type[]"></td><td><input type="text" name="group_name[]"></td><td class="syfz_bl"><input type="text" name="occupy[]"></td><td><input type="text" name="subject_number[]" readonly="readonly"></td></tr>';
    	    $('#syfz_table tbody').append(html_string);
            syfz_count++;
            $('#num_of_group').val(syfz_count);

            if(syfz_count>2){
                $('#syfz_del').removeAttr("disabled");
            }else{
                $('#syfz_del').attr({"disabled":"disabled"});
            }
    	});

        $('#syfz_del').click(function(){
            console.log('syfz del is clicked');

            $('#syfz_table tr:last').remove();
            syfz_count--;
            $('#num_of_group').val(syfz_count);

            if(syfz_count<=2){
                $('#syfz_del').attr({"disabled":"disabled"});
            }
        });
        //tab2 分组比例更改 计算受试者数量
        $('#syfz_table tbody').on("change",".syfz_bl",function(){
            total=0;
            //计算total
            $('#syfz_table tr').each(function(){
                tds=$(this).children();
                prop=tds.eq(2).find('input').val();
                if(prop!=undefined && prop!='undefined')  { total+=Number(prop);}

            });
            //样本量
            simple_size=Number($('#simple_size').val());
            $('#syfz_table tr').each(function(){
                tds=$(this).children();
                prop=tds.eq(2).find('input').val();
                if(prop!=undefined && prop!='undefined')  {
                    tds.eq(3).find('input').val(simple_size*Number(prop)/total);
                }

            });
        });
        $('#simple_size').change(function(){
            total=0;
            //计算total
            $('#syfz_table tr').each(function(){
                tds=$(this).children();
                prop=tds.eq(2).find('input').val();
                if(prop!=undefined && prop!='undefined')  { total+=Number(prop);}

            });
            //样本量
            simple_size=Number($('#simple_size').val());
            $('#syfz_table tr').each(function(){
                tds=$(this).children();
                prop=tds.eq(2).find('input').val();
                if(prop!=undefined && prop!='undefined')  {
                    if(prop>0){
                        tds.eq(3).find('input').val(simple_size*Number(prop)/total);
                    }
                    else {
                        tds.eq(3).find('input').val(0);
                    }
                }else{
                    tds.eq(3).find('input').val(0);
                }
            });
        });
        //end 分组页面功能

        //tab4分层设置
        var groupCount=1;
        //表id fenzu_table
        //添加组 id AddGroup
        //删除组 class removegroup
        //添加条件 class addcon
        //删除条件 class removecon
        //添加组
        $('#AddGroup').click(function(e){
            groupNumber= 'group'+groupCount++;
            // console.log(groupNumber);
            input_string = '<tr  groupnumber="'+groupNumber+'" totalchild="2"><td class="hadspan" rowspan="2"><input type="text" name="'+groupNumber+'" value="请输入组名"></input></td><td  class="hadspan" rowspan="2"><a href="#"  class="addcon">添加</a></td><td><input type="text" name="'+groupNumber+'_0" value="输入分组信息"></input></td><td  class="hadspan" rowspan="2"><a href="#" class="removegroup">删除</a></td></tr><tr groupnumber="'+groupNumber+'"><td>  <input type="text" name="'+groupNumber+'_1" value="输入分组信息"></input></td></tr> ';
            $("#fenzu_table tbody").append (input_string);
            return false;
            });
        //删除层
        $("body").on("click",".removegroup", function(e){
            number = $(this).parent("td").parent("tr").attr('groupnumber');
            console.log(number);
            $("tr[groupnumber="+number+"]").remove();
            groupCount--;
            return false;
        });
        //添加条件
        $("body").on("click",".addcon", function(e){
            parent_tr= $(this).parent("td").parent("tr");
            number =parent_tr.attr('groupnumber');//group Number
            child=parent_tr.attr('totalchild'); //childs number
            child=Number(child)+1;
            input_string='  <tr groupnumber="'+number+'" ><td>  <input type="text" name="'+number+'_'+child+'" value="输入分组信息"></input><a href="#" class="removecon">×</a></td></tr>';
            console.log(input_string);
            $("tr[groupnumber="+number+"] > td[class=hadspan]").attr("rowSpan",child);
            $("tr[groupnumber="+number+"]:first").attr("totalchild",child);
            $("tr[groupnumber="+number+"]:last").after(input_string);
            return false;
        });
        //删除条件
        $("body").on("click",".removecon",function(e){
          parent_tr= $(this).parent("td").parent("tr");
          number =parent_tr.attr('groupnumber');//group Number
          child = $("tr[groupnumber="+number+"]:first").attr("totalchild");//childs number
          child=Number(child)-1;
          $("tr[groupnumber="+number+"]:first").attr("totalchild",child);//设置totalchild
            $("tr[groupnumber="+number+"] > td[class=hadspan]").attr("rowSpan",child);//设置rowspan
            parent_tr.remove();//干掉自己
            return false;
        });

        $("#is_stratifi").change(function(){
            if($("#is_stratifi").val()=="yes"){
                $("#fenzu_table").show();
            }else{
                $("#fenzu_table").hide();
            }
        });
        //end 分层设置

        //tab5 区段长度与区段数联动
        $('#num_of_length').change(function(){
            total=Number($('#simple_size').val());
            qds=Number($(this).val());
            $('#num_of_block').val(total/qds);
        });


        //$('#rootwizard .finish').click(function() {
        //	console.log('Finished!, Starting over!');
        //	$('#commentForm').submit();
        //});


   });
};
function showProp(){
    console.log('call show prop');
    nameProp='';
    numberProp='';
    $('#syfz_table tr').each(function(){
        tds=$(this).children();
        name=tds.eq(0).find('input').val();
        prop=tds.eq(2).find('input').val();
        if(name!=undefined && name!='undefined') { nameProp +=name+':';}
        if(prop!=undefined && prop!='undefined')  {numberProp+=prop+':';}

    });
    return nameProp.substring(0,nameProp.length-1)+'='+numberProp.substring(0,numberProp.length-1);
};

//Random.RandomGroupingSubmit = function(){
//    event.preventDefault();
//    $.ajax({
//        type: 'post',
//        url: '/random/generate',
//        data: $("#commentForm").serialize(),
//        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
//            alert(data);
//        }
//    });
//};


Random.FetchSequenceNumber = function(){
    console.log('进入取号页面');
    $('.tile_count').fadeOut("slow");
    //$("#rightBlock").hide();
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/random/fetchSequenceNumber", function () {
        $('#module-title').html("分层项列表");

        $('#form_get_stratification').bootstrapValidator({
            fields:{
                subjectid:{
                    validators:{
                        notEmpty:{
                            message:'请输入受试者姓名'
                        }
                    }
                }
            },
            submitHandler: function (validator, form, submitButton) {
                //禁用取号按钮
                $("#fetchNumberButton").attr({"disabled":"disabled"});
                event.preventDefault();
                console.log('进入取号提交模块');

                $.ajax({
                    type: 'post',
                    url: '/random/getnumber',
                    data: $("#form_get_stratification").serialize(),
                    success: function(data) {
                        $('#returnedStatus').text(data.status);

                        //如果取号失败，显示RNID列，但内容为error message.
                        $('#returnedRNID').text(data.rnid);

                        if(data.groupName!=null){
                            //不盲
                            $('#returnedGroupName').text(data.groupName);
                            $('#returnedGroupNameHead').show();
                        }

                        $('.ln_solid').show('slow');
                        $('.table').show();

                        $('#form_get_stratification').data('bootstrapValidator').resetForm(true);
                        $("#fetchNumberButton").removeAttr("disabled");
                    }
                });
            }
        });
    });
};

//Random.FetchSequenceNumberSubmit = function(){
//    console.log('进入取号提交模块');
//    event.preventDefault();
//    $.ajax({
//        beforeSend: function(){
//            $("#icon").addClass("fa fa-circle-o-notch fa-spin");
//        },
//        type: 'post',
//        url: '/random/getnumber',
//        data: $("#form_get_stratification").serialize(),
//        success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
//           alert(data);
//           $("#icon").removeClass("fa fa-circle-o-notch fa-spin");
//        }
//    });
//};

Random.CheckSequenceNumberList = function(){
    console.log('进入医生查看取号结果模块');
    $('.tile_count').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/random/checkOfferNumberInfoList", function () {
        $('#module-title').html("取号结果列表");
        $("#sequenceNumberListDataTable").DataTable({
            language: {
                "url": "/js/dataTables/dataTablesLanguage.json"   //中文提示信息
            },
            bLengthChange: false,
            ajax: function (data, callback) {
                $.ajax({
                    url: "/random/queryOfferNumberInfoList",
                    success: function (result) {
                        var returnData = {};
                        returnData.data = result;
                        callback(returnData);
                    }
                });
            },
            columns: [
                { "data": "offerNumberResult.subjectId" },
                { "data": "offerNumberResult.rnid" },
                { "data": "investiGroup.groupName" }
            ]
        });
    });
};

Random.UnfoldBlindness = function(){
    console.log('进入受试者揭盲页面');
    $('.tile_count').fadeOut("slow");
    $("#resultsBlock").empty();
    $("#resultsBlock").load("/random/unfoldBlindness", function () {
        $('#headLineInMainPanel').hide();
    });
};
