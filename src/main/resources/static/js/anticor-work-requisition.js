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
	 * 异步请求加载防腐（保温）申请单
	 */
	$.ajax({
		type: 'GET',
		url: '/iot_process/workrequisition/anticor/show',
		data: {'piid': piid},
		dataType: 'json',
		success: function(json){
			if(json.state == 0){
				var data = json.data;
				$("#applydept").val(data.applydept)
				$("#applydate").val(data.applydate)
				$("#requirement").val(data.requirement);
				$("#applypeople").val(data.applypeople);
				$("#deptowner").val(data.deptowner);
				var tablelist = data.workTables;
				for(var i=0;i<tablelist.length;i++){
					$("td .laytable-cell-1-0-1")[i].innerText = tablelist[i].tableContent;
					$("td .laytable-cell-1-0-2")[i].innerText = tablelist[i].tableVersion;
					$("td .laytable-cell-1-0-3")[i].innerText = tablelist[i].tableNumber;
					$("td .laytable-cell-1-0-4")[i].innerText = tablelist[i].tableComment;
				}
			}
		}
	})
	
	
	/**
	 * 防腐（保温）内容及工作量输入表
	 */
	table.render({
	    elem: '#anticorrosion'
	    ,cols: [[ //标题栏
	      {field:'id', title:'序号', width:50, type:'numbers',align:'center'}
	      ,{field:'content', title:'防腐（保温）内容', width:220, edit: 'text',align:'center'}
	      ,{field:'version', title:'规格型号', edit: 'text', width:100, align:'center'}
	      ,{field:'num', title:'数量', width:80, edit:'text', width:80,align:'center'}
	      ,{field:'comment', title:'备注', edit: 'text', align:'center',width:313}
	    ]]
	    ,data: [{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""},
	    		{"content": "","version": "","num": "","comment": ""}]
	  });	
	
	form.on('submit(print-page1)', function(data){
		console.log("开始打印");
		$("#base").css({"display":"block"})
		fillDate_one();
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
	 * 保存防腐保温申请单
	 */
	form.on('submit(save-page1)', function(data){
		console.log("开始保存");
		
		//获取表单数据
		var formdata = {'piid':piid};
		var obj, tabledata = [];
		formdata.applydept = $("#applydept").val();
		formdata.applypeople = $("#applypeople").val();
		formdata.deptowner = $("#deptowner").val();
		formdata.requirement = $("#requirement").val();
		formdata.applydate = $("#applydate").val();
		
		var tableNo=[], tableContent=[], tableVersion=[], tableNumber=[], tableComment=[];
		$("td .laytable-cell-1-0-1").each(function(index,element){
			tableNo[index] = index + 1;
			tableContent[index] = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-2").each(function(index,element){
			tableVersion[index] = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-3").each(function(index,element){
			tableNumber[index] = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-4").each(function(index,element){
			tableComment[index] = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		for(var i=0;i<tableNo.length;i++){
			obj = new Object();
			obj.tableNo = tableNo[i];
			obj.tableContent = tableContent[i];
			obj.tableVersion = tableVersion[i];
			obj.tableNumber = tableNumber[i];
			obj.tableComment = tableComment[i];
			tabledata.push(obj);
		}
		formdata.workTables = tabledata;
		console.log(formdata);
		$.ajax({
			async: false,
			type: 'POST',
			url: '/iot_process/workrequisition/anticor/save',
			data: JSON.stringify(formdata),
			contentType: 'application/json',
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
	 * 防腐（保温）作业申请单
	 * 根据用户输入值填充工作单
	 */
	function fillDate_one(){
		$("#applydept_").text($("#applydept").val());
		$("#applypeople_").text($("#applypeople").val());
		$("#deptowner_").text($("#deptowner").val());
		$("#requirement_").html($("#requirement").val());
		console.log($("#requirement").val());
		var arr = $("#applydate").val().split("-");
		if(arr.length < 3){
			arr[0] = arr[1] = arr[2] = '  ';
		}
		$("#applydate_").text(arr[0]+"年"+arr[1]+"月"+arr[2]+"日");
		
		$("td .laytable-cell-1-0-1").each(function(index,element){
			$('.content-1')[index].innerText = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-2").each(function(index,element){
			$('.content-2')[index].innerText = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-3").each(function(index,element){
			$('.content-3')[index].innerText = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		$("td .laytable-cell-1-0-4").each(function(index,element){
			$('.content-4')[index].innerText = element.innerText==undefined||element.innerText==''?' ':element.innerText;
		})
		
	}
	
	/**
	 * 清空表单数据
	 */
	form.on('submit(clean-data)', function(data){
		$(".layui-input").val("");
		$("td .laytable-cell-1-0-1").each(function(index,element){
			console.log(element.innerText);
			element.innerText = '';
		})
		$("td .laytable-cell-1-0-2").each(function(index,element){
			element.innerText = '';
		})
		$("td .laytable-cell-1-0-3").each(function(index,element){
			element.innerText = '';
		})
		$("td .laytable-cell-1-0-4").each(function(index,element){
			element.innerText = '';
		})
	});
});
