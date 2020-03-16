/**
 * 日期插件
 */
layui.use('laydate', function(){
	var laydate = layui.laydate;
	//常规用法
	laydate.render({
		elem: '#applydate'
		,format: 'yyyy-MM-dd'
	});
});

//从url中取出piid
var piid = GetQueryString("piid");
$("#piid").val(piid);
layui.use(['layer', 'form', 'table'], function() {
	var layer = layui.layer, $ = layui.$, form = layui.form;
	var table = layui.table;
	
	/**
	 * 异步请求加载脚手架搭拆申请单
	 */
	$.ajax({
		type: 'GET',
		url: '/iot_process/workrequisition/staging/show',
		data: {'piid': piid},
		dataType: 'json',
		success: function(json){
			if(json.state == 0){
				var data = json.data;
				$("#applydept").val(data.applydept)
				$("#applydate").val(data.applydate)
				$("#location").val(data.stagingLocation);
				$("#applypeople").val(data.applypeople);
				$("#deptowner").val(data.deptowner);
				$("#workdescription").val(data.workdescription);
				$("#tips").val(data.tips);
				$("#action").val(data.action);
				$("#caculator").val(data.caculator);
				$("#workAmount").val(data.workAmount);
				$("#comment").val(data.stagingComment);	
			}
		}
	})

	/**
	 * 打印脚手架搭拆申请单
	 */
	form.on('submit(print-page)', function(data){
		console.log("开始打印");
		$("#base").css({"display":"block"})
		fillDate();
		$("#base").print({
			globalStyles: true,
		    mediaPrint: false,
		    stylesheet: null,
		    noPrintSelector: ".no-print",
		    iframe: true,
		    append: null,
		    prepend: null,
		    manuallyCopyFormValues: true,
		    deferred: $.Deferred()
		 }); 
		$("#base").css({"display":"none"});
		console.log("打印结束");
	});
	
	/**
	 * 保存脚手架搭拆申请单
	 */
	form.on('submit(save-page)', function(data){
		console.log("开始保存");
		$("#piid").val(piid);
		$.ajax({
			async: false,
			type: 'POST',
			url: '/iot_process/workrequisition/staging/save',
			data: $("form").serialize(),
			dataType: 'json',
			success: function(json){
				if(json.state == 0){
					layer.msg("保存成功",{icon:1, time: 2000});
				}else{
					layer.msg("保存失败",{icon:2, time: 2000});
				}
			},
			error: function(){
				layer.msg("保存失败， 请检查网路是否正常",{icon:2, time: 2000});
			}
		})
	});
	
	/**
	 * 清空表单数据
	 */
	form.on('submit(clean-data)', function(data){
		$(".layui-input").val("");
	});
	
	/**
	 * 脚手架搭拆申请作业单
	 * 根据用户输入值填充工作单
	 */
	function fillDate(){
		$("#applydept_").text($("#applydept").val());
		var arr = $("#applydate").val().split("-");
		if(arr.length < 3){
			arr[0] = arr[1] = arr[2] = '  ';
		}
		$("#applydate_").text(arr[0]+"年"+arr[1]+"月"+arr[2]+"日");
		$("#location_").text($("#location").val());
		$("#applypeople_").text($("#applypeople").val());
		$("#deptowner_").text($("#deptowner").val());
		$("#workdescription_").text($("#workdescription").val());
		$("#tips_").text($("#tips").val());
		$("#action_").text($("#action").val());
		$("#caculator_").text($("#caculator").val());
		$("#workAmount_").text($("#workAmount").val());
		$("#comment_").text($("#comment").val());
	}
	
});
